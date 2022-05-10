package com.eastnets.enGpiNotifer.Beans;

import java.sql.Date;

public class GbiHistoryBean {
	private Integer seq;
	private String mesgSeq;
	private Integer mailAtempt=0;
	private Integer confirmAtempt=0; 
	private Date mailAttemptsDate;
	private Date confirmAttemptsDate;
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public Integer getMailAtempt() {
		return mailAtempt;
	}
	public void setMailAtempt(Integer mailAtempt) {
		this.mailAtempt = mailAtempt;
	}
	public Integer getConfirmAtempt() {
		return confirmAtempt;
	}
	public void setConfirmAtempt(Integer confirmAtempt) {
		this.confirmAtempt = confirmAtempt;
	}
	public Date getMailAttemptsDate() {
		return mailAttemptsDate;
	}
	public void setMailAttemptsDate(Date mailAttemptsDate) {
		this.mailAttemptsDate = mailAttemptsDate;
	}
	public Date getConfirmAttemptsDate() {
		return confirmAttemptsDate;
	}
	public void setConfirmAttemptsDate(Date confirmAttemptsDate) {
		this.confirmAttemptsDate = confirmAttemptsDate;
	}
	public String getMesgSeq() {
		return mesgSeq;
	}
	public void setMesgSeq(String mesgSeq) {
		this.mesgSeq = mesgSeq;
	}

}
