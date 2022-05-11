package com.eastnets.notifier.domain;

import java.sql.Clob;
import java.util.Date;

public class Appendix {

	private String netwrokStatus;
	private String sourceEntity;
	// APPE_IAPP_NAME APPE_TYPE

	private String appendixType;

	private String appendixIappName;

	// fields for RJE
	private Integer appeSessionNbr; // appe_session_nbr
	private Integer appeSequenceNbr; // appe_sequence_nbr
	private String appeSessionHolder;// appe_session_holder
	private Date appeLocalOutputTime;// appe_local_output_time
	private Date appeRemoteInputTime; // appe_remote_input_time
	private String appeRemoteInputReference; // appe_remote_input_reference
	private Clob appeAckNackText; // appe_ack_nack_text
	private String appeChecksumValue; // appe_checksum_value
	private String appeChecksumResult; // appe_checksum_result
	private Clob appeAuthValue; // appe_auth_value
	private String appeAuthResult; // appe_auth_result

	public String getNetwrokStatus() {
		return netwrokStatus;
	}

	public void setNetwrokStatus(String netwrokStatus) {
		this.netwrokStatus = netwrokStatus;
	}

	public String getSourceEntity() {
		return sourceEntity;
	}

	public void setSourceEntity(String sourceEntity) {
		this.sourceEntity = sourceEntity;
	}

	@Override
	public String toString() {
		return "Appendix [netwrokStatus=" + netwrokStatus + ", sourceEntity=" + sourceEntity + "]";
	}

	public Integer getAppeSessionNbr() {
		return appeSessionNbr;
	}

	public void setAppeSessionNbr(Integer appeSessionNbr) {
		this.appeSessionNbr = appeSessionNbr;
	}

	public Integer getAppeSequenceNbr() {
		return appeSequenceNbr;
	}

	public void setAppeSequenceNbr(Integer appeSequenceNbr) {
		this.appeSequenceNbr = appeSequenceNbr;
	}

	public String getAppeSessionHolder() {
		return appeSessionHolder;
	}

	public void setAppeSessionHolder(String appeSessionHolder) {
		this.appeSessionHolder = appeSessionHolder;
	}

	public Date getAppeLocalOutputTime() {
		return appeLocalOutputTime;
	}

	public void setAppeLocalOutputTime(Date appeLocalOutputTime) {
		this.appeLocalOutputTime = appeLocalOutputTime;
	}

	public Date getAppeRemoteInputTime() {
		return appeRemoteInputTime;
	}

	public void setAppeRemoteInputTime(Date appeRemoteInputTime) {
		this.appeRemoteInputTime = appeRemoteInputTime;
	}

	public String getAppeRemoteInputReference() {
		return appeRemoteInputReference;
	}

	public void setAppeRemoteInputReference(String appeRemoteInputReference) {
		this.appeRemoteInputReference = appeRemoteInputReference;
	}

	public Clob getAppeAckNackText() {
		return appeAckNackText;
	}

	public void setAppeAckNackText(Clob appeAckNackText) {
		this.appeAckNackText = appeAckNackText;
	}

	public String getAppeChecksumValue() {
		return appeChecksumValue;
	}

	public void setAppeChecksumValue(String appeChecksumValue) {
		this.appeChecksumValue = appeChecksumValue;
	}

	public String getAppeChecksumResult() {
		return appeChecksumResult;
	}

	public void setAppeChecksumResult(String appeChecksumResult) {
		this.appeChecksumResult = appeChecksumResult;
	}

	public Clob getAppeAuthValue() {
		return appeAuthValue;
	}

	public void setAppeAuthValue(Clob appeAuthValue) {
		this.appeAuthValue = appeAuthValue;
	}

	public String getAppeAuthResult() {
		return appeAuthResult;
	}

	public void setAppeAuthResult(String appeAuthResult) {
		this.appeAuthResult = appeAuthResult;
	}

	public String getAppendixType() {
		return appendixType;
	}

	public void setAppendixType(String appendixType) {
		this.appendixType = appendixType;
	}

	public String getAppendixIappName() {
		return appendixIappName;
	}

	public void setAppendixIappName(String appendixIappName) {
		this.appendixIappName = appendixIappName;
	}

}
