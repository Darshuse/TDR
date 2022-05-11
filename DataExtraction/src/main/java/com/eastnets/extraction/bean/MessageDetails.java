package com.eastnets.extraction.bean;

import java.math.BigDecimal;
import java.sql.Clob;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import com.eastnets.extraction.service.helper.Pair;
import com.eastnets.extraction.service.helper.SearchUtils;

public class MessageDetails {

	private Integer aid;
	private Integer mesgUmidl;
	private Integer mesgUmidh;
	private String mesgUumid;
	private Integer mesgUumidSuffix;
	private String mesgUserIssuedAsPde;
	private String mesgPossibleDupCreation;
	private Boolean mesgIsPartial;
	private String mesgClass;
	private String mesgReceiverAliaName;
	private Boolean mesgIsLive;
	private Boolean mesgIsTextReadonly;
	private Boolean mesgIsTextModified;
	private Boolean mesgIsDeleteInhibited;
	private String mesgFrmtName;
	private String mesgSubFormat;
	private String mesgType;
	private String mesgNature;
	private String mesgSenderSwiftAddress;
	private String mesgSenderCorrType;
	private String mesgSenderX1;
	private String mesgSenderX2;
	private String mesgSenderX3;
	private String mesgSenderX4;
	private String mesgReceiverSwiftAddress;
	private String instReceiverCorrType;
	private String instReceiverX1;
	private String instReceiverX2;
	private String instReceiverX3;
	private String instReceiverX4;
	private String instDeliveryMode;
	private String mesgTrnRef;
	private String mesgRelTrnRef;
	private BigDecimal xFinAmount;
	private BigDecimal instructedAmount;
	private String mesgInstructedCur;
	private String xFinCcy;
	private Date xFinValueDate;
	private Date lastUpdate;
	private Boolean archived;
	private Boolean restored;
	private Integer requestAid;
	private String mesgNetworkPriority;
	private String mesgDelvOverdueWarnReq;
	private String mesgCopyServiceId;
	private String mesgNetworkDelvNotifReq;
	private String mesgUserPriorityCode;
	private String mesgUserReferenceText;
	private String mesgCreaApplServName;
	private String mesgCreaMpfnName;
	private Date mesgCreaDateTime;
	private String mesgSyntaxTableVer;
	private String mesgValidationRequested;
	private String mesgNetworkApplInd;
	private String mesgValidationPassed;
	private Clob textDataBlock;
	private String textSwiftBlock5;
	private String mesgReleaseInfo;
	private String mesgMesgUserGroup;
	private Boolean mesgIsRetrieved;
	private Integer mesgNetworkObsoPeriod;
	private String mesgIdentifier;
	private String mesgRequestorDn;
	private String instResponderDn;
	private String mesgService;
	private String mesgSecurityRequired;
	private String mesgXmlQueryRef1;
	private String mesgXmlQueryRef2;
	private String mesgXmlQueryRef3;
	private String mesgCreaDateTimeStr;
	// extra fields that is not filled from the same query
	private String mesgStatus;
	private String mesgUnExpandedText;
	private String mesgBlock5;
	private String serverToReceiver;

	private Boolean mesgCanExpand;
	private String mesgHistory;
	private List<InstanceDetails> mesgInstances;
	private String mesgSenderAddress;
	private String mesgReceiverAddress;
	private String mesgSAAName;
	// private String appeNonRepudiationType;
	private Boolean isBeingUpdated;
	private FileAct mesgFile;
	private List<XMLMessage> xmlMessages;
	private List<Boolean> expandedCheckBoxes;

	private Integer timeZoneOffset;
	private List<MessageNote> messageNotes;
	private boolean checkAllNotes;
	private String mesgCredebAccountNumber;
	private String mesgDebitAmount;
	private String mesgDebitCcy;
	private String mesgCreditAmount;
	private String mesgCreditCcy;
	private String mesgBeneficiaryName;
	private Date mesgTransactionDate;
	private String mesgBeneficiaryAccount;
	private String mesgBeneficiaryBankName;
	private String mesgExchangeRate;
	private String mesgContractId;
	private String mesgCharges;
	private String mesgAccountName;
	private String mesgDebitAccountInstructing;
	private String mesgBeneficiaryAddress;
	private String mesgIntermediaryBankCode;
	private String mesgInstructions;
	private String mesgB2bInstructions;
	private String mesgSndChargesAmount;
	private String mesgSndChargesCurr;
	private String mesgRcvChargesAmount;
	private String mesgRcvChargesCurr;
	private String mesgOriginatorName;
	private String mesgOriginatorAccountId;
	private String mesgOriginatorAddress;
	private String mesgOderingPartyName;
	private String mesgOderingPartyInstitution;
	private String mesgOderingPartyAddress;
	private String mesgRemitInfo;
	private String statusCode;
	private String reasonCode;
	private String forwardedTo;
	private String statusOriginator;
	private String nakCode;
	private String Charges;
	private String bankingPriority;
	private String mur;
	private String uetr;
	private String slaId;
	private String mesgIsCopyRequest;
	private String mesgAuthDelNotificationRequest;
	private String identifierDescription;
	private String mxMessageText;
	private String mxMessageHeader;
	private String correspondent;
	private String mesgBeneficiaryBankCode;
	private List<MessageDetails> relatedMessages;
	private String mxKeyword1;
	private String mxKeyword2;
	private String mxKeyword3;
	private String mesgOverDueDateTime;
	// For GPI Notifer
	private String queueName;
	private String queueResonCode;
	private String sattlmentMethod;
	private String clearingSystem;
	// for Cove
	private String senderCorr;
	private String recieverCorr;
	private String reimbInst;
	private String ordringInstution;
	private String senderChargeAmount;
	private String senderChargeCur;

