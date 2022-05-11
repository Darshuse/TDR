package com.eastnets.domain.reports.repository;

import java.io.Serializable;
import java.util.Date;

public class FileMetadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1851988453710124293L;
	private String fileLocation;
	private String fileName;
	private String fileExtension;
	private String fileSize;
	private Date modificationDate;

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getImagePath() {
		return fileExtension.equalsIgnoreCase("pdf") ? "/images/pdf_2_32.png"
				: (fileExtension.equalsIgnoreCase("xlsx")) ? "/images/xlsx-icon.png"
						: "/images/docx-icon.png";
	}

}
