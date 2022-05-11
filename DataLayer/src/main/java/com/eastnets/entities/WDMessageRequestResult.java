package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "WDUSERREQUESTRESULT")
public class WDMessageRequestResult extends WDSearchResult {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userResultSeq")
	@SequenceGenerator(name = "userResultSeq", sequenceName = "WDUSERREQUESTRESULT_ID", allocationSize = 1)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "AID")
	private Integer aid;

	@Column(name = "APPE_S_UMIDL")
	private Integer appeUmidl;

	@Column(name = "APPE_S_UMIDH")
	private Integer appeUmidh;

	@Column(name = "APPE_INST_NUM")
	private Integer appeInstNum;

	@Column(name = "APPE_DATE_TIME")
	private Date appeDateTime;

	@Column(name = "APPE_SEQ_NBR")
	private Integer appeSeqNumber;

	@Column(name = "REQUEST_ID")
	private Integer requestId;

	@Column(name = "MESG_CREA_OPER_NICKNAME")
	private String mesgCreateOperNickname;

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
	private Double xFinAmount;

	@Column(name = "X_FIN_VALUE_DATE")
	private Date xFinValueDate;

	@Column(name = "APPE_SESSION_HOLDER")
	private String appeSessionHolder;

	@Column(name = "APPE_CREA_MPFN_NAME")
	private String appeCreaMPFNName;

	@Column(name = "APPE_SESSION_NBR")
	private Integer appeSessionNumber;

	@Column(name = "APPE_SEQUENCE_NBR")
	private Integer appeSequenceNumber;

	@Column(name = "INSERT_TIME")
	private Date insertTime;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "USERGROUP")
	private Integer userGroup;

	@Column(name = "PROCESSED")
	private Integer processed;

	@Column(name = "IDENTIFIER")
	private String identifier;

	@Column(name = "REQUESTOR_DN")
	private String requestorDN;

	@Column(name = "RESPONDER_DN")
	private String responderDN;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getAppeUmidl() {
		return appeUmidl;
	}

	public void setAppeUmidl(Integer appeUmidl) {
		this.appeUmidl = appeUmidl;
	}

	public Integer getAppeUmidh() {
		return appeUmidh;
	}

	public void setAppeUmidh(Integer appeUmidh) {
		this.appeUmidh = appeUmidh;
	}

	public Integer getAppeInstNum() {
		return appeInstNum;
	}

	public void setAppeInstNum(Integer appeInstNum) {
		this.appeInstNum = appeInstNum;
	}

	public Date getAppeDateTime() {
		return appeDateTime;
	}

	public void setAppeDateTime(Date appeDateTime) {
		this.appeDateTime = appeDateTime;
	}

	public Integer getAppeSeqNumber() {
		return appeSeqNumber;
	}

	public void setAppeSeqNumber(Integer appeSeqNumber) {
		this.appeSeqNumber = appeSeqNumber;
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

	public Double getxFinAmount() {
		return xFinAmount;
	}

	public void setxFinAmount(Double xFinAmount) {
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

	public Integer getAppeSessionNumber() {
		return appeSessionNumber;
	}

	public void setAppeSessionNumber(Integer appeSessionNumber) {
		this.appeSessionNumber = appeSessionNumber;
	}

	public Integer getAppeSequenceNumber() {
		return appeSequenceNumber;
	}

	public void setAppeSequenceNumber(Integer appeSequenceNumber) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aid == null) ? 0 : aid.hashCode());
		result = prime * result + ((appeCreaMPFNName == null) ? 0 : appeCreaMPFNName.hashCode());
		result = prime * result + ((appeDateTime == null) ? 0 : appeDateTime.hashCode());
		result = prime * result + ((appeInstNum == null) ? 0 : appeInstNum.hashCode());
		result = prime * result + ((appeSeqNumber == null) ? 0 : appeSeqNumber.hashCode());
		result = prime * result + ((appeSequenceNumber == null) ? 0 : appeSequenceNumber.hashCode());
		result = prime * result + ((appeSessionHolder == null) ? 0 : appeSessionHolder.hashCode());
		result = prime * result + ((appeSessionNumber == null) ? 0 : appeSessionNumber.hashCode());
		result = prime * result + ((appeUmidh == null) ? 0 : appeUmidh.hashCode());
		result = prime * result + ((appeUmidl == null) ? 0 : appeUmidl.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((insertTime == null) ? 0 : insertTime.hashCode());
		result = prime * result + ((mesgCreateDateTime == null) ? 0 : mesgCreateDateTime.hashCode());
		result = prime * result + ((mesgCreateOperNickname == null) ? 0 : mesgCreateOperNickname.hashCode());
		result = prime * result + ((mesgReceiverSwiftAddress == null) ? 0 : mesgReceiverSwiftAddress.hashCode());
		result = prime * result + ((mesgSenderSwiftAddress == null) ? 0 : mesgSenderSwiftAddress.hashCode());
		result = prime * result + ((mesgSubFormat == null) ? 0 : mesgSubFormat.hashCode());
		result = prime * result + ((mesgTrnRef == null) ? 0 : mesgTrnRef.hashCode());
		result = prime * result + ((mesgType == null) ? 0 : mesgType.hashCode());
		result = prime * result + ((processed == null) ? 0 : processed.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((requestorDN == null) ? 0 : requestorDN.hashCode());
		result = prime * result + ((responderDN == null) ? 0 : responderDN.hashCode());
		result = prime * result + ((userGroup == null) ? 0 : userGroup.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((xFinAmount == null) ? 0 : xFinAmount.hashCode());
		result = prime * result + ((xFinCcy == null) ? 0 : xFinCcy.hashCode());
		result = prime * result + ((xFinValueDate == null) ? 0 : xFinValueDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WDMessageRequestResult other = (WDMessageRequestResult) obj;
		if (aid == null) {
			if (other.aid != null)
				return false;
		} else if (!aid.equals(other.aid))
			return false;
		if (appeCreaMPFNName == null) {
			if (other.appeCreaMPFNName != null)
				return false;
		} else if (!appeCreaMPFNName.equals(other.appeCreaMPFNName))
			return false;
		if (appeDateTime == null) {
			if (other.appeDateTime != null)
				return false;
		} else if (!appeDateTime.equals(other.appeDateTime))
			return false;
		if (appeInstNum == null) {
			if (other.appeInstNum != null)
				return false;
		} else if (!appeInstNum.equals(other.appeInstNum))
			return false;
		if (appeSeqNumber == null) {
			if (other.appeSeqNumber != null)
				return false;
		} else if (!appeSeqNumber.equals(other.appeSeqNumber))
			return false;
		if (appeSequenceNumber == null) {
			if (other.appeSequenceNumber != null)
				return false;
		} else if (!appeSequenceNumber.equals(other.appeSequenceNumber))
			return false;
		if (appeSessionHolder == null) {
			if (other.appeSessionHolder != null)
				return false;
		} else if (!appeSessionHolder.equals(other.appeSessionHolder))
			return false;
		if (appeSessionNumber == null) {
			if (other.appeSessionNumber != null)
				return false;
		} else if (!appeSessionNumber.equals(other.appeSessionNumber))
			return false;
		if (appeUmidh == null) {
			if (other.appeUmidh != null)
				return false;
		} else if (!appeUmidh.equals(other.appeUmidh))
			return false;
		if (appeUmidl == null) {
			if (other.appeUmidl != null)
				return false;
		} else if (!appeUmidl.equals(other.appeUmidl))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (insertTime == null) {
			if (other.insertTime != null)
				return false;
		} else if (!insertTime.equals(other.insertTime))
			return false;
		if (mesgCreateDateTime == null) {
			if (other.mesgCreateDateTime != null)
				return false;
		} else if (!mesgCreateDateTime.equals(other.mesgCreateDateTime))
			return false;
		if (mesgCreateOperNickname == null) {
			if (other.mesgCreateOperNickname != null)
				return false;
		} else if (!mesgCreateOperNickname.equals(other.mesgCreateOperNickname))
			return false;
		if (mesgReceiverSwiftAddress == null) {
			if (other.mesgReceiverSwiftAddress != null)
				return false;
		} else if (!mesgReceiverSwiftAddress.equals(other.mesgReceiverSwiftAddress))
			return false;
		if (mesgSenderSwiftAddress == null) {
			if (other.mesgSenderSwiftAddress != null)
				return false;
		} else if (!mesgSenderSwiftAddress.equals(other.mesgSenderSwiftAddress))
			return false;
		if (mesgSubFormat == null) {
			if (other.mesgSubFormat != null)
				return false;
		} else if (!mesgSubFormat.equals(other.mesgSubFormat))
			return false;
		if (mesgTrnRef == null) {
			if (other.mesgTrnRef != null)
				return false;
		} else if (!mesgTrnRef.equals(other.mesgTrnRef))
			return false;
		if (mesgType == null) {
			if (other.mesgType != null)
				return false;
		} else if (!mesgType.equals(other.mesgType))
			return false;
		if (processed == null) {
			if (other.processed != null)
				return false;
		} else if (!processed.equals(other.processed))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (requestorDN == null) {
			if (other.requestorDN != null)
				return false;
		} else if (!requestorDN.equals(other.requestorDN))
			return false;
		if (responderDN == null) {
			if (other.responderDN != null)
				return false;
		} else if (!responderDN.equals(other.responderDN))
			return false;
		if (userGroup == null) {
			if (other.userGroup != null)
				return false;
		} else if (!userGroup.equals(other.userGroup))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (xFinAmount == null) {
			if (other.xFinAmount != null)
				return false;
		} else if (!xFinAmount.equals(other.xFinAmount))
			return false;
		if (xFinCcy == null) {
			if (other.xFinCcy != null)
				return false;
		} else if (!xFinCcy.equals(other.xFinCcy))
			return false;
		if (xFinValueDate == null) {
			if (other.xFinValueDate != null)
				return false;
		} else if (!xFinValueDate.equals(other.xFinValueDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WDMessageRequestResult [id=" + id + ", aid=" + aid + ", appeUmidl=" + appeUmidl + ", appeUmidh="
				+ appeUmidh + ", appeInstNum=" + appeInstNum + ", appeDateTime=" + appeDateTime + ", appeSeqNumber="
				+ appeSeqNumber + ", requestId=" + requestId + ", mesgCreateOperNickname=" + mesgCreateOperNickname
				+ ", mesgCreateDateTime=" + mesgCreateDateTime + ", mesgReceiverSwiftAddress="
				+ mesgReceiverSwiftAddress + ", mesgSenderSwiftAddress=" + mesgSenderSwiftAddress + ", mesgSubFormat="
				+ mesgSubFormat + ", mesgType=" + mesgType + ", mesgTrnRef=" + mesgTrnRef + ", xFinCcy=" + xFinCcy
				+ ", xFinAmount=" + xFinAmount + ", xFinValueDate=" + xFinValueDate + ", appeSessionHolder="
				+ appeSessionHolder + ", appeCreaMPFNName=" + appeCreaMPFNName + ", appeSessionNumber="
				+ appeSessionNumber + ", appeSequenceNumber=" + appeSequenceNumber + ", insertTime=" + insertTime
				+ ", description=" + description + ", username=" + username + ", userGroup=" + userGroup
				+ ", processed=" + processed + ", identifier=" + identifier + ", requestorDN=" + requestorDN
				+ ", responderDN=" + responderDN + "]";
	}

}