	private List<InterventionDetails> interventionDetailsList;
	private List<AppendixDetails> appendixDetailsList;
	private List<TextFieldData> TextFieldDataList;
	private Pair<String, Boolean> pairs;

	private String checkSenderCorr() {

		if (senderCorr == null || senderCorr.isEmpty()) {
			return null;
		}

		if (mesgUnExpandedText != null && mesgUnExpandedText.contains("53A")) {
			return senderCorr;
		}

		return null;

	}

	private String checkRecieverCorr() {

		if (recieverCorr == null || recieverCorr.isEmpty()) {
			return null;
		}

		if (mesgUnExpandedText != null && mesgUnExpandedText.contains("54A")) {
			return recieverCorr;
		}

		return null;

	}

	private String checkReimbInst() {

		if (reimbInst == null || reimbInst.isEmpty()) {
			return null;
		}

		if (mesgUnExpandedText != null && mesgUnExpandedText.contains("55A")) {
			return reimbInst;
		}

		return null;

	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getMesgUmidl() {
		return mesgUmidl;
	}

	public void setMesgUmidl(Integer mesgUmidl) {
		this.mesgUmidl = mesgUmidl;
	}

	public Integer getMesgUmidh() {
		return mesgUmidh;
	}

	public void setMesgUmidh(Integer mesgUmidh) {
		this.mesgUmidh = mesgUmidh;
	}

	public String getMesgUumid() {
		return mesgUumid;
	}

	public void setMesgUumid(String mesgUumid) {
		this.mesgUumid = mesgUumid;
	}

	public Integer getMesgUumidSuffix() {
		return mesgUumidSuffix;
	}

	public void setMesgUumidSuffix(Integer mesgUumidSuffix) {
		this.mesgUumidSuffix = mesgUumidSuffix;
	}

	public String getMesgUserIssuedAsPde() {
		return mesgUserIssuedAsPde;
	}

	public void setMesgUserIssuedAsPde(String mesgUserIssuedAsPde) {
		this.mesgUserIssuedAsPde = mesgUserIssuedAsPde;
	}

	public String getMesgPossibleDupCreation() {
		return mesgPossibleDupCreation;
	}

	public void setMesgPossibleDupCreation(String mesgPossibleDupCreation) {
		this.mesgPossibleDupCreation = mesgPossibleDupCreation;
	}

	public Boolean getMesgIsPartial() {
		return mesgIsPartial;
	}

	public void setMesgIsPartial(Boolean mesgIsPartial) {
		this.mesgIsPartial = mesgIsPartial;
	}

	public String getMesgClass() {
		return mesgClass;
	}

	public void setMesgClass(String mesgClass) {
		this.mesgClass = mesgClass;
	}

	public String getMesgReceiverAliaName() {
		return mesgReceiverAliaName;
	}

	public void setMesgReceiverAliaName(String mesgReceiverAliaName) {
		this.mesgReceiverAliaName = mesgReceiverAliaName;
	}

	public Boolean getMesgIsLive() {
		return mesgIsLive;
	}

	public void setMesgIsLive(Boolean mesgIsLive) {
		this.mesgIsLive = mesgIsLive;
	}

	public Boolean getMesgIsTextReadonly() {
		return mesgIsTextReadonly;
	}

	public void setMesgIsTextReadonly(Boolean mesgIsTextReadonly) {
		this.mesgIsTextReadonly = mesgIsTextReadonly;
	}

	public Boolean getMesgIsTextModified() {
		return mesgIsTextModified;
	}

	public void setMesgIsTextModified(Boolean mesgIsTextModified) {
		this.mesgIsTextModified = mesgIsTextModified;
	}

	public Boolean getMesgIsDeleteInhibited() {
		return mesgIsDeleteInhibited;
	}

	public void setMesgIsDeleteInhibited(Boolean mesgIsDeleteInhibited) {
		this.mesgIsDeleteInhibited = mesgIsDeleteInhibited;
	}

	public String getMesgFrmtName() {
		return mesgFrmtName;
	}

	public void setMesgFrmtName(String mesgFrmtName) {
		this.mesgFrmtName = mesgFrmtName;
	}

	public String getMesgSubFormat() {
		return mesgSubFormat;
	}

	public String getMesgSubFormatFormatted() {
		return StringUtils.capitalize(StringUtils.lowerCase(mesgSubFormat));
	}

	public void setMesgSubFormatFormatted() {
		throw new NotImplementedException("this should not be called as it should be readonly");
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

	public String getMesgNature() {
		return mesgNature;
	}

	public String getMesgNatureFormatted() {
		String fromattedNature = mesgNature.trim();

		if (!StringUtils.isEmpty(fromattedNature) && fromattedNature.length() > 4) {
			fromattedNature = StringUtils.substring(fromattedNature, 0, fromattedNature.length() - 4);
		}
		fromattedNature = StringUtils.capitalize(StringUtils.lowerCase(fromattedNature));
		return fromattedNature;
	}

	public void setMesgNatureFormatted() {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public void setMesgNature(String mesgNature) {
		this.mesgNature = mesgNature;
	}

	public String getMesgSenderSwiftAddress() {
		return mesgSenderSwiftAddress;
	}

	public void setMesgSenderSwiftAddress(String mesgSenderSwiftAddress) {
		this.mesgSenderSwiftAddress = mesgSenderSwiftAddress;
	}

	public void setMesgSenderLT(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getMesgSenderLT() {
		return StringUtils.substring(mesgSenderSwiftAddress, 8, 9);
	}

	public String getMesgSenderCorrType() {
		return mesgSenderCorrType;
	}

	public void setMesgSenderCorrType(String mesgSenderCorrType) {
		this.mesgSenderCorrType = mesgSenderCorrType;
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

	public String getMesgReceiverSwiftAddress() {
		return mesgReceiverSwiftAddress;
	}

	public void setMesgReceiverSwiftAddress(String mesgReceiverSwiftAddress) {
		this.mesgReceiverSwiftAddress = mesgReceiverSwiftAddress;
	}

	public void setMesgReceiverLT(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getMesgReceiverLT() {
		return StringUtils.substring(mesgReceiverSwiftAddress, 8, 9);
	}

	public String getInstReceiverCorrType() {
		return instReceiverCorrType;
	}

	public void setInstReceiverCorrType(String instReceiverCorrType) {
		this.instReceiverCorrType = instReceiverCorrType;
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

	public String getMesgTrnRef() {
		return mesgTrnRef;
	}

	public void setMesgTrnRef(String mesgTrnRef) {
		this.mesgTrnRef = mesgTrnRef;
	}

	public String getMesgRelTrnRef() {
		return mesgRelTrnRef;
	}

	public void setMesgRelTrnRef(String mesgRelTrnRef) {
		this.mesgRelTrnRef = mesgRelTrnRef;
	}

	public BigDecimal getxFinAmount() {
		return xFinAmount;
	}

	public String getxFinAmountFormatted() {
		if (xFinAmount == null) {
			return "";
		}
		return new DecimalFormat("0.00##").format(xFinAmount);
	}

	public void setxFinAmountFormatted() {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getInstructedAmountFormatted() {
		if (instructedAmount == null)
			return "";

		return instructedAmount.toString();
	}

	public String getInstructedAmountGpi() {
		if (getInstructedAmountFormatted() == null || getInstructedAmountFormatted().isEmpty()) {
			return getxFinAmountFormatted();
		} else {
			return getInstructedAmountFormatted();
		}

	}

	public String getInstructedCurGpi() {
		if (getMesgInstructedCur() == null || getMesgInstructedCur().isEmpty()) {
			return getxFinCcy();
		} else {
			return getMesgInstructedCur();
		}

	}

	public void setInstructedAmountFormatted() {
		throw new NotImplementedException("this should not be called as it should be readonly");
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

	public Date getxFinValueDateOnDB() {
		return xFinValueDate;
	}

	public Date getxFinValueDate() {
		// value date has nothing to do with the timezone, so it should not be
		// affected by it
		return getxFinValueDateOnDB();
	}

	public void setxFinValueDate(Date xFinValueDate) {
		this.xFinValueDate = xFinValueDate;
	}

	public Date getLastUpdateOnDB() {
		return lastUpdate;
	}

	public Date getLastUpdate() {
		if (lastUpdate == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastUpdate);
		if (timeZoneOffset != null) {
			cal.add(Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date(cal.getTime().getTime());
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Boolean getArchived() {
		return archived;
	}

	public void setArchivedBool(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getArchivedBool() {
		if (archived) {
			return "Yes";
		}
		return "No";
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public Boolean getRestored() {
		return restored;
	}

	public void setRestoredBool(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getRestoredBool() {
		if (restored) {
			return "Yes";
		}
		return "No";
	}

	public void setRestored(Boolean restored) {
		this.restored = restored;
	}

	public Integer getRequestAid() {
		return requestAid;
	}

	public void setRequestAid(Integer requestAid) {
		this.requestAid = requestAid;
	}

	public String getMesgNetworkPriority() {
		return mesgNetworkPriority;
	}

	public void setMesgNetworkPriority(String mesgNetworkPriority) {
		this.mesgNetworkPriority = mesgNetworkPriority;
	}

	public String getMesgDelvOverdueWarnReq() {
		return mesgDelvOverdueWarnReq;
	}

	public String getMesgDelvOverdueWarnReqBool() {
		if (mesgDelvOverdueWarnReq == null || mesgDelvOverdueWarnReq.isEmpty()) {
			return "";
		}
		String val = mesgDelvOverdueWarnReq.trim();
		if (val.equalsIgnoreCase("1") || val.equalsIgnoreCase("true")) {
			return "TRUE";
		}
		return "FALSE";
	}

	public void setMesgDelvOverdueWarnReq(String mesgDelvOverdueWarnReq) {
		this.mesgDelvOverdueWarnReq = mesgDelvOverdueWarnReq;
	}

	public String getMesgCopyServiceId() {
		return mesgCopyServiceId;
	}

	public void setMesgCopyServiceId(String mesgCopyServiceId) {
		this.mesgCopyServiceId = mesgCopyServiceId;
	}

	public String getMesgNetworkDelvNotifReq() {
		return mesgNetworkDelvNotifReq;
	}

	public void setMesgNetworkDelvNotifReqBool(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getMesgNetworkDelvNotifReqBool() {
		if (mesgNetworkDelvNotifReq == null || mesgNetworkDelvNotifReq.isEmpty()) {
			return "";
		}
		String val = mesgNetworkDelvNotifReq.trim();
		if (val.equalsIgnoreCase("1") || val.equalsIgnoreCase("true")) {
			return "TRUE";
		}
		return "FALSE";
	}

	public void setMesgNetworkDelvNotifReq(String mesgNetworkDelvNotifReq) {
		this.mesgNetworkDelvNotifReq = mesgNetworkDelvNotifReq;
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

	public String getMesgCreaApplServName() {
		return mesgCreaApplServName;
	}

	public void setMesgCreaApplServName(String mesgCreaApplServName) {
		this.mesgCreaApplServName = mesgCreaApplServName;
	}

	public String getMesgCreaMpfnName() {
		return mesgCreaMpfnName;
	}

	public void setMesgCreaMpfnName(String mesgCreaMpfnName) {
		this.mesgCreaMpfnName = mesgCreaMpfnName;
	}

	public Date getMesgCreaDateTimeOnDB() {
		return mesgCreaDateTime;
	}

	public Date getMesgCreaDateTime() {
		if (mesgCreaDateTime == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(mesgCreaDateTime);
		if (timeZoneOffset != null) {
			cal.add(Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date(cal.getTime().getTime());
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public String getMesgSyntaxTableVer() {
		return mesgSyntaxTableVer;
	}

	public void setMesgSyntaxTableVer(String mesgSyntaxTableVer) {
		this.mesgSyntaxTableVer = mesgSyntaxTableVer;
	}

	public String getMesgValidationRequested() {
		return mesgValidationRequested;
	}

	public void setMesgValidationRequested(String mesgValidationRequested) {
		this.mesgValidationRequested = mesgValidationRequested;
	}

	public String getMesgNetworkApplInd() {
		return mesgNetworkApplInd;
	}

	public void setMesgNetworkApplInd(String mesgNetworkApplInd) {
		this.mesgNetworkApplInd = mesgNetworkApplInd;
	}

	public String getMesgValidationPassed() {
		return mesgValidationPassed;
	}

	public void setMesgValidationPassed(String mesgValidationPassed) {
		this.mesgValidationPassed = mesgValidationPassed;
	}

	public Clob getTextDataBlock() {
		return textDataBlock;
	}

	public void setTextDataBlock(Clob textDataBlock) {
		this.textDataBlock = textDataBlock;
	}

	public String getTextSwiftBlock5() {
		return textSwiftBlock5;
	}

	public void setTextSwiftBlock5(String textSwiftBlock5) {
		this.textSwiftBlock5 = textSwiftBlock5;
	}

	public String getMesgReleaseInfo() {
		return mesgReleaseInfo;
	}

	public void setMesgReleaseInfo(String mesgReleaseInfo) {
		this.mesgReleaseInfo = mesgReleaseInfo;
	}

	public String getMesgMesgUserGroup() {
		return mesgMesgUserGroup;
	}

	public void setMesgMesgUserGroup(String mesgMesgUserGroup) {
		this.mesgMesgUserGroup = mesgMesgUserGroup;
	}

	public Boolean getMesgIsRetrieved() {
		return mesgIsRetrieved;
	}

	public void setMesgIsRetrieved(Boolean mesgIsRetrieved) {
		this.mesgIsRetrieved = mesgIsRetrieved;
	}

	public Integer getMesgNetworkObsoPeriod() {
		return mesgNetworkObsoPeriod;
	}

	public void setMesgNetworkObsoPeriod(Integer mesgNetworkObsoPeriod) {
		this.mesgNetworkObsoPeriod = mesgNetworkObsoPeriod;
	}

	public String getMesgUnExpandedText() {
		return mesgUnExpandedText;
	}

	public void setMesgUnExpandedText(String mesgUnExpandedText) {
		this.mesgUnExpandedText = mesgUnExpandedText;
	}

	public Boolean getMesgCanExpand() {
		return mesgCanExpand;
	}

	public void setMesgCanExpand(Boolean mesgCanExpand) {
		this.mesgCanExpand = mesgCanExpand;
	}

	public String getMesgHistory() {
		return mesgHistory;
	}

	public void setMesgHistory(String mesgHistory) {
		this.mesgHistory = mesgHistory;
	}

	public List<InstanceDetails> getMesgInstances() {
		return mesgInstances;
	}

	public void setMesgInstances(List<InstanceDetails> mesgInstances) {
		this.mesgInstances = mesgInstances;
	}

	public String getMesgSenderAddress() {
		return mesgSenderAddress;
	}

	public void setMesgSenderAddress(String mesgSenderAddress) {
		this.mesgSenderAddress = mesgSenderAddress;
	}

	public String getMesgReceiverAddress() {
		return mesgReceiverAddress;
	}

	public void setMesgReceiverAddress(String mesgReceiverAddress) {
		this.mesgReceiverAddress = mesgReceiverAddress;
	}

	public void setMesgSAAName(String mesgSAAName) {
		this.mesgSAAName = mesgSAAName;
	}

	public String getMesgSAAName() {
		return mesgSAAName;
	}

	public void setMesgStatus(String mesgStatus) {
		this.mesgStatus = mesgStatus;
	}

	public String getMesgStatus() {
		return mesgStatus;
	}

	public void setMesgIdentifier(String mesgIdentifier) {
		this.mesgIdentifier = mesgIdentifier;
	}

	public String getMesgIdentifier() {
		return mesgIdentifier;
	}

	public void setMesgRequestorDn(String mesgRequestorDn) {
		this.mesgRequestorDn = mesgRequestorDn;
	}

	public String getMesgRequestorDn() {
		return mesgRequestorDn;
	}

	public void setInstResponderDn(String instResponderDn) {
		this.instResponderDn = instResponderDn;
	}

	public String getInstResponderDn() {
		return instResponderDn;
	}

	public void setMesgService(String mesgService) {
		this.mesgService = mesgService;
	}

	public String getMesgService() {
		return mesgService;
	}

	public void setMesgSecurityRequired(String mesgSecurityRequired) {
		this.mesgSecurityRequired = mesgSecurityRequired;
	}

	public String getMesgSecurityRequired() {
		return mesgSecurityRequired;
	}

	public void setMesgSecurityRequiredBool(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getMesgSecurityRequiredBool() {
		if (mesgSecurityRequired == null || mesgSecurityRequired.isEmpty()) {
			return "";
		}
		String val = mesgSecurityRequired.trim();
		if (val.equalsIgnoreCase("1") || val.equalsIgnoreCase("true")) {
			return "TRUE";
		}
		return "FALSE";
	}

	public void setMesgXmlQueryRef1(String mesgXmlQueryRef1) {
		this.mesgXmlQueryRef1 = mesgXmlQueryRef1;
	}

	public String getMesgXmlQueryRef1() {
		return mesgXmlQueryRef1;
	}

	public void setMesgXmlQueryRef2(String mesgXmlQueryRef2) {
		this.mesgXmlQueryRef2 = mesgXmlQueryRef2;
	}

	public String getMesgXmlQueryRef2() {
		return mesgXmlQueryRef2;
	}

	public void setMesgXmlQueryRef3(String mesgXmlQueryRef3) {
		this.mesgXmlQueryRef3 = mesgXmlQueryRef3;
	}

	public String getMesgXmlQueryRef3() {
		return mesgXmlQueryRef3;
	}

	/*
	 * public void setAppeNonRepudiationType(String appeNonRepudiationType) { this.appeNonRepudiationType = appeNonRepudiationType; }
	 */
	public void setAppeNonRepudiationType(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getAppeNonRepudiationType() {
		if (mesgInstances == null || mesgInstances.size() < 1) {
			return "";
		}
		String nonRepu = "";
		for (InstanceDetails inst : mesgInstances) {
			if (inst.getInstNum() == 0) {
				nonRepu = inst.getInstNrIndicator();
				if (nonRepu == null || nonRepu.trim().isEmpty()) {
					nonRepu = "";
				} else {
					if (nonRepu.trim().equalsIgnoreCase("1")) {
						nonRepu = "TRUE";
					} else {
						nonRepu = "FALSE";
					}
				}
			}
		}
		return nonRepu;
	}

	public void setIsBeingUpdated(Boolean isBeingUpdated) {
		this.isBeingUpdated = isBeingUpdated;
	}

	public Boolean getIsBeingUpdated() {
		return isBeingUpdated;
	}

	public void setMesgFile(FileAct mesgFile) {
		this.mesgFile = mesgFile;
	}

	public FileAct getMesgFile() {
		return mesgFile;
	}

	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public void setPriority(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getPriority() {
		return StringUtils.capitalize(StringUtils.lowerCase(StringUtils.substring(mesgNetworkPriority, 4)));
	}

	public void setValidationChecked(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getValidationChecked() {
		return StringUtils.capitalize(StringUtils.lowerCase(StringUtils.substring(mesgValidationRequested, 4)));
	}

	public void setValidationPassed(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getValidationPassed() {
		return StringUtils.capitalize(StringUtils.lowerCase(StringUtils.substring(mesgValidationPassed, 4)));
	}

	public void setWarningStatus(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getWarningStatus() {
		String sBuf = "";
		if (StringUtils.containsIgnoreCase(mesgPossibleDupCreation, "PDE")) {
			sBuf = sBuf + "Possible Duplicate Emission/";
		}
		if (StringUtils.containsIgnoreCase(mesgPossibleDupCreation, "PDR")) {
			sBuf = sBuf + "Possible Duplicate Reception/";
		}
		if (sBuf.length() > 1) {
			sBuf = StringUtils.substring(sBuf, 0, sBuf.length() - 2);
		}
		return sBuf;
	}

	public boolean isXML() {
		String formatName = getMesgFrmtName();
		if (formatName != null) {
			formatName = formatName.trim();
		}
		return ("MX".equalsIgnoreCase(formatName) || "AnyXML".equalsIgnoreCase(formatName));
	}

	public String getMesgBlock5() {
		return mesgBlock5;
	}

	public void setMesgBlock5(String mesgBlock5) {
		this.mesgBlock5 = mesgBlock5;
	}

	public String getMesgUnExpandedTextWithBlock5() {

		return (mesgUnExpandedText != null ? mesgUnExpandedText : "") + (mesgBlock5 != null ? mesgBlock5 : "");
	}

	public String getInstDeliveryMode() {
		return instDeliveryMode;
	}

	public void setInstDeliveryModeFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getInstDeliveryModeFormatted() {
		if (instDeliveryMode == null || instDeliveryMode.trim().isEmpty()) {
			return "";
		}
		String val = instDeliveryMode.trim();
		if (val.equalsIgnoreCase("DELIVERY_MODE_STORE_AND_FORWARD")) {
			return "Store-and-forward";
		}
		if (val.equalsIgnoreCase("DELIVERY_MODE_REAL_TIME")) {
			return "Real-time";
		}

		return "";
	}

	public void setInstDeliveryMode(String instDeliveryMode) {
		this.instDeliveryMode = instDeliveryMode;
	}

	public List<MessageNote> getMessageNotes() {

		return messageNotes;
	}

	public void setMessageNotes(List<MessageNote> messageNotes) {
		this.messageNotes = messageNotes;
	}

	public boolean isCheckAllNotes() {
		return checkAllNotes;
	}

	public void setCheckAllNotes(boolean checkAllNotes) {
		this.checkAllNotes = checkAllNotes;
	}

	public List<XMLMessage> getXmlMessages() {
		return xmlMessages;
	}

	public void setXmlMessages(List<XMLMessage> xmlMessages) {
		this.xmlMessages = xmlMessages;
	}

	public List<Boolean> getExpandedCheckBoxes() {
		return expandedCheckBoxes;
	}

	public void setExpandedCheckBoxes(List<Boolean> expandedCheckBoxes) {
		this.expandedCheckBoxes = expandedCheckBoxes;
	}

	public String getBankingPriority() {
		return bankingPriority;
	}

	public void setBankingPriority(String bankingPriority) {
		this.bankingPriority = bankingPriority;
	}

	public String getMur() {
		return mur;
	}

	public void setMur(String mur) {
		this.mur = mur;
	}

	public String getUetr() {
		return uetr;
	}

	public void setUetr(String uetr) {
		this.uetr = uetr;
	}

	public String getSlaId() {
		return slaId;
	}

	public void setSlaId(String slaId) {
		this.slaId = slaId;
	}

	public String getServerToReceiver() {
		return serverToReceiver;
	}

	public void setServerToReceiver(String serverToReceiver) {
		this.serverToReceiver = serverToReceiver;
	}

	public String getMesgIsCopyRequest() {
		return mesgIsCopyRequest;
	}

	public void setMesgIsCopyRequest(String mesgIsCopyRequest) {
		this.mesgIsCopyRequest = mesgIsCopyRequest;
	}

	public String getMesgAuthDelNotificationRequest() {
		return mesgAuthDelNotificationRequest;
	}

	public void setMesgAuthDelNotificationRequest(String mesgAuthDelNotificationRequest) {
		this.mesgAuthDelNotificationRequest = mesgAuthDelNotificationRequest;
	}

	public String getMxMessageText() {
		return mxMessageText;
	}

	public void setMxMessageText(String mxMessageText) {
		this.mxMessageText = mxMessageText;
	}

	public String getMxMessageHeader() {
		return mxMessageHeader;
	}

	public void setMxMessageHeader(String mxMessageHeader) {
		this.mxMessageHeader = mxMessageHeader;
	}

	public List<MessageDetails> getRelatedMessages() {
		return relatedMessages;
	}

	public void setRelatedMessages(List<MessageDetails> relatedMessages) {
		this.relatedMessages = relatedMessages;
	}

	public String getIdentifierDescription() {
		return identifierDescription;
	}

	public void setIdentifierDescription(String identifierDescription) {
		this.identifierDescription = identifierDescription;
	}

	public String getCorrespondent() {
		return correspondent;
	}

	public void setCorrespondent(String correspondent) {
		this.correspondent = correspondent;
	}

	public String getMxKeyword1() {
		return mxKeyword1;
	}

	public void setMxKeyword1(String mxKeyword1) {
		this.mxKeyword1 = mxKeyword1;
	}

	public String getMxKeyword2() {
		return mxKeyword2;
	}

	public void setMxKeyword2(String mxKeyword2) {
		this.mxKeyword2 = mxKeyword2;
	}

	public String getMxKeyword3() {
		return mxKeyword3;
	}

	public void setMxKeyword3(String mxKeyword3) {
		this.mxKeyword3 = mxKeyword3;
	}

	public String getMesgOverDueDateTime() {
		return mesgOverDueDateTime;
	}

	public void setMesgOverDueDateTime(String mesgOverDueDateTime) {
		this.mesgOverDueDateTime = mesgOverDueDateTime;
	}

	public String getMesgCreaDateTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(mesgCreaDateTime);
	}

	public void setMesgCreaDateTimeStr(String mesgCreaDateTimeStr) {
		this.mesgCreaDateTimeStr = mesgCreaDateTimeStr;
	}

	public String getMesgCredebAccountNumber() {
		return mesgCredebAccountNumber;
	}

	public void setMesgCredebAccountNumber(String mesgCredebAccountNumber) {
		this.mesgCredebAccountNumber = mesgCredebAccountNumber;
	}

	public String getMesgDebitAmount() {
		return mesgDebitAmount;
	}

	public void setMesgDebitAmount(String mesgDebitAmount) {
		this.mesgDebitAmount = mesgDebitAmount;
	}

	public String getMesgDebitCcy() {
		return mesgDebitCcy;
	}

	public void setMesgDebitCcy(String mesgDebitCcy) {
		this.mesgDebitCcy = mesgDebitCcy;
	}

	public String getMesgCreditAmount() {
		return mesgCreditAmount;
	}

	public void setMesgCreditAmount(String mesgCreditAmount) {
		this.mesgCreditAmount = mesgCreditAmount;
	}

	public String getMesgCreditCcy() {
		return mesgCreditCcy;
	}

	public void setMesgCreditCcy(String mesgCreditCcy) {
		this.mesgCreditCcy = mesgCreditCcy;
	}

	public String getMesgBeneficiaryName() {
		return mesgBeneficiaryName;
	}

	public void setMesgBeneficiaryName(String mesgBeneficiaryName) {
		this.mesgBeneficiaryName = mesgBeneficiaryName;
	}

	public Date getMesgTransactionDate() {
		return mesgTransactionDate;
	}

	public void setMesgTransactionDate(Date mesgTransactionDate) {
		this.mesgTransactionDate = mesgTransactionDate;
	}

	public String getMesgBeneficiaryAccount() {
		return mesgBeneficiaryAccount;
	}

	public void setMesgBeneficiaryAccount(String mesgBeneficiaryAccount) {
		this.mesgBeneficiaryAccount = mesgBeneficiaryAccount;
	}

	public String getMesgBeneficiaryBankName() {
		return mesgBeneficiaryBankName;
	}

	public void setMesgBeneficiaryBankName(String mesgBeneficiaryBankName) {
		this.mesgBeneficiaryBankName = mesgBeneficiaryBankName;
	}

	public String getMesgExchangeRate() {
		return mesgExchangeRate;
	}

	public void setMesgExchangeRate(String mesgExchangeRate) {
		this.mesgExchangeRate = mesgExchangeRate;
	}

	public String getMesgContractId() {
		return mesgContractId;
	}

	public void setMesgContractId(String mesgContractId) {
		this.mesgContractId = mesgContractId;
	}

	public String getMesgCharges() {
		return mesgCharges;
	}

	public void setMesgCharges(String mesgCharges) {
		this.mesgCharges = mesgCharges;
	}

	public String getMesgAccountName() {
		return mesgAccountName;
	}

	public void setMesgAccountName(String mesgAccountName) {
		this.mesgAccountName = mesgAccountName;
	}

	public String getMesgDebitAccountInstructing() {
		return mesgDebitAccountInstructing;
	}

	public void setMesgDebitAccountInstructing(String mesgDebitAccountInstructing) {
		this.mesgDebitAccountInstructing = mesgDebitAccountInstructing;
	}

	public String getMesgBeneficiaryAddress() {
		return mesgBeneficiaryAddress;
	}

	public void setMesgBeneficiaryAddress(String mesgBeneficiaryAddress) {
		this.mesgBeneficiaryAddress = mesgBeneficiaryAddress;
	}

	public String getMesgBeneficiaryBankCode() {
		return mesgBeneficiaryBankCode;
	}

	public void setMesgBeneficiaryBankCode(String mesgBeneficiaryBankCode) {
		this.mesgBeneficiaryBankCode = mesgBeneficiaryBankCode;
	}

	public String getMesgIntermediaryBankCode() {
		return mesgIntermediaryBankCode;
	}

	public void setMesgIntermediaryBankCode(String mesgIntermediaryBankCode) {
		this.mesgIntermediaryBankCode = mesgIntermediaryBankCode;
	}

	public String getMesgInstructions() {
		return mesgInstructions;
	}

	public void setMesgInstructions(String mesgInstructions) {
		this.mesgInstructions = mesgInstructions;
	}

	public String getMesgB2bInstructions() {
		return mesgB2bInstructions;
	}

	public void setMesgB2bInstructions(String mesgB2bInstructions) {
		this.mesgB2bInstructions = mesgB2bInstructions;
	}

	public String getMesgSndChargesAmount() {
		return mesgSndChargesAmount;
	}

	public void setMesgSndChargesAmount(String mesgSndChargesAmount) {
		this.mesgSndChargesAmount = mesgSndChargesAmount;
	}

	public String getMesgSndChargesCurr() {
		return mesgSndChargesCurr;
	}

	public void setMesgSndChargesCurr(String mesgSndChargesCurr) {
		this.mesgSndChargesCurr = mesgSndChargesCurr;
	}

	public String getMesgRcvChargesAmount() {
		return mesgRcvChargesAmount;
	}

	public void setMesgRcvChargesAmount(String mesgRcvChargesAmount) {
		this.mesgRcvChargesAmount = mesgRcvChargesAmount;
	}

	public String getMesgRcvChargesCurr() {
		return mesgRcvChargesCurr;
	}

	public void setMesgRcvChargesCurr(String mesgRcvChargesCurr) {
		this.mesgRcvChargesCurr = mesgRcvChargesCurr;
	}

	public String getMesgOriginatorName() {
		return mesgOriginatorName;
	}

	public void setMesgOriginatorName(String mesgOriginatorName) {
		this.mesgOriginatorName = mesgOriginatorName;
	}

	public String getMesgOriginatorAccountId() {
		return mesgOriginatorAccountId;
	}

	public void setMesgOriginatorAccountId(String mesgOriginatorAccountId) {
		this.mesgOriginatorAccountId = mesgOriginatorAccountId;
	}

	public String getMesgOriginatorAddress() {
		return mesgOriginatorAddress;
	}

	public void setMesgOriginatorAddress(String mesgOriginatorAddress) {
		this.mesgOriginatorAddress = mesgOriginatorAddress;
	}

	public String getMesgOderingPartyName() {
		return mesgOderingPartyName;
	}

	public void setMesgOderingPartyName(String mesgOderingPartyName) {
		this.mesgOderingPartyName = mesgOderingPartyName;
	}

	public String getMesgOderingPartyInstitution() {
		return mesgOderingPartyInstitution;
	}

	public void setMesgOderingPartyInstitution(String mesgOderingPartyInstitution) {
		this.mesgOderingPartyInstitution = mesgOderingPartyInstitution;
	}

	public String getMesgOderingPartyAddress() {
		return mesgOderingPartyAddress;
	}

	public void setMesgOderingPartyAddress(String mesgOderingPartyAddress) {
		this.mesgOderingPartyAddress = mesgOderingPartyAddress;
	}

	public String getMesgRemitInfo() {
		return mesgRemitInfo;
	}

	public void setMesgRemitInfo(String mesgRemitInfo) {
		this.mesgRemitInfo = mesgRemitInfo;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getForwardedTo() {
		return forwardedTo;
	}

	public void setForwardedTo(String forwardedTo) {
		this.forwardedTo = forwardedTo;
	}

	public String getStatusOriginator() {
		return statusOriginator;
	}

	public void setStatusOriginator(String statusOriginator) {
		this.statusOriginator = statusOriginator;
	}

	public String getNakCode() {
		return nakCode;
	}

	public void setNakCode(String nakCode) {
		this.nakCode = nakCode;
	}

	public String getCharges() {
		return Charges;
	}

	public void setCharges(String charges) {
		Charges = charges;
	}

	public BigDecimal getInstructedAmount() {
		return instructedAmount;
	}

	public void setInstructedAmount(BigDecimal instructedAmount) {
		this.instructedAmount = instructedAmount;
	}

	public String getDeductsCcy() {

		if (mesgSndChargesAmount == null) {
			return "";
		}
		return xFinCcy;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getQueueResonCode() {
		return queueResonCode;
	}

	public void setQueueResonCode(String queueResonCode) {
		this.queueResonCode = queueResonCode;
	}

	public CoveType getCoveType() {
		if (checkSenderCorr() != null && checkRecieverCorr() != null && checkReimbInst() != null) {
			return CoveType.coveWithDiffThreAcount;
		} else if (checkSenderCorr() != null && checkRecieverCorr() != null) {
			return CoveType.coveWithDiffTowAcount;
		} else if (checkSenderCorr() != null) {
			return CoveType.coveWithShaerdAcount;
		}
		return CoveType.notCovePayment;
	}

	public String getSenderCorr() {
		return senderCorr;
	}

	public void setSenderCorr(String senderCorr) {
		this.senderCorr = senderCorr;
	}

	public String getRecieverCorr() {
		return recieverCorr;
	}

	public void setRecieverCorr(String recieverCorr) {
		this.recieverCorr = recieverCorr;
	}

	public String getReimbInst() {
		return reimbInst;
	}

	public void setReimbInst(String reimbInst) {
		this.reimbInst = reimbInst;
	}

	public String getSattlmentMethod() {
		return sattlmentMethod;
	}

	public void setSattlmentMethod(String sattlmentMethod) {
		this.sattlmentMethod = sattlmentMethod;
	}

	public String getClearingSystem() {
		return clearingSystem;
	}

	public void setClearingSystem(String clearingSystem) {
		this.clearingSystem = clearingSystem;
	}

	public String getOrdringInstution() {
		return ordringInstution;
	}

	public void setOrdringInstution(String ordringInstution) {
		this.ordringInstution = ordringInstution;
	}

	public String getMesgInstructedCur() {
		return mesgInstructedCur;
	}

	public void setMesgInstructedCur(String mesgInstructedCur) {
		this.mesgInstructedCur = mesgInstructedCur;
	}

	public String getSenderChargeAmount() {
		return senderChargeAmount;
	}

	public void setSenderChargeAmount(String senderChargeAmount) {
		this.senderChargeAmount = senderChargeAmount;
	}

	public String getSenderChargeCur() {
		return senderChargeCur;
	}

	public void setSenderChargeCur(String senderChargeCur) {
		this.senderChargeCur = senderChargeCur;
	}

	public List<InterventionDetails> getInterventionDetailsList() {
		return interventionDetailsList;
	}

	public void setInterventionDetailsList(List<InterventionDetails> interventionDetailsList) {
		this.interventionDetailsList = interventionDetailsList;
	}

	public List<AppendixDetails> getAppendixDetailsList() {
		return appendixDetailsList;
	}

	public void setAppendixDetailsList(List<AppendixDetails> appendixDetailsList) {
		this.appendixDetailsList = appendixDetailsList;
	}

	public List<TextFieldData> getTextFieldDataList() {
		return TextFieldDataList;
	}

	public void setTextFieldDataList(List<TextFieldData> textFieldDataList) {
		TextFieldDataList = textFieldDataList;
	}

	public Pair<String, Boolean> getPairs() {
		return pairs;
	}

	public void setPairs(Pair<String, Boolean> pairs) {
		this.pairs = pairs;
	}

	public String getDateTimeForQuery() {
		return SearchUtils.DateToSqlStr(mesgCreaDateTime);
	}

}
