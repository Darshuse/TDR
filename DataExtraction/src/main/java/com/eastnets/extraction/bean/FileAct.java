package com.eastnets.extraction.bean;

import java.sql.Clob;

public class FileAct {

	private String 	mesgTransferDesc;
	private String 	mesgTransferInfo;
	private String 	mesgFileLogicalName;
	private Integer mesgFileSize;
	private String 	mesgFileDesc;
	private String	mesgFileInfo;
	private String 	mesgFileDigestAlgo;
	private String 	mesgFileDigestValue;	
	private Clob 	mesgFileHeaderInfo;
	
	private PayloadType payloadType;
	
	public void setMesgTransferDesc(String mesgTransferDesc) {
		this.mesgTransferDesc = mesgTransferDesc;
	}
	public String getMesgTransferDesc() {
		return mesgTransferDesc;
	}
	public void setMesgTransferInfo(String mesgTransferInfo) {
		this.mesgTransferInfo = mesgTransferInfo;
	}
	public String getMesgTransferInfo() {
		return mesgTransferInfo;
	}
	public void setMesgFileLogicalName(String mesgFileLogicalName) {
		this.mesgFileLogicalName = mesgFileLogicalName;
	}
	public String getMesgFileLogicalName() {
		return mesgFileLogicalName;
	}
	public void setMesgFileSize(Integer mesgFileSize) {
		this.mesgFileSize = mesgFileSize;
	}
	public Integer getMesgFileSize() {
		return mesgFileSize;
	}
	public void setMesgFileDesc(String mesgFileDesc) {
		this.mesgFileDesc = mesgFileDesc;
	}
	public String getMesgFileDesc() {
		return mesgFileDesc;
	}
	public void setMesgFileInfo(String mesgFileInfo) {
		this.mesgFileInfo = mesgFileInfo;
	}
	public String getMesgFileInfo() {
		return mesgFileInfo;
	}
	public void setMesgFileDigestAlgo(String mesgFileDigestAlgo) {
		this.mesgFileDigestAlgo = mesgFileDigestAlgo;
	}
	public String getMesgFileDigestAlgo() {
		return mesgFileDigestAlgo;
	}
	public void setMesgFileDigestValue(String mesgFileDigestValue) {
		this.mesgFileDigestValue = mesgFileDigestValue;
	}
	public String getMesgFileDigestValue() {
		return mesgFileDigestValue;
	}
	public void setMesgFileHeaderInfo(Clob mesgFileHeaderInfo) {
		this.mesgFileHeaderInfo = mesgFileHeaderInfo;
	}
	public Clob getMesgFileHeaderInfo() {
		return mesgFileHeaderInfo;
	}
	public PayloadType getPayloadType() {
		return payloadType;
	}
	public void setPayloadType(PayloadType payloadType) {
		this.payloadType = payloadType;
	}

}
