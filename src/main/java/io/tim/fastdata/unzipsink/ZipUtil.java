package io.tim.fastdata.unzipsink;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class ZipUtil {


    /**
     * Decompress a ZIP file into the outputDir directory, then return alist of File objects for further
     * processing. User is responsible for cleaning up the files later
     * Decompress  on a file with a .unzipping suffix and then remove the suffix.
     *
     * @param zipFile File object representing the zipped file to be decompressed
     * @param outputDir folder to decompress the file
     * @return a List containing File objects for each file that was in the zip archive
     * @throws FileNotFoundException zip archive file was not found
     * @throws ArchiveException      something went wrong creating
     *                               the archive input stream
     * @throws IOException
     */
    public static List<File> decompress(File zipFile, String outputDir) throws
            FileNotFoundException,
            ArchiveException,
            IOException {

        List<File> archiveContents = new ArrayList<File>();

        // create the input stream for the file, then the input stream for the actual zip file
        final InputStream is = new FileInputStream(zipFile);
        ArchiveInputStream ais = new
                ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP,
                is);

        // cycle through the entries in the zip archive and write them to the system temp dir
        ZipArchiveEntry entry = (ZipArchiveEntry) ais.getNextEntry();
        while (entry != null) {
        	String entryName = entry.getName();
        	String temporaryName = entryName + ".unzipping";
            
            OutputStream os = new FileOutputStream(new File(outputDir, temporaryName));
            
            IOUtils.copy(ais, os);  // copy from the archive inputstream to the output stream
            os.close();     // close the output stream
            
            // move the suffixed file to the target file
            Files.move(Paths.get(outputDir, temporaryName), 
            			Paths.get(outputDir, entryName),
	            			StandardCopyOption.ATOMIC_MOVE, 
	            			StandardCopyOption.REPLACE_EXISTING);
            
            archiveContents.add(new File(outputDir, entryName));
            entry = (ZipArchiveEntry) ais.getNextEntry();
        }

        ais.close();
        is.close();

        return archiveContents;
    }
}

