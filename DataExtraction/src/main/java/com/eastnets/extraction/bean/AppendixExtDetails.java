package com.eastnets.extraction.bean;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

public class AppendixExtDetails {

	private String appeType;
	private String instUnitName;
	private String appeIAppName;
	private String appeSessionHolder;
	private Integer appeSessionNbr;
	private Long appeSequenceNbr;
	private Date appeDateTime;
	private Date appeLocalOutputTime;
	private String appeRemoteInputReference;
	private String appeCheckSumValue;
	private String appeCheckSumResult;
	private String appeAuthValue;
	private String appeAuthResult;
	private String appePacValue;
	private String appePacResult;
	private String appeRcvDeliveryStatus;
	private String appeNetworkDeliveryStatus;
	private String appeSenderCancelStatus;
	private String appeCreaApplServName;
	private String appeCreaRpName;
	private String appeAckNackText;
	private String appeTelexNumber;
	private String appeAnswerBack;
	private String appeTnapName;
	private String appeFaxNumber;
	private String appeFaxTnapName;
	private String appePkiAuthResult;
	private String appePkiPac2Result;
	private String appeRmaCheckResult;
	private Boolean appeUsePkiSignature;
	private String appePkiAuthorisationRes;
	private String instTelexNumber;
	private String instAnswerBack;
	private String instTnapName;
	private String instFaxNumber;
	private String instFaxTnapName;
	private Long appeRecordId;
	private String appePkiAuthValue;
	private String appePkiPac2Value;
	private String appeSignerDn;
	private Boolean appeSnfDelvNotifReq;
	private Boolean appeNrIndicator;
	private String appeRespMvalResult;
	private String appeSwiftResponseRef;
	private String appeSwiftRef;
	private String appeSwiftRequestRef;
	private Integer timeZoneOffset;

	public String getInstUnitName() {
		return instUnitName;
	}

	public void setInstUnitName(String instUnitName) {
		this.instUnitName = instUnitName;
	}

	public String getAppeIAppName() {
		return appeIAppName;
	}

	public void setAppeIAppName(String appeIAppName) {
		this.appeIAppName = appeIAppName;
	}

	public String getAppeSessionHolder() {
		return appeSessionHolder;
	}

	public void setAppeSessionHolder(String appeSessionHolder) {
		this.appeSessionHolder = appeSessionHolder;
	}

	public Integer getAppeSessionNbr() {
		return appeSessionNbr;
	}

	public void setAppeSessionNbrFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeSessionNbrFormatted() {
		return String.format("%04d", appeSessionNbr);
	}

	public void setAppeSessionNbr(Integer appeSessionNbr) {
		this.appeSessionNbr = appeSessionNbr;
	}

	public Long getAppeSequenceNbr() {
		return appeSequenceNbr;
	}

	public void setAppeSequenceNbrFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeSequenceNbrFormatted() {
		return String.format("%06d", appeSequenceNbr);
	}

	public void setAppeSequenceNbr(Long appeSequenceNbr) {
		this.appeSequenceNbr = appeSequenceNbr;
	}

	public Date getAppeDateTimeOnDB() {
		return appeDateTime;
	}

