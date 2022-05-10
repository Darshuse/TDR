package com.eastnets.response.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppendixDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private long aid;
	private long appeSUmidh;
	private long appeSUmidl;
	private long appeInstNum = 0;
	private Date appeDateTime;
	private long appeSeqNbr;
	private String appeType;
	private String appeNetworkDeliveryStatus;
	private String appeRcvDeliveryStatus;

	public AppendixDTO(long aid, long appeSUmidh, long appeSUmidl, long appeInstNum, Date appeDateTime, long appeSeqNbr,
			String appeType, String appeNetworkDeliveryStatus, String appeRcvDeliveryStatus) {
		super();
		this.aid = aid;
		this.appeSUmidh = appeSUmidh;
		this.appeSUmidl = appeSUmidl;
		this.appeInstNum = appeInstNum;
		this.appeDateTime = appeDateTime;
		this.appeSeqNbr = appeSeqNbr;
		this.appeType = appeType;
		this.appeNetworkDeliveryStatus = appeNetworkDeliveryStatus;
		this.appeRcvDeliveryStatus = appeRcvDeliveryStatus;
	}

	public AppendixDTO() {
		super();
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

	public Date getAppeDateTime() {
		return appeDateTime;
	}

	public void setAppeDateTime(Date appeDateTime) {
		this.appeDateTime = appeDateTime;
	}

	public long getAppeSeqNbr() {
		return appeSeqNbr;
	}

	public void setAppeSeqNbr(long appeSeqNbr) {
		this.appeSeqNbr = appeSeqNbr;
	}

	public String getAppeType() {
		return appeType;
	}

	public void setAppeType(String appeType) {
		this.appeType = appeType;
	}

	public String getAppeNetworkDeliveryStatus() {
		return appeNetworkDeliveryStatus;
	}

	public void setAppeNetworkDeliveryStatus(String appeNetworkDeliveryStatus) {
		this.appeNetworkDeliveryStatus = appeNetworkDeliveryStatus;
	}

	public String getAppeRcvDeliveryStatus() {
		return appeRcvDeliveryStatus;
	}

	public void setAppeRcvDeliveryStatus(String appeRcvDeliveryStatus) {
		this.appeRcvDeliveryStatus = appeRcvDeliveryStatus;
	}

	@Override
	public String toString() {
		return "AppendixDTO [aid=" + aid + ", appeSUmidh=" + appeSUmidh + ", appeSUmidl=" + appeSUmidl
				+ ", appeInstNum=" + appeInstNum + ", appeDateTime=" + appeDateTime + ", appeSeqNbr=" + appeSeqNbr
				+ ", appeType=" + appeType + ", appeNetworkDeliveryStatus=" + appeNetworkDeliveryStatus
				+ ", appeRcvDeliveryStatus=" + appeRcvDeliveryStatus + "]";
	}

	
}
