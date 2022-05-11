package com.eastnets.domain.viewer;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MessageKey {

	private int aid;
	private Integer umidh;
	private Integer unidl;
	private Date mesgCreaDate;
	private java.util.Date mesgCraetionUtil;
	private String mesgType;
	private String appeNacReason;
	private java.util.Date mesgModFDate;
	private String uetr;
	private String mesgIdentifier;
	private String mesgSLA;
	private String mesgSubFormat;
	private String mesgSender;
	private String mesgSwiftAdrres;
	private String mesgReciverX1;

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public java.util.Date getMesgModFDate() {
		return mesgModFDate;
	}

	public void setMesgModFDate(java.util.Date mesgModFDate) {
		this.mesgModFDate = mesgModFDate;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public Integer getUmidh() {
		return umidh;
	}

	public void setUmidh(Integer umidh) {
		this.umidh = umidh;
	}

	public Integer getUnidl() {
		return unidl;
	}

	public void setUnidl(Integer unidl) {
		this.unidl = unidl;
	}

	public Date getMesgCreaDate() {
		return mesgCreaDate;
	}

	public void setMesgCreaDate(Date mesgCreaDate) {
		this.mesgCreaDate = mesgCreaDate;
	}

	public String getUetr() {
		return uetr;
	}

	public void setUetr(String uetr) {
		this.uetr = uetr;
	}

	public String getAppeNacReason() {
		return appeNacReason;
	}

	public void setAppeNacReason(String appeNacReason) {
		this.appeNacReason = appeNacReason;
	}

	public String getMesgIdentifier() {
		return mesgIdentifier;
	}

	public void setMesgIdentifier(String mesgIdentifier) {
		this.mesgIdentifier = mesgIdentifier;
	}

	public String getMesgSLA() {
		return mesgSLA;
	}

	public void setMesgSLA(String mesgSLA) {
		this.mesgSLA = mesgSLA;
	}

	public String getMesgCreaDateTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(mesgCreaDate);
	}

	public java.util.Date getMesgCraetionUtil() {
		return mesgCraetionUtil;
	}

	public void setMesgCraetionUtil(java.util.Date mesgCraetionUtil) {
		this.mesgCraetionUtil = mesgCraetionUtil;
	}

	public String getMesgSubFormat() {
		return mesgSubFormat;
	}

	public void setMesgSubFormat(String mesgSubFormat) {
		this.mesgSubFormat = mesgSubFormat;
	}

	public String getMesgSender() {
		return mesgSender;
	}

	public void setMesgSender(String mesgSender) {
		this.mesgSender = mesgSender;
	}

	public String getMesgReciver() {
		if (mesgSwiftAdrres != null && !mesgSwiftAdrres.isEmpty()) {
			return mesgSwiftAdrres;
		} else if (mesgReciverX1 != null && !mesgReciverX1.isEmpty()) {
			return mesgReciverX1;
		}

		return "";
	}

	public String getMesgSwiftAdrres() {
		return mesgSwiftAdrres;
	}

	public void setMesgSwiftAdrres(String mesgSwiftAdrres) {
		this.mesgSwiftAdrres = mesgSwiftAdrres;
	}

	public String getMesgReciverX1() {
		return mesgReciverX1;
	}

	public void setMesgReciverX1(String mesgReciverX1) {
		this.mesgReciverX1 = mesgReciverX1;
	}

}
