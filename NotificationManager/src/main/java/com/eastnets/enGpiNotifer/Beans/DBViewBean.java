package com.eastnets.enGpiNotifer.Beans;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;


public class DBViewBean {

	private BigDecimal seq;
	private String mesgText;
	private Date mesgCreaDate;
	private String queueName;
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
 
	public String getMesgCreaDateTimeStr() {
		return fullDateTimeFormat.format(mesgCreaDate);
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public BigDecimal getSeq() {
		return seq;
	}
	public void setSeq(BigDecimal seq) {
		this.seq = seq;
	}
}
