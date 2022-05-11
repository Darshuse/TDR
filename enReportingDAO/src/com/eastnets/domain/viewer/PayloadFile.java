package com.eastnets.domain.viewer;

import java.io.InputStream;
import java.io.Serializable;

public class PayloadFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3841358553307513919L;
	private String fileName;
	private InputStream fileStream;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	public InputStream getFileStream() {
		return fileStream;
	}
	public void setFileStream(InputStream fileStream) {
		this.fileStream = fileStream;
	}
	
	
}
