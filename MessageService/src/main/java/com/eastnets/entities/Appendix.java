package com.eastnets.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "RAPPE")
@Cacheable(value = false)
public class Appendix implements Serializable {

	private static final long serialVersionUID = -1597935097668039311L;

	@EmbeddedId
	private AppendixPK id;

	@Column(name = "APPE_ACK_NACK_LAU_RESULT")
	private String appeAckNackLauResult;

	@Column(name = "APPE_ACK_NACK_TEXT")
	private String appeAckNackText;

	@Column(name = "APPE_ANSWERBACK")
	private String appeAnswerback;

	@Column(name = "APPE_AUTH_RESULT")
	private String appeAuthResult;

	@Column(name = "APPE_AUTH_VALUE")
	private String appeAuthValue;

	@Column(name = "APPE_AUTHORISER_DN")
	private String appeAuthoriserDn;

	@Column(name = "APPE_CARRIER_ACCEPTANCE_ID")
	private String appeCarrierAcceptanceId;

	@Column(name = "APPE_CHECKSUM_RESULT")
	private String appeChecksumResult;

	@Column(name = "APPE_CHECKSUM_VALUE")
	private String appeChecksumValue;

	@Column(name = "APPE_COMBINED_AUTH_RES")
	private String appeCombinedAuthRes;

	@Column(name = "APPE_COMBINED_PAC_RES")
	private String appeCombinedPacRes;

	@Column(name = "APPE_CONN_RESPONSE_CODE")
	private String appeConnResponseCode;

	@Column(name = "APPE_CONN_RESPONSE_TEXT")
	private String appeConnResponseText;

	@Column(name = "APPE_CREA_APPL_SERV_NAME")
	private String appeCreaApplServName;

	@Column(name = "APPE_CREA_MPFN_NAME")
	private String appeCreaMpfnName;

	@Column(name = "APPE_CREA_RP_NAME")
	private String appeCreaRpName;

	@Column(name = "APPE_CREST_COM_SERVER_ID")
	private String appeCrestComServerId;

	@Column(name = "APPE_CREST_GATEWAY_ID")
	private String appeCrestGatewayId;

	@Column(name = "APPE_CUI")
	private String appeCui;

	@Column(name = "APPE_DATA_LAST")
	private BigDecimal appeDataLast;

	@Column(name = "APPE_FAX_BATCH_SEQUENCE")
	private String appeFaxBatchSequence;

	@Column(name = "APPE_FAX_DURATION")
	private String appeFaxDuration;

	@Column(name = "APPE_FAX_NUMBER")
	private String appeFaxNumber;

	@Column(name = "APPE_FAX_TNAP_NAME")
	private String appeFaxTnapName;

	@Column(name = "APPE_IAPP_NAME")
	private String appeIappName;

	@Lob
	@Column(name = "APPE_LARGE_DATA")
	private String appeLargeData;

	@Column(name = "APPE_LAU_RESULT")
	private String appeLauResult;

	@Temporal(TemporalType.DATE)
	@Column(name = "APPE_LOCAL_OUTPUT_TIME")
	private Date appeLocalOutputTime;

	@Column(name = "APPE_NAK_REASON")
	private String appeNakReason;

	@Column(name = "APPE_NETWORK_DELIVERY_STATUS")
	private String appeNetworkDeliveryStatus;

	@Column(name = "APPE_NONREP_TYPE")
	private String appeNonrepType;

	@Column(name = "APPE_NONREP_WARNING")
	private String appeNonrepWarning;

	@Column(name = "APPE_NR_INDICATOR")
	private BigDecimal appeNrIndicator;

	@Column(name = "APPE_PAC_RESULT")
	private String appePacResult;

	@Column(name = "APPE_PAC_VALUE")
	private String appePacValue;

	@Column(name = "APPE_PKI_AUTH_RESULT")
	private String appePkiAuthResult;

	@Column(name = "APPE_PKI_AUTHENTICATION_RES")
	private String appePkiAuthenticationRes;

	@Column(name = "APPE_PKI_AUTHORISATION_RES")
	private String appePkiAuthorisationRes;

	@Column(name = "APPE_PKI_PAC2_RESULT")
	private String appePkiPac2Result;

