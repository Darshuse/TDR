package com.eastnets.domain.filepayload;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class FilePayload implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3074216092903639052L;
	private Integer aid;
	private Integer sumidl;
	private Integer sumidh;
	private Timestamp creationDateTime;
	
	private String fileName;
	private Integer fileSize;
	private String fileDigest;
	private String fileDigestAlgo;

	private String payloadFilePath;
	
	@Override
	public boolean equals(Object obj) {
		FilePayload payload= (FilePayload) obj;
		if ( payload != null ){
			return aid == payload.aid && sumidl == payload.sumidl && sumidh == payload.sumidh && creationDateTime == payload.creationDateTime ;
		}
		return super.equals(obj);
	}
	@Override
	public String toString(){
		return String.format("Payload(%d,%d,%d,%s,%s)", aid, sumidl, sumidh, new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(creationDateTime), fileName );
	}


	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getSumidl() {
		return sumidl;
	}

	public void setSumidl(Integer sumidl) {
		this.sumidl = sumidl;
	}

	public Integer getSumidh() {
		return sumidh;
	}

	public void setSumidh(Integer sumidh) {
		this.sumidh = sumidh;
	}

	public Timestamp getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Timestamp creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileDigest() {
		return fileDigest;
	}

	public void setFileDigest(String fileDigest) {
		this.fileDigest = fileDigest;
	}

	public String getFileDigestAlgo() {
		return fileDigestAlgo;
	}

	public void setFileDigestAlgo(String fileDigestAlgo) {
		this.fileDigestAlgo = fileDigestAlgo;
	}
	
	public String getPayloadFilePath() {
		return payloadFilePath;
	}

	public void setPayloadFilePath(String payloadFilePath) {
		this.payloadFilePath = payloadFilePath;
	}
}
