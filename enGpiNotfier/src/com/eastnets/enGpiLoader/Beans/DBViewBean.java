package com.eastnets.enGpiLoader.Beans;

import java.sql.Date;
import java.text.SimpleDateFormat;


public class DBViewBean {

	private String seq;
	private String mesgText;
	private Date mesgCreaDate;
	SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
 
	public String getMesgText() {
		return mesgText;
	}
	public void setMesgText(String mesgText) {
		this.mesgText = mesgText;
	}
	public Date getMesgCreaDate() {
		return mesgCreaDate;
	}
	public void setMesgCreaDate(Date mesgCreaDate) {
		this.mesgCreaDate = mesgCreaDate;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getMesgCreaDateTimeStr() {
		return fullDateTimeFormat.format(mesgCreaDate);
	}
}
