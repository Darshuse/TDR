package com.eastnets.textbreak.bean;

import java.util.Date;
import java.util.List;

import com.eastnets.textbreak.entities.SystemTextField;
import com.eastnets.textbreak.entities.TextField;
import com.eastnets.textbreak.entities.TextFieldLoop;

public class ParsedData {
	private long aid;
	private long mesgUmidl;
	private long mesgUmidh;
	private String messageText;
	private String mesgType;
	private String stxVersion;
	private Date mesgCreaDateTime;
	private String textDataBlock;
	private boolean isSysMessages;
	private boolean lastBulkMessage;
	private boolean faildParsing;

	// list of rtextfield
	private List<TextField> textFieldList;

	// list of rtextfieldLoop
	private List<TextFieldLoop> textFieldLoopList;

	// list of rSystemtextfield
	private List<SystemTextField> sysTextFieldList;

	public boolean isSysMessages() {
		return isSysMessages;
	}

	public void setSysMessages(boolean isSysMessages) {
		this.isSysMessages = isSysMessages;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getMesgUmidl() {
		return mesgUmidl;
	}

	public void setMesgUmidl(long mesgUmidl) {
		this.mesgUmidl = mesgUmidl;
	}

	public long getMesgUmidh() {
		return mesgUmidh;
	}

	public void setMesgUmidh(long mesgUmidh) {
		this.mesgUmidh = mesgUmidh;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public String getStxVersion() {
		return stxVersion;
	}

	public void setStxVersion(String stxVersion) {
		this.stxVersion = stxVersion;
	}

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public String getTextDataBlock() {
		return textDataBlock;
	}

	public void setTextDataBlock(String textDataBlock) {
		this.textDataBlock = textDataBlock;
	}

	public List<TextField> getTextFieldList() {
		return textFieldList;
	}

	public void setTextFieldList(List<TextField> textFieldList) {
		this.textFieldList = textFieldList;
	}

	public List<TextFieldLoop> getTextFieldLoopList() {
		return textFieldLoopList;
	}

	public void setTextFieldLoopList(List<TextFieldLoop> textFieldLoopList) {
		this.textFieldLoopList = textFieldLoopList;
	}

	public List<SystemTextField> getSysTextFieldList() {
		return sysTextFieldList;
	}

	public void setSysTextFieldList(List<SystemTextField> sysTextFieldList) {
		this.sysTextFieldList = sysTextFieldList;
	}

	public boolean isLastBulkMessage() {
		return lastBulkMessage;
	}

	public void setLastBulkMessage(boolean lastBulkMessage) {
		this.lastBulkMessage = lastBulkMessage;
	}

	public boolean isFaildParsing() {
		return faildParsing;
	}

	public void setFaildParsing(boolean faildParsing) {
		this.faildParsing = faildParsing;
	}

}
