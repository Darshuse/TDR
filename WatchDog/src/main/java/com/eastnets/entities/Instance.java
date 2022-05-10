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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "RINST")
public class Instance implements Serializable {

	private static final long serialVersionUID = -6136214908609194446L;

	@EmbeddedId
	private InstancePK id;

	@Column(name = "INITIAL_TARGET_RP_NAME")
	private String initialTargetRpName;

	@Column(name = "INST_ANSWERBACK")
	private String instAnswerback = null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INST_APPE_DATE_TIME")
	private Date instAppeDateTime;

	@Column(name = "INST_APPE_SEQ_NBR")
	private BigDecimal instAppeSeqNbr;

	@Column(name = "INST_APPLI_RP_NAME")
	private String instAppliRpName = null;

	@Column(name = "INST_AUTH_OPER_NICKNAME")
	private String instAuthOperNickname = null;

	@Column(name = "INST_CALC_DESCRIPTION")
	private String instCalcDescription;

	@Column(name = "INST_CALCULATED_TESTKEY_VALUE")
	private String instCalculatedTestkeyValue;

	@Column(name = "INST_CBT_REFERENCE")
	private String instCbtReference;

	@Column(name = "INST_COMPUTATION_DETAILS")
	private String instComputationDetails;

	@Column(name = "INST_CORR_X1")
	private String instCorrX1;

	@Column(name = "INST_CORR_X2")
	private String instCorrX2;

	@Column(name = "INST_CREA_APPL_SERV_NAME")
	private String instCreaApplServName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INST_CREA_DATE_TIME")
	private Date instCreaDateTime;

	@Column(name = "INST_CREA_MPFN_NAME")
	private String instCreaMpfnName = "mpc";

	@Column(name = "INST_CREA_RP_NAME")
	private String instCreaRpName = "REP_DUMMY_QUEUE";

	@Column(name = "INST_CREST_COM_SERVER_ID")
	private String instCrestComServerId;

	@Column(name = "INST_CREST_GATEWAY_ID")
	private String instCrestGatewayId;

	@Column(name = "INST_DATA_LAST")
	private BigDecimal instDataLast = null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INST_DEFERRED_TIME")
	private Date instDeferredTime = null;

	@Column(name = "INST_DELIVERY_MODE")
	private String instDeliveryMode;

	@Column(name = "INST_DISP_ADDRESS_CODE")
	private String instDispAddressCode;

	@Column(name = "INST_EXTRACTED_TESTKEY_VALUE")
	private String instExtractedTestkeyValue;

	@Column(name = "INST_FAX_CUI")
	private String instFaxCui;

	@Column(name = "INST_FAX_NUMBER")
	private String instFaxNumber;

	@Column(name = "INST_FAX_ORIGIN")
	private String instFaxOrigin;

	@Column(name = "INST_FAX_TNAP_NAME")
	private String instFaxTnapName;

	@Temporal(TemporalType.DATE)
	@Column(name = "INST_INTV_DATE_TIME")
	private Date instIntvDateTime;

	@Column(name = "INST_INTV_SEQ_NBR")
	private BigDecimal instIntvSeqNbr;

	@Column(name = "INST_LAST_MPFN_RESULT")
	private String instLastMpfnResult = "R_RESULT";

	@Column(name = "INST_LAST_OPER_NICKNAME")
	private String instLastOperNickname = null;

	@Column(name = "INST_LETTER_CODING")
	private String instLetterCoding;

	@Column(name = "INST_MPFN_HANDLE")
	private String instMpfnHandle = null;

	@Column(name = "INST_MPFN_NAME")
	private String instMpfnName = null;

	@Column(name = "INST_NOTIFICATION_TYPE")
	private String instNotificationType = "INST_NOTIFICATION_NONE";

	@Column(name = "INST_NR_INDICATOR")
	private BigDecimal instNrIndicator;

