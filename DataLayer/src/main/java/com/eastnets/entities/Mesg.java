package com.eastnets.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "RMESG")
public class Mesg implements Serializable {

	private static final long serialVersionUID = 8537850318907926781L;

	@EmbeddedId
	private MesgPK id;

	private BigDecimal archived = BigDecimal.ZERO;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATE")
	private Date lastUpdate;

	@Column(name = "MESG_APPL_SENDER_REFERENCE")
	private String mesgApplSenderReference = null;

	@Column(name = "MESG_BATCH_REFERENCE")
	private String mesgBatchReference = null;

	@Column(name = "MESG_CAS_SENDER_REFERENCE")
	private String mesgCasSenderReference = null;

	@Column(name = "MESG_CAS_TARGET_RP_NAME")
	private String mesgCasTargetRpName = null;

	@Column(name = "MESG_CCY_AMOUNT")
	private String mesgCcyAmount = null;

	@Column(name = "MESG_CLASS")
	private String mesgClass = "MESG_NORMAL";

	@Column(name = "MESG_COPY_SERVICE_ID")
	private String mesgCopyServiceId;

	@Column(name = "MESG_CREA_APPL_SERV_NAME")
	private String mesgCreaApplServName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MESG_CREA_DATE_TIME")
	private Date mesgCreaDateTime;

	@Column(name = "MESG_CREA_MPFN_NAME")
	private String mesgCreaMpfnName = "mpc";

	@Column(name = "MESG_CREA_OPER_NICKNAME")
	private String mesgCreaOperNickname;

	@Column(name = "MESG_CREA_RP_NAME")
	private String mesgCreaRpName;

	@Column(name = "MESG_DATA_KEYWORD1")
	private String mesgDataKeyword1 = null;

	@Column(name = "MESG_DATA_KEYWORD2")
	private String mesgDataKeyword2 = null;

	@Column(name = "MESG_DATA_KEYWORD3")
	private String mesgDataKeyword3 = null;

	@Column(name = "MESG_DATA_LAST")
	private BigDecimal mesgDataLast = null;

	@Column(name = "MESG_DELV_OVERDUE_WARN_REQ")
	private BigDecimal mesgDelvOverdueWarnReq;

	@Column(name = "MESG_FIN_CCY_AMOUNT")
	private String mesgFinCcyAmount = null;

	@Column(name = "MESG_FIN_VALUE_DATE")
	private String mesgFinValueDate = null;

	@Column(name = "MESG_FRMT_NAME")
	private String mesgFrmtName;

	@Column(name = "MESG_IDENTIFIER")
	private String mesgIdentifier;

	@Column(name = "MESG_IS_DELETE_INHIBITED")
	private BigDecimal mesgIsDeleteInhibited = BigDecimal.ZERO;

	@Column(name = "MESG_IS_LIVE")
	private BigDecimal mesgIsLive;

	@Column(name = "MESG_IS_PARTIAL")
	private BigDecimal mesgIsPartial = BigDecimal.ZERO;

	@Column(name = "MESG_IS_RETRIEVED")
	private BigDecimal mesgIsRetrieved;

	@Column(name = "MESG_IS_TEXT_MODIFIED")
	private BigDecimal mesgIsTextModified = BigDecimal.ZERO;

	@Column(name = "MESG_IS_TEXT_READONLY")
	private BigDecimal mesgIsTextReadonly = BigDecimal.ZERO;

	@Column(name = "MESG_MESG_USER_GROUP")
	private String mesgMesgUserGroup;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MESG_MOD_DATE_TIME")
	private Date mesgModDateTime;

	@Column(name = "MESG_MOD_OPER_NICKNAME")
	private String mesgModOperNickname;

	@Column(name = "MESG_NATURE")
	private String mesgNature;

	@Column(name = "MESG_NETWORK_APPL_IND")
	private String mesgNetworkApplInd = null;

	@Column(name = "MESG_NETWORK_DELV_NOTIF_REQ")
	private BigDecimal mesgNetworkDelvNotifReq;

	@Column(name = "MESG_NETWORK_OBSO_PERIOD")
	private BigDecimal mesgNetworkObsoPeriod = null;

	@Column(name = "MESG_NETWORK_PRIORITY")
	private String mesgNetworkPriority;

