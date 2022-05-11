package com.eastnets.dao.bicloader;

import java.sql.Date;

public class BicFile {

	private String filename;
	private String path;
	private Date modificationDate;
	private String fileType;
	private boolean fullFile;
	private boolean deleteOldOption;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public boolean isFullFile() {
		return fullFile;
	}

	public void setFullFile(boolean fullFile) {
		this.fullFile = fullFile;
	}

	public boolean isDeleteOldOption() {
		return deleteOldOption;
	}

	public void setDeleteOldOption(boolean deleteOldOption) {
		this.deleteOldOption = deleteOldOption;
	}

}