	@Column(name = "INST_OPER_COMMENT")
	private String instOperComment;

	@Column(name = "INST_PROCESS_STATE")
	private BigDecimal instProcessState = BigDecimal.ZERO;

	@Column(name = "INST_RCV_DELIVERY_STATUS")
	private String instRcvDeliveryStatus;

	@Column(name = "INST_RECEIVER_BRANCH_INFO")
	private String instReceiverBranchInfo;

	@Column(name = "INST_RECEIVER_CITY_NAME")
	private String instReceiverCityName;

	@Column(name = "INST_RECEIVER_CORR_TYPE")
	private String instReceiverCorrType;

	@Column(name = "INST_RECEIVER_CTRY_CODE")
	private String instReceiverCtryCode;

	@Column(name = "INST_RECEIVER_CTRY_NAME")
	private String instReceiverCtryName;

	@Column(name = "INST_RECEIVER_INSTITUTION_NAME")
	private String instReceiverInstitutionName;

	@Column(name = "INST_RECEIVER_LOCATION")
	private String instReceiverLocation;

	@Column(name = "INST_RECEIVER_NETWORK_IAPP_NAM")
	private String instReceiverNetworkIappNam;

	@Column(name = "INST_RECEIVER_SECU_IAPP_NAME")
	private String instReceiverSecuIappName;

	@Column(name = "INST_RECEIVER_X1")
	private String instReceiverX1;

	@Column(name = "INST_RECEIVER_X2")
	private String instReceiverX2;

	@Column(name = "INST_RECEIVER_X3")
	private String instReceiverX3;

	@Column(name = "INST_RECEIVER_X4")
	private String instReceiverX4;

	@Column(name = "INST_RELATED_NBR")
	private BigDecimal instRelatedNbr = BigDecimal.ZERO;

	@Column(name = "INST_RELATIVE_REF")
	private BigDecimal instRelativeRef = BigDecimal.ZERO;

	@Column(name = "INST_RESPONDER_DN")
	private String instResponderDn;

	@Column(name = "INST_RETRY_COUNT")
	private BigDecimal instRetryCount;

	@Column(name = "INST_RETRY_CYCLE")
	private BigDecimal instRetryCycle;

	@Column(name = "INST_ROUTING_CODE")
	private String instRoutingCode;

	@Column(name = "INST_RP_NAME")
	private String instRpName = null;

	@Column(name = "INST_SM2000_PRIORITY")
	private BigDecimal instSm2000Priority = new BigDecimal(1000);

	@Column(name = "INST_STATUS")
	private String instStatus = "COMPLETED";

	@Column(name = "INST_TELEX_NUMBER")
	private String instTelexNumber;

	@Column(name = "INST_TELEX_ORIGIN")
	private String instTelexOrigin;

	@Column(name = "INST_TEST_COMMENT")
	private String instTestComment;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INST_TEST_DATE_TIME")
	private Date instTestDateTime;

	@Column(name = "INST_TEST_SYSTEM")
	private String instTestSystem;

	@Column(name = "INST_TESTKEY_REQUIRED")
	private String instTestkeyRequired;

	@Column(name = "INST_TESTKEY_STATUS")
	private String instTestkeyStatus;

	@Column(name = "INST_TNAP_NAME")
	private String instTnapName;

	@Column(name = "INST_TOKEN")
	private BigDecimal instToken = BigDecimal.ZERO;

	@Column(name = "INST_TYPE")
	private String instType = "INST_TYPE_ORIGINAL";

	@Column(name = "INST_UNIT_NAME")
	private String instUnitName;

	@Column(name = "INST_VA41_AUDI_SEQ_NBR")
	private BigDecimal instVa41AudiSeqNbr;

	@Column(name = "INST_VA41_AWB_CHECK_TEXT_RESUL")
	private String instVa41AwbCheckTextResul;