	@Column(name = "MESG_PAYLOAD_TYPE")
	private String mesgPayloadType;

	@Column(name = "MESG_POSSIBLE_DUP_CREATION")
	private String mesgPossibleDupCreation;

	@Column(name = "MESG_RECEIVER_ALIA_NAME")
	private String mesgReceiverAliaName = null;

	@Column(name = "MESG_RECEIVER_SWIFT_ADDRESS")
	private String mesgReceiverSwiftAddress = null;

	@Column(name = "MESG_RECOVERY_ACCEPT_INFO")
	private String mesgRecoveryAcceptInfo = null;

	@Column(name = "MESG_REL_TRN_REF")
	private String mesgRelTrnRef = null;

	@Column(name = "MESG_RELATED_S_UMID")
	private String mesgRelatedSUmid = null;

	@Column(name = "MESG_RELEASE_INFO")
	private String mesgReleaseInfo;

	@Column(name = "MESG_REQUEST_TYPE")
	private String mesgRequestType;

	@Column(name = "MESG_REQUESTOR_DN")
	private String mesgRequestorDn;

	@Column(name = "MESG_SECURITY_IAPP_NAME")
	private String mesgSecurityIappName = null;

	@Column(name = "MESG_SECURITY_REQUIRED")
	private BigDecimal mesgSecurityRequired = null;

	@Column(name = "MESG_SENDER_BRANCH_INFO")
	private String mesgSenderBranchInfo;

	@Column(name = "MESG_SENDER_CITY_NAME")
	private String mesgSenderCityName;

	@Column(name = "MESG_SENDER_CORR_TYPE")
	private String mesgSenderCorrType;

	@Column(name = "MESG_SENDER_CTRY_CODE")
	private String mesgSenderCtryCode;

	@Column(name = "MESG_SENDER_CTRY_NAME")
	private String mesgSenderCtryName;

	@Column(name = "MESG_SENDER_INSTITUTION_NAME")
	private String mesgSenderInstitutionName;

	@Column(name = "MESG_SENDER_LOCATION")
	private String mesgSenderLocation;

	@Column(name = "MESG_SENDER_SWIFT_ADDRESS")
	private String mesgSenderSwiftAddress = null;

	@Column(name = "MESG_SENDER_X1")
	private String mesgSenderX1;

	@Column(name = "MESG_SENDER_X2")
	private String mesgSenderX2;

	@Column(name = "MESG_SENDER_X3")
	private String mesgSenderX3;

	@Column(name = "MESG_SENDER_X4")
	private String mesgSenderX4;

	@Column(name = "MESG_SERVICE")
	private String mesgService;

	@Column(name = "MESG_SIGN_DIGEST_REFERENCE")
	private String mesgSignDigestReference;

	@Column(name = "MESG_SIGN_DIGEST_VALUE")
	private String mesgSignDigestValue;

	@Column(name = "MESG_STATUS")
	private String mesgStatus = "COMPLETED";

	@Column(name = "MESG_SUB_FORMAT")
	private String mesgSubFormat;

	@Column(name = "MESG_SYNTAX_TABLE_VER")
	private String mesgSyntaxTableVer;

	@Column(name = "MESG_TEMPLATE_NAME")
	private String mesgTemplateName = null;

	@Column(name = "MESG_TOKEN")
	private BigDecimal mesgToken = BigDecimal.ZERO;

	@Column(name = "MESG_TRN_REF")
	private String mesgTrnRef = null;

	@Column(name = "MESG_TYPE")
	private String mesgType;

	@Column(name = "MESG_USE_PKI_SIGNATURE")
	private BigDecimal mesgUsePkiSignature = null;

	@Column(name = "MESG_USER_ISSUED_AS_PDE")
	private BigDecimal mesgUserIssuedAsPde;

	@Column(name = "MESG_USER_PRIORITY_CODE")
	private String mesgUserPriorityCode;

	@Column(name = "MESG_USER_REFERENCE_TEXT")
	private String mesgUserReferenceText;

	@Column(name = "MESG_UUMID")
	private String mesgUumid;

	@Column(name = "MESG_UUMID_SUFFIX")
	private BigDecimal mesgUumidSuffix = BigDecimal.ZERO;

