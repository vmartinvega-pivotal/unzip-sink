package io.tim.fastdata.unzipsink;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.util.StringUtils;

@EnableBinding(Sink.class)
@EnableConfigurationProperties(UnzipSinkProperties.class)
@SpringBootApplication
public class UnzipSinkApplication {
	
	private static final Logger LOG = LoggerFactory.getLogger(UnzipSinkApplication.class);
	private static final String DEFAULT_ARCHIVE_FOLDER_NAME = "archive";
	
	@Autowired
	UnzipSinkProperties properties;
	 
	public static void main(String[] args) {
		SpringApplication.run(UnzipSinkApplication.class, args);
	}
	
	@StreamListener(Sink.INPUT)
    public void unzipFile(File inputFile) throws Exception {
        
		LOG.info("Uncompressing file {}", inputFile.getAbsolutePath());
        String output = getOutputDir(inputFile);
        LOG.info("Using {} as the output folder", output);
        List<File> list = ZipUtil.decompress(inputFile, output);
        LOG.info("Extracted {} files: {}", list.size(), list.toString());

        if(properties.isArchiveZipfile()) {
        	moveToArchive(inputFile, output);
        }
        if (properties.isDeleteZipfile()){
            deleteZipFile(inputFile);
        }
    }

	private String getOutputDir(File inputFile) {
		String output = properties.getOutputdir();
        if (StringUtils.isEmpty(output)) {
            output = inputFile.getParent();
        }
		return output;
	}

	private void deleteZipFile(File inputFile) {
		if(inputFile.delete()){
		    LOG.info("Input file {} deleted!", inputFile.getAbsolutePath());
		}else{
		    LOG.warn("Input file {} was not deleted!", inputFile.getAbsolutePath());
		}
	}

	private void moveToArchive(File inputFile, String output)
			throws IOException {
		String archiveDir = properties.getArchivedir();
		 if (StringUtils.isEmpty(archiveDir)) {
			 archiveDir = output + "/" + DEFAULT_ARCHIVE_FOLDER_NAME;
		 }
		 LOG.info("Using [{}] as the archive folder", archiveDir);
		 
		 Path source = null;
		 Path target = null;
		 try {

		 source = Paths.get(inputFile.getAbsolutePath());
		 target = Paths.get(archiveDir, inputFile.getName());
		 
		 Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		 LOG.info("Input file [{}] archived to [{}]", 
						 source, 
						 target);
		 } catch(Exception e) {
			 LOG.error("cannot move {} to {}", source, target, e);
		 }
	}
}
