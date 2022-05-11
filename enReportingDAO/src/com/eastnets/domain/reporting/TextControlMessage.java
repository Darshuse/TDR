package com.eastnets.domain.reporting;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class TextControlMessage {
	private int aid;
	private Long umidh;
	private Long unidl;
	private Date mesgCreaDate;
	private String mesgType;
	private String textDataBlcok;
	private String stxVearsin;
	private String mesgSender;
	private String mesgReceiver;
	private String mesgSubFormat;
	private String mesgIdinfier;

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public String getMesgType() {
		return mesgType;
	}

	public Date getMesgCreaDate() {
		return mesgCreaDate;
	}

	public void setMesgCreaDate(Date mesgCreaDate) {
		this.mesgCreaDate = mesgCreaDate;
	}

	public String getMesgCreaDateTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(mesgCreaDate);
	}

	public String getTextDataBlcok() {
		return textDataBlcok;
	}

	public void setTextDataBlcok(String textDataBlcok) {
		this.textDataBlcok = textDataBlcok;
	}

	public String getStxVearsin() {
		return stxVearsin;
	}

	public void setStxVearsin(String stxVearsin) {
		this.stxVearsin = stxVearsin;
	}

	public Long getUmidh() {
		return umidh;
	}

	public void setUmidh(Long umidh) {
		this.umidh = umidh;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public Long getUnidl() {
		return unidl;
	}

	public void setUnidl(Long unidl) {
		this.unidl = unidl;
	}

	public String getMesgSender() {
		return mesgSender;
	}

	public void setMesgSender(String mesgSender) {
		this.mesgSender = mesgSender;
	}

	public String getMesgReceiver() {
		return mesgReceiver;
	}

	public void setMesgReceiver(String mesgReceiver) {
		this.mesgReceiver = mesgReceiver;
	}

	public String getMesgSubFormat() {
		return mesgSubFormat;
	}

	public void setMesgSubFormat(String mesgSubFormat) {
		this.mesgSubFormat = mesgSubFormat;
	}

	public String getMesgIdinfier() {
		return mesgIdinfier;
	}

	public void setMesgIdinfier(String mesgIdinfier) {
		this.mesgIdinfier = mesgIdinfier;
	}

}