	@Column(name = "MESG_VALIDATION_PASSED")
	private String mesgValidationPassed;

	@Column(name = "MESG_VALIDATION_REQUESTED")
	private String mesgValidationRequested = "VAL_NO_VALIDATION";

	@Column(name = "MESG_VERF_OPER_NICKNAME")
	private String mesgVerfOperNickname;

	@Column(name = "MESG_XML_QUERY_REF1")
	private String mesgXmlQueryRef1 = null;

	@Column(name = "MESG_XML_QUERY_REF2")
	private String mesgXmlQueryRef2 = null;

	@Column(name = "MESG_XML_QUERY_REF3")
	private String mesgXmlQueryRef3 = null;

	@Column(name = "MESG_ZZ41_IS_POSSIBLE_DUP")
	private BigDecimal mesgZz41IsPossibleDup;

	private BigDecimal restored = BigDecimal.ZERO;

	@Column(name = "SET_ID")
	private BigDecimal setId = null;

	@Column(name = "X_CATEGORY")
	private String xCategory = "O";

	@Column(name = "X_FIN_AMOUNT")
	private BigDecimal xFinAmount = null;

	@Column(name = "X_FIN_CCY")
	private String xFinCcy = null;

	@Column(name = "X_FIN_OCMT_AMOUNT")
	private BigDecimal xFinOcmtAmount = null;

	@Column(name = "X_FIN_OCMT_CCY")
	private String xFinOcmtCcy = null;

	@Temporal(TemporalType.DATE)
	@Column(name = "X_FIN_VALUE_DATE")
	private Date xFinValueDate = null;

	@Column(name = "X_INST0_UNIT_NAME")
	private String xInst0UnitName;

	@Column(name = "X_OWN_LT")
	private String xOwnLt;

	@Column(name = "X_RECEIVER_X1")
	private String xReceiverX1;

	@Column(name = "X_RECEIVER_X2")
	private String xReceiverX2;

	@Column(name = "X_RECEIVER_X3")
	private String xReceiverX3;

	@Column(name = "X_RECEIVER_X4")
	private String xReceiverX4;

	// bi-directional one-to-one association to Text
	@OneToOne(mappedBy = "mesg", cascade = CascadeType.PERSIST)
	private Text text;

	// bi-directional one-to-one association to Text
	@OneToOne(mappedBy = "mesg", cascade = CascadeType.PERSIST)
	private LdTextbreakHistory ldTextbreakHistory;


	// bi-directional many-to-one association to Inst
	@OneToMany(mappedBy = "mesg", cascade = CascadeType.PERSIST)
	private List<Instance> instances = new ArrayList<Instance>();

	// // bi-directional many-to-one association to Inst
	// @OneToMany(mappedBy = "rmesg", cascade = CascadeType.PERSIST)
	// private List<InstancePart> instancesPart = new ArrayList<InstancePart>();

	// bi-directional one-to-one association to Text
	// @OneToOne(mappedBy = "rmesg", cascade = CascadeType.PERSIST)
	// private TextPart textPart;