	@Column(name = "APPE_RCV_DELIVERY_STATUS")
	private String appeRcvDeliveryStatus;

	@Column(name = "APPE_REMOTE_INPUT_REFERENCE")
	private String appeRemoteInputReference;

	@Temporal(TemporalType.DATE)
	@Column(name = "APPE_REMOTE_INPUT_TIME")
	private Date appeRemoteInputTime;

	@Column(name = "APPE_RESP_AUTH_RESULT")
	private String appeRespAuthResult;

	@Column(name = "APPE_RESP_CBT_REFERENCE")
	private String appeRespCbtReference;

	@Column(name = "APPE_RESP_NONREP_TYPE")
	private String appeRespNonrepType;

	@Column(name = "APPE_RESP_NONREP_WARNING")
	private String appeRespNonrepWarning;

	@Column(name = "APPE_RESP_POSSIBLE_DUP_CREA")
	private String appeRespPossibleDupCrea;

	@Column(name = "APPE_RESP_RESPONDER_DN")
	private String appeRespResponderDn;

	@Column(name = "APPE_RESP_SIGNER_DN")
	private String appeRespSignerDn;

	@Column(name = "APPE_RESPONSE_REF")
	private String appeResponseRef;

	@Column(name = "APPE_RMA_CHECK_RESULT")
	private String appeRmaCheckResult;

	@Column(name = "APPE_SENDER_CANCEL_STATUS")
	private String appeSenderCancelStatus;

	@Column(name = "APPE_SENDER_SWIFT_ADDRESS")
	private String appeSenderSwiftAddress;

	@Column(name = "APPE_SEQUENCE_NBR")
	private BigDecimal appeSequenceNbr;

	@Column(name = "APPE_SESSION_HOLDER")
	private String appeSessionHolder;

	@Column(name = "APPE_SESSION_NBR")
	private BigDecimal appeSessionNbr;

	@Column(name = "APPE_SIGNER_DN")
	private String appeSignerDn;

	@Column(name = "APPE_SNF_DELV_NOTIF_REQ")
	private BigDecimal appeSnfDelvNotifReq;

	@Column(name = "APPE_SNF_INPUT_TIME")
	private String appeSnfInputTime;

	@Column(name = "APPE_SNF_QUEUE_NAME")
	private String appeSnfQueueName;

	@Column(name = "APPE_SNL_ENDPOINT")
	private String appeSnlEndpoint;

	@Column(name = "APPE_SWIFT_REF")
	private String appeSwiftRef;

	@Column(name = "APPE_SWIFT_REQUEST_REF")
	private String appeSwiftRequestRef;

	@Column(name = "APPE_SWIFT_RESPONSE_REF")
	private String appeSwiftResponseRef;

	@Column(name = "APPE_TELEX_BATCH_SEQUENCE")
	private String appeTelexBatchSequence;

	@Column(name = "APPE_TELEX_DURATION")
	private String appeTelexDuration;

	@Column(name = "APPE_TELEX_NUMBER")
	private String appeTelexNumber;

	@Column(name = "APPE_TNAP_NAME")
	private String appeTnapName;

	@Column(name = "APPE_TOKEN")
	private BigDecimal appeToken = BigDecimal.ZERO;

	@Column(name = "APPE_TRANSMISSION_NBR")
	private BigDecimal appeTransmissionNbr;

	@Column(name = "APPE_TYPE")
	private String appeType;

	@Column(name = "APPE_USE_PKI_SIGNATURE")
	private BigDecimal appeUsePkiSignature;

	@Column(name = "X_APPE_LAST")
	private BigDecimal xAppeLast = BigDecimal.ONE;