	public Date getAppeDateTime() {
		if (appeDateTime == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(appeDateTime);
		if (timeZoneOffset != null) {
			cal.add(Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date(cal.getTime().getTime());
	}

	public void setAppeDateTime(Date appeDateTime) {
		this.appeDateTime = appeDateTime;
	}

	public Date getAppeLocalOutputTimeOnDB() {
		return appeLocalOutputTime;
	}

	public Date getAppeLocalOutputTime() {
		if (appeLocalOutputTime == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(appeLocalOutputTime);
		if (timeZoneOffset != null) {
			cal.add(Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date(cal.getTime().getTime());
	}

	public void setAppeLocalOutputTime(Date appeLocalOutputTime) {
		this.appeLocalOutputTime = appeLocalOutputTime;
	}

	public String getAppeRemoteInputReference() {
		return appeRemoteInputReference;
	}

	public void setAppeRemoteInputReference(String appeRemoteInputReference) {
		this.appeRemoteInputReference = appeRemoteInputReference;
	}

	public String getAppeCheckSumValue() {
		return appeCheckSumValue;
	}

	public void setAppeCheckSumValue(String appeCheckSumValue) {
		this.appeCheckSumValue = appeCheckSumValue;
	}

	public String getAppeCheckSumResult() {
		return appeCheckSumResult;
	}

	public void setAppeCheckSumResultFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeCheckSumResultFormatted() {
		return StringUtils.capitalize(StringUtils.lowerCase(StringUtils.substring(appeCheckSumResult, 5)));
	}

	public void setAppeCheckSumResult(String appeCheckSumResult) {
		this.appeCheckSumResult = appeCheckSumResult;
	}

	public String getAppeAuthResult() {
		return appeAuthResult;
	}

	public void setAppeAuthResultFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeAuthResultFormatted() {
		return formatAuthResult(appeAuthResult);
	}

	public void setAppeAuthResult(String appeAuthResult) {
		this.appeAuthResult = appeAuthResult;
	}

	public String getAppePacResult() {
		return appePacResult;
	}

	public void setAppePacResult(String appePacResult) {
		this.appePacResult = appePacResult;
	}

	public String getAppeRcvDeliveryStatus() {
		return appeRcvDeliveryStatus;
	}

	public void setAppeRcvDeliveryStatusFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeRcvDeliveryStatusFormatted() {
		if (StringUtils.equalsIgnoreCase(appeRcvDeliveryStatus, "RE_N_A"))
			return "-";
		if (StringUtils.equalsIgnoreCase(appeRcvDeliveryStatus, "RE_UACKED"))
			return "Receiver Acked";
		return "";
	}

	public void setAppeRcvDeliveryStatus(String appeRcvDeliveryStatus) {
		this.appeRcvDeliveryStatus = appeRcvDeliveryStatus;
	}

	public String getAppeNetworkDeliveryStatus() {
		return appeNetworkDeliveryStatus;
	}

	public void setAppeNetworkDeliveryStatusFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeNetworkDeliveryStatusFormatted() {
		return formatAckSts(appeNetworkDeliveryStatus, "Network");
	}

	public void setAppeNetworkDeliveryStatus(String appeNetworkDeliveryStatus) {
		this.appeNetworkDeliveryStatus = appeNetworkDeliveryStatus;
	}

	public String getAppeSenderCancelStatus() {
		return appeSenderCancelStatus;
	}

	public void setAppeSenderCancelStatusFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeSenderCancelStatusFormatted() {
		return StringUtils.capitalize(StringUtils.lowerCase(StringUtils.substring(appeSenderCancelStatus, 7)));
	}

	public void setAppeSenderCancelStatus(String appeSenderCancelStatus) {
		this.appeSenderCancelStatus = appeSenderCancelStatus;
	}

	public String getAppeCreaApplServName() {
		return appeCreaApplServName;
	}

	public void setAppeCreaApplServName(String appeCreaApplServName) {
		this.appeCreaApplServName = appeCreaApplServName;
	}

	public String getAppeCreaRpName() {
		return appeCreaRpName;
	}

	public void setAppeCreaRpName(String appeCreaRpName) {
		this.appeCreaRpName = appeCreaRpName;
	}

	public String getAppeTelexNumber() {
		return appeTelexNumber;
	}

	public void setAppeTelexNumber(String appeTelexNumber) {
		this.appeTelexNumber = appeTelexNumber;
	}

	public String getAppeAnswerBack() {
		return appeAnswerBack;
	}

	public void setAppeAnswerBack(String appeAnswerBack) {
		this.appeAnswerBack = appeAnswerBack;
	}

	public String getAppeTnapName() {
		return appeTnapName;
	}

	public void setAppeTnapName(String appeTnapName) {
		this.appeTnapName = appeTnapName;
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

	public String getAppePkiAuthResult() {
		return appePkiAuthResult;
	}

	public void setAppePkiAuthResultFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppePkiAuthResultFormatted() {
		return formatAuthResult(appePkiAuthResult);
	}

	public void setAppePkiAuthResult(String appePkiAuthResult) {
		this.appePkiAuthResult = appePkiAuthResult;
	}

	public String getAppePkiPac2Result() {
		return appePkiPac2Result;
	}

	public void setAppePkiPac2Result(String appePkiPac2Result) {
		this.appePkiPac2Result = appePkiPac2Result;
	}

	public String getAppeRmaCheckResult() {
		return appeRmaCheckResult;
	}

	public void setAppeRmaCheckResultFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeRmaCheckResultFormatted() {
		return formatRmaCheckResult(appeRmaCheckResult);
	}

	public void setAppeRmaCheckResult(String appeRmaCheckResult) {
		this.appeRmaCheckResult = appeRmaCheckResult;
	}

	public Boolean getAppeUsePkiSignature() {
		return appeUsePkiSignature;
	}

	public void setAppeUsePkiSignature(Boolean appeUsePkiSignature) {
		this.appeUsePkiSignature = appeUsePkiSignature;
	}

	public String getAppePkiAuthorisationRes() {
		return appePkiAuthorisationRes;
	}

	public void setAppePkiAuthorisationResFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppePkiAuthorisationResFormatted() {
		return formatAuthResult(appePkiAuthorisationRes);
	}

	public void setAppePkiAuthorisationRes(String appePkiAuthorisationRes) {
		this.appePkiAuthorisationRes = appePkiAuthorisationRes;
	}

	public String getInstTelexNumber() {
		return instTelexNumber;
	}

	public void setInstTelexNumber(String instTelexNumber) {
		this.instTelexNumber = instTelexNumber;
	}

	public String getInstAnswerBack() {
		return instAnswerBack;
	}

	public void setInstAnswerBack(String instAnswerBack) {
		this.instAnswerBack = instAnswerBack;
	}

	public String getInstTnapName() {
		return instTnapName;
	}

	public void setInstTnapName(String instTnapName) {
		this.instTnapName = instTnapName;
	}

	public String getInstFaxNumber() {
		return instFaxNumber;
	}

	public void setInstFaxNumber(String instFaxNumber) {
		this.instFaxNumber = instFaxNumber;
	}

	public String getInstFaxTnapName() {
		return instFaxTnapName;
	}

	public void setInstFaxTnapName(String instFaxTnapName) {
		this.instFaxTnapName = instFaxTnapName;
	}

	public Long getAppeRecordId() {
		return appeRecordId;
	}

	public void setAppeRecordId(Long appeRecordId) {
		this.appeRecordId = appeRecordId;
	}

	public String getAppeSignerDn() {
		return appeSignerDn;
	}

	public void setAppeSignerDn(String appeSignerDn) {
		this.appeSignerDn = appeSignerDn;
	}

	public Boolean getAppeSnfDelvNotifReq() {
		return appeSnfDelvNotifReq;
	}

	public void setAppeSnfDelvNotifReqBool(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeSnfDelvNotifReqBool() {

		if (appeSnfDelvNotifReq == null) {
			return "";
		}
		if (!appeSnfDelvNotifReq)
			return "FALSE";
		return "TRUE";
	}

	public void setAppeSnfDelvNotifReq(Boolean appeSnfDelvNotifReq) {
		this.appeSnfDelvNotifReq = appeSnfDelvNotifReq;
	}

	public Boolean getAppeNrIndicator() {
		return appeNrIndicator;
	}

	public void setAppeNrIndicatorBool(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeNrIndicatorBool() {
		if (appeNrIndicator == null) {
			return "";
		}
		if (!appeNrIndicator)
			return "FALSE";
		return "TRUE";
	}

	public void setAppeNrIndicator(Boolean appeNrIndicator) {
		this.appeNrIndicator = appeNrIndicator;
	}

	public String getAppeSwiftResponseRef() {
		return appeSwiftResponseRef;
	}

	public void setAppeSwiftResponseRef(String appeSwiftResponseRef) {
		this.appeSwiftResponseRef = appeSwiftResponseRef;
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

	public void setAppeType(String appeType) {
		this.appeType = appeType;
	}

	public String getAppeType() {
		return appeType;
	}

	public void setAppeTypeFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeTypeFormatted() {
		return StringUtils.capitalize(StringUtils.lowerCase(StringUtils.substring(appeType, 5)));
	}

	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public static String formatAuthResult(String sts) {
		if (StringUtils.equalsIgnoreCase(sts, "DLV_N_A"))
			return "";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_N_A"))
			return "";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_SUCCESS"))
			return "Success";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_SUCCESS_OLD_KEY"))
			return "Old Key";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_SUCCESS_FUTURE_KEY"))
			return "Future KEY";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_FAILURE"))
			return "Failure";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_NO_KEY"))
			return "No Key";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_BYPASSED"))
			return "Bypassed";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_NO_RMA_REC"))
			return "No RMA Rec";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_RMA_NOT_ENABLED"))
			return "RMA Not Enabled";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_RMA_NOT_IN_VALID_PERIOD"))
			return "RMA not in valid period";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_INVALID_DIGEST"))
			return "Invalid Digest";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_INVALID_SIGN_DN"))
			return "Invalid Sign Onx";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_SIG_FAILURE"))
			return "Sig Failure";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_INVALID_CERT_ID"))
			return "Invalid Cert ID";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_NO_RMAU_BKEY"))
			return "No RMAU BKEY";
		return "";
	}

	public static String formatRmaCheckResult(String sts) {
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_N_A"))
			return "-";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_SUCCESS"))
			return "Success";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_FAILURE"))
			return "Failure";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_BYPASSED"))
			return "Bypassed";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NO_REC"))
			return "No Record";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NOT_ENABLED"))
			return "Not Enabled";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NOT_IN_VALID_PERIOD"))
			return "Invalid Period";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NOT_AUTHORISED"))
			return "Not allowed";
		return "";
	}

	public static String formatAckSts(String status, String app_name) {
		if (!StringUtils.isEmpty(status)) {
			if (StringUtils.equalsIgnoreCase(status, "DLV_N_A"))
				return "N/A";
			if (StringUtils.equalsIgnoreCase(status, "DLV_WAITING_ACK"))
				return "Waiting " + StringUtils.defaultString(app_name) + " Ack";
			if (StringUtils.equalsIgnoreCase(status, "DLV_TIMED_OUT"))
				return StringUtils.defaultString(app_name) + " Time out";
			if (StringUtils.equalsIgnoreCase(status, "DLV_ACKED"))
				return StringUtils.defaultString(app_name) + " Ack";
			if (StringUtils.equalsIgnoreCase(status, "DLV_NACKED"))
				return StringUtils.defaultString(app_name) + " Nack";
			if (StringUtils.equalsIgnoreCase(status, "DLV_REJECTED_LOCALLY"))
				return "Rejected Locally";
			if (StringUtils.equalsIgnoreCase(status, "DLV_ABORTED"))
				return StringUtils.defaultString(app_name) + " Aborted";
		}
		// else
		return "-";
	}

	public String getAppeAuthValue() {
		return appeAuthValue;
	}

	public void setAppeAuthValue(String appeAuthValue) {
		this.appeAuthValue = appeAuthValue;
	}

	public String getAppeAckNackText() {
		return appeAckNackText;
	}

	public void setAppeAckNackText(String appeAckNackText) {
		this.appeAckNackText = appeAckNackText;
	}

	public String getAppePkiAuthValue() {
		return appePkiAuthValue;
	}

	public void setAppePkiAuthValue(String appePkiAuthValue) {
		this.appePkiAuthValue = appePkiAuthValue;
	}

	public String getAppePkiPac2Value() {
		return appePkiPac2Value;
	}

	public void setAppePkiPac2Value(String appePkiPac2Value) {
		this.appePkiPac2Value = appePkiPac2Value;
	}

	public String getAppeRespMvalResult() {
		return appeRespMvalResult;
	}

	public void setAppeRespMvalResult(String appeRespMvalResult) {
		this.appeRespMvalResult = appeRespMvalResult;
	}

	public String getAppePacValue() {
		return appePacValue;
	}

	public void setAppePacValue(String appePacValue) {
		this.appePacValue = appePacValue;
	}
}
