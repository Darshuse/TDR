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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the RINST database table.
 * 
 */
@Entity
@Table(name = "RINST")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQuery(name = "Inst.findAll", query = "SELECT i FROM Inst i")
@Cacheable(value = false)
public class Inst extends AbstractReportingEntity implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 7375315170260341994L;
/*	private Date defaultDate = null ;
	{try {
		defaultDate = new SimpleDateFormat("dd/MM/yyyy").parse("1/1/1970");
	} catch (ParseException e) {
		e.printStackTrace();
	}}
	*/
	@EmbeddedId
	private InstPK id;

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
	private String instAppliRpName = null ;

	@Column(name = "INST_AUTH_OPER_NICKNAME")
	private String instAuthOperNickname = null ;

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
	private BigDecimal instDataLast = null ;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INST_DEFERRED_TIME")
	private Date instDeferredTime = null ;

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
	private String instLastMpfnResult="R_RESULT";

	@Column(name = "INST_LAST_OPER_NICKNAME")
	private String instLastOperNickname = null ;

	@Column(name = "INST_LETTER_CODING")
	private String instLetterCoding;

	@Column(name = "INST_MPFN_HANDLE")
	private String instMpfnHandle= null;

	@Column(name = "INST_MPFN_NAME")
	private String instMpfnName = null ;

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

 
	
	//bi-directional many-to-one association to Appe
	@OneToMany(mappedBy="rinst",cascade=CascadeType.PERSIST)
	private List<Appe> rappes = new ArrayList<Appe>();
	
	//bi-directional many-to-one association to Appe
	@OneToMany(mappedBy="rinst",cascade=CascadeType.PERSIST)
	private List<AppePart> rappesPart = new ArrayList<AppePart>();
	
	//bi-directional many-to-one association to Rintv
	@OneToMany(mappedBy="rinst",cascade=CascadeType.PERSIST)
	private List<Rintv> rIntv = new ArrayList<Rintv>();
	
	
	//bi-directional many-to-one association to Rintv
	@OneToMany(mappedBy="rinst",cascade=CascadeType.PERSIST)
	private List<RintvPart> rIntvPart = new ArrayList<RintvPart>();
	// bi-directional many-to-one association to Mesg
	 @ManyToOne(cascade=CascadeType.PERSIST)
	 @JoinColumns({
	 @JoinColumn(name="AID", referencedColumnName="AID",insertable=false, updatable=false),
	 @JoinColumn(name="INST_S_UMIDH", referencedColumnName="MESG_S_UMIDH",insertable=false, updatable=false),
	 @JoinColumn(name="INST_S_UMIDL", referencedColumnName="MESG_S_UMIDL",insertable=false, updatable=false)
	 })
	private Mesg rmesg;

	public Inst() {
	}

	public InstPK getId() {
		return this.id;
	}

	public void setId(InstPK id) {
		this.id = id;
	}

	public String getInitialTargetRpName() {
		return this.initialTargetRpName;
	}

	public void setInitialTargetRpName(String initialTargetRpName) {
		this.initialTargetRpName = initialTargetRpName;
	}

	public String getInstAnswerback() {
		return this.instAnswerback;
	}

	public void setInstAnswerback(String instAnswerback) {
		this.instAnswerback = instAnswerback;
	}

	public Date getInstAppeDateTime() {
		return this.instAppeDateTime;
	}

	public void setInstAppeDateTime(Date instAppeDateTime) {
		this.instAppeDateTime = instAppeDateTime;
	}

	public BigDecimal getInstAppeSeqNbr() {
		return this.instAppeSeqNbr;
	}

	public void setInstAppeSeqNbr(BigDecimal instAppeSeqNbr) {
		this.instAppeSeqNbr = instAppeSeqNbr;
	}

	public String getInstAppliRpName() {
		return this.instAppliRpName;
	}

	public void setInstAppliRpName(String instAppliRpName) {
		this.instAppliRpName = instAppliRpName;
	}

	public String getInstAuthOperNickname() {
		return this.instAuthOperNickname;
	}

	public void setInstAuthOperNickname(String instAuthOperNickname) {
		this.instAuthOperNickname = instAuthOperNickname;
	}

	public String getInstCalcDescription() {
		return this.instCalcDescription;
	}

	public void setInstCalcDescription(String instCalcDescription) {
		this.instCalcDescription = instCalcDescription;
	}

	public String getInstCalculatedTestkeyValue() {
		return this.instCalculatedTestkeyValue;
	}

	public void setInstCalculatedTestkeyValue(String instCalculatedTestkeyValue) {
		this.instCalculatedTestkeyValue = instCalculatedTestkeyValue;
	}

	public String getInstCbtReference() {
		return this.instCbtReference;
	}

	public void setInstCbtReference(String instCbtReference) {
		this.instCbtReference = instCbtReference;
	}

	public String getInstComputationDetails() {
		return this.instComputationDetails;
	}

	public void setInstComputationDetails(String instComputationDetails) {
		this.instComputationDetails = instComputationDetails;
	}

	public String getInstCorrX1() {
		return this.instCorrX1;
	}

	public void setInstCorrX1(String instCorrX1) {
		this.instCorrX1 = instCorrX1;
	}

	public String getInstCorrX2() {
		return this.instCorrX2;
	}

	public void setInstCorrX2(String instCorrX2) {
		this.instCorrX2 = instCorrX2;
	}

	public String getInstCreaApplServName() {
		return this.instCreaApplServName;
	}

	public void setInstCreaApplServName(String instCreaApplServName) {
		this.instCreaApplServName = instCreaApplServName;
	}

	public Date getInstCreaDateTime() {
		return this.instCreaDateTime;
	}

	public void setInstCreaDateTime(Date instCreaDateTime) {
		this.instCreaDateTime = instCreaDateTime;
	}

	public String getInstCreaMpfnName() {
		return this.instCreaMpfnName;
	}

	public void setInstCreaMpfnName(String instCreaMpfnName) {
		this.instCreaMpfnName = instCreaMpfnName;
	}

	public String getInstCreaRpName() {
		return this.instCreaRpName;
	}

	public void setInstCreaRpName(String instCreaRpName) {
		this.instCreaRpName = instCreaRpName;
	}

	public String getInstCrestComServerId() {
		return this.instCrestComServerId;
	}

	public void setInstCrestComServerId(String instCrestComServerId) {
		this.instCrestComServerId = instCrestComServerId;
	}

	public String getInstCrestGatewayId() {
		return this.instCrestGatewayId;
	}

	public void setInstCrestGatewayId(String instCrestGatewayId) {
		this.instCrestGatewayId = instCrestGatewayId;
	}

	public BigDecimal getInstDataLast() {
		return this.instDataLast;
	}

	public void setInstDataLast(BigDecimal instDataLast) {
		this.instDataLast = instDataLast;
	}

	public Date getInstDeferredTime() {
		return this.instDeferredTime;
	}

	public void setInstDeferredTime(Date instDeferredTime) {
		this.instDeferredTime = instDeferredTime;
	}

	public String getInstDeliveryMode() {
		return this.instDeliveryMode;
	}

	public void setInstDeliveryMode(String instDeliveryMode) {
		this.instDeliveryMode = instDeliveryMode;
	}

	public String getInstDispAddressCode() {
		return this.instDispAddressCode;
	}

	public void setInstDispAddressCode(String instDispAddressCode) {
		this.instDispAddressCode = instDispAddressCode;
	}

	public String getInstExtractedTestkeyValue() {
		return this.instExtractedTestkeyValue;
	}

	public void setInstExtractedTestkeyValue(String instExtractedTestkeyValue) {
		this.instExtractedTestkeyValue = instExtractedTestkeyValue;
	}

	public String getInstFaxCui() {
		return this.instFaxCui;
	}

	public void setInstFaxCui(String instFaxCui) {
		this.instFaxCui = instFaxCui;
	}

	public String getInstFaxNumber() {
		return this.instFaxNumber;
	}

	public void setInstFaxNumber(String instFaxNumber) {
		this.instFaxNumber = instFaxNumber;
	}

	public String getInstFaxOrigin() {
		return this.instFaxOrigin;
	}

	public void setInstFaxOrigin(String instFaxOrigin) {
		this.instFaxOrigin = instFaxOrigin;
	}

	public String getInstFaxTnapName() {
		return this.instFaxTnapName;
	}

	public void setInstFaxTnapName(String instFaxTnapName) {
		this.instFaxTnapName = instFaxTnapName;
	}

	public Date getInstIntvDateTime() {
		return this.instIntvDateTime;
	}

	public void setInstIntvDateTime(Date instIntvDateTime) {
		this.instIntvDateTime = instIntvDateTime;
	}

	public BigDecimal getInstIntvSeqNbr() {
		return this.instIntvSeqNbr;
	}

	public void setInstIntvSeqNbr(BigDecimal instIntvSeqNbr) {
		this.instIntvSeqNbr = instIntvSeqNbr;
	}

	public String getInstLastMpfnResult() {
		return this.instLastMpfnResult;
	}

	public void setInstLastMpfnResult(String instLastMpfnResult) {
		this.instLastMpfnResult = instLastMpfnResult;
	}

	public String getInstLastOperNickname() {
		return this.instLastOperNickname;
	}

	public void setInstLastOperNickname(String instLastOperNickname) {
		this.instLastOperNickname = instLastOperNickname;
	}

	public String getInstLetterCoding() {
		return this.instLetterCoding;
	}

	public void setInstLetterCoding(String instLetterCoding) {
		this.instLetterCoding = instLetterCoding;
	}

	public String getInstMpfnHandle() {
		return this.instMpfnHandle;
	}

	public void setInstMpfnHandle(String instMpfnHandle) {
		this.instMpfnHandle = instMpfnHandle;
	}

	public String getInstMpfnName() {
		return this.instMpfnName;
	}

	public void setInstMpfnName(String instMpfnName) {
		this.instMpfnName = instMpfnName;
	}

	public String getInstNotificationType() {
		return this.instNotificationType;
	}

	public void setInstNotificationType(String instNotificationType) {
		this.instNotificationType = instNotificationType;
	}

	public BigDecimal getInstNrIndicator() {
		return this.instNrIndicator;
	}

	public void setInstNrIndicator(BigDecimal instNrIndicator) {
		this.instNrIndicator = instNrIndicator;
	}

	public String getInstOperComment() {
		return this.instOperComment;
	}

	public void setInstOperComment(String instOperComment) {
		this.instOperComment = instOperComment;
	}

	public BigDecimal getInstProcessState() {
		return this.instProcessState;
	}

	public void setInstProcessState(BigDecimal instProcessState) {
		this.instProcessState = instProcessState;
	}

	public String getInstRcvDeliveryStatus() {
		return this.instRcvDeliveryStatus;
	}

	public void setInstRcvDeliveryStatus(String instRcvDeliveryStatus) {
		this.instRcvDeliveryStatus = instRcvDeliveryStatus;
	}

	public String getInstReceiverBranchInfo() {
		return this.instReceiverBranchInfo;
	}

	public void setInstReceiverBranchInfo(String instReceiverBranchInfo) {
		this.instReceiverBranchInfo = instReceiverBranchInfo;
	}

	public String getInstReceiverCityName() {
		return this.instReceiverCityName;
	}

	public void setInstReceiverCityName(String instReceiverCityName) {
		this.instReceiverCityName = instReceiverCityName;
	}

	public String getInstReceiverCorrType() {
		return this.instReceiverCorrType;
	}

	public void setInstReceiverCorrType(String instReceiverCorrType) {
		this.instReceiverCorrType = instReceiverCorrType;
	}

	public String getInstReceiverCtryCode() {
		return this.instReceiverCtryCode;
	}

	public void setInstReceiverCtryCode(String instReceiverCtryCode) {
		this.instReceiverCtryCode = instReceiverCtryCode;
	}

	public String getInstReceiverCtryName() {
		return this.instReceiverCtryName;
	}

	public void setInstReceiverCtryName(String instReceiverCtryName) {
		this.instReceiverCtryName = instReceiverCtryName;
	}

	public String getInstReceiverInstitutionName() {
		return this.instReceiverInstitutionName;
	}

	public void setInstReceiverInstitutionName(String instReceiverInstitutionName) {
		this.instReceiverInstitutionName = instReceiverInstitutionName;
	}

	public String getInstReceiverLocation() {
		return this.instReceiverLocation;
	}

	public void setInstReceiverLocation(String instReceiverLocation) {
		this.instReceiverLocation = instReceiverLocation;
	}

	public String getInstReceiverNetworkIappNam() {
		return this.instReceiverNetworkIappNam;
	}

	public void setInstReceiverNetworkIappNam(String instReceiverNetworkIappNam) {
		this.instReceiverNetworkIappNam = instReceiverNetworkIappNam;
	}

	public String getInstReceiverSecuIappName() {
		return this.instReceiverSecuIappName;
	}

	public void setInstReceiverSecuIappName(String instReceiverSecuIappName) {
		this.instReceiverSecuIappName = instReceiverSecuIappName;
	}

	public String getInstReceiverX1() {
		return this.instReceiverX1;
	}

	public void setInstReceiverX1(String instReceiverX1) {
		this.instReceiverX1 = instReceiverX1;
	}

	public String getInstReceiverX2() {
		return this.instReceiverX2;
	}

	public void setInstReceiverX2(String instReceiverX2) {
		this.instReceiverX2 = instReceiverX2;
	}

	public String getInstReceiverX3() {
		return this.instReceiverX3;
	}

	public void setInstReceiverX3(String instReceiverX3) {
		this.instReceiverX3 = instReceiverX3;
	}

	public String getInstReceiverX4() {
		return this.instReceiverX4;
	}

	public void setInstReceiverX4(String instReceiverX4) {
		this.instReceiverX4 = instReceiverX4;
	}

	public BigDecimal getInstRelatedNbr() {
		return this.instRelatedNbr;
	}

	public void setInstRelatedNbr(BigDecimal instRelatedNbr) {
		this.instRelatedNbr = instRelatedNbr;
	}

	public BigDecimal getInstRelativeRef() {
		return this.instRelativeRef;
	}

	public void setInstRelativeRef(BigDecimal instRelativeRef) {
		this.instRelativeRef = instRelativeRef;
	}

	public String getInstResponderDn() {
		return this.instResponderDn;
	}

	public void setInstResponderDn(String instResponderDn) {
		this.instResponderDn = instResponderDn;
	}

	public BigDecimal getInstRetryCount() {
		return this.instRetryCount;
	}

	public void setInstRetryCount(BigDecimal instRetryCount) {
		this.instRetryCount = instRetryCount;
	}

	public BigDecimal getInstRetryCycle() {
		return this.instRetryCycle;
	}

	public void setInstRetryCycle(BigDecimal instRetryCycle) {
		this.instRetryCycle = instRetryCycle;
	}

	public String getInstRoutingCode() {
		return this.instRoutingCode;
	}

	public void setInstRoutingCode(String instRoutingCode) {
		this.instRoutingCode = instRoutingCode;
	}

	public String getInstRpName() {
		return this.instRpName;
	}

	public void setInstRpName(String instRpName) {
		this.instRpName = instRpName;
	}

	public BigDecimal getInstSm2000Priority() {
		return this.instSm2000Priority;
	}

	public void setInstSm2000Priority(BigDecimal instSm2000Priority) {
		this.instSm2000Priority = instSm2000Priority;
	}

	public String getInstStatus() {
		return this.instStatus;
	}

	public void setInstStatus(String instStatus) {
		this.instStatus = instStatus;
	}

	public String getInstTelexNumber() {
		return this.instTelexNumber;
	}

	public void setInstTelexNumber(String instTelexNumber) {
		this.instTelexNumber = instTelexNumber;
	}

	public String getInstTelexOrigin() {
		return this.instTelexOrigin;
	}

	public void setInstTelexOrigin(String instTelexOrigin) {
		this.instTelexOrigin = instTelexOrigin;
	}

	public String getInstTestComment() {
		return this.instTestComment;
	}

	public void setInstTestComment(String instTestComment) {
		this.instTestComment = instTestComment;
	}

	public Date getInstTestDateTime() {
		return this.instTestDateTime;
	}

	public void setInstTestDateTime(Date instTestDateTime) {
		this.instTestDateTime = instTestDateTime;
	}

	public String getInstTestSystem() {
		return this.instTestSystem;
	}

	public void setInstTestSystem(String instTestSystem) {
		this.instTestSystem = instTestSystem;
	}

	public String getInstTestkeyRequired() {
		return this.instTestkeyRequired;
	}

	public void setInstTestkeyRequired(String instTestkeyRequired) {
		this.instTestkeyRequired = instTestkeyRequired;
	}

	public String getInstTestkeyStatus() {
		return this.instTestkeyStatus;
	}

	public void setInstTestkeyStatus(String instTestkeyStatus) {
		this.instTestkeyStatus = instTestkeyStatus;
	}

	public String getInstTnapName() {
		return this.instTnapName;
	}

	public void setInstTnapName(String instTnapName) {
		this.instTnapName = instTnapName;
	}

	public BigDecimal getInstToken() {
		return this.instToken;
	}

	public void setInstToken(BigDecimal instToken) {
		this.instToken = instToken;
	}

	public String getInstType() {
		return this.instType;
	}

	public void setInstType(String instType) {
		this.instType = instType;
	}

	public String getInstUnitName() {
		return this.instUnitName;
	}

	public void setInstUnitName(String instUnitName) {
		this.instUnitName = instUnitName;
	}

	public BigDecimal getInstVa41AudiSeqNbr() {
		return this.instVa41AudiSeqNbr;
	}

	public void setInstVa41AudiSeqNbr(BigDecimal instVa41AudiSeqNbr) {
		this.instVa41AudiSeqNbr = instVa41AudiSeqNbr;
	}

	public String getInstVa41AwbCheckTextResul() {
		return this.instVa41AwbCheckTextResul;
	}

	public void setInstVa41AwbCheckTextResul(String instVa41AwbCheckTextResul) {
		this.instVa41AwbCheckTextResul = instVa41AwbCheckTextResul;
	}

	public Date getInstVa41CalculDateTime() {
		return this.instVa41CalculDateTime;
	}

	public void setInstVa41CalculDateTime(Date instVa41CalculDateTime) {
		this.instVa41CalculDateTime = instVa41CalculDateTime;
	}

	public BigDecimal getInstVa41ContainsUnscissored() {
		return this.instVa41ContainsUnscissored;
	}

	public void setInstVa41ContainsUnscissored(BigDecimal instVa41ContainsUnscissored) {
		this.instVa41ContainsUnscissored = instVa41ContainsUnscissored;
	}

	public BigDecimal getInstVa41TestkeyCalculated() {
		return this.instVa41TestkeyCalculated;
	}

	public void setInstVa41TestkeyCalculated(BigDecimal instVa41TestkeyCalculated) {
		this.instVa41TestkeyCalculated = instVa41TestkeyCalculated;
	}

	public Date getXLastEmiAppeDateTime() {
		return this.xLastEmiAppeDateTime;
	}

	public void setXLastEmiAppeDateTime(Date xLastEmiAppeDateTime) {
		this.xLastEmiAppeDateTime = xLastEmiAppeDateTime;
	}

	public BigDecimal getXLastEmiAppeSeqNbr() {
		return this.xLastEmiAppeSeqNbr;
	}

	public void setXLastEmiAppeSeqNbr(BigDecimal xLastEmiAppeSeqNbr) {
		this.xLastEmiAppeSeqNbr = xLastEmiAppeSeqNbr;
	}

	public Date getXLastRecAppeDateTime() {
		return this.xLastRecAppeDateTime;
	}

	public void setXLastRecAppeDateTime(Date xLastRecAppeDateTime) {
		this.xLastRecAppeDateTime = xLastRecAppeDateTime;
	}

	public BigDecimal getXLastRecAppeSeqNbr() {
		return this.xLastRecAppeSeqNbr;
	}

	public void setXLastRecAppeSeqNbr(BigDecimal xLastRecAppeSeqNbr) {
		this.xLastRecAppeSeqNbr = xLastRecAppeSeqNbr;
	}

	public Mesg getRmesg() {
		return this.rmesg;
	}

	public void setRmesg(Mesg rmesg) {
		this.rmesg = rmesg;
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

	public List<Appe> getRappes() {
		return rappes;
	}

	public void setRappes(List<Appe> rappes) {
		this.rappes = rappes;
	}
	
	public Appe addRappe(Appe rappe) {
		getRappes().add(rappe);
		rappe.setRinst(this);

		return rappe;
	}

	public Appe removeRappe(Appe rappe) {
		getRappes().remove(rappe);
		rappe.setRinst(null);

		return rappe;
	}

	
	
	public Appe addRappePart(AppePart appePart) {
		getRappesPart().add(appePart);
		appePart.setRinst(this);

		return appePart;
	}

	public Appe removeRappePart(AppePart appePart) {
		getRappesPart().remove(appePart);
		appePart.setRinst(null);

		return appePart;
	}
	
	
	public Rintv addRintPart(RintvPart rintvPart) {
		getrIntvPart().add(rintvPart);
		rintvPart.setRinst(this); 
		return rintvPart;
	}

 
	public List<AppePart> getRappesPart() {
		return rappesPart;
	}

	public void setRappesPart(List<AppePart> rappesPart) {
		this.rappesPart = rappesPart;
	}

	public List<RintvPart> getrIntvPart() {
		return rIntvPart;
	}

	public void setrIntvPart(List<RintvPart> rIntvPart) {
		this.rIntvPart = rIntvPart;
	}
 
 
	
	public List<Rintv> getrIntv() {
		return rIntv;
	}

	public void setrIntv(List<Rintv> rIntv) {
		this.rIntv = rIntv;
	}
	
	@Override
	public String toString() {
		return "Inst [id=" + id + ", initialTargetRpName=" + initialTargetRpName + ", instAnswerback=" + instAnswerback + ", instAppeDateTime="
				+ instAppeDateTime + ", instAppeSeqNbr=" + instAppeSeqNbr + ", instAppliRpName=" + instAppliRpName + ", instAuthOperNickname="
				+ instAuthOperNickname + ", instCalcDescription=" + instCalcDescription + ", instCalculatedTestkeyValue="
				+ instCalculatedTestkeyValue + ", instCbtReference=" + instCbtReference + ", instComputationDetails=" + instComputationDetails
				+ ", instCorrX1=" + instCorrX1 + ", instCorrX2=" + instCorrX2 + ", instCreaApplServName=" + instCreaApplServName
				+ ", instCreaDateTime=" + instCreaDateTime + ", instCreaMpfnName=" + instCreaMpfnName + ", instCreaRpName=" + instCreaRpName
				+ ", instCrestComServerId=" + instCrestComServerId + ", instCrestGatewayId=" + instCrestGatewayId + ", instDataLast=" + instDataLast
				+ ", instDeferredTime=" + instDeferredTime + ", instDeliveryMode=" + instDeliveryMode + ", instDispAddressCode="
				+ instDispAddressCode + ", instExtractedTestkeyValue=" + instExtractedTestkeyValue + ", instFaxCui=" + instFaxCui
				+ ", instFaxNumber=" + instFaxNumber + ", instFaxOrigin=" + instFaxOrigin + ", instFaxTnapName=" + instFaxTnapName
				+ ", instIntvDateTime=" + instIntvDateTime + ", instIntvSeqNbr=" + instIntvSeqNbr + ", instLastMpfnResult=" + instLastMpfnResult
				+ ", instLastOperNickname=" + instLastOperNickname + ", instLetterCoding=" + instLetterCoding + ", instMpfnHandle=" + instMpfnHandle
				+ ", instMpfnName=" + instMpfnName + ", instNotificationType=" + instNotificationType + ", instNrIndicator=" + instNrIndicator
				+ ", instOperComment=" + instOperComment + ", instProcessState=" + instProcessState + ", instRcvDeliveryStatus="
				+ instRcvDeliveryStatus + ", instReceiverBranchInfo=" + instReceiverBranchInfo + ", instReceiverCityName=" + instReceiverCityName
				+ ", instReceiverCorrType=" + instReceiverCorrType + ", instReceiverCtryCode=" + instReceiverCtryCode + ", instReceiverCtryName="
				+ instReceiverCtryName + ", instReceiverInstitutionName=" + instReceiverInstitutionName + ", instReceiverLocation="
				+ instReceiverLocation + ", instReceiverNetworkIappNam=" + instReceiverNetworkIappNam + ", instReceiverSecuIappName="
				+ instReceiverSecuIappName + ", instReceiverX1=" + instReceiverX1 + ", instReceiverX2=" + instReceiverX2 + ", instReceiverX3="
				+ instReceiverX3 + ", instReceiverX4=" + instReceiverX4 + ", instRelatedNbr=" + instRelatedNbr + ", instRelativeRef="
				+ instRelativeRef + ", instResponderDn=" + instResponderDn + ", instRetryCount=" + instRetryCount + ", instRetryCycle="
				+ instRetryCycle + ", instRoutingCode=" + instRoutingCode + ", instRpName=" + instRpName + ", instSm2000Priority="
				+ instSm2000Priority + ", instStatus=" + instStatus + ", instTelexNumber=" + instTelexNumber + ", instTelexOrigin=" + instTelexOrigin
				+ ", instTestComment=" + instTestComment + ", instTestDateTime=" + instTestDateTime + ", instTestSystem=" + instTestSystem
				+ ", instTestkeyRequired=" + instTestkeyRequired + ", instTestkeyStatus=" + instTestkeyStatus + ", instTnapName=" + instTnapName
				+ ", instToken=" + instToken + ", instType=" + instType + ", instUnitName=" + instUnitName + ", instVa41AudiSeqNbr="
				+ instVa41AudiSeqNbr + ", instVa41AwbCheckTextResul=" + instVa41AwbCheckTextResul + ", instVa41CalculDateTime="
				+ instVa41CalculDateTime + ", instVa41ContainsUnscissored=" + instVa41ContainsUnscissored + ", instVa41TestkeyCalculated="
				+ instVa41TestkeyCalculated + ", xLastEmiAppeDateTime=" + xLastEmiAppeDateTime + ", xLastEmiAppeSeqNbr=" + xLastEmiAppeSeqNbr
				+ ", xLastRecAppeDateTime=" + xLastRecAppeDateTime + ", xLastRecAppeSeqNbr=" + xLastRecAppeSeqNbr + ", rmesg=" + rmesg + "]";
	}

}