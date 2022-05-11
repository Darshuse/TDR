package com.eastnets.domain.reports.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DirectoryMetadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1851455453710124293L;
	private String directoryName;
	private String directoryLocation;
	private Date modificaitonDate;
	private List<DirectoryMetadata> subDirectories;
	private List<FileMetadata> files;

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public String getDirectoryLocation() {
		return directoryLocation;
	}

	public void setDirectoryLocation(String directoryLocation) {
		this.directoryLocation = directoryLocation;
	}

	public Date getModificaitonDate() {
		return modificaitonDate;
	}

	public void setModificaitonDate(Date modificaitonDate) {
		this.modificaitonDate = modificaitonDate;
	}

	public List<DirectoryMetadata> getSubDirectories() {
		return subDirectories;
	}

	public void setSubDirectories(List<DirectoryMetadata> subDirectories) {
		this.subDirectories = subDirectories;
	}

	public List<FileMetadata> getFiles() {
		return files;
	}

	public void setFiles(List<FileMetadata> files) {
		this.files = files;
	}

}
