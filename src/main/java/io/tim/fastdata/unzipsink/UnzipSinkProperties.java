/**
 * 
 */
package io.tim.fastdata.unzipsink;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @author Doruk Kutukculer
 *
 */
@ConfigurationProperties("unzip")
@Validated
public class UnzipSinkProperties {

	 /**
     * Folder to decompress the file. Default value parent's file folder.
     */
    private String outputdir = null;

    /**
     * Delete the zip file after decompressing. Default value false.
     */
    private boolean deleteZipfile = false;

    /**
     * Directory to move the file after decompressing. Defaults to outputdir.
     */
    private String archivedir = null;
    
    /**
     * Move the zip file to another directory after decompressing. Default value false.
     */
    private boolean archiveZipfile = false;
    
    public String getOutputdir() {
        return outputdir;
    }

    public void setOutputdir(String outputdir) {
        this.outputdir = outputdir;
    }

    public boolean isDeleteZipfile() {
        return deleteZipfile;
    }

    public void setDeleteZipfile(boolean deleteZipfile) {
        this.deleteZipfile = deleteZipfile;
    }

	public boolean isArchiveZipfile() {
		return archiveZipfile;
	}

	public void setArchiveZipfile(boolean archiveZipfile) {
		this.archiveZipfile = archiveZipfile;
	}

	public String getArchivedir() {
		return archivedir;
	}

	public void setArchivedir(String archivedir) {
		this.archivedir = archivedir;
	}
}