	// bi-directional many-to-one association to Inst
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "AID", referencedColumnName = "AID", insertable = false, updatable = false),
			@JoinColumn(name = "APPE_INST_NUM", referencedColumnName = "INST_NUM", insertable = false, updatable = false),
			@JoinColumn(name = "APPE_S_UMIDH", referencedColumnName = "INST_S_UMIDH", insertable = false, updatable = false),
			@JoinColumn(name = "APPE_S_UMIDL", referencedColumnName = "INST_S_UMIDL", insertable = false, updatable = false) })
	private Instance rinst;

	public AppendixPK getId() {
		return id;
	}

	public void setId(AppendixPK id) {
		this.id = id;
	}

	public String getAppeAckNackLauResult() {
		return appeAckNackLauResult;
	}

	public void setAppeAckNackLauResult(String appeAckNackLauResult) {
		this.appeAckNackLauResult = appeAckNackLauResult;
	}

	public String getAppeAckNackText() {
		return appeAckNackText;
	}

	public void setAppeAckNackText(String appeAckNackText) {
		this.appeAckNackText = appeAckNackText;
	}

	public String getAppeAnswerback() {
		return appeAnswerback;
	}

	public void setAppeAnswerback(String appeAnswerback) {
		this.appeAnswerback = appeAnswerback;
	}

	public String getAppeAuthResult() {
		return appeAuthResult;
	}

	public void setAppeAuthResult(String appeAuthResult) {
		this.appeAuthResult = appeAuthResult;
	}

	public String getAppeAuthValue() {
		return appeAuthValue;
	}

	public void setAppeAuthValue(String appeAuthValue) {
		this.appeAuthValue = appeAuthValue;
	}

	public String getAppeAuthoriserDn() {
		return appeAuthoriserDn;
	}

	public void setAppeAuthoriserDn(String appeAuthoriserDn) {
		this.appeAuthoriserDn = appeAuthoriserDn;
	}

	public String getAppeCarrierAcceptanceId() {
		return appeCarrierAcceptanceId;
	}

	public void setAppeCarrierAcceptanceId(String appeCarrierAcceptanceId) {
		this.appeCarrierAcceptanceId = appeCarrierAcceptanceId;
	}

	public String getAppeChecksumResult() {
		return appeChecksumResult;
	}

	public void setAppeChecksumResult(String appeChecksumResult) {
		this.appeChecksumResult = appeChecksumResult;
	}

	public String getAppeChecksumValue() {
		return appeChecksumValue;
	}

	public void setAppeChecksumValue(String appeChecksumValue) {
		this.appeChecksumValue = appeChecksumValue;
	}

	public String getAppeCombinedAuthRes() {
		return appeCombinedAuthRes;
	}

	public void setAppeCombinedAuthRes(String appeCombinedAuthRes) {
		this.appeCombinedAuthRes = appeCombinedAuthRes;
	}

	public String getAppeCombinedPacRes() {
		return appeCombinedPacRes;
	}

	public void setAppeCombinedPacRes(String appeCombinedPacRes) {
		this.appeCombinedPacRes = appeCombinedPacRes;
	}

	public String getAppeConnResponseCode() {
		return appeConnResponseCode;
	}

	public void setAppeConnResponseCode(String appeConnResponseCode) {
		this.appeConnResponseCode = appeConnResponseCode;
	}

	public String getAppeConnResponseText() {
		return appeConnResponseText;
	}

	public void setAppeConnResponseText(String appeConnResponseText) {
		this.appeConnResponseText = appeConnResponseText;
	}

	public String getAppeCreaApplServName() {
		return appeCreaApplServName;
	}

	public void setAppeCreaApplServName(String appeCreaApplServName) {
		this.appeCreaApplServName = appeCreaApplServName;
	}

	public String getAppeCreaMpfnName() {
		return appeCreaMpfnName;
	}

	public void setAppeCreaMpfnName(String appeCreaMpfnName) {
		this.appeCreaMpfnName = appeCreaMpfnName;
	}

	public String getAppeCreaRpName() {
		return appeCreaRpName;
	}

	public void setAppeCreaRpName(String appeCreaRpName) {
		this.appeCreaRpName = appeCreaRpName;
	}

	public String getAppeCrestComServerId() {
		return appeCrestComServerId;
	}

	public void setAppeCrestComServerId(String appeCrestComServerId) {
		this.appeCrestComServerId = appeCrestComServerId;
	}

	public String getAppeCrestGatewayId() {
		return appeCrestGatewayId;
	}

	public void setAppeCrestGatewayId(String appeCrestGatewayId) {
		this.appeCrestGatewayId = appeCrestGatewayId;
	}

	public String getAppeCui() {
		return appeCui;
	}

	public void setAppeCui(String appeCui) {
		this.appeCui = appeCui;
	}

	public BigDecimal getAppeDataLast() {
		return appeDataLast;
	}

	public void setAppeDataLast(BigDecimal appeDataLast) {
		this.appeDataLast = appeDataLast;
	}

	public String getAppeFaxBatchSequence() {
		return appeFaxBatchSequence;
	}

	public void setAppeFaxBatchSequence(String appeFaxBatchSequence) {
		this.appeFaxBatchSequence = appeFaxBatchSequence;
	}

	public String getAppeFaxDuration() {
		return appeFaxDuration;
	}

	public void setAppeFaxDuration(String appeFaxDuration) {
		this.appeFaxDuration = appeFaxDuration;
	}

	public String getAppeFaxNumber() {
		return appeFaxNumber;
	}

	public void setAppeFaxNumber(String appeFaxNumber) {
		this.appeFaxNumber = appeFaxNumber;
	}

	public String getAppeFaxTnapName() {
		return appeFaxTnapName;
	}

	public void setAppeFaxTnapName(String appeFaxTnapName) {
		this.appeFaxTnapName = appeFaxTnapName;
	}

	public String getAppeIappName() {
		return appeIappName;
	}

	public void setAppeIappName(String appeIappName) {
		this.appeIappName = appeIappName;
	}

	public String getAppeLargeData() {
		return appeLargeData;
	}

	public void setAppeLargeData(String appeLargeData) {
		this.appeLargeData = appeLargeData;
	}

	public String getAppeLauResult() {
		return appeLauResult;
	}

	public void setAppeLauResult(String appeLauResult) {
		this.appeLauResult = appeLauResult;
	}

	public Date getAppeLocalOutputTime() {
		return appeLocalOutputTime;
	}

	public void setAppeLocalOutputTime(Date appeLocalOutputTime) {
		this.appeLocalOutputTime = appeLocalOutputTime;
	}

	public String getAppeNakReason() {
		return appeNakReason;
	}

	public void setAppeNakReason(String appeNakReason) {
		this.appeNakReason = appeNakReason;
	}

	public String getAppeNetworkDeliveryStatus() {
		return appeNetworkDeliveryStatus;
	}

	public void setAppeNetworkDeliveryStatus(String appeNetworkDeliveryStatus) {
		this.appeNetworkDeliveryStatus = appeNetworkDeliveryStatus;
	}

	public String getAppeNonrepType() {
		return appeNonrepType;
	}

	public void setAppeNonrepType(String appeNonrepType) {
		this.appeNonrepType = appeNonrepType;
	}

	public String getAppeNonrepWarning() {
		return appeNonrepWarning;
	}

	public void setAppeNonrepWarning(String appeNonrepWarning) {
		this.appeNonrepWarning = appeNonrepWarning;
	}

	public BigDecimal getAppeNrIndicator() {
		return appeNrIndicator;
	}

	public void setAppeNrIndicator(BigDecimal appeNrIndicator) {
		this.appeNrIndicator = appeNrIndicator;
	}

	public String getAppePacResult() {
		return appePacResult;
	}

	public void setAppePacResult(String appePacResult) {
		this.appePacResult = appePacResult;
	}

	public String getAppePacValue() {
		return appePacValue;
	}

	public void setAppePacValue(String appePacValue) {
		this.appePacValue = appePacValue;
	}

	public String getAppePkiAuthResult() {
		return appePkiAuthResult;
	}

	public void setAppePkiAuthResult(String appePkiAuthResult) {
		this.appePkiAuthResult = appePkiAuthResult;
	}

	public String getAppePkiAuthenticationRes() {
		return appePkiAuthenticationRes;
	}

	public void setAppePkiAuthenticationRes(String appePkiAuthenticationRes) {
		this.appePkiAuthenticationRes = appePkiAuthenticationRes;
	}

	public String getAppePkiAuthorisationRes() {
		return appePkiAuthorisationRes;
	}

	public void setAppePkiAuthorisationRes(String appePkiAuthorisationRes) {
		this.appePkiAuthorisationRes = appePkiAuthorisationRes;
	}

	public String getAppePkiPac2Result() {
		return appePkiPac2Result;
	}

	public void setAppePkiPac2Result(String appePkiPac2Result) {
		this.appePkiPac2Result = appePkiPac2Result;
	}

	public String getAppeRcvDeliveryStatus() {
		return appeRcvDeliveryStatus;
	}

	public void setAppeRcvDeliveryStatus(String appeRcvDeliveryStatus) {
		this.appeRcvDeliveryStatus = appeRcvDeliveryStatus;
	}

	public String getAppeRemoteInputReference() {
		return appeRemoteInputReference;
	}

	public void setAppeRemoteInputReference(String appeRemoteInputReference) {
		this.appeRemoteInputReference = appeRemoteInputReference;
	}

	public Date getAppeRemoteInputTime() {
		return appeRemoteInputTime;
	}

	public void setAppeRemoteInputTime(Date appeRemoteInputTime) {
		this.appeRemoteInputTime = appeRemoteInputTime;
	}

	public String getAppeRespAuthResult() {
		return appeRespAuthResult;
	}

	public void setAppeRespAuthResult(String appeRespAuthResult) {
		this.appeRespAuthResult = appeRespAuthResult;
	}

	public String getAppeRespCbtReference() {
		return appeRespCbtReference;
	}

	public void setAppeRespCbtReference(String appeRespCbtReference) {
		this.appeRespCbtReference = appeRespCbtReference;
	}

	public String getAppeRespNonrepType() {
		return appeRespNonrepType;
	}

	public void setAppeRespNonrepType(String appeRespNonrepType) {
		this.appeRespNonrepType = appeRespNonrepType;
	}

	public String getAppeRespNonrepWarning() {
		return appeRespNonrepWarning;
	}

	public void setAppeRespNonrepWarning(String appeRespNonrepWarning) {
		this.appeRespNonrepWarning = appeRespNonrepWarning;
	}

	public String getAppeRespPossibleDupCrea() {
		return appeRespPossibleDupCrea;
	}

	public void setAppeRespPossibleDupCrea(String appeRespPossibleDupCrea) {
		this.appeRespPossibleDupCrea = appeRespPossibleDupCrea;
	}

	public String getAppeRespResponderDn() {
		return appeRespResponderDn;
	}

	public void setAppeRespResponderDn(String appeRespResponderDn) {
		this.appeRespResponderDn = appeRespResponderDn;
	}

	public String getAppeRespSignerDn() {
		return appeRespSignerDn;
	}

	public void setAppeRespSignerDn(String appeRespSignerDn) {
		this.appeRespSignerDn = appeRespSignerDn;
	}

	public String getAppeResponseRef() {
		return appeResponseRef;
	}

	public void setAppeResponseRef(String appeResponseRef) {
		this.appeResponseRef = appeResponseRef;
	}

	public String getAppeRmaCheckResult() {
		return appeRmaCheckResult;
	}

	public void setAppeRmaCheckResult(String appeRmaCheckResult) {
		this.appeRmaCheckResult = appeRmaCheckResult;
	}

	public String getAppeSenderCancelStatus() {
		return appeSenderCancelStatus;
	}

	public void setAppeSenderCancelStatus(String appeSenderCancelStatus) {
		this.appeSenderCancelStatus = appeSenderCancelStatus;
	}

	public String getAppeSenderSwiftAddress() {
		return appeSenderSwiftAddress;
	}

	public void setAppeSenderSwiftAddress(String appeSenderSwiftAddress) {
		this.appeSenderSwiftAddress = appeSenderSwiftAddress;
	}

	public BigDecimal getAppeSequenceNbr() {
		return appeSequenceNbr;
	}

	public void setAppeSequenceNbr(BigDecimal appeSequenceNbr) {
		this.appeSequenceNbr = appeSequenceNbr;
	}

	public String getAppeSessionHolder() {
		return appeSessionHolder;
	}

	public void setAppeSessionHolder(String appeSessionHolder) {
		this.appeSessionHolder = appeSessionHolder;
	}

	public BigDecimal getAppeSessionNbr() {
		return appeSessionNbr;
	}

	public void setAppeSessionNbr(BigDecimal appeSessionNbr) {
		this.appeSessionNbr = appeSessionNbr;
	}

	public String getAppeSignerDn() {
		return appeSignerDn;
	}

	public void setAppeSignerDn(String appeSignerDn) {
		this.appeSignerDn = appeSignerDn;
	}

	public BigDecimal getAppeSnfDelvNotifReq() {
		return appeSnfDelvNotifReq;
	}

	public void setAppeSnfDelvNotifReq(BigDecimal appeSnfDelvNotifReq) {
		this.appeSnfDelvNotifReq = appeSnfDelvNotifReq;
	}

	public String getAppeSnfInputTime() {
		return appeSnfInputTime;
	}

	public void setAppeSnfInputTime(String appeSnfInputTime) {
		this.appeSnfInputTime = appeSnfInputTime;
	}

	public String getAppeSnfQueueName() {
		return appeSnfQueueName;
	}

	public void setAppeSnfQueueName(String appeSnfQueueName) {
		this.appeSnfQueueName = appeSnfQueueName;
	}

	public String getAppeSnlEndpoint() {
		return appeSnlEndpoint;
	}

	public void setAppeSnlEndpoint(String appeSnlEndpoint) {
		this.appeSnlEndpoint = appeSnlEndpoint;
	}

	public String getAppeSwiftRef() {
		return appeSwiftRef;
	}

	public void setAppeSwiftRef(String appeSwiftRef) {
		this.appeSwiftRef = appeSwiftRef;
	}

	public String getAppeSwiftRequestRef() {
		return appeSwiftRequestRef;
	}

	public void setAppeSwiftRequestRef(String appeSwiftRequestRef) {
		this.appeSwiftRequestRef = appeSwiftRequestRef;
	}

	public String getAppeSwiftResponseRef() {
		return appeSwiftResponseRef;
	}

	public void setAppeSwiftResponseRef(String appeSwiftResponseRef) {
		this.appeSwiftResponseRef = appeSwiftResponseRef;
	}

	public String getAppeTelexBatchSequence() {
		return appeTelexBatchSequence;
	}

	public void setAppeTelexBatchSequence(String appeTelexBatchSequence) {
		this.appeTelexBatchSequence = appeTelexBatchSequence;
	}

	public String getAppeTelexDuration() {
		return appeTelexDuration;
	}

	public void setAppeTelexDuration(String appeTelexDuration) {
		this.appeTelexDuration = appeTelexDuration;
	}

	public String getAppeTelexNumber() {
		return appeTelexNumber;
	}

	public void setAppeTelexNumber(String appeTelexNumber) {
		this.appeTelexNumber = appeTelexNumber;
	}

	public String getAppeTnapName() {
		return appeTnapName;
	}

	public void setAppeTnapName(String appeTnapName) {
		this.appeTnapName = appeTnapName;
	}

	public BigDecimal getAppeToken() {
		return appeToken;
	}

	public void setAppeToken(BigDecimal appeToken) {
		this.appeToken = appeToken;
	}

	public BigDecimal getAppeTransmissionNbr() {
		return appeTransmissionNbr;
	}

	public void setAppeTransmissionNbr(BigDecimal appeTransmissionNbr) {
		this.appeTransmissionNbr = appeTransmissionNbr;
	}

	public String getAppeType() {
		return appeType;
	}

	public void setAppeType(String appeType) {
		this.appeType = appeType;
	}

	public BigDecimal getAppeUsePkiSignature() {
		return appeUsePkiSignature;
	}

	public void setAppeUsePkiSignature(BigDecimal appeUsePkiSignature) {
		this.appeUsePkiSignature = appeUsePkiSignature;
	}

	public BigDecimal getxAppeLast() {
		return xAppeLast;
	}

	public void setxAppeLast(BigDecimal xAppeLast) {
		this.xAppeLast = xAppeLast;
	}

	public Instance getRinst() {
		return rinst;
	}

	public void setRinst(Instance rinst) {
		this.rinst = rinst;
	}

	@Override
	public String toString() {
		return "Appendix [id=" + id + ", appeAckNackLauResult=" + appeAckNackLauResult + ", appeAckNackText="
				+ appeAckNackText + ", appeAnswerback=" + appeAnswerback + ", appeAuthResult=" + appeAuthResult
				+ ", appeAuthValue=" + appeAuthValue + ", appeAuthoriserDn=" + appeAuthoriserDn
				+ ", appeCarrierAcceptanceId=" + appeCarrierAcceptanceId + ", appeChecksumResult=" + appeChecksumResult
				+ ", appeChecksumValue=" + appeChecksumValue + ", appeCombinedAuthRes=" + appeCombinedAuthRes
				+ ", appeCombinedPacRes=" + appeCombinedPacRes + ", appeConnResponseCode=" + appeConnResponseCode
				+ ", appeConnResponseText=" + appeConnResponseText + ", appeCreaApplServName=" + appeCreaApplServName
				+ ", appeCreaMpfnName=" + appeCreaMpfnName + ", appeCreaRpName=" + appeCreaRpName
				+ ", appeCrestComServerId=" + appeCrestComServerId + ", appeCrestGatewayId=" + appeCrestGatewayId
				+ ", appeCui=" + appeCui + ", appeDataLast=" + appeDataLast + ", appeFaxBatchSequence="
				+ appeFaxBatchSequence + ", appeFaxDuration=" + appeFaxDuration + ", appeFaxNumber=" + appeFaxNumber
				+ ", appeFaxTnapName=" + appeFaxTnapName + ", appeIappName=" + appeIappName + ", appeLargeData="
				+ appeLargeData + ", appeLauResult=" + appeLauResult + ", appeLocalOutputTime=" + appeLocalOutputTime
				+ ", appeNakReason=" + appeNakReason + ", appeNetworkDeliveryStatus=" + appeNetworkDeliveryStatus
				+ ", appeNonrepType=" + appeNonrepType + ", appeNonrepWarning=" + appeNonrepWarning
				+ ", appeNrIndicator=" + appeNrIndicator + ", appePacResult=" + appePacResult + ", appePacValue="
				+ appePacValue + ", appePkiAuthResult=" + appePkiAuthResult + ", appePkiAuthenticationRes="
				+ appePkiAuthenticationRes + ", appePkiAuthorisationRes=" + appePkiAuthorisationRes
				+ ", appePkiPac2Result=" + appePkiPac2Result + ", appeRcvDeliveryStatus=" + appeRcvDeliveryStatus
				+ ", appeRemoteInputReference=" + appeRemoteInputReference + ", appeRemoteInputTime="
				+ appeRemoteInputTime + ", appeRespAuthResult=" + appeRespAuthResult + ", appeRespCbtReference="
				+ appeRespCbtReference + ", appeRespNonrepType=" + appeRespNonrepType + ", appeRespNonrepWarning="
				+ appeRespNonrepWarning + ", appeRespPossibleDupCrea=" + appeRespPossibleDupCrea
				+ ", appeRespResponderDn=" + appeRespResponderDn + ", appeRespSignerDn=" + appeRespSignerDn
				+ ", appeResponseRef=" + appeResponseRef + ", appeRmaCheckResult=" + appeRmaCheckResult
				+ ", appeSenderCancelStatus=" + appeSenderCancelStatus + ", appeSenderSwiftAddress="
				+ appeSenderSwiftAddress + ", appeSequenceNbr=" + appeSequenceNbr + ", appeSessionHolder="
				+ appeSessionHolder + ", appeSessionNbr=" + appeSessionNbr + ", appeSignerDn=" + appeSignerDn
				+ ", appeSnfDelvNotifReq=" + appeSnfDelvNotifReq + ", appeSnfInputTime=" + appeSnfInputTime
				+ ", appeSnfQueueName=" + appeSnfQueueName + ", appeSnlEndpoint=" + appeSnlEndpoint + ", appeSwiftRef="
				+ appeSwiftRef + ", appeSwiftRequestRef=" + appeSwiftRequestRef + ", appeSwiftResponseRef="
				+ appeSwiftResponseRef + ", appeTelexBatchSequence=" + appeTelexBatchSequence + ", appeTelexDuration="
				+ appeTelexDuration + ", appeTelexNumber=" + appeTelexNumber + ", appeTnapName=" + appeTnapName
				+ ", appeToken=" + appeToken + ", appeTransmissionNbr=" + appeTransmissionNbr + ", appeType=" + appeType
				+ ", appeUsePkiSignature=" + appeUsePkiSignature + ", xAppeLast=" + xAppeLast + ", rinst=" + rinst
				+ "]";
	}

}
