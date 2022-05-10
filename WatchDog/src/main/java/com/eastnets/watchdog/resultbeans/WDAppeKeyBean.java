package com.eastnets.watchdog.resultbeans;

import java.util.Date;

public class WDAppeKeyBean {

	private long aid;

	private long appeSUmidh;

	private long appeSUmidl;

	private long appeInstNum = 0;

	private java.util.Date appeDateTime;

	private long appeSeqNbr;

	private Date lastUpdate;

	private Date xCreaDtetime;

	public WDAppeKeyBean(long aid, long appeSUmidh, long appeSUmidl, long appeInstNum, Date appeDateTime, long appeSeqNbr, Date lastUpdate) {
		super();
		this.aid = aid;
		this.appeSUmidh = appeSUmidh;
		this.appeSUmidl = appeSUmidl;
		this.appeInstNum = appeInstNum;
		this.appeDateTime = appeDateTime;
		this.appeSeqNbr = appeSeqNbr;
		this.lastUpdate = lastUpdate;

	}

	public WDAppeKeyBean(long aid, long appeSUmidh, long appeSUmidl, long appeInstNum, Date appeDateTime, long appeSeqNbr, Date lastUpdate, Date xCreaDtetime) {
		super();
		this.aid = aid;
		this.appeSUmidh = appeSUmidh;
		this.appeSUmidl = appeSUmidl;
		this.appeInstNum = appeInstNum;
		this.appeDateTime = appeDateTime;
		this.appeSeqNbr = appeSeqNbr;
		this.lastUpdate = lastUpdate;
		this.xCreaDtetime = xCreaDtetime;
	}

	public Date getxCreaDtetime() {
		return xCreaDtetime;
	}

	public void setxCreaDtetime(Date xCreaDtetime) {
		this.xCreaDtetime = xCreaDtetime;
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

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
