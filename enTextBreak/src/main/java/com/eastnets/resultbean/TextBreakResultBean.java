package com.eastnets.resultbean;

import java.util.Date;

public class TextBreakResultBean {

	private Date mesgCreaDateTime;
	private Integer aid;
	private Integer umidl;
	private Integer umidh;
	private String mesgType;
	private String mesgSyntaxTableVer;
	private String textDataBlock;

	public TextBreakResultBean() {
		// TODO Auto-generated constructor stub
	}

	public TextBreakResultBean(Date mesgCreaDateTime, Integer aid, Integer umidl, Integer umidh, String mesgType, String mesgSyntaxTableVer, String textDataBlock) {
		super();
		this.mesgCreaDateTime = mesgCreaDateTime;
		this.aid = aid;
		this.umidl = umidl;
		this.umidh = umidh;
		this.mesgType = mesgType;
		this.mesgSyntaxTableVer = mesgSyntaxTableVer;
		this.textDataBlock = textDataBlock;
	}

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getUmidl() {
		return umidl;
	}

	public void setUmidl(Integer umidl) {
		this.umidl = umidl;
	}

	public Integer getUmidh() {
		return umidh;
	}

	public void setUmidh(Integer umidh) {
		this.umidh = umidh;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public String getMesgSyntaxTableVer() {
		return mesgSyntaxTableVer;
	}

	public void setMesgSyntaxTableVer(String mesgSyntaxTableVer) {
		this.mesgSyntaxTableVer = mesgSyntaxTableVer;
	}

	public String getTextDataBlock() {
		return textDataBlock;
	}

	public void setTextDataBlock(String textDataBlock) {
		this.textDataBlock = textDataBlock;
	}

}
