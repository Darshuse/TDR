package com.eastnets.domain.loader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the RMESG database table.
 * 
 */
@Entity
@Table(name = "RMESG")
@NamedQuery(name = "Mesg.findAll", query = "SELECT m FROM Mesg m")
@Cacheable(value = false)
public class Mesg extends AbstractReportingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8285892825199282955L;

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

	@Column(name = "MESG_IS_COPY_REQUIRED")
	private BigDecimal mesgIsCopyRequired = null;

	@Column(name = "MESG_AUTH_DELV_NOTIF_REQ")
	private BigDecimal mesgAuthDelvNotifReq = null;

	@Column(name = "X_LAST_REC_APPE_SEQUENCE_NBR")
	private BigDecimal x_Last_Rec_Appe_Seq_Nbr = null;

	@Column(name = "X_LAST_EMI_APPE_SEQUENCE_NBR")
	private BigDecimal x_Last_Emi_Appe_Seq_Nbr = null;

	@Column(name = "X_LAST_EMI_APPE_SESSION_NBR")
	private BigDecimal X_LAST_EMI_APPE_SESSION_NBR = null;

	@Column(name = "X_LAST_REC_APPE_SESSION_NBR")
	private BigDecimal X_LAST_REC_APPE_SESSION_NBR = null;

	@Column(name = "MESG_USER_ISSUED_AS_PDE")
	private BigDecimal mesgUserIssuedAsPde;

	@Column(name = "MESG_USER_PRIORITY_CODE")
	private String mesgUserPriorityCode;

	@Column(name = "X_LAST_EMI_APPE_IAPP_NAME")
	private String X_LAST_EMI_APPE_IAPP_NAME;

	@Column(name = "X_LAST_REC_APPE_IAPP_NAME")
	private String X_LAST_REC_APPE_IAPP_NAME;

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
	@Column(name = "MESG_E2E_TRANSACTION_REFERENCE")
	private String mesgUetr;

	@Column(name = "MESG_SLA")
	private String mesgSLA;

	public String getMesgSLA() {
		return mesgSLA;
	}

	public void setMesgSLA(String mesgSLA) {
		this.mesgSLA = mesgSLA;
	}

	public String getMesgUetr() {
		return mesgUetr;
	}

	public void setMesgUetr(String mesgUetr) {
		this.mesgUetr = mesgUetr;
	}

	// bi-directional many-to-one association to Inst
	@OneToMany(mappedBy = "rmesg", cascade = CascadeType.PERSIST)
	private List<Inst> rinsts = new ArrayList<Inst>();

	// bi-directional many-to-one association to Inst
	@OneToMany(mappedBy = "rmesg", cascade = CascadeType.PERSIST)
	private List<InstPart> rinstsPart = new ArrayList<InstPart>();

	// bi-directional one-to-one association to Text
	@OneToOne(mappedBy = "rmesg", cascade = CascadeType.PERSIST)
	private Text rtext;

	// bi-directional one-to-one association to Text
	@OneToOne(mappedBy = "rmesg", cascade = CascadeType.PERSIST)
	private TextPart rtextPart;

	@OneToMany(mappedBy = "xmlRmesg", cascade = CascadeType.PERSIST)
	private List<XmlTextMessage> rXmlText;

	@OneToOne(mappedBy = "rmesg", cascade = CascadeType.PERSIST)
	private Rfile rfile;

	public Mesg() {
	}

	public MesgPK getId() {
		return this.id;
	}

	public void setId(MesgPK id) {
		this.id = id;
	}

	public BigDecimal getArchived() {
		return this.archived;
	}

	public void setArchived(BigDecimal archived) {
		this.archived = archived;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getMesgApplSenderReference() {
		return this.mesgApplSenderReference;
	}

	public void setMesgApplSenderReference(String mesgApplSenderReference) {
		this.mesgApplSenderReference = mesgApplSenderReference;
	}

	public String getMesgBatchReference() {
		return this.mesgBatchReference;
	}

	public void setMesgBatchReference(String mesgBatchReference) {
		this.mesgBatchReference = mesgBatchReference;
	}

	public String getMesgCasSenderReference() {
		return this.mesgCasSenderReference;
	}

	public void setMesgCasSenderReference(String mesgCasSenderReference) {
		this.mesgCasSenderReference = mesgCasSenderReference;
	}

	public String getMesgCasTargetRpName() {
		return this.mesgCasTargetRpName;
	}

	public void setMesgCasTargetRpName(String mesgCasTargetRpName) {
		this.mesgCasTargetRpName = mesgCasTargetRpName;
	}

	public String getMesgCcyAmount() {
		return this.mesgCcyAmount;
	}

	public void setMesgCcyAmount(String mesgCcyAmount) {
		this.mesgCcyAmount = mesgCcyAmount;
	}

	public String getMesgClass() {
		return this.mesgClass;
	}

	public void setMesgClass(String mesgClass) {
		this.mesgClass = mesgClass;
	}

	public String getMesgCopyServiceId() {
		return this.mesgCopyServiceId;
	}

	public void setMesgCopyServiceId(String mesgCopyServiceId) {
		this.mesgCopyServiceId = mesgCopyServiceId;
	}

	public String getMesgCreaApplServName() {
		return this.mesgCreaApplServName;
	}

	public void setMesgCreaApplServName(String mesgCreaApplServName) {
		this.mesgCreaApplServName = mesgCreaApplServName;
	}

	public Date getMesgCreaDateTime() {
		return this.mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public String getMesgCreaMpfnName() {
		return this.mesgCreaMpfnName;
	}

	public void setMesgCreaMpfnName(String mesgCreaMpfnName) {
		this.mesgCreaMpfnName = mesgCreaMpfnName;
	}

	public String getMesgCreaOperNickname() {
		return this.mesgCreaOperNickname;
	}

	public void setMesgCreaOperNickname(String mesgCreaOperNickname) {
		this.mesgCreaOperNickname = mesgCreaOperNickname;
	}

	public String getMesgCreaRpName() {
		return this.mesgCreaRpName;
	}

	public void setMesgCreaRpName(String mesgCreaRpName) {
		this.mesgCreaRpName = mesgCreaRpName;
	}

	public String getMesgDataKeyword1() {
		return this.mesgDataKeyword1;
	}

	public void setMesgDataKeyword1(String mesgDataKeyword1) {
		this.mesgDataKeyword1 = mesgDataKeyword1;
	}

	public String getMesgDataKeyword2() {
		return this.mesgDataKeyword2;
	}

	public void setMesgDataKeyword2(String mesgDataKeyword2) {
		this.mesgDataKeyword2 = mesgDataKeyword2;
	}

	public String getMesgDataKeyword3() {
		return this.mesgDataKeyword3;
	}

	public void setMesgDataKeyword3(String mesgDataKeyword3) {
		this.mesgDataKeyword3 = mesgDataKeyword3;
	}

	public BigDecimal getMesgDataLast() {
		return this.mesgDataLast;
	}

	public void setMesgDataLast(BigDecimal mesgDataLast) {
		this.mesgDataLast = mesgDataLast;
	}

	public BigDecimal getMesgDelvOverdueWarnReq() {
		return this.mesgDelvOverdueWarnReq;
	}

	public void setMesgDelvOverdueWarnReq(BigDecimal mesgDelvOverdueWarnReq) {
		this.mesgDelvOverdueWarnReq = mesgDelvOverdueWarnReq;
	}

	public String getMesgFinCcyAmount() {
		return this.mesgFinCcyAmount;
	}

	public void setMesgFinCcyAmount(String mesgFinCcyAmount) {
		this.mesgFinCcyAmount = mesgFinCcyAmount;
	}

	public String getMesgFinValueDate() {
		return this.mesgFinValueDate;
	}

	public void setMesgFinValueDate(String mesgFinValueDate) {
		this.mesgFinValueDate = mesgFinValueDate;
	}

	public String getMesgFrmtName() {
		return this.mesgFrmtName;
	}

	public void setMesgFrmtName(String mesgFrmtName) {
		this.mesgFrmtName = mesgFrmtName;
	}

	public String getMesgIdentifier() {
		return this.mesgIdentifier;
	}

	public void setMesgIdentifier(String mesgIdentifier) {
		this.mesgIdentifier = mesgIdentifier;
	}

	public BigDecimal getMesgIsDeleteInhibited() {
		return this.mesgIsDeleteInhibited;
	}

	public void setMesgIsDeleteInhibited(BigDecimal mesgIsDeleteInhibited) {
		this.mesgIsDeleteInhibited = mesgIsDeleteInhibited;
	}

	public BigDecimal getMesgIsLive() {
		return this.mesgIsLive;
	}

	public void setMesgIsLive(BigDecimal mesgIsLive) {
		this.mesgIsLive = mesgIsLive;
	}

	public BigDecimal getMesgIsPartial() {
		return this.mesgIsPartial;
	}

	public void setMesgIsPartial(BigDecimal mesgIsPartial) {
		this.mesgIsPartial = mesgIsPartial;
	}

	public BigDecimal getMesgIsRetrieved() {
		return this.mesgIsRetrieved;
	}

	public void setMesgIsRetrieved(BigDecimal mesgIsRetrieved) {
		this.mesgIsRetrieved = mesgIsRetrieved;
	}

	public BigDecimal getMesgIsTextModified() {
		return this.mesgIsTextModified;
	}

	public void setMesgIsTextModified(BigDecimal mesgIsTextModified) {
		this.mesgIsTextModified = mesgIsTextModified;
	}

	public BigDecimal getMesgIsTextReadonly() {
		return this.mesgIsTextReadonly;
	}

	public void setMesgIsTextReadonly(BigDecimal mesgIsTextReadonly) {
		this.mesgIsTextReadonly = mesgIsTextReadonly;
	}

	public String getMesgMesgUserGroup() {
		return this.mesgMesgUserGroup;
	}

	public void setMesgMesgUserGroup(String mesgMesgUserGroup) {
		this.mesgMesgUserGroup = mesgMesgUserGroup;
	}

	public Date getMesgModDateTime() {
		return this.mesgModDateTime;
	}

	public void setMesgModDateTime(Date mesgModDateTime) {
		this.mesgModDateTime = mesgModDateTime;
	}

	public String getMesgModOperNickname() {
		return this.mesgModOperNickname;
	}

	public void setMesgModOperNickname(String mesgModOperNickname) {
		this.mesgModOperNickname = mesgModOperNickname;
	}

	public String getMesgNature() {
		return this.mesgNature;
	}

	public void setMesgNature(String mesgNature) {
		this.mesgNature = mesgNature;
	}

	public String getMesgNetworkApplInd() {
		return this.mesgNetworkApplInd;
	}

	public void setMesgNetworkApplInd(String mesgNetworkApplInd) {
		this.mesgNetworkApplInd = mesgNetworkApplInd;
	}

	public BigDecimal getMesgNetworkDelvNotifReq() {
		return this.mesgNetworkDelvNotifReq;
	}

	public void setMesgNetworkDelvNotifReq(BigDecimal mesgNetworkDelvNotifReq) {
		this.mesgNetworkDelvNotifReq = mesgNetworkDelvNotifReq;
	}

	public BigDecimal getMesgNetworkObsoPeriod() {
		return this.mesgNetworkObsoPeriod;
	}

	public void setMesgNetworkObsoPeriod(BigDecimal mesgNetworkObsoPeriod) {
		this.mesgNetworkObsoPeriod = mesgNetworkObsoPeriod;
	}

	public String getMesgNetworkPriority() {
		return this.mesgNetworkPriority;
	}

	public void setMesgNetworkPriority(String mesgNetworkPriority) {
		this.mesgNetworkPriority = mesgNetworkPriority;
	}

	public String getMesgPayloadType() {
		return this.mesgPayloadType;
	}

	public void setMesgPayloadType(String mesgPayloadType) {
		this.mesgPayloadType = mesgPayloadType;
	}

	public String getMesgPossibleDupCreation() {
		return this.mesgPossibleDupCreation;
	}

	public void setMesgPossibleDupCreation(String mesgPossibleDupCreation) {
		this.mesgPossibleDupCreation = mesgPossibleDupCreation;
	}

	public String getMesgReceiverAliaName() {
		return this.mesgReceiverAliaName;
	}

	public void setMesgReceiverAliaName(String mesgReceiverAliaName) {
		this.mesgReceiverAliaName = mesgReceiverAliaName;
	}

	public String getMesgReceiverSwiftAddress() {
		return this.mesgReceiverSwiftAddress;
	}

	public void setMesgReceiverSwiftAddress(String mesgReceiverSwiftAddress) {
		this.mesgReceiverSwiftAddress = mesgReceiverSwiftAddress;
	}

	public String getMesgRecoveryAcceptInfo() {
		return this.mesgRecoveryAcceptInfo;
	}

	public void setMesgRecoveryAcceptInfo(String mesgRecoveryAcceptInfo) {
		this.mesgRecoveryAcceptInfo = mesgRecoveryAcceptInfo;
	}

	public String getMesgRelTrnRef() {
		return this.mesgRelTrnRef;
	}

	public void setMesgRelTrnRef(String mesgRelTrnRef) {
		this.mesgRelTrnRef = mesgRelTrnRef;
	}

	public String getMesgRelatedSUmid() {
		return this.mesgRelatedSUmid;
	}

	public void setMesgRelatedSUmid(String mesgRelatedSUmid) {
		this.mesgRelatedSUmid = mesgRelatedSUmid;
	}

	public String getMesgReleaseInfo() {
		return this.mesgReleaseInfo;
	}

	public void setMesgReleaseInfo(String mesgReleaseInfo) {
		this.mesgReleaseInfo = mesgReleaseInfo;
	}

	public String getMesgRequestType() {
		return this.mesgRequestType;
	}

	public void setMesgRequestType(String mesgRequestType) {
		this.mesgRequestType = mesgRequestType;
	}

	public String getMesgRequestorDn() {
		return this.mesgRequestorDn;
	}

	public void setMesgRequestorDn(String mesgRequestorDn) {
		this.mesgRequestorDn = mesgRequestorDn;
	}

	public String getMesgSecurityIappName() {
		return this.mesgSecurityIappName;
	}

	public void setMesgSecurityIappName(String mesgSecurityIappName) {
		this.mesgSecurityIappName = mesgSecurityIappName;
	}

	public BigDecimal getMesgSecurityRequired() {
		return this.mesgSecurityRequired;
	}

	public void setMesgSecurityRequired(BigDecimal mesgSecurityRequired) {
		this.mesgSecurityRequired = mesgSecurityRequired;
	}

	public String getMesgSenderBranchInfo() {
		return this.mesgSenderBranchInfo;
	}

	public void setMesgSenderBranchInfo(String mesgSenderBranchInfo) {
		this.mesgSenderBranchInfo = mesgSenderBranchInfo;
	}

	public String getMesgSenderCityName() {
		return this.mesgSenderCityName;
	}

	public void setMesgSenderCityName(String mesgSenderCityName) {
		this.mesgSenderCityName = mesgSenderCityName;
	}

	public String getMesgSenderCorrType() {
		return this.mesgSenderCorrType;
	}

	public void setMesgSenderCorrType(String mesgSenderCorrType) {
		this.mesgSenderCorrType = mesgSenderCorrType;
	}

	public String getMesgSenderCtryCode() {
		return this.mesgSenderCtryCode;
	}

	public void setMesgSenderCtryCode(String mesgSenderCtryCode) {
		this.mesgSenderCtryCode = mesgSenderCtryCode;
	}

	public String getMesgSenderCtryName() {
		return this.mesgSenderCtryName;
	}

	public void setMesgSenderCtryName(String mesgSenderCtryName) {
		this.mesgSenderCtryName = mesgSenderCtryName;
	}

	public String getMesgSenderInstitutionName() {
		return this.mesgSenderInstitutionName;
	}

	public void setMesgSenderInstitutionName(String mesgSenderInstitutionName) {
		this.mesgSenderInstitutionName = mesgSenderInstitutionName;
	}

	public String getMesgSenderLocation() {
		return this.mesgSenderLocation;
	}

	public void setMesgSenderLocation(String mesgSenderLocation) {
		this.mesgSenderLocation = mesgSenderLocation;
	}

	public String getMesgSenderSwiftAddress() {
		return this.mesgSenderSwiftAddress;
	}

	public void setMesgSenderSwiftAddress(String mesgSenderSwiftAddress) {
		this.mesgSenderSwiftAddress = mesgSenderSwiftAddress;
	}

	public String getMesgSenderX1() {
		return this.mesgSenderX1;
	}

	public void setMesgSenderX1(String mesgSenderX1) {
		this.mesgSenderX1 = mesgSenderX1;
	}

	public String getMesgSenderX2() {
		return this.mesgSenderX2;
	}

	public void setMesgSenderX2(String mesgSenderX2) {
		this.mesgSenderX2 = mesgSenderX2;
	}

	public String getMesgSenderX3() {
		return this.mesgSenderX3;
	}

	public void setMesgSenderX3(String mesgSenderX3) {
		this.mesgSenderX3 = mesgSenderX3;
	}

	public String getMesgSenderX4() {
		return this.mesgSenderX4;
	}

	public void setMesgSenderX4(String mesgSenderX4) {
		this.mesgSenderX4 = mesgSenderX4;
	}

	public String getMesgService() {
		return this.mesgService;
	}

	public void setMesgService(String mesgService) {
		this.mesgService = mesgService;
	}

	public String getMesgSignDigestReference() {
		return this.mesgSignDigestReference;
	}

	public void setMesgSignDigestReference(String mesgSignDigestReference) {
		this.mesgSignDigestReference = mesgSignDigestReference;
	}

	public String getMesgSignDigestValue() {
		return this.mesgSignDigestValue;
	}

	public void setMesgSignDigestValue(String mesgSignDigestValue) {
		this.mesgSignDigestValue = mesgSignDigestValue;
	}

	public String getMesgStatus() {
		return this.mesgStatus;
	}

	public void setMesgStatus(String mesgStatus) {
		this.mesgStatus = mesgStatus;
	}

	public String getMesgSubFormat() {
		return this.mesgSubFormat;
	}

	public void setMesgSubFormat(String mesgSubFormat) {
		this.mesgSubFormat = mesgSubFormat;
	}

	public String getMesgSyntaxTableVer() {
		return this.mesgSyntaxTableVer;
	}

	public void setMesgSyntaxTableVer(String mesgSyntaxTableVer) {
		this.mesgSyntaxTableVer = mesgSyntaxTableVer;
	}

	public String getMesgTemplateName() {
		return this.mesgTemplateName;
	}

	public void setMesgTemplateName(String mesgTemplateName) {
		this.mesgTemplateName = mesgTemplateName;
	}

	public BigDecimal getMesgToken() {
		return this.mesgToken;
	}

	public void setMesgToken(BigDecimal mesgToken) {
		this.mesgToken = mesgToken;
	}

	public String getMesgTrnRef() {
		return this.mesgTrnRef;
	}

	public void setMesgTrnRef(String mesgTrnRef) {
		this.mesgTrnRef = mesgTrnRef;
	}

	public String getMesgType() {
		return this.mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public BigDecimal getMesgUsePkiSignature() {
		return this.mesgUsePkiSignature;
	}

	public void setMesgUsePkiSignature(BigDecimal mesgUsePkiSignature) {
		this.mesgUsePkiSignature = mesgUsePkiSignature;
	}

	public BigDecimal getMesgUserIssuedAsPde() {
		return this.mesgUserIssuedAsPde;
	}

	public void setMesgUserIssuedAsPde(BigDecimal mesgUserIssuedAsPde) {
		this.mesgUserIssuedAsPde = mesgUserIssuedAsPde;
	}

	public String getMesgUserPriorityCode() {
		return this.mesgUserPriorityCode;
	}

	public void setMesgUserPriorityCode(String mesgUserPriorityCode) {
		this.mesgUserPriorityCode = mesgUserPriorityCode;
	}

	public String getMesgUserReferenceText() {
		return this.mesgUserReferenceText;
	}

	public void setMesgUserReferenceText(String mesgUserReferenceText) {
		this.mesgUserReferenceText = mesgUserReferenceText;
	}

	public String getMesgUumid() {
		return this.mesgUumid;
	}

	public void setMesgUumid(String mesgUumid) {
		this.mesgUumid = mesgUumid;
	}

	public BigDecimal getMesgUumidSuffix() {
		return this.mesgUumidSuffix;
	}

	public void setMesgUumidSuffix(BigDecimal mesgUumidSuffix) {
		this.mesgUumidSuffix = mesgUumidSuffix;
	}

	public String getMesgValidationPassed() {
		return this.mesgValidationPassed = "VAL_NO_VALIDATION";
	}

	public void setMesgValidationPassed(String mesgValidationPassed) {
		this.mesgValidationPassed = mesgValidationPassed;
	}

	public String getMesgValidationRequested() {
		return this.mesgValidationRequested;
	}

	public void setMesgValidationRequested(String mesgValidationRequested) {
		this.mesgValidationRequested = mesgValidationRequested;
	}

	public String getMesgVerfOperNickname() {
		return this.mesgVerfOperNickname;
	}

	public void setMesgVerfOperNickname(String mesgVerfOperNickname) {
		this.mesgVerfOperNickname = mesgVerfOperNickname;
	}

	public String getMesgXmlQueryRef1() {
		return this.mesgXmlQueryRef1;
	}

	public void setMesgXmlQueryRef1(String mesgXmlQueryRef1) {
		this.mesgXmlQueryRef1 = mesgXmlQueryRef1;
	}

	public String getMesgXmlQueryRef2() {
		return this.mesgXmlQueryRef2;
	}

	public void setMesgXmlQueryRef2(String mesgXmlQueryRef2) {
		this.mesgXmlQueryRef2 = mesgXmlQueryRef2;
	}

	public String getMesgXmlQueryRef3() {
		return this.mesgXmlQueryRef3;
	}

	public void setMesgXmlQueryRef3(String mesgXmlQueryRef3) {
		this.mesgXmlQueryRef3 = mesgXmlQueryRef3;
	}

	public BigDecimal getMesgZz41IsPossibleDup() {
		return this.mesgZz41IsPossibleDup;
	}

	public void setMesgZz41IsPossibleDup(BigDecimal mesgZz41IsPossibleDup) {
		this.mesgZz41IsPossibleDup = mesgZz41IsPossibleDup;
	}

	public BigDecimal getRestored() {
		return this.restored;
	}

	public void setRestored(BigDecimal restored) {
		this.restored = restored;
	}

	public BigDecimal getSetId() {
		return this.setId;
	}

	public void setSetId(BigDecimal setId) {
		this.setId = setId;
	}

	public String getXCategory() {
		return this.xCategory;
	}

	public void setXCategory(String xCategory) {
		this.xCategory = xCategory;
	}

	public BigDecimal getXFinAmount() {
		return this.xFinAmount;
	}

	public void setXFinAmount(BigDecimal xFinAmount) {
		this.xFinAmount = xFinAmount;
	}

	public String getXFinCcy() {
		return this.xFinCcy;
	}

	public void setXFinCcy(String xFinCcy) {
		this.xFinCcy = xFinCcy;
	}

	public BigDecimal getXFinOcmtAmount() {
		return this.xFinOcmtAmount;
	}

	public void setXFinOcmtAmount(BigDecimal xFinOcmtAmount) {
		this.xFinOcmtAmount = xFinOcmtAmount;
	}

	public String getXFinOcmtCcy() {
		return this.xFinOcmtCcy;
	}

	public void setXFinOcmtCcy(String xFinOcmtCcy) {
		this.xFinOcmtCcy = xFinOcmtCcy;
	}

	public Date getXFinValueDate() {
		return this.xFinValueDate;
	}

	public void setXFinValueDate(Date xFinValueDate) {
		this.xFinValueDate = xFinValueDate;
	}

	public String getXInst0UnitName() {
		return this.xInst0UnitName;
	}

	public void setXInst0UnitName(String xInst0UnitName) {
		this.xInst0UnitName = xInst0UnitName;
	}

	public String getXOwnLt() {
		return this.xOwnLt;
	}

	public void setXOwnLt(String xOwnLt) {
		this.xOwnLt = xOwnLt;
	}

	public String getXReceiverX1() {
		return this.xReceiverX1;
	}

	public void setXReceiverX1(String xReceiverX1) {
		this.xReceiverX1 = xReceiverX1;
	}

	public String getXReceiverX2() {
		return this.xReceiverX2;
	}

	public void setXReceiverX2(String xReceiverX2) {
		this.xReceiverX2 = xReceiverX2;
	}

	public String getXReceiverX3() {
		return this.xReceiverX3;
	}

	public void setXReceiverX3(String xReceiverX3) {
		this.xReceiverX3 = xReceiverX3;
	}

	public String getXReceiverX4() {
		return this.xReceiverX4;
	}

	public void setXReceiverX4(String xReceiverX4) {
		this.xReceiverX4 = xReceiverX4;
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

	public Text getRtext() {
		return this.rtext;
	}

	public void setRtext(Text rtext) {
		this.rtext = rtext;
	}

	public List<XmlTextMessage> getrXmlText() {
		return rXmlText;
	}

	public void setrXmlText(List<XmlTextMessage> rXmlText) {
		this.rXmlText = rXmlText;
	}

	public List<Inst> getRinsts() {
		return rinsts;
	}

	public void setRinsts(List<Inst> rinsts) {
		this.rinsts = rinsts;
	}

	public Inst addRinst(Inst rinst) {
		getRinsts().add(rinst);
		rinst.setRmesg(this);

		return rinst;
	}

	public Inst removeRinst(Inst rinst) {
		getRinsts().remove(rinst);
		rinst.setRmesg(null);

		return rinst;
	}

	public Inst addRinstPart(InstPart rinstPart) {
		getRinstsPart().add(rinstPart);
		rinstPart.setRmesg(this);

		return rinstPart;
	}

	public Inst removeRinstPart(InstPart rinstPart) {
		getRinstsPart().remove(rinstPart);
		rinstPart.setRmesg(null);

		return rinstPart;
	}

	public Rfile getRfile() {
		return rfile;
	}

	public void setRfile(Rfile rfile) {
		this.rfile = rfile;
	}

	public List<InstPart> getRinstsPart() {
		return rinstsPart;
	}

	public void setRinstsPart(List<InstPart> rinstsPart) {
		this.rinstsPart = rinstsPart;
	}

	public TextPart getRtextPart() {
		return rtextPart;
	}

	public void setRtextPart(TextPart rtextPart) {
		this.rtextPart = rtextPart;
	}

	public BigDecimal getMesgIsCopyRequired() {
		return mesgIsCopyRequired;
	}

	public void setMesgIsCopyRequired(BigDecimal mesgIsCopyRequired) {
		this.mesgIsCopyRequired = mesgIsCopyRequired;
	}

	public BigDecimal getMesgAuthDelvNotifReq() {
		return mesgAuthDelvNotifReq;
	}

	public void setMesgAuthDelvNotifReq(BigDecimal mesgAuthDelvNotifReq) {
		this.mesgAuthDelvNotifReq = mesgAuthDelvNotifReq;
	}

	public BigDecimal getX_Last_Rec_Appe_Seq_Nbr() {
		return x_Last_Rec_Appe_Seq_Nbr;
	}

	public void setX_Last_Rec_Appe_Seq_Nbr(BigDecimal x_Last_Rec_Appe_Seq_Nbr) {
		this.x_Last_Rec_Appe_Seq_Nbr = x_Last_Rec_Appe_Seq_Nbr;
	}

	public BigDecimal getX_Last_Emi_Appe_Seq_Nbr() {
		return x_Last_Emi_Appe_Seq_Nbr;
	}

	public void setX_Last_Emi_Appe_Seq_Nbr(BigDecimal x_Last_Emi_Appe_Seq_Nbr) {
		this.x_Last_Emi_Appe_Seq_Nbr = x_Last_Emi_Appe_Seq_Nbr;
	}

	public BigDecimal getX_LAST_EMI_APPE_SESSION_NBR() {
		return X_LAST_EMI_APPE_SESSION_NBR;
	}

	public void setX_LAST_EMI_APPE_SESSION_NBR(BigDecimal x_LAST_EMI_APPE_SESSION_NBR) {
		X_LAST_EMI_APPE_SESSION_NBR = x_LAST_EMI_APPE_SESSION_NBR;
	}

	public BigDecimal getX_LAST_REC_APPE_SESSION_NBR() {
		return X_LAST_REC_APPE_SESSION_NBR;
	}

	public void setX_LAST_REC_APPE_SESSION_NBR(BigDecimal x_LAST_REC_APPE_SESSION_NBR) {
		X_LAST_REC_APPE_SESSION_NBR = x_LAST_REC_APPE_SESSION_NBR;
	}

	public String getX_LAST_EMI_APPE_IAPP_NAME() {
		return X_LAST_EMI_APPE_IAPP_NAME;
	}

	public void setX_LAST_EMI_APPE_IAPP_NAME(String x_LAST_EMI_APPE_IAPP_NAME) {
		X_LAST_EMI_APPE_IAPP_NAME = x_LAST_EMI_APPE_IAPP_NAME;
	}

	public String getX_LAST_REC_APPE_IAPP_NAME() {
		return X_LAST_REC_APPE_IAPP_NAME;
	}

	public void setX_LAST_REC_APPE_IAPP_NAME(String x_LAST_REC_APPE_IAPP_NAME) {
		X_LAST_REC_APPE_IAPP_NAME = x_LAST_REC_APPE_IAPP_NAME;
	}

	@Override
	public String toString() {
		return "Mesg [id=" + id + ", archived=" + archived + ", lastUpdate=" + lastUpdate + ", mesgApplSenderReference=" + mesgApplSenderReference + ", mesgBatchReference=" + mesgBatchReference + ", mesgCasSenderReference=" + mesgCasSenderReference
				+ ", mesgCasTargetRpName=" + mesgCasTargetRpName + ", mesgCcyAmount=" + mesgCcyAmount + ", mesgClass=" + mesgClass + ", mesgCopyServiceId=" + mesgCopyServiceId + ", mesgCreaApplServName=" + mesgCreaApplServName + ", mesgCreaDateTime="
				+ mesgCreaDateTime + ", mesgCreaMpfnName=" + mesgCreaMpfnName + ", mesgCreaOperNickname=" + mesgCreaOperNickname + ", mesgCreaRpName=" + mesgCreaRpName + ", mesgDataKeyword1=" + mesgDataKeyword1 + ", mesgDataKeyword2="
				+ mesgDataKeyword2 + ", mesgDataKeyword3=" + mesgDataKeyword3 + ", mesgDataLast=" + mesgDataLast + ", mesgDelvOverdueWarnReq=" + mesgDelvOverdueWarnReq + ", mesgFinCcyAmount=" + mesgFinCcyAmount + ", mesgFinValueDate="
				+ mesgFinValueDate + ", mesgFrmtName=" + mesgFrmtName + ", mesgIdentifier=" + mesgIdentifier + ", mesgIsDeleteInhibited=" + mesgIsDeleteInhibited + ", mesgIsLive=" + mesgIsLive + ", mesgIsPartial=" + mesgIsPartial
				+ ", mesgIsRetrieved=" + mesgIsRetrieved + ", mesgIsTextModified=" + mesgIsTextModified + ", mesgIsTextReadonly=" + mesgIsTextReadonly + ", mesgMesgUserGroup=" + mesgMesgUserGroup + ", mesgModDateTime=" + mesgModDateTime
				+ ", mesgModOperNickname=" + mesgModOperNickname + ", mesgNature=" + mesgNature + ", mesgNetworkApplInd=" + mesgNetworkApplInd + ", mesgNetworkDelvNotifReq=" + mesgNetworkDelvNotifReq + ", mesgNetworkObsoPeriod="
				+ mesgNetworkObsoPeriod + ", mesgNetworkPriority=" + mesgNetworkPriority + ", mesgPayloadType=" + mesgPayloadType + ", mesgPossibleDupCreation=" + mesgPossibleDupCreation + ", mesgReceiverAliaName=" + mesgReceiverAliaName
				+ ", mesgReceiverSwiftAddress=" + mesgReceiverSwiftAddress + ", mesgRecoveryAcceptInfo=" + mesgRecoveryAcceptInfo + ", mesgRelTrnRef=" + mesgRelTrnRef + ", mesgRelatedSUmid=" + mesgRelatedSUmid + ", mesgReleaseInfo=" + mesgReleaseInfo
				+ ", mesgRequestType=" + mesgRequestType + ", mesgRequestorDn=" + mesgRequestorDn + ", mesgSecurityIappName=" + mesgSecurityIappName + ", mesgSecurityRequired=" + mesgSecurityRequired + ", mesgSenderBranchInfo=" + mesgSenderBranchInfo
				+ ", mesgSenderCityName=" + mesgSenderCityName + ", mesgSenderCorrType=" + mesgSenderCorrType + ", mesgSenderCtryCode=" + mesgSenderCtryCode + ", mesgSenderCtryName=" + mesgSenderCtryName + ", mesgSenderInstitutionName="
				+ mesgSenderInstitutionName + ", mesgSenderLocation=" + mesgSenderLocation + ", mesgSenderSwiftAddress=" + mesgSenderSwiftAddress + ", mesgSenderX1=" + mesgSenderX1 + ", mesgSenderX2=" + mesgSenderX2 + ", mesgSenderX3=" + mesgSenderX3
				+ ", mesgSenderX4=" + mesgSenderX4 + ", mesgService=" + mesgService + ", mesgSignDigestReference=" + mesgSignDigestReference + ", mesgSignDigestValue=" + mesgSignDigestValue + ", mesgStatus=" + mesgStatus + ", mesgSubFormat="
				+ mesgSubFormat + ", mesgSyntaxTableVer=" + mesgSyntaxTableVer + ", mesgTemplateName=" + mesgTemplateName + ", mesgToken=" + mesgToken + ", mesgTrnRef=" + mesgTrnRef + ", mesgType=" + mesgType + ", mesgUsePkiSignature="
				+ mesgUsePkiSignature + ", mesgUserIssuedAsPde=" + mesgUserIssuedAsPde + ", mesgUserPriorityCode=" + mesgUserPriorityCode + ", mesgUserReferenceText=" + mesgUserReferenceText + ", mesgUumid=" + mesgUumid + ", mesgUumidSuffix="
				+ mesgUumidSuffix + ", mesgValidationPassed=" + mesgValidationPassed + ", mesgValidationRequested=" + mesgValidationRequested + ", mesgVerfOperNickname=" + mesgVerfOperNickname + ", mesgXmlQueryRef1=" + mesgXmlQueryRef1
				+ ", mesgXmlQueryRef2=" + mesgXmlQueryRef2 + ", mesgXmlQueryRef3=" + mesgXmlQueryRef3 + ", mesgZz41IsPossibleDup=" + mesgZz41IsPossibleDup + ", restored=" + restored + ", setId=" + setId + ", xCategory=" + xCategory + ", xFinAmount="
				+ xFinAmount + ", xFinCcy=" + xFinCcy + ", xFinOcmtAmount=" + xFinOcmtAmount + ", xFinOcmtCcy=" + xFinOcmtCcy + ", xFinValueDate=" + xFinValueDate + ", xInst0UnitName=" + xInst0UnitName + ", xOwnLt=" + xOwnLt + ", xReceiverX1="
				+ xReceiverX1 + ", xReceiverX2=" + xReceiverX2 + ", xReceiverX3=" + xReceiverX3 + ", xReceiverX4=" + xReceiverX4 + "]";
	}

}