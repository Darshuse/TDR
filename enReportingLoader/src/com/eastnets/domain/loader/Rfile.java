package com.eastnets.domain.loader;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "rfile")
@NamedQuery(name = "Rfile.findAll", query = "SELECT m FROM Rfile m")
@Cacheable(value = false)
public class Rfile extends AbstractReportingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8179373639369479390L;

	@EmbeddedId
	private RfilePK id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MESG_CREA_DATE_TIME")
	private Date mesgCreaDateTime;
	@Column(name = "MESG_TRANSFER_DESC")
	private String mesgTranDesc = "";
	@Column(name = "MESG_TRANSFER_INFO")
	private String mesgTranInfo = "";
	@Column(name = "MESG_FILE_LOGICAL_NAME")
	private String mesgFileLogicalName = "";
	@Column(name = "MESG_FILE_DESC")
	private String mesgFileDesc = "";
	@Column(name = "MESG_FILE_INFO")
	private String mesgFileInfo = "";
	@Column(name = "MESG_FILE_DIGEST_ALGO")
	private String mesgFileDigestAlgo = "";
	@Column(name = "MESG_FILE_DIGEST_VALUE")
	private String mesgFileDigestValue = "";
	@Column(name = "MESG_DELV_NOTIF_REQ_RECDN")
	private String mesgDelvNotifReqRecdn = "";
	@Column(name = "MESG_DELV_NOTIF_REQ_MTYPE")
	private String mesgDelvNotifReqMtype = "";
	@Column(name = "TRANSFER_STATUS")
	private String transferStatus = "";
	@Column(name = "MESG_FILE_HEADER_INFO")
	private String mesgFileHeaderInfo = "";

	@Lob
	@Column(name = "PAYLOAD")
	private byte[] payload;

	@Column(name = "PAYLOAD_TEXT")
	private String payloadTest;

	@Column(name = "MESG_FILE_SIZE")
	private long mesgFileSize = 0;
	@Column(name = "TRIALS_COUNT")
	private long trialsCount = 0;
	@Column(name = "REQUESTED")
	private long requested = 0;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_TRY")
	private Date lastTry;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({ @JoinColumn(name = "AID", referencedColumnName = "AID", insertable = false, updatable = false),
			@JoinColumn(name = "FILE_S_UMIDH", referencedColumnName = "MESG_S_UMIDH", insertable = false, updatable = false),
			@JoinColumn(name = "FILE_S_UMIDL", referencedColumnName = "MESG_S_UMIDL", insertable = false, updatable = false) })
	private Mesg rmesg;

	public RfilePK getId() {
		return id;
	}

	public void setId(RfilePK id) {
		this.id = id;
	}

	public String getMesgTranDesc() {
		return mesgTranDesc;
	}

	public void setMesgTranDesc(String mesgTranDesc) {
		this.mesgTranDesc = mesgTranDesc;
	}

	public String getMesgTranInfo() {
		return mesgTranInfo;
	}

	public void setMesgTranInfo(String mesgTranInfo) {
		this.mesgTranInfo = mesgTranInfo;
	}

	public String getMesgFileLogicalName() {
		return mesgFileLogicalName;
	}

	public void setMesgFileLogicalName(String mesgFileLogicalName) {
		this.mesgFileLogicalName = mesgFileLogicalName;
	}

	public String getMesgFileDesc() {
		return mesgFileDesc;
	}

	public void setMesgFileDesc(String mesgFileDesc) {
		this.mesgFileDesc = mesgFileDesc;
	}

	public String getMesgFileInfo() {
		return mesgFileInfo;
	}

	public void setMesgFileInfo(String mesgFileInfo) {
		this.mesgFileInfo = mesgFileInfo;
	}

	public String getMesgFileDigestAlgo() {
		return mesgFileDigestAlgo;
	}

	public void setMesgFileDigestAlgo(String mesgFileDigestAlgo) {
		this.mesgFileDigestAlgo = mesgFileDigestAlgo;
	}

	public String getMesgFileDigestValue() {
		return mesgFileDigestValue;
	}

	public void setMesgFileDigestValue(String mesgFileDigestValue) {
		this.mesgFileDigestValue = mesgFileDigestValue;
	}

	public String getMesgDelvNotifReqRecdn() {
		return mesgDelvNotifReqRecdn;
	}

	public void setMesgDelvNotifReqRecdn(String mesgDelvNotifReqRecdn) {
		this.mesgDelvNotifReqRecdn = mesgDelvNotifReqRecdn;
	}

	public String getMesgDelvNotifReqMtype() {
		return mesgDelvNotifReqMtype;
	}

	public void setMesgDelvNotifReqMtype(String mesgDelvNotifReqMtype) {
		this.mesgDelvNotifReqMtype = mesgDelvNotifReqMtype;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public String getMesgFileHeaderInfo() {
		return mesgFileHeaderInfo;
	}

	public void setMesgFileHeaderInfo(String mesgFileHeaderInfo) {
		this.mesgFileHeaderInfo = mesgFileHeaderInfo;
	}

	public String getPayloadTest() {
		return payloadTest;
	}

	public void setPayloadTest(String payloadTest) {
		this.payloadTest = payloadTest;
	}

	public long getMesgFileSize() {
		return mesgFileSize;
	}

	public void setMesgFileSize(long mesgFileSize) {
		this.mesgFileSize = mesgFileSize;
	}

	public long getTrialsCount() {
		return trialsCount;
	}

	public void setTrialsCount(long trialsCount) {
		this.trialsCount = trialsCount;
	}

	public long getRequested() {
		return requested;
	}

	public void setRequested(long requested) {
		this.requested = requested;
	}

	public Date getLastTry() {
		return lastTry;
	}

	public void setLastTry(Date lastTry) {
		this.lastTry = lastTry;
	}

	public Mesg getRmesg() {
		return rmesg;
	}

	public void setRmesg(Mesg rmesg) {
		this.rmesg = rmesg;
	}

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

}
