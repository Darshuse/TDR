
package com.eastnets.textbreak.bean;

import java.util.Date;

import com.eastnets.textbreak.enums.ParserType;

public class SourceData {

	private long aid;
	private long mesgUmidl;
	private long mesgUmidh;
	private String messageText;
	private String mesgType;
	private String stxVersion;
	private Date mesgCreaDateTime;
	private String textDataBlock;

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

	public ParserType getMessageParserType() {
		if (mesgType == null)
			return ParserType.FIN_MESSAGE_PARSER;
		if (Integer.parseInt(mesgType) <= 99) {
			return ParserType.SYS_MESSAGE_PARSER;
		}
		return ParserType.FIN_MESSAGE_PARSER;
	}

}
