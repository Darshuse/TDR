package com.eastnets.domain.viewer;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

public class InstanceTransmissionPrintInfo implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6720103398496097864L;
	private String 	mesgSubFormat;
	private String 	mesgNetworkPriority;
	private Integer lastInstNum;
	private String 	lastInstType;
	private String 	lastInstNotificationType;
	private Integer	lastInstRelatedNbr;
	private String 	firstInstType;
	private String 	appeIappName;
	private String 	appeNetworkDeliveryStatus;
	private String 	appeNakReason;
	private String 	appeRcvDeliveryStatus;    
	private Date 	appeDateTime;    
	private String 	appeRemoteInputReference ;    
	private Date 	appeRemoteInputTime;    
	private Date 	appeLocalOutputTime;    
	private String 	appeSessionHolder;    
	private Integer	appeSessionNbr;    
	private Integer	appeSequenceNbr;    
	private String 	textSwiftBlock5;    
	private String 	appeType ;    
	private String 	appeAckNackText;
	private Integer timeZoneOffset;
	
	public void setMesgSubFormat(String mesgSubFormat) {
		this.mesgSubFormat = mesgSubFormat;
	}
	public String getMesgSubFormat() {
		return mesgSubFormat;
	}
	public void setMesgNetworkPriority(String mesgNetworkPriority) {
		this.mesgNetworkPriority = mesgNetworkPriority;
	}
	public String getMesgNetworkPriority() {
		return mesgNetworkPriority;
	}
	public void setLastInstNum(Integer lastInstNum) {
		this.lastInstNum = lastInstNum;
	}
	public Integer getLastInstNum() {
		return lastInstNum;
	}
	public void setLastInstType(String lastInstType) {
		this.lastInstType = lastInstType;
	}
	public String getLastInstType() {
		return lastInstType;
	}
	public void setLastInstNotificationType(String lastInstNotificationType) {
		this.lastInstNotificationType = lastInstNotificationType;
	}
	public String getLastInstNotificationType() {
		return lastInstNotificationType;
	}
	public void setLastInstRelatedNbr(Integer lastInstRelatedNbr) {
		this.lastInstRelatedNbr = lastInstRelatedNbr;
	}
	public Integer getLastInstRelatedNbr() {
		return lastInstRelatedNbr;
	}
	public void setFirstInstType(String firstInstType) {
		this.firstInstType = firstInstType;
	}
	public String getFirstInstType() {
		return firstInstType;
	}
	public void setAppeIappName(String appeIappName) {
		this.appeIappName = appeIappName;
	}
	public String getAppeIappName() {
		return appeIappName;
	}
	public void setAppeNetworkDeliveryStatus(String appeNetworkDeliveryStatus) {
		this.appeNetworkDeliveryStatus = appeNetworkDeliveryStatus;
	}
	public String getAppeNetworkDeliveryStatus() {
		return appeNetworkDeliveryStatus;
	}
	public void setAppeNakReason(String appeNakReason) {
		this.appeNakReason = appeNakReason;
	}
	public String getAppeNakReason() {
		return appeNakReason;
	}
	public void setAppeRcvDeliveryStatus(String appeRcvDeliveryStatus) {
		this.appeRcvDeliveryStatus = appeRcvDeliveryStatus;
	}
	public String getAppeRcvDeliveryStatus() {
		return appeRcvDeliveryStatus;
	}
	public void setAppeDateTime(Date appeDateTime) {
		this.appeDateTime = appeDateTime;
	}
	public Date getAppeDateTimeOnDB() {
		return appeDateTime;
	}
	public Date getAppeDateTime() {
		if ( appeDateTime == null ){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(appeDateTime);
		if ( timeZoneOffset != null ){
			cal.add( Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date( cal.getTime().getTime() );
	}
	public void setAppeRemoteInputReference(String appeRemoteInputReference) {
		this.appeRemoteInputReference = appeRemoteInputReference;
	}
	public String getAppeRemoteInputReference() {
		return appeRemoteInputReference;
	}
	public void setAppeRemoteInputTime(Date appeRemoteInputTime) {
		this.appeRemoteInputTime = appeRemoteInputTime;
	}
	public Date getAppeRemoteInputTimeOnDB() {
		return appeRemoteInputTime;
	}
	public Date getAppeRemoteInputTime() {
		if ( appeRemoteInputTime == null ){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(appeRemoteInputTime);
		if ( timeZoneOffset != null ){
			cal.add( Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date( cal.getTime().getTime() );
	}
	public void setAppeLocalOutputTime(Date appeLocalOutputTime) {
		this.appeLocalOutputTime = appeLocalOutputTime;
	}
	public Date getAppeLocalOutputTimeOnDB() {
		return appeLocalOutputTime;
	}
	public Date getAppeLocalOutputTime() {
		if ( appeLocalOutputTime == null ){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(appeLocalOutputTime);
		if ( timeZoneOffset != null ){
			cal.add( Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date( cal.getTime().getTime() );
	}
	public void setAppeSessionHolder(String appeSessionHolder) {
		this.appeSessionHolder = appeSessionHolder;
	}
	public String getAppeSessionHolder() {
		return appeSessionHolder;
	}
	public void setAppeSessionNbr(Integer appeSessionNbr) {
		this.appeSessionNbr = appeSessionNbr;
	}
	public Integer getAppeSessionNbr() {
		return appeSessionNbr;
	}
	public void setAppeSequenceNbr(Integer appeSequenceNbr) {
		this.appeSequenceNbr = appeSequenceNbr;
	}
	public Integer getAppeSequenceNbr() {
		return appeSequenceNbr;
	}
	public void setTextSwiftBlock5(String textSwiftBlock5) {
		this.textSwiftBlock5 = textSwiftBlock5;
	}
	public String getTextSwiftBlock5() {
		return textSwiftBlock5;
	}
	public void setAppeType(String appeType) {
		this.appeType = appeType;
	}
	public String getAppeType() {
		return appeType;
	}
	public void setAppeAckNackText(String appeAckNackText) {
		this.appeAckNackText = appeAckNackText;
	}
	public String getAppeAckNackText() {
		return appeAckNackText;
	}
	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}
	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}
}