	@Temporal(TemporalType.DATE)
	@Column(name = "INST_VA41_CALCUL_DATE_TIME")
	private Date instVa41CalculDateTime;

	@Column(name = "INST_VA41_CONTAINS_UNSCISSORED")
	private BigDecimal instVa41ContainsUnscissored;

	@Column(name = "INST_VA41_TESTKEY_CALCULATED")
	private BigDecimal instVa41TestkeyCalculated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "X_LAST_EMI_APPE_DATE_TIME")
	private Date xLastEmiAppeDateTime;

	@Column(name = "X_LAST_EMI_APPE_SEQ_NBR")
	private BigDecimal xLastEmiAppeSeqNbr;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "X_LAST_REC_APPE_DATE_TIME")
	private Date xLastRecAppeDateTime;

	@Column(name = "X_LAST_REC_APPE_SEQ_NBR")
	private BigDecimal xLastRecAppeSeqNbr;

	// bi-directional many-to-one association to Appe
	@OneToMany(mappedBy = "rinst", cascade = CascadeType.PERSIST)
	private List<Appendix> appendixes = new ArrayList<Appendix>();

	// bi-directional many-to-one association to Mesg
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({ @JoinColumn(name = "AID", referencedColumnName = "AID", insertable = false, updatable = false), @JoinColumn(name = "INST_S_UMIDH", referencedColumnName = "MESG_S_UMIDH", insertable = false, updatable = false),
			@JoinColumn(name = "INST_S_UMIDL", referencedColumnName = "MESG_S_UMIDL", insertable = false, updatable = false) })
	private Mesg mesg;

	public InstancePK getId() {
		return id;
	}

	public void setId(InstancePK id) {
		this.id = id;
	}

	public String getInitialTargetRpName() {
		return initialTargetRpName;
	}

	public void setInitialTargetRpName(String initialTargetRpName) {
		this.initialTargetRpName = initialTargetRpName;
	}

	public String getInstAnswerback() {
		return instAnswerback;
	}

	public void setInstAnswerback(String instAnswerback) {
		this.instAnswerback = instAnswerback;
	}

	public Date getInstAppeDateTime() {
		return instAppeDateTime;
	}

	public void setInstAppeDateTime(Date instAppeDateTime) {
		this.instAppeDateTime = instAppeDateTime;
	}

	public BigDecimal getInstAppeSeqNbr() {
		return instAppeSeqNbr;
	}

	public void setInstAppeSeqNbr(BigDecimal instAppeSeqNbr) {
		this.instAppeSeqNbr = instAppeSeqNbr;
	}

	public String getInstAppliRpName() {
		return instAppliRpName;
	}

	public void setInstAppliRpName(String instAppliRpName) {
		this.instAppliRpName = instAppliRpName;
	}

	public String getInstAuthOperNickname() {
		return instAuthOperNickname;
	}

	public void setInstAuthOperNickname(String instAuthOperNickname) {
		this.instAuthOperNickname = instAuthOperNickname;
	}

	public String getInstCalcDescription() {
		return instCalcDescription;
	}

	public void setInstCalcDescription(String instCalcDescription) {
		this.instCalcDescription = instCalcDescription;
	}

	public String getInstCalculatedTestkeyValue() {
		return instCalculatedTestkeyValue;
	}

	public void setInstCalculatedTestkeyValue(String instCalculatedTestkeyValue) {
		this.instCalculatedTestkeyValue = instCalculatedTestkeyValue;
	}

	public String getInstCbtReference() {
		return instCbtReference;
	}

	public void setInstCbtReference(String instCbtReference) {
		this.instCbtReference = instCbtReference;
	}

	public String getInstComputationDetails() {
		return instComputationDetails;
	}

	public void setInstComputationDetails(String instComputationDetails) {
		this.instComputationDetails = instComputationDetails;
	}

	public String getInstCorrX1() {
		return instCorrX1;
	}

	public void setInstCorrX1(String instCorrX1) {
		this.instCorrX1 = instCorrX1;
	}

	public String getInstCorrX2() {
		return instCorrX2;
	}

	public void setInstCorrX2(String instCorrX2) {
		this.instCorrX2 = instCorrX2;
	}

	public String getInstCreaApplServName() {
		return instCreaApplServName;
	}

	public void setInstCreaApplServName(String instCreaApplServName) {
		this.instCreaApplServName = instCreaApplServName;
	}

	public Date getInstCreaDateTime() {
		return instCreaDateTime;
	}

	public void setInstCreaDateTime(Date instCreaDateTime) {
		this.instCreaDateTime = instCreaDateTime;
	}

	public String getInstCreaMpfnName() {
		return instCreaMpfnName;
	}

	public void setInstCreaMpfnName(String instCreaMpfnName) {
		this.instCreaMpfnName = instCreaMpfnName;
	}

	public String getInstCreaRpName() {
		return instCreaRpName;
	}

	public void setInstCreaRpName(String instCreaRpName) {
		this.instCreaRpName = instCreaRpName;
	}

	public String getInstCrestComServerId() {
		return instCrestComServerId;
	}

	public void setInstCrestComServerId(String instCrestComServerId) {
		this.instCrestComServerId = instCrestComServerId;
	}

	public String getInstCrestGatewayId() {
		return instCrestGatewayId;
	}

	public void setInstCrestGatewayId(String instCrestGatewayId) {
		this.instCrestGatewayId = instCrestGatewayId;
	}

	public BigDecimal getInstDataLast() {
		return instDataLast;
	}

	public void setInstDataLast(BigDecimal instDataLast) {
		this.instDataLast = instDataLast;
	}

	public Date getInstDeferredTime() {
		return instDeferredTime;
	}

	public void setInstDeferredTime(Date instDeferredTime) {
		this.instDeferredTime = instDeferredTime;
	}

	public String getInstDeliveryMode() {
		return instDeliveryMode;
	}

	public void setInstDeliveryMode(String instDeliveryMode) {
		this.instDeliveryMode = instDeliveryMode;
	}

	public String getInstDispAddressCode() {
		return instDispAddressCode;
	}

	public void setInstDispAddressCode(String instDispAddressCode) {
		this.instDispAddressCode = instDispAddressCode;
	}

	public String getInstExtractedTestkeyValue() {
		return instExtractedTestkeyValue;
	}

	public void setInstExtractedTestkeyValue(String instExtractedTestkeyValue) {
		this.instExtractedTestkeyValue = instExtractedTestkeyValue;
	}

	public String getInstFaxCui() {
		return instFaxCui;
	}

	public void setInstFaxCui(String instFaxCui) {
		this.instFaxCui = instFaxCui;
	}

	public String getInstFaxNumber() {
		return instFaxNumber;
	}

	public void setInstFaxNumber(String instFaxNumber) {
		this.instFaxNumber = instFaxNumber;
	}

	public String getInstFaxOrigin() {
		return instFaxOrigin;
	}

	public void setInstFaxOrigin(String instFaxOrigin) {
		this.instFaxOrigin = instFaxOrigin;
	}

	public String getInstFaxTnapName() {
		return instFaxTnapName;
	}

	public void setInstFaxTnapName(String instFaxTnapName) {
		this.instFaxTnapName = instFaxTnapName;
	}

	public Date getInstIntvDateTime() {
		return instIntvDateTime;
	}

	public void setInstIntvDateTime(Date instIntvDateTime) {
		this.instIntvDateTime = instIntvDateTime;
	}

	public BigDecimal getInstIntvSeqNbr() {
		return instIntvSeqNbr;
	}

	public void setInstIntvSeqNbr(BigDecimal instIntvSeqNbr) {
		this.instIntvSeqNbr = instIntvSeqNbr;
	}

	public String getInstLastMpfnResult() {
		return instLastMpfnResult;
	}

	public void setInstLastMpfnResult(String instLastMpfnResult) {
		this.instLastMpfnResult = instLastMpfnResult;
	}

	public String getInstLastOperNickname() {
		return instLastOperNickname;
	}

	public void setInstLastOperNickname(String instLastOperNickname) {
		this.instLastOperNickname = instLastOperNickname;
	}

	public String getInstLetterCoding() {
		return instLetterCoding;
	}

	public void setInstLetterCoding(String instLetterCoding) {
		this.instLetterCoding = instLetterCoding;
	}

	public String getInstMpfnHandle() {
		return instMpfnHandle;
	}

	public void setInstMpfnHandle(String instMpfnHandle) {
		this.instMpfnHandle = instMpfnHandle;
	}

	public String getInstMpfnName() {
		return instMpfnName;
	}

	public void setInstMpfnName(String instMpfnName) {
		this.instMpfnName = instMpfnName;
	}

	public String getInstNotificationType() {
		return instNotificationType;
	}

	public void setInstNotificationType(String instNotificationType) {
		this.instNotificationType = instNotificationType;
	}

	public BigDecimal getInstNrIndicator() {
		return instNrIndicator;
	}

	public void setInstNrIndicator(BigDecimal instNrIndicator) {
		this.instNrIndicator = instNrIndicator;
	}

	public String getInstOperComment() {
		return instOperComment;
	}

	public void setInstOperComment(String instOperComment) {
		this.instOperComment = instOperComment;
	}

	public BigDecimal getInstProcessState() {
		return instProcessState;
	}

	public void setInstProcessState(BigDecimal instProcessState) {
		this.instProcessState = instProcessState;
	}

	public String getInstRcvDeliveryStatus() {
		return instRcvDeliveryStatus;
	}

	public void setInstRcvDeliveryStatus(String instRcvDeliveryStatus) {
		this.instRcvDeliveryStatus = instRcvDeliveryStatus;
	}

	public String getInstReceiverBranchInfo() {
		return instReceiverBranchInfo;
	}

	public void setInstReceiverBranchInfo(String instReceiverBranchInfo) {
		this.instReceiverBranchInfo = instReceiverBranchInfo;
	}

	public String getInstReceiverCityName() {
		return instReceiverCityName;
	}

	public void setInstReceiverCityName(String instReceiverCityName) {
		this.instReceiverCityName = instReceiverCityName;
	}

	public String getInstReceiverCorrType() {
		return instReceiverCorrType;
	}

	public void setInstReceiverCorrType(String instReceiverCorrType) {
		this.instReceiverCorrType = instReceiverCorrType;
	}

	public String getInstReceiverCtryCode() {
		return instReceiverCtryCode;
	}

	public void setInstReceiverCtryCode(String instReceiverCtryCode) {
		this.instReceiverCtryCode = instReceiverCtryCode;
	}

	public String getInstReceiverCtryName() {
		return instReceiverCtryName;
	}

	public void setInstReceiverCtryName(String instReceiverCtryName) {
		this.instReceiverCtryName = instReceiverCtryName;
	}

	public String getInstReceiverInstitutionName() {
		return instReceiverInstitutionName;
	}

	public void setInstReceiverInstitutionName(String instReceiverInstitutionName) {
		this.instReceiverInstitutionName = instReceiverInstitutionName;
	}

	public String getInstReceiverLocation() {
		return instReceiverLocation;
	}

	public void setInstReceiverLocation(String instReceiverLocation) {
		this.instReceiverLocation = instReceiverLocation;
	}

	public String getInstReceiverNetworkIappNam() {
		return instReceiverNetworkIappNam;
	}

	public void setInstReceiverNetworkIappNam(String instReceiverNetworkIappNam) {
		this.instReceiverNetworkIappNam = instReceiverNetworkIappNam;
	}

	public String getInstReceiverSecuIappName() {
		return instReceiverSecuIappName;
	}

	public void setInstReceiverSecuIappName(String instReceiverSecuIappName) {
		this.instReceiverSecuIappName = instReceiverSecuIappName;
	}

	public String getInstReceiverX1() {
		return instReceiverX1;
	}

	public void setInstReceiverX1(String instReceiverX1) {
		this.instReceiverX1 = instReceiverX1;
	}

	public String getInstReceiverX2() {
		return instReceiverX2;
	}

	public void setInstReceiverX2(String instReceiverX2) {
		this.instReceiverX2 = instReceiverX2;
	}

	public String getInstReceiverX3() {
		return instReceiverX3;
	}

	public void setInstReceiverX3(String instReceiverX3) {
		this.instReceiverX3 = instReceiverX3;
	}

	public String getInstReceiverX4() {
		return instReceiverX4;
	}

	public void setInstReceiverX4(String instReceiverX4) {
		this.instReceiverX4 = instReceiverX4;
	}

	public BigDecimal getInstRelatedNbr() {
		return instRelatedNbr;
	}

	public void setInstRelatedNbr(BigDecimal instRelatedNbr) {
		this.instRelatedNbr = instRelatedNbr;
	}

	public BigDecimal getInstRelativeRef() {
		return instRelativeRef;
	}

	public void setInstRelativeRef(BigDecimal instRelativeRef) {
		this.instRelativeRef = instRelativeRef;
	}

	public String getInstResponderDn() {
		return instResponderDn;
	}

	public void setInstResponderDn(String instResponderDn) {
		this.instResponderDn = instResponderDn;
	}

	public BigDecimal getInstRetryCount() {
		return instRetryCount;
	}

	public void setInstRetryCount(BigDecimal instRetryCount) {
		this.instRetryCount = instRetryCount;
	}

	public BigDecimal getInstRetryCycle() {
		return instRetryCycle;
	}

	public void setInstRetryCycle(BigDecimal instRetryCycle) {
		this.instRetryCycle = instRetryCycle;
	}

	public String getInstRoutingCode() {
		return instRoutingCode;
	}

	public void setInstRoutingCode(String instRoutingCode) {
		this.instRoutingCode = instRoutingCode;
	}

	public String getInstRpName() {
		return instRpName;
	}

	public void setInstRpName(String instRpName) {
		this.instRpName = instRpName;
	}

	public BigDecimal getInstSm2000Priority() {
		return instSm2000Priority;
	}

	public void setInstSm2000Priority(BigDecimal instSm2000Priority) {
		this.instSm2000Priority = instSm2000Priority;
	}

	public String getInstStatus() {
		return instStatus;
	}

	public void setInstStatus(String instStatus) {
		this.instStatus = instStatus;
	}

	public String getInstTelexNumber() {
		return instTelexNumber;
	}

	public void setInstTelexNumber(String instTelexNumber) {
		this.instTelexNumber = instTelexNumber;
	}

	public String getInstTelexOrigin() {
		return instTelexOrigin;
	}

	public void setInstTelexOrigin(String instTelexOrigin) {
		this.instTelexOrigin = instTelexOrigin;
	}

	public String getInstTestComment() {
		return instTestComment;
	}

	public void setInstTestComment(String instTestComment) {
		this.instTestComment = instTestComment;
	}

	public Date getInstTestDateTime() {
		return instTestDateTime;
	}

	public void setInstTestDateTime(Date instTestDateTime) {
		this.instTestDateTime = instTestDateTime;
	}

	public String getInstTestSystem() {
		return instTestSystem;
	}

	public void setInstTestSystem(String instTestSystem) {
		this.instTestSystem = instTestSystem;
	}

	public String getInstTestkeyRequired() {
		return instTestkeyRequired;
	}

	public void setInstTestkeyRequired(String instTestkeyRequired) {
		this.instTestkeyRequired = instTestkeyRequired;
	}

	public String getInstTestkeyStatus() {
		return instTestkeyStatus;
	}

	public void setInstTestkeyStatus(String instTestkeyStatus) {
		this.instTestkeyStatus = instTestkeyStatus;
	}

	public String getInstTnapName() {
		return instTnapName;
	}

	public void setInstTnapName(String instTnapName) {
		this.instTnapName = instTnapName;
	}

	public BigDecimal getInstToken() {
		return instToken;
	}

	public void setInstToken(BigDecimal instToken) {
		this.instToken = instToken;
	}

	public String getInstType() {
		return instType;
	}

	public void setInstType(String instType) {
		this.instType = instType;
	}

	public String getInstUnitName() {
		return instUnitName;
	}

	public void setInstUnitName(String instUnitName) {
		this.instUnitName = instUnitName;
	}

	public BigDecimal getInstVa41AudiSeqNbr() {
		return instVa41AudiSeqNbr;
	}

	public void setInstVa41AudiSeqNbr(BigDecimal instVa41AudiSeqNbr) {
		this.instVa41AudiSeqNbr = instVa41AudiSeqNbr;
	}

	public String getInstVa41AwbCheckTextResul() {
		return instVa41AwbCheckTextResul;
	}

	public void setInstVa41AwbCheckTextResul(String instVa41AwbCheckTextResul) {
		this.instVa41AwbCheckTextResul = instVa41AwbCheckTextResul;
	}

	public Date getInstVa41CalculDateTime() {
		return instVa41CalculDateTime;
	}

	public void setInstVa41CalculDateTime(Date instVa41CalculDateTime) {
		this.instVa41CalculDateTime = instVa41CalculDateTime;
	}

	public BigDecimal getInstVa41ContainsUnscissored() {
		return instVa41ContainsUnscissored;
	}

	public void setInstVa41ContainsUnscissored(BigDecimal instVa41ContainsUnscissored) {
		this.instVa41ContainsUnscissored = instVa41ContainsUnscissored;
	}

	public BigDecimal getInstVa41TestkeyCalculated() {
		return instVa41TestkeyCalculated;
	}

	public void setInstVa41TestkeyCalculated(BigDecimal instVa41TestkeyCalculated) {
		this.instVa41TestkeyCalculated = instVa41TestkeyCalculated;
	}

	public Date getxLastEmiAppeDateTime() {
		return xLastEmiAppeDateTime;
	}

	public void setxLastEmiAppeDateTime(Date xLastEmiAppeDateTime) {
		this.xLastEmiAppeDateTime = xLastEmiAppeDateTime;
	}

	public BigDecimal getxLastEmiAppeSeqNbr() {
		return xLastEmiAppeSeqNbr;
	}

	public void setxLastEmiAppeSeqNbr(BigDecimal xLastEmiAppeSeqNbr) {
		this.xLastEmiAppeSeqNbr = xLastEmiAppeSeqNbr;
	}

	public Date getxLastRecAppeDateTime() {
		return xLastRecAppeDateTime;
	}

	public void setxLastRecAppeDateTime(Date xLastRecAppeDateTime) {
		this.xLastRecAppeDateTime = xLastRecAppeDateTime;
	}

	public BigDecimal getxLastRecAppeSeqNbr() {
		return xLastRecAppeSeqNbr;
	}

	public void setxLastRecAppeSeqNbr(BigDecimal xLastRecAppeSeqNbr) {
		this.xLastRecAppeSeqNbr = xLastRecAppeSeqNbr;
	}

	public List<Appendix> getAppendixes() {
		return appendixes;
	}

	public void setAppendixes(List<Appendix> appendixes) {
		this.appendixes = appendixes;
	}

	public Mesg getMessage() {
		return mesg;
	}

	public void setMessage(Mesg mesg) {
		this.mesg = mesg;
	}

}
