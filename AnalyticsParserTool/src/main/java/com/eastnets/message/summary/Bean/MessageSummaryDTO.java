package com.eastnets.message.summary.Bean;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageSummaryDTO {

	private long aid;
	private long umidh;
	private long umidl;
	private String mesgReference;
	private String mesgRelatedReference;
	private String mesgCreateMPFNName;
	private String mesgCreateRPName;
	private String mesgCreateOperatorNickName;
	private Date mesgCreateDateTime;
	private String modificationOperatorNickName;
	private Date modificationDateTime;
	private String mesgFormatName;
	private String xOwnLT;
	private String xInst0UnitName;
	private String xCategory;
	private long archived;
	private String mesgStatus;
	private String mesgNetworkPriority;
	private String mesgPossibleDuplicateCreation;
	private String mesgReceiverSwiftAddress;
	private String mesgSubFormat;
	private String mesgType;
	private String xFinCcy;
	private Double xFinAmount;
	private Date xFinValueDate;
	private String receicerBIC;
	private String senderBIC;
	private String mesgSLA;
	private String mesgUETR;
	private String xInst0RPName;
	private String xLastEMIAppeDelvStatus;
	private String mesgStatusCode;
	private String mesgReasonCode;
	private String mesgNakCode;
	private String mesgCharges;
	private String appeSessionHolder;
	private Date appeDateTime;
	private String appeType;
	private String appeApplicationName;
	private String messagePartner;
	private String ownerBIC8;
	private String ownerBIC11;
	private String ownerInstitutionName;
	private String ownerBranchCode;
	private String ownerBranchInfo;
	private String ownerCountryCode;
	private String ownerCountryName;
	private String ownerGeoLocation;
	private String counterPartBIC8;
	private String counterPartBIC11;
	private String counterPartInstitutionName;
	private String counterPartBranchCode;
	private String counterPartBranchInfo;
	private String counterPartCountryCode;
	private String counterPartCountryName;
	private String counterPartGeoLocation;
	private String currencyName;
	private int fetchStatus;
	private String businessArea;
	private String mesgStxVersion;
	private String mesgCopyService;
	private String transmissionDelay;
	private String categoryDescription;
	private String customerRoute;
	private String geoRoute;
	private String day;
	private String month;
	private String year;
	private int monthNumber;
	private String quarterlyPeriod;
	private String period;
	private String standerValueBucket;
	private Double baseAmount;

	private Integer gpiFlag;

	Map<Integer, TextFieldBean> fieldsOptionsValueList = new HashMap<>();

	public void setCounterPartInformation(CorrespondentBean correspondentBean) {

		this.counterPartBIC11 = correspondentBean.getCorrBIC11();
		this.counterPartBIC8 = correspondentBean.getCorrBIC8();
		this.counterPartInstitutionName = correspondentBean.getCorrInstitutionName();
		this.counterPartBranchCode = correspondentBean.getCorrBranchCode();
		this.counterPartBranchInfo = correspondentBean.getCorrBranchInfo();
		this.counterPartCountryCode = correspondentBean.getCorrCountryCode();
		this.counterPartCountryName = correspondentBean.getCorrCountryName();

	}

	public void setOwnerInformation(CorrespondentBean correspondentBean) {

		this.ownerBIC11 = correspondentBean.getCorrBIC11();
		this.ownerBIC8 = correspondentBean.getCorrBIC8();
		this.ownerInstitutionName = correspondentBean.getCorrInstitutionName();
		this.ownerBranchCode = correspondentBean.getCorrBranchCode();
		this.ownerBranchInfo = correspondentBean.getCorrBranchInfo();
		this.ownerCountryCode = correspondentBean.getCorrCountryCode();
		this.ownerCountryName = correspondentBean.getCorrCountryName();
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getUmidh() {
		return umidh;
	}

	public void setUmidh(long umidh) {
		this.umidh = umidh;
	}

	public long getUmidl() {
		return umidl;
	}

	public void setUmidl(long umidl) {
		this.umidl = umidl;
	}

	public String getMesgReference() {
		return mesgReference;
	}

	public void setMesgReference(String mesgReference) {
		this.mesgReference = mesgReference;
	}

	public String getMesgRelatedReference() {
		return mesgRelatedReference;
	}

	public void setMesgRelatedReference(String mesgRelatedReference) {
		this.mesgRelatedReference = mesgRelatedReference;
	}

	public String getMesgCreateMPFNName() {
		return mesgCreateMPFNName;
	}

	public void setMesgCreateMPFNName(String mesgCreateMPFNName) {
		this.mesgCreateMPFNName = mesgCreateMPFNName;
	}

	public String getMesgCreateRPName() {
		return mesgCreateRPName;
	}

	public void setMesgCreateRPName(String mesgCreateRPName) {
		this.mesgCreateRPName = mesgCreateRPName;
	}

	public String getMesgCreateOperatorNickName() {
		return mesgCreateOperatorNickName;
	}

	public void setMesgCreateOperatorNickName(String mesgCreateOperatorNickName) {
		this.mesgCreateOperatorNickName = mesgCreateOperatorNickName;
	}

	public Date getMesgCreateDateTime() {
		return mesgCreateDateTime;
	}

	public void setMesgCreateDateTime(Date mesgCreateDateTime) {
		this.mesgCreateDateTime = mesgCreateDateTime;
	}

	public String getModificationOperatorNickName() {
		return modificationOperatorNickName;
	}

	public void setModificationOperatorNickName(String modificationOperatorNickName) {
		this.modificationOperatorNickName = modificationOperatorNickName;
	}

	public Date getModificationDateTime() {
		return modificationDateTime;
	}

	public void setModificationDateTime(Date modificationDateTime) {
		this.modificationDateTime = modificationDateTime;
	}

	public String getMesgFormatName() {
		return mesgFormatName;
	}

	public void setMesgFormatName(String mesgFormatName) {
		this.mesgFormatName = mesgFormatName;
	}

	public String getxOwnLT() {
		return xOwnLT;
	}

	public void setxOwnLT(String xOwnLT) {
		this.xOwnLT = xOwnLT;
	}

	public String getxInst0UnitName() {
		return xInst0UnitName;
	}

	public void setxInst0UnitName(String xInst0UnitName) {
		this.xInst0UnitName = xInst0UnitName;
	}

	public String getxCategory() {
		return xCategory;
	}

	public void setxCategory(String xCategory) {
		this.xCategory = xCategory;
	}

	public long getArchived() {
		return archived;
	}

	public void setArchived(long archived) {
		this.archived = archived;
	}

	public String getMesgStatus() {
		return mesgStatus;
	}

	public void setMesgStatus(String mesgStatus) {
		this.mesgStatus = mesgStatus;
	}

	public String getMesgNetworkPriority() {
		return mesgNetworkPriority;
	}

	public void setMesgNetworkPriority(String mesgNetworkPriority) {
		this.mesgNetworkPriority = mesgNetworkPriority;
	}

	public String getMesgPossibleDuplicateCreation() {
		return mesgPossibleDuplicateCreation;
	}

	public void setMesgPossibleDuplicateCreation(String mesgPossibleDuplicateCreation) {
		this.mesgPossibleDuplicateCreation = mesgPossibleDuplicateCreation;
	}

	public String getMesgReceiverSwiftAddress() {
		return mesgReceiverSwiftAddress;
	}

	public void setMesgReceiverSwiftAddress(String mesgReceiverSwiftAddress) {
		this.mesgReceiverSwiftAddress = mesgReceiverSwiftAddress;
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

	public String getReceicerBIC() {
		return receicerBIC;
	}

	public void setReceicerBIC(String receicerBIC) {
		this.receicerBIC = receicerBIC;
	}

	public String getSenderBIC() {
		return senderBIC;
	}

	public void setSenderBIC(String senderBIC) {
		this.senderBIC = senderBIC;
	}

	public String getMesgSLA() {
		return mesgSLA;
	}

	public void setMesgSLA(String mesgSLA) {
		this.mesgSLA = mesgSLA;
	}

	public String getxInst0RPName() {
		return xInst0RPName;
	}

	public void setxInst0RPName(String xInst0RPName) {
		this.xInst0RPName = xInst0RPName;
	}

	public String getxLastEMIAppeDelvStatus() {
		return xLastEMIAppeDelvStatus;
	}

	public void setxLastEMIAppeDelvStatus(String xLastEMIAppeDelvStatus) {
		this.xLastEMIAppeDelvStatus = xLastEMIAppeDelvStatus;
	}

	public String getMesgStatusCode() {
		return mesgStatusCode;
	}

	public void setMesgStatusCode(String mesgStatusCode) {
		this.mesgStatusCode = mesgStatusCode;
	}

	public String getMesgReasonCode() {
		return mesgReasonCode;
	}

	public void setMesgReasonCode(String mesgReasonCode) {
		this.mesgReasonCode = mesgReasonCode;
	}

	public String getMesgNakCode() {
		return mesgNakCode;
	}

	public void setMesgNakCode(String mesgNakCode) {
		this.mesgNakCode = mesgNakCode;
	}

	public String getMesgCharges() {
		return mesgCharges;
	}

	public void setMesgCharges(String mesgCharges) {
		this.mesgCharges = mesgCharges;
	}

	public String getAppeSessionHolder() {
		return appeSessionHolder;
	}

	public void setAppeSessionHolder(String appeSessionHolder) {
		this.appeSessionHolder = appeSessionHolder;
	}

	public Date getAppeDateTime() {
		return appeDateTime;
	}

	public void setAppeDateTime(Date appeDateTime) {
		this.appeDateTime = appeDateTime;
	}

	public String getAppeType() {
		return appeType;
	}

	public void setAppeType(String appeType) {
		this.appeType = appeType;
	}

	public String getMessagePartner() {

		if (appeApplicationName != null && appeApplicationName.trim().toUpperCase().equalsIgnoreCase("APPLI")) {
			return appeSessionHolder;
		}
		return messagePartner;
	}

	public void setMessagePartner(String messagePartner) {
		this.messagePartner = messagePartner;
	}

	public String getOwnerBIC8() {
		return ownerBIC8;
	}

	public void setOwnerBIC8(String ownerBIC8) {
		this.ownerBIC8 = ownerBIC8;
	}

	public String getOwnerBIC11() {
		return ownerBIC11;
	}

	public void setOwnerBIC11(String ownerBIC11) {
		this.ownerBIC11 = ownerBIC11;
	}

	public String getOwnerInstitutionName() {
		return ownerInstitutionName;
	}

	public void setOwnerInstitutionName(String ownerInstitutionName) {
		this.ownerInstitutionName = ownerInstitutionName;
	}

	public String getOwnerBranchCode() {
		return ownerBranchCode;
	}

	public void setOwnerBranchCode(String ownerBranchCode) {
		this.ownerBranchCode = ownerBranchCode;
	}

	public String getOwnerBranchInfo() {
		return ownerBranchInfo;
	}

	public void setOwnerBranchInfo(String ownerBranchInfo) {
		this.ownerBranchInfo = ownerBranchInfo;
	}

	public String getOwnerCountryCode() {
		return ownerCountryCode;
	}

	public void setOwnerCountryCode(String ownerCountryCode) {
		this.ownerCountryCode = ownerCountryCode;
	}

	public String getOwnerCountryName() {
		return ownerCountryName;
	}

	public void setOwnerCountryName(String ownerCountryName) {
		this.ownerCountryName = ownerCountryName;
	}

	public String getOwnerGeoLocation() {
		return ownerGeoLocation;
	}

	public void setOwnerGeoLocation(String ownerGeoLocation) {
		this.ownerGeoLocation = ownerGeoLocation;
	}

	public String getCounterPartBIC8() {
		return counterPartBIC8;
	}

	public void setCounterPartBIC8(String counterPartBIC8) {
		this.counterPartBIC8 = counterPartBIC8;
	}

	public String getCounterPartBIC11() {
		return counterPartBIC11;
	}

	public void setCounterPartBIC11(String counterPartBIC11) {
		this.counterPartBIC11 = counterPartBIC11;
	}

	public String getCounterPartInstitutionName() {
		return counterPartInstitutionName;
	}

	public void setCounterPartInstitutionName(String counterPartInstitutionName) {
		this.counterPartInstitutionName = counterPartInstitutionName;
	}

	public String getCounterPartBranchCode() {
		return counterPartBranchCode;
	}

	public void setCounterPartBranchCode(String counterPartBranchCode) {
		this.counterPartBranchCode = counterPartBranchCode;
	}

	public String getCounterPartBranchInfo() {
		return counterPartBranchInfo;
	}

	public void setCounterPartBranchInfo(String counterPartBranchInfo) {
		this.counterPartBranchInfo = counterPartBranchInfo;
	}

	public String getCounterPartCountryCode() {
		return counterPartCountryCode;
	}

	public void setCounterPartCountryCode(String counterPartCountryCode) {
		this.counterPartCountryCode = counterPartCountryCode;
	}

	public String getCounterPartCountryName() {
		return counterPartCountryName;
	}

	public void setCounterPartCountryName(String counterPartCountryName) {
		this.counterPartCountryName = counterPartCountryName;
	}

	public String getCounterPartGeoLocation() {
		return counterPartGeoLocation;
	}

	public void setCounterPartGeoLocation(String counterPartGeoLocation) {
		this.counterPartGeoLocation = counterPartGeoLocation;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public int getFetchStatus() {
		return fetchStatus;
	}

	public void setFetchStatus(int fetchStatus) {
		this.fetchStatus = fetchStatus;
	}

	public String getAppeApplicationName() {
		return appeApplicationName;
	}

	public void setAppeApplicationName(String appeApplicationName) {
		this.appeApplicationName = appeApplicationName;
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

	public String getMesgStxVersion() {
		return mesgStxVersion;
	}

	public void setMesgStxVersion(String mesgStxVersion) {
		this.mesgStxVersion = mesgStxVersion;
	}

	public String getMesgCopyService() {
		return mesgCopyService;
	}

	public void setMesgCopyService(String mesgCopyService) {
		this.mesgCopyService = mesgCopyService;
	}

	public String getTransmissionDelay() {
		return transmissionDelay;
	}

	public void setTransmissionDelay(String transmissionDelay) {
		this.transmissionDelay = transmissionDelay;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public String getCustomerRoute() {
		return customerRoute;
	}

	public void setCustomerRoute(String customerRoute) {
		this.customerRoute = customerRoute;
	}

	public String getGeoRoute() {
		return geoRoute;
	}

	public void setGeoRoute(String geoRoute) {
		this.geoRoute = geoRoute;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getQuarterlyPeriod() {
		return quarterlyPeriod;
	}

	public void setQuarterlyPeriod(String quarterlyPeriod) {
		this.quarterlyPeriod = quarterlyPeriod;
	}

	public int getMonthNumber() {
		return monthNumber;
	}

	public void setMonthNumber(int monthNumber) {
		this.monthNumber = monthNumber;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getStanderValueBucket() {
		return standerValueBucket;
	}

	public void setStanderValueBucket(String standerValueBucket) {
		this.standerValueBucket = standerValueBucket;
	}

	public Double getBaseAmount() {
		return baseAmount;
	}

	public void setBaseAmount(Double baseAmount) {
		this.baseAmount = baseAmount;
	}

	public Map<Integer, TextFieldBean> getFieldsOptionsValueList() {
		return fieldsOptionsValueList;
	}

	public void setFieldsOptionsValueList(Map<Integer, TextFieldBean> fieldsOptionsValueList) {
		this.fieldsOptionsValueList = fieldsOptionsValueList;
	}

	public Integer getGpiFlag() {
		return gpiFlag;
	}

	public void setGpiFlag(Integer gpiFlag) {
		this.gpiFlag = gpiFlag;
	}

	public String getMesgUETR() {
		return mesgUETR;
	}

	public void setMesgUETR(String mesgUETR) {
		this.mesgUETR = mesgUETR;
	}

}
