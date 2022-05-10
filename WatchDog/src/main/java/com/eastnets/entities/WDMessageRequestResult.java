package com.eastnets.entities;

import java.math.BigDecimal;
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
@Table(name = "WDUSERREQUESTRESULT")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class WDMessageRequestResult extends WDSearchResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "AID")
	private long aid;

	@Column(name = "APPE_S_UMIDL")
	private long appeUmidl;

	@Column(name = "APPE_S_UMIDH")
	private long appeUmidh;

	@Column(name = "APPE_INST_NUM")
	private long appeInstNum;

	@Column(name = "APPE_DATE_TIME")
	private Date appeDateTime;

	@Column(name = "APPE_SEQ_NBR")
	private long appeSeqNumber;

	@Column(name = "REQUEST_ID")
	private Integer requestId;

	@Column(name = "MESG_CREA_OPER_NICKNAME")
	private String mesgCreateOperNickname;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MESG_CREA_DATE_TIME")
	private Date mesgCreateDateTime;

	@Column(name = "MESG_RECEIVER_SWIFT_ADDRESS")
	private String mesgReceiverSwiftAddress;

	@Column(name = "MESG_SENDER_SWIFT_ADDRESS")
	private String mesgSenderSwiftAddress;

	@Column(name = "MESG_SUB_FORMAT")
	private String mesgSubFormat;

	@Column(name = "MESG_TYPE")
	private String mesgType;

	@Column(name = "MESG_TRN_REF")
	private String mesgTrnRef;

	@Column(name = "X_FIN_CCY")
	private String xFinCcy;

	@Column(name = "X_FIN_AMOUNT")
	private BigDecimal xFinAmount;

	@Column(name = "X_FIN_VALUE_DATE")
	private Date xFinValueDate;

	@Column(name = "APPE_SESSION_HOLDER")
	private String appeSessionHolder;

	@Column(name = "APPE_CREA_MPFN_NAME")
	private String appeCreaMPFNName;

	@Column(name = "APPE_SESSION_NBR")
	private BigDecimal appeSessionNumber;

	@Column(name = "APPE_SEQUENCE_NBR")
	private BigDecimal appeSequenceNumber;

	@Column(name = "INSERT_TIME")
	private Date insertTime;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "USERGROUP")
	private Integer userGroup;

	@Column(name = "PROCESSED")
	private Integer processed = 0;

	@Column(name = "IDENTIFIER")
	private String identifier;

	@Column(name = "REQUESTOR_DN")
	private String requestorDN;

	@Column(name = "RESPONDER_DN")
	private String responderDN;

	@Transient
	private boolean addToEmailQueue;

	public AppendixPK getAppendixPK() {
		AppendixPK id = new AppendixPK();
		id.setAid(this.aid);
		id.setAppeSUmidh(this.appeUmidh);
		id.setAppeSUmidl(this.appeUmidl);
		id.setAppeInstNum(this.appeInstNum);
		id.setMesgCreateDateTime(mesgCreateDateTime);
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

	public long getAppeUmidl() {
		return appeUmidl;
	}

	public void setAppeUmidl(long appeUmidl) {
		this.appeUmidl = appeUmidl;
	}

	public long getAppeUmidh() {
		return appeUmidh;
	}

	public void setAppeUmidh(long appeUmidh) {
		this.appeUmidh = appeUmidh;
	}

	public long getAppeInstNum() {
		return appeInstNum;
	}

	public void setAppeInstNum(long appeInstNum) {
		this.appeInstNum = appeInstNum;
	}

	public long getAppeSeqNumber() {
		return appeSeqNumber;
	}

	public void setAppeSeqNumber(long appeSeqNumber) {
		this.appeSeqNumber = appeSeqNumber;
	}

	public Date getAppeDateTime() {
		return appeDateTime;
	}

	public void setAppeDateTime(Date appeDateTime) {
		this.appeDateTime = appeDateTime;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public String getMesgCreateOperNickname() {
		return mesgCreateOperNickname;
	}

	public void setMesgCreateOperNickname(String mesgCreateOperNickname) {
		this.mesgCreateOperNickname = mesgCreateOperNickname;
	}

	public Date getMesgCreateDateTime() {
		return mesgCreateDateTime;
	}

	public void setMesgCreateDateTime(Date mesgCreateDateTime) {
		this.mesgCreateDateTime = mesgCreateDateTime;
	}

	public String getMesgReceiverSwiftAddress() {
		return mesgReceiverSwiftAddress;
	}

	public void setMesgReceiverSwiftAddress(String mesgReceiverSwiftAddress) {
		this.mesgReceiverSwiftAddress = mesgReceiverSwiftAddress;
	}

	public String getMesgSenderSwiftAddress() {
		return mesgSenderSwiftAddress;
	}

	public void setMesgSenderSwiftAddress(String mesgSenderSwiftAddress) {
		this.mesgSenderSwiftAddress = mesgSenderSwiftAddress;
	}

	public String getMesgSubFormat() {
		return mesgSubFormat;
	}

	public void setMesgSubFormat(String mesgSubFormat) {
		this.mesgSubFormat = mesgSubFormat;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public String getMesgTrnRef() {
		return mesgTrnRef;
	}

	public void setMesgTrnRef(String mesgTrnRef) {
		this.mesgTrnRef = mesgTrnRef;
	}

	public String getxFinCcy() {
		return xFinCcy;
	}

	public void setxFinCcy(String xFinCcy) {
		this.xFinCcy = xFinCcy;
	}

	public BigDecimal getxFinAmount() {
		return xFinAmount;
	}

	public void setxFinAmount(BigDecimal xFinAmount) {
		this.xFinAmount = xFinAmount;
	}

	public Date getxFinValueDate() {
		return xFinValueDate;
	}

	public void setxFinValueDate(Date xFinValueDate) {
		this.xFinValueDate = xFinValueDate;
	}

	public String getAppeSessionHolder() {
		return appeSessionHolder;
	}

	public void setAppeSessionHolder(String appeSessionHolder) {
		this.appeSessionHolder = appeSessionHolder;
	}

	public String getAppeCreaMPFNName() {
		return appeCreaMPFNName;
	}

	public void setAppeCreaMPFNName(String appeCreaMPFNName) {
		this.appeCreaMPFNName = appeCreaMPFNName;
	}

	public BigDecimal getAppeSessionNumber() {
		return appeSessionNumber;
	}

	public void setAppeSessionNumber(BigDecimal appeSessionNumber) {
		this.appeSessionNumber = appeSessionNumber;
	}

	public BigDecimal getAppeSequenceNumber() {
		return appeSequenceNumber;
	}

	public void setAppeSequenceNumber(BigDecimal appeSequenceNumber) {
		this.appeSequenceNumber = appeSequenceNumber;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(Integer userGroup) {
		this.userGroup = userGroup;
	}

	public Integer getProcessed() {
		return processed;
	}

	public void setProcessed(Integer processed) {
		this.processed = processed;
	}

	public boolean isAddToEmailQueue() {
		return addToEmailQueue;
	}

	public void setAddToEmailQueue(boolean addToEmailQueue) {
		this.addToEmailQueue = addToEmailQueue;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getRequestorDN() {
		return requestorDN;
	}

	public void setRequestorDN(String requestorDN) {
		this.requestorDN = requestorDN;
	}

	public String getResponderDN() {
		return responderDN;
	}

	public void setResponderDN(String responderDN) {
		this.responderDN = responderDN;
	}

	public void setAppeID(AppendixPK appePK) {
		this.aid = appePK.getAid();
		this.appeUmidh = appePK.getAppeSUmidh();
		this.appeUmidl = appePK.getAppeSUmidl();
		this.appeInstNum = appePK.getAppeInstNum();
		this.appeSeqNumber = appePK.getAppeSeqNbr();
		this.appeDateTime = appePK.getAppeDateTime();
	}

}
