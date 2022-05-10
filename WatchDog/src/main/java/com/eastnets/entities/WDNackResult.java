package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "WDNACKRESULT")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class WDNackResult {

	@Id
	@Column(name = "SYSID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "AID")
	private long aid;

	@Column(name = "APPE_S_UMIDH")
	private long appeSUmidh;

	@Column(name = "APPE_S_UMIDL")
	private long appeSUmidl;

	@Column(name = "APPE_INST_NUM")
	private long appeInstNum = 0;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPE_DATE_TIME")
	private java.util.Date appeDateTime;

	@Column(name = "APPE_SEQ_NBR")
	private long appeSeqNbr;

	@Column(name = "INSERT_TIME")
	private Date insertTime;

	@Transient
	private Date tempCreaDateTime;

	public Date getTempCreaDateTime() {
		return tempCreaDateTime;
	}

	public void setTempCreaDateTime(Date tempCreaDateTime) {
		this.tempCreaDateTime = tempCreaDateTime;
	}

	public AppendixPK getAppendixPK() {
		AppendixPK id = new AppendixPK();
		id.setAid(this.aid);
		id.setAppeSUmidh(this.appeSUmidh);
		id.setAppeSUmidl(this.appeSUmidl);
		id.setAppeInstNum(this.appeInstNum);
		return id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getAppeSUmidh() {
		return appeSUmidh;
	}

	public void setAppeSUmidh(long appeSUmidh) {
		this.appeSUmidh = appeSUmidh;
	}

	public long getAppeSUmidl() {
		return appeSUmidl;
	}

	public void setAppeSUmidl(long appeSUmidl) {
		this.appeSUmidl = appeSUmidl;
	}

	public long getAppeInstNum() {
		return appeInstNum;
	}

	public void setAppeInstNum(long appeInstNum) {
		this.appeInstNum = appeInstNum;
	}

	public java.util.Date getAppeDateTime() {
		return appeDateTime;
	}

	public void setAppeDateTime(java.util.Date appeDateTime) {
		this.appeDateTime = appeDateTime;
	}

	public long getAppeSeqNbr() {
		return appeSeqNbr;
	}

	public void setAppeSeqNbr(long appeSeqNbr) {
		this.appeSeqNbr = appeSeqNbr;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Override
	public String toString() {
		return "WDNackResult [id=" + id + ", aid=" + aid + ", appeSUmidh=" + appeSUmidh + ", appeSUmidl=" + appeSUmidl + ", appeInstNum=" + appeInstNum + ", appeDateTime=" + appeDateTime + ", appeSeqNbr=" + appeSeqNbr + ", insertTime=" + insertTime
				+ "]";
	}

	public void setAppendixKey(AppendixPK id) {
		this.aid = id.getAid();
		this.appeSUmidh = id.getAppeSUmidh();
		this.appeSUmidl = id.getAppeSUmidl();
		this.appeDateTime = id.getAppeDateTime();
		this.appeInstNum = id.getAppeInstNum();
		this.appeSeqNbr = id.getAppeSeqNbr();
	}
}