	@ManyToOne(cascade = CascadeType.PERSIST,fetch=FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "MESG_TYPE", referencedColumnName = "MESG_TYPE", insertable = false, updatable = false)})
	private LdParseMsgType ldParseMsgType;




	public LdParseMsgType getLdParseMsgType() {
		return ldParseMsgType;
	}

	public void setLdParseMsgType(LdParseMsgType ldParseMsgType) {
		this.ldParseMsgType = ldParseMsgType;
	}

	public BigDecimal getArchived() {
		return archived;
	}

	public MesgPK getId() {
		return id;
	}

	public void setId(MesgPK id) {
		this.id = id;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getMesgApplSenderReference() {
		return mesgApplSenderReference;
	}

	public void setMesgApplSenderReference(String mesgApplSenderReference) {
		this.mesgApplSenderReference = mesgApplSenderReference;
	}

	public String getMesgBatchReference() {
		return mesgBatchReference;
	}

	public void setMesgBatchReference(String mesgBatchReference) {
		this.mesgBatchReference = mesgBatchReference;
	}

	public String getMesgCasSenderReference() {
		return mesgCasSenderReference;
	}

	public void setMesgCasSenderReference(String mesgCasSenderReference) {
		this.mesgCasSenderReference = mesgCasSenderReference;
	}

	public String getMesgCasTargetRpName() {
		return mesgCasTargetRpName;
	}

	public void setMesgCasTargetRpName(String mesgCasTargetRpName) {
		this.mesgCasTargetRpName = mesgCasTargetRpName;
	}

	public String getMesgCcyAmount() {
		return mesgCcyAmount;
	}

	public void setMesgCcyAmount(String mesgCcyAmount) {
		this.mesgCcyAmount = mesgCcyAmount;
	}

	public String getMesgClass() {
		return mesgClass;
	}

	public void setMesgClass(String mesgClass) {
		this.mesgClass = mesgClass;
	}

	public String getMesgCopyServiceId() {
		return mesgCopyServiceId;
	}

	public void setMesgCopyServiceId(String mesgCopyServiceId) {
		this.mesgCopyServiceId = mesgCopyServiceId;
	}

	public String getMesgCreaApplServName() {
		return mesgCreaApplServName;
	}

	public void setMesgCreaApplServName(String mesgCreaApplServName) {
		this.mesgCreaApplServName = mesgCreaApplServName;
	}

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public String getMesgCreaMpfnName() {
		return mesgCreaMpfnName;
	}

	public void setMesgCreaMpfnName(String mesgCreaMpfnName) {
		this.mesgCreaMpfnName = mesgCreaMpfnName;
	}

	public String getMesgCreaOperNickname() {
		return mesgCreaOperNickname;
	}

	public void setMesgCreaOperNickname(String mesgCreaOperNickname) {
		this.mesgCreaOperNickname = mesgCreaOperNickname;
	}

	public String getMesgCreaRpName() {
		return mesgCreaRpName;
	}

	public void setMesgCreaRpName(String mesgCreaRpName) {
		this.mesgCreaRpName = mesgCreaRpName;
	}

	public String getMesgDataKeyword1() {
		return mesgDataKeyword1;
	}

	public void setMesgDataKeyword1(String mesgDataKeyword1) {
		this.mesgDataKeyword1 = mesgDataKeyword1;
	}

	public String getMesgDataKeyword2() {
		return mesgDataKeyword2;
	}

	public void setMesgDataKeyword2(String mesgDataKeyword2) {
		this.mesgDataKeyword2 = mesgDataKeyword2;
	}

	public String getMesgDataKeyword3() {
		return mesgDataKeyword3;
	}

	public void setMesgDataKeyword3(String mesgDataKeyword3) {
		this.mesgDataKeyword3 = mesgDataKeyword3;
	}

	public BigDecimal getMesgDataLast() {
		return mesgDataLast;
	}

	public void setMesgDataLast(BigDecimal mesgDataLast) {
		this.mesgDataLast = mesgDataLast;
	}

	public BigDecimal getMesgDelvOverdueWarnReq() {
		return mesgDelvOverdueWarnReq;
	}

	public void setMesgDelvOverdueWarnReq(BigDecimal mesgDelvOverdueWarnReq) {
		this.mesgDelvOverdueWarnReq = mesgDelvOverdueWarnReq;
	}

	public String getMesgFinCcyAmount() {
		return mesgFinCcyAmount;
	}

	public void setMesgFinCcyAmount(String mesgFinCcyAmount) {
		this.mesgFinCcyAmount = mesgFinCcyAmount;
	}

	public String getMesgFinValueDate() {
		return mesgFinValueDate;
	}

	public void setMesgFinValueDate(String mesgFinValueDate) {
		this.mesgFinValueDate = mesgFinValueDate;
	}

	public String getMesgFrmtName() {
		return mesgFrmtName;
	}

	public void setMesgFrmtName(String mesgFrmtName) {
		this.mesgFrmtName = mesgFrmtName;
	}

	public String getMesgIdentifier() {
		return mesgIdentifier;
	}

	public void setMesgIdentifier(String mesgIdentifier) {
		this.mesgIdentifier = mesgIdentifier;
	}

	public BigDecimal getMesgIsDeleteInhibited() {
		return mesgIsDeleteInhibited;
	}

	public void setMesgIsDeleteInhibited(BigDecimal mesgIsDeleteInhibited) {
		this.mesgIsDeleteInhibited = mesgIsDeleteInhibited;
	}

	public BigDecimal getMesgIsLive() {
		return mesgIsLive;
	}

	public void setMesgIsLive(BigDecimal mesgIsLive) {
		this.mesgIsLive = mesgIsLive;
	}

	public BigDecimal getMesgIsPartial() {
		return mesgIsPartial;
	}

	public void setMesgIsPartial(BigDecimal mesgIsPartial) {
		this.mesgIsPartial = mesgIsPartial;
	}

	public BigDecimal getMesgIsRetrieved() {
		return mesgIsRetrieved;
	}

	public void setMesgIsRetrieved(BigDecimal mesgIsRetrieved) {
		this.mesgIsRetrieved = mesgIsRetrieved;
	}

	public BigDecimal getMesgIsTextModified() {
		return mesgIsTextModified;
	}

	public void setMesgIsTextModified(BigDecimal mesgIsTextModified) {
		this.mesgIsTextModified = mesgIsTextModified;
	}

	public BigDecimal getMesgIsTextReadonly() {
		return mesgIsTextReadonly;
	}

	public void setMesgIsTextReadonly(BigDecimal mesgIsTextReadonly) {
		this.mesgIsTextReadonly = mesgIsTextReadonly;
	}

	public String getMesgMesgUserGroup() {
		return mesgMesgUserGroup;
	}

	public void setMesgMesgUserGroup(String mesgMesgUserGroup) {
		this.mesgMesgUserGroup = mesgMesgUserGroup;
	}

	public Date getMesgModDateTime() {
		return mesgModDateTime;
	}

	public void setMesgModDateTime(Date mesgModDateTime) {
		this.mesgModDateTime = mesgModDateTime;
	}

	public String getMesgModOperNickname() {
		return mesgModOperNickname;
	}

	public void setMesgModOperNickname(String mesgModOperNickname) {
		this.mesgModOperNickname = mesgModOperNickname;
	}

	public String getMesgNature() {
		return mesgNature;
	}

	public void setMesgNature(String mesgNature) {
		this.mesgNature = mesgNature;
	}

	public String getMesgNetworkApplInd() {
		return mesgNetworkApplInd;
	}

	public void setMesgNetworkApplInd(String mesgNetworkApplInd) {
		this.mesgNetworkApplInd = mesgNetworkApplInd;
	}

	public BigDecimal getMesgNetworkDelvNotifReq() {
		return mesgNetworkDelvNotifReq;
	}

	public void setMesgNetworkDelvNotifReq(BigDecimal mesgNetworkDelvNotifReq) {
		this.mesgNetworkDelvNotifReq = mesgNetworkDelvNotifReq;
	}

	public BigDecimal getMesgNetworkObsoPeriod() {
		return mesgNetworkObsoPeriod;
	}

	public void setMesgNetworkObsoPeriod(BigDecimal mesgNetworkObsoPeriod) {
		this.mesgNetworkObsoPeriod = mesgNetworkObsoPeriod;
	}

	public String getMesgNetworkPriority() {
		return mesgNetworkPriority;
	}

	public void setMesgNetworkPriority(String mesgNetworkPriority) {
		this.mesgNetworkPriority = mesgNetworkPriority;
	}

	public String getMesgPayloadType() {
		return mesgPayloadType;
	}

	public void setMesgPayloadType(String mesgPayloadType) {
		this.mesgPayloadType = mesgPayloadType;
	}

	public String getMesgPossibleDupCreation() {
		return mesgPossibleDupCreation;
	}

	public void setMesgPossibleDupCreation(String mesgPossibleDupCreation) {
		this.mesgPossibleDupCreation = mesgPossibleDupCreation;
	}

	public String getMesgReceiverAliaName() {
		return mesgReceiverAliaName;
	}

	public void setMesgReceiverAliaName(String mesgReceiverAliaName) {
		this.mesgReceiverAliaName = mesgReceiverAliaName;
	}

	public String getMesgReceiverSwiftAddress() {
		return mesgReceiverSwiftAddress;
	}

	public void setMesgReceiverSwiftAddress(String mesgReceiverSwiftAddress) {
		this.mesgReceiverSwiftAddress = mesgReceiverSwiftAddress;
	}

	public String getMesgRecoveryAcceptInfo() {
		return mesgRecoveryAcceptInfo;
	}

	public void setMesgRecoveryAcceptInfo(String mesgRecoveryAcceptInfo) {
		this.mesgRecoveryAcceptInfo = mesgRecoveryAcceptInfo;
	}

	public String getMesgRelTrnRef() {
		return mesgRelTrnRef;
	}

	public void setMesgRelTrnRef(String mesgRelTrnRef) {
		this.mesgRelTrnRef = mesgRelTrnRef;
	}

	public String getMesgRelatedSUmid() {
		return mesgRelatedSUmid;
	}

	public void setMesgRelatedSUmid(String mesgRelatedSUmid) {
		this.mesgRelatedSUmid = mesgRelatedSUmid;
	}

	public String getMesgReleaseInfo() {
		return mesgReleaseInfo;
	}

	public void setMesgReleaseInfo(String mesgReleaseInfo) {
		this.mesgReleaseInfo = mesgReleaseInfo;
	}

	public String getMesgRequestType() {
		return mesgRequestType;
	}

	public void setMesgRequestType(String mesgRequestType) {
		this.mesgRequestType = mesgRequestType;
	}

	public String getMesgRequestorDn() {
		return mesgRequestorDn;
	}

	public void setMesgRequestorDn(String mesgRequestorDn) {
		this.mesgRequestorDn = mesgRequestorDn;
	}

	public String getMesgSecurityIappName() {
		return mesgSecurityIappName;
	}

	public void setMesgSecurityIappName(String mesgSecurityIappName) {
		this.mesgSecurityIappName = mesgSecurityIappName;
	}

	public BigDecimal getMesgSecurityRequired() {
		return mesgSecurityRequired;
	}

	public void setMesgSecurityRequired(BigDecimal mesgSecurityRequired) {
		this.mesgSecurityRequired = mesgSecurityRequired;
	}

	public String getMesgSenderBranchInfo() {
		return mesgSenderBranchInfo;
	}

	public void setMesgSenderBranchInfo(String mesgSenderBranchInfo) {
		this.mesgSenderBranchInfo = mesgSenderBranchInfo;
	}

	public String getMesgSenderCityName() {
		return mesgSenderCityName;
	}

	public void setMesgSenderCityName(String mesgSenderCityName) {
		this.mesgSenderCityName = mesgSenderCityName;
	}

	public String getMesgSenderCorrType() {
		return mesgSenderCorrType;
	}

	public void setMesgSenderCorrType(String mesgSenderCorrType) {
		this.mesgSenderCorrType = mesgSenderCorrType;
	}

	public String getMesgSenderCtryCode() {
		return mesgSenderCtryCode;
	}

	public void setMesgSenderCtryCode(String mesgSenderCtryCode) {
		this.mesgSenderCtryCode = mesgSenderCtryCode;
	}

	public String getMesgSenderCtryName() {
		return mesgSenderCtryName;
	}

	public void setMesgSenderCtryName(String mesgSenderCtryName) {
		this.mesgSenderCtryName = mesgSenderCtryName;
	}

	public String getMesgSenderInstitutionName() {
		return mesgSenderInstitutionName;
	}

	public void setMesgSenderInstitutionName(String mesgSenderInstitutionName) {
		this.mesgSenderInstitutionName = mesgSenderInstitutionName;
	}

	public String getMesgSenderLocation() {
		return mesgSenderLocation;
	}

	public void setMesgSenderLocation(String mesgSenderLocation) {
		this.mesgSenderLocation = mesgSenderLocation;
	}

	public String getMesgSenderSwiftAddress() {
		return mesgSenderSwiftAddress;
	}

	public void setMesgSenderSwiftAddress(String mesgSenderSwiftAddress) {
		this.mesgSenderSwiftAddress = mesgSenderSwiftAddress;
	}

	public String getMesgSenderX1() {
		return mesgSenderX1;
	}

	public void setMesgSenderX1(String mesgSenderX1) {
		this.mesgSenderX1 = mesgSenderX1;
	}

	public String getMesgSenderX2() {
		return mesgSenderX2;
	}

	public void setMesgSenderX2(String mesgSenderX2) {
		this.mesgSenderX2 = mesgSenderX2;
	}

	public String getMesgSenderX3() {
		return mesgSenderX3;
	}

	public void setMesgSenderX3(String mesgSenderX3) {
		this.mesgSenderX3 = mesgSenderX3;
	}

	public String getMesgSenderX4() {
		return mesgSenderX4;
	}

	public void setMesgSenderX4(String mesgSenderX4) {
		this.mesgSenderX4 = mesgSenderX4;
	}

	public String getMesgService() {
		return mesgService;
	}

	public void setMesgService(String mesgService) {
		this.mesgService = mesgService;
	}

	public String getMesgSignDigestReference() {
		return mesgSignDigestReference;
	}

	public void setMesgSignDigestReference(String mesgSignDigestReference) {
		this.mesgSignDigestReference = mesgSignDigestReference;
	}

	public String getMesgSignDigestValue() {
		return mesgSignDigestValue;
	}

	public void setMesgSignDigestValue(String mesgSignDigestValue) {
		this.mesgSignDigestValue = mesgSignDigestValue;
	}

	public String getMesgStatus() {
		return mesgStatus;
	}

	public void setMesgStatus(String mesgStatus) {
		this.mesgStatus = mesgStatus;
	}

	public String getMesgSubFormat() {
		return mesgSubFormat;
	}

	public void setMesgSubFormat(String mesgSubFormat) {
		this.mesgSubFormat = mesgSubFormat;
	}

	public String getMesgSyntaxTableVer() {
		return mesgSyntaxTableVer;
	}

	public void setMesgSyntaxTableVer(String mesgSyntaxTableVer) {
		this.mesgSyntaxTableVer = mesgSyntaxTableVer;
	}

	public String getMesgTemplateName() {
		return mesgTemplateName;
	}

	public void setMesgTemplateName(String mesgTemplateName) {
		this.mesgTemplateName = mesgTemplateName;
	}

	public BigDecimal getMesgToken() {
		return mesgToken;
	}

	public void setMesgToken(BigDecimal mesgToken) {
		this.mesgToken = mesgToken;
	}

	public String getMesgTrnRef() {
		return mesgTrnRef;
	}

	public void setMesgTrnRef(String mesgTrnRef) {
		this.mesgTrnRef = mesgTrnRef;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public BigDecimal getMesgUsePkiSignature() {
		return mesgUsePkiSignature;
	}

	public void setMesgUsePkiSignature(BigDecimal mesgUsePkiSignature) {
		this.mesgUsePkiSignature = mesgUsePkiSignature;
	}

	public BigDecimal getMesgUserIssuedAsPde() {
		return mesgUserIssuedAsPde;
	}

	public void setMesgUserIssuedAsPde(BigDecimal mesgUserIssuedAsPde) {
		this.mesgUserIssuedAsPde = mesgUserIssuedAsPde;
	}

	public String getMesgUserPriorityCode() {
		return mesgUserPriorityCode;
	}

	public void setMesgUserPriorityCode(String mesgUserPriorityCode) {
		this.mesgUserPriorityCode = mesgUserPriorityCode;
	}

	public String getMesgUserReferenceText() {
		return mesgUserReferenceText;
	}

	public void setMesgUserReferenceText(String mesgUserReferenceText) {
		this.mesgUserReferenceText = mesgUserReferenceText;
	}

	public String getMesgUumid() {
		return mesgUumid;
	}

	public void setMesgUumid(String mesgUumid) {
		this.mesgUumid = mesgUumid;
	}

	public BigDecimal getMesgUumidSuffix() {
		return mesgUumidSuffix;
	}

	public void setMesgUumidSuffix(BigDecimal mesgUumidSuffix) {
		this.mesgUumidSuffix = mesgUumidSuffix;
	}

	public String getMesgValidationPassed() {
		return mesgValidationPassed;
	}

	public void setMesgValidationPassed(String mesgValidationPassed) {
		this.mesgValidationPassed = mesgValidationPassed;
	}

	public String getMesgValidationRequested() {
		return mesgValidationRequested;
	}

	public void setMesgValidationRequested(String mesgValidationRequested) {
		this.mesgValidationRequested = mesgValidationRequested;
	}

	public String getMesgVerfOperNickname() {
		return mesgVerfOperNickname;
	}

	public void setMesgVerfOperNickname(String mesgVerfOperNickname) {
		this.mesgVerfOperNickname = mesgVerfOperNickname;
	}

	public String getMesgXmlQueryRef1() {
		return mesgXmlQueryRef1;
	}

	public void setMesgXmlQueryRef1(String mesgXmlQueryRef1) {
		this.mesgXmlQueryRef1 = mesgXmlQueryRef1;
	}

	public String getMesgXmlQueryRef2() {
		return mesgXmlQueryRef2;
	}

	public void setMesgXmlQueryRef2(String mesgXmlQueryRef2) {
		this.mesgXmlQueryRef2 = mesgXmlQueryRef2;
	}

	public String getMesgXmlQueryRef3() {
		return mesgXmlQueryRef3;
	}

	public void setMesgXmlQueryRef3(String mesgXmlQueryRef3) {
		this.mesgXmlQueryRef3 = mesgXmlQueryRef3;
	}

	public BigDecimal getMesgZz41IsPossibleDup() {
		return mesgZz41IsPossibleDup;
	}

	public void setMesgZz41IsPossibleDup(BigDecimal mesgZz41IsPossibleDup) {
		this.mesgZz41IsPossibleDup = mesgZz41IsPossibleDup;
	}

	public BigDecimal getSetId() {
		return setId;
	}

	public void setSetId(BigDecimal setId) {
		this.setId = setId;
	}

	public String getxCategory() {
		return xCategory;
	}

	public void setxCategory(String xCategory) {
		this.xCategory = xCategory;
	}

	public BigDecimal getxFinAmount() {
		return xFinAmount;
	}

	public void setxFinAmount(BigDecimal xFinAmount) {
		this.xFinAmount = xFinAmount;
	}

	public String getxFinCcy() {
		return xFinCcy;
	}

	public void setxFinCcy(String xFinCcy) {
		this.xFinCcy = xFinCcy;
	}

	public BigDecimal getxFinOcmtAmount() {
		return xFinOcmtAmount;
	}

	public void setxFinOcmtAmount(BigDecimal xFinOcmtAmount) {
		this.xFinOcmtAmount = xFinOcmtAmount;
	}

	public String getxFinOcmtCcy() {
		return xFinOcmtCcy;
	}

	public void setxFinOcmtCcy(String xFinOcmtCcy) {
		this.xFinOcmtCcy = xFinOcmtCcy;
	}

	public Date getxFinValueDate() {
		return xFinValueDate;
	}

	public void setxFinValueDate(Date xFinValueDate) {
		this.xFinValueDate = xFinValueDate;
	}

	public String getxInst0UnitName() {
		return xInst0UnitName;
	}

	public void setxInst0UnitName(String xInst0UnitName) {
		this.xInst0UnitName = xInst0UnitName;
	}

	public String getxOwnLt() {
		return xOwnLt;
	}

	public void setxOwnLt(String xOwnLt) {
		this.xOwnLt = xOwnLt;
	}

	public String getxReceiverX1() {
		return xReceiverX1;
	}

	public void setxReceiverX1(String xReceiverX1) {
		this.xReceiverX1 = xReceiverX1;
	}

	public String getxReceiverX2() {
		return xReceiverX2;
	}

	public void setxReceiverX2(String xReceiverX2) {
		this.xReceiverX2 = xReceiverX2;
	}

	public String getxReceiverX3() {
		return xReceiverX3;
	}

	public void setxReceiverX3(String xReceiverX3) {
		this.xReceiverX3 = xReceiverX3;
	}

	public String getxReceiverX4() {
		return xReceiverX4;
	}

	public void setxReceiverX4(String xReceiverX4) {
		this.xReceiverX4 = xReceiverX4;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public List<Instance> getInstances() {
		return instances;
	}

	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}

	public void setArchived(BigDecimal archived) {
		this.archived = archived;
	}

	public BigDecimal getRestored() {
		return restored;
	}

	public void setRestored(BigDecimal restored) {
		this.restored = restored;
	}


	public LdTextbreakHistory getLdTextbreakHistory() {
		return ldTextbreakHistory;
	}

	public void setLdTextbreakHistory(LdTextbreakHistory ldTextbreakHistory) {
		this.ldTextbreakHistory = ldTextbreakHistory;
	}

}