/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.domain.viewer;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.eastnets.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SearchResultEntity POJO
 * 
 * @author EastNets
 * @since September 20, 2012
 */

@XmlRootElement
public class SearchResultEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7346150922764901227L;
	private Integer aid;
	private String alliance_instance;
	private Integer mesgUmidl;
	private Integer mesgUmidh;
	private String mesgSubFormat;
	private String mesgType;
	private String mesgUumid;
	private String mesgSenderX1;
	private String mesgTrnRef;
	private String mesgUserReferenceText;
	private Date mesgCreaDateTime;
	private Date xFinValueDate;
	@JsonIgnore
	private BigDecimal xFinAmount;
	@JsonIgnore
	private BigDecimal sortableXfinAmount;
	private String xFinCcy;
	private String mesgFrmtName;
	private String mesgStatus;
	private String mesgMesgUserGroup;
	private Integer mesgUumidSuffix;
	private String mesgIdentifier;
	private String instRpName;
	private String instReceiverX1;
	private String emiIAppName;
	private Integer emiSessionNbr;
	private String emiSequenceNbr;
	@JsonIgnore
	private String emiNetworkDeliveryStatus;
	@JsonIgnore
	private String recIAppName;
	private Integer recSessionNbr;
	private String recSequenceNbr;
	private String note;
	@JsonIgnore
	private String thousandAmountFormat;
	@JsonIgnore
	private String decimalAmountFormat;
	@JsonIgnore
	private transient boolean checked;
	private String orderingCustomer;
	private String orderingInstitution;
	private String beneficiaryCustomer;
	private String accountWithInstitution;
	private String detailsOfcharges;
	private String deducts;
	private String exchangeRate;
	private String statusCode;
	private String statusDesc;
	private String reasonCodes;
	private String statusOriginatorBIC;
	private String forwardedToAgent;
	private String transactionStatus;
	@JsonIgnore
	private String NAKCode;
	private String gpiCur;
	@JsonIgnore
	private BigDecimal instructedAmount;
	@JsonIgnore
	private String deductsFormatted;
	private String senderCorr;
	private String receiverCorr;
	private String reimbursementInst;
	@JsonProperty("settlementMethod")
	private String sattlmentMethod;
	private String clearingSystem;
	private String notifDateTime;
	private boolean notValidMsg;
	// OCBC Bank
	private String suffix;
	@JsonIgnore
	private String date_time_suffix;
	transient SimpleDateFormat suffixFormat = new SimpleDateFormat("yyMMdd");
	public static transient SimpleDateFormat fullDateFormat;
	public static transient SimpleDateFormat fullDateTimeFormat;
	transient DecimalFormat twoDigitsFormat = new DecimalFormat("###,###.00");
	transient DecimalFormat threeDigitsFormat = new DecimalFormat("###,###.000");

	private String deductsCurr;
	@JsonIgnore
	private String moneyFormat = "###,###.";
	@JsonIgnore
	private String location;
	private String validationFlag;
	@JsonIgnore
	private String mxKeyBoard1;
	@JsonIgnore
	private String mxKeyBoard2;
	@JsonIgnore
	private String mxKeyBoard3;
	@JsonIgnore
	private Integer timeZoneOffset;

	private String mesgSyntaxTableVer;

	private String mesgHistory;

	/**
	 * Temporary text used for caching purposes only
	 */
	@JsonIgnore
	private String cachedText;

	private String uetr;
	private String slaId;

	private String mesgRelatedReference;
	private String mXKeyword1;
	private String mXKeyword2;
	private String mXKeyword3;
	private String serviceName;
	private String xmlText;
	@JsonIgnore
	private java.util.Date creatDate;

	private boolean messageMasked = false;

	public java.util.Date getCreatDate() {
		return new java.util.Date(mesgCreaDateTime.getTime());
	}

	public void setCreatDate(java.util.Date creatDate) {
		this.creatDate = creatDate;
	}

	@JsonIgnore
	private boolean viewerAmountZerosPadding;

	public String getCorrespondent() {
		if (mesgSubFormat.toLowerCase().startsWith("i")) {
			return instReceiverX1;
		}
		return mesgSenderX1;
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

	public String getMesgSubFormat() {
		return mesgSubFormat;
	}

	public void setMesgSubFormat(String mesgSubFormat) {
		if (mesgSubFormat == null) {
			mesgSubFormat = "";
		}
		this.mesgSubFormat = mesgSubFormat;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getMesgUumid() {
		return mesgUumid;
	}

	public void setMesgUumid(String mesgUumid) {
		this.mesgUumid = mesgUumid;
	}

	public String getMesgSenderX1() {
		return mesgSenderX1;
	}

	public void setMesgSenderX1(String mesgSenderX1) {
		this.mesgSenderX1 = mesgSenderX1;
	}

	public String getMesgTrnRef() {
		return mesgTrnRef;
	}

	public void setMesgTrnRef(String mesgTrnRef) {
		if (mesgTrnRef == null) {
			mesgTrnRef = "";
		}
		this.mesgTrnRef = mesgTrnRef;
	}

	public String getMesgUserReferenceText() {
		return mesgUserReferenceText;
	}

	public void setMesgUserReferenceText(String mesgUserReferenceText) {
		if (mesgUserReferenceText == null) {
			mesgUserReferenceText = "";
		}
		this.mesgUserReferenceText = mesgUserReferenceText;
	}

	@JsonIgnore
	public Date getMesgCreaDateTimeOnDB() {
		return mesgCreaDateTime;
	}

	@JsonIgnore
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

	@JsonProperty("MesgCreaDateTime")
	public String getMesgCreaDateTimeStr() {
		return fullDateTimeFormat.format(getMesgCreaDateTime());
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	@JsonIgnore
	public Date getxFinValueDateOnDB() {
		return xFinValueDate;
	}

	public Date getxFinValueDate() {
		// message value date has nothing to do with timezone
		return getxFinValueDateOnDB();
	}

	@JsonIgnore
	public String getxFinValueDateFormatted() {
		// message value date has nothing to do with timezone
		String value = "";
		if (getxFinValueDate() != null) {
			value = fullDateFormat.format(getxFinValueDate());
		}
		return value;
	}

	public void setxFinValueDate(Date xFinValueDate) {
		this.xFinValueDate = xFinValueDate;
	}

	public BigDecimal getxFinAmount() {
		return xFinAmount;
	}

	public String getDate_time_suffix() {

		if (mesgCreaDateTime != null && suffix != null)
			date_time_suffix = (suffixFormat.format(mesgCreaDateTime)) + suffix;

		return date_time_suffix;
	}

	public void setDate_time_suffix(String date_time_suffix) {
		this.date_time_suffix = date_time_suffix;
	}

	public String amountFormatWithZeroPading(String text, String thousandAmountFormat, String decimalAmountFormat) {

		if ((thousandAmountFormat != null && !thousandAmountFormat.isEmpty()) || (decimalAmountFormat != null && !decimalAmountFormat.isEmpty())) {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setGroupingSeparator(',');
			twoDigitsFormat.setDecimalFormatSymbols(symbols);
			twoDigitsFormat.setGroupingSize(3); // 105.5
			twoDigitsFormat.setGroupingUsed(true);
			return twoDigitsFormat.format(new BigDecimal(text));

		} else if (isViewerAmountZerosPadding()) {
			String value = new DecimalFormat("0.00##").format(new BigDecimal(text));
			return value;
		} else {

			return text;
		}

	}

	@JsonIgnore
	public String amountFormat(String text, String currency, String thousandAmountFormat, String decimalAmountFormat) {
		moneyFormat = "###,###.";
		int numberOfDeciamlDigits = 0;

		if (text != null && !text.isEmpty() && text.contains(".")) {
			String fraction = text.substring(text.indexOf(".") + 1);
			numberOfDeciamlDigits = fraction.length();
		}

		for (int i = 0; i < numberOfDeciamlDigits; i++) {
			moneyFormat += "0";
		}

		DecimalFormat formatter = new DecimalFormat(moneyFormat);

		/*
		 * check amount configured separator from default config file
		 */
		if ((thousandAmountFormat != null && !thousandAmountFormat.isEmpty()) || (decimalAmountFormat != null && !decimalAmountFormat.isEmpty())) {
			String fraction = "";
			int lastIndex = text.lastIndexOf('.');
			if (lastIndex != -1) {

				fraction = text.substring(lastIndex + 1);
			}

			Double number = Double.parseDouble(text);

			String foramtedNumberStr = formatter.format(number);

			String[] parts = foramtedNumberStr.split("\\.");
			String result = "";
			if (parts == null || parts.length == 0) {
				// Do nothing
			} else {
				if (fraction.isEmpty()) {
					result = parts[0] + ".";
					// return part zero only
				} else {
					result = parts[0] + "." + fraction.substring(0, fraction.length());
				}
			}

			if (thousandAmountFormat.equals(",")) {
				text = result;
				return text;
			} else if (decimalAmountFormat.equals(",")) {
				text = result;
				text = text.replace(",", "-"); // , = -
				text = text.replace(".", ",");
				text = text.replace("-", ".");
				return text;
			} else {
				return text;
			}

		} else {
			return text;
		}

	}

	public String getxFinAmountFormatted() {
		if (xFinAmount == null) {
			return "";
		}
		if (isViewerAmountZerosPadding()) {
			return amountFormatWithZeroPading(xFinAmount.toString(), getThousandAmountFormat(), getDecimalAmountFormat());
		}

		return amountFormat(xFinAmount.toString(), getxFinCcy(), getThousandAmountFormat(), getDecimalAmountFormat());

	}

	public String getDeductsFormatted() {
		if (deducts == null || deducts.isEmpty() || deducts.equals("0")) {
			return "";
		}

		return amountFormatWithZeroPading(deducts, getThousandAmountFormat(), getDecimalAmountFormat());

	}

	public String getEmiIAppNameFormatted() {// emiIAppName+"
												// "+getEmiSessionNbrFormatted()+"
												// "+getEmiSequenceNbrFormatted();
		if (emiIAppName == null || emiIAppName.isEmpty()) {
			return "";
		}

		return emiIAppName + " " + getEmiSessionNbrFormatted() + " " + getEmiSequenceNbrFormatted();

	}

	public String getRecIAppNameFormatted() {
		if (recIAppName == null || recIAppName.isEmpty()) {
			return "";
		}
		return recIAppName + " " + getRecSessionNbrFormatted() + " " + getRecSequenceNbrFormatted();
	}

	public void setxFinAmount(BigDecimal xFinAmount) {
		this.xFinAmount = xFinAmount;
		setSortableXfinAmount(xFinAmount);
	}

	public String getxFinCcy() {
		return xFinCcy;
	}

	public void setxFinCcy(String xFinCcy) {
		if (xFinCcy == null) {
			xFinCcy = "";
		}
		this.xFinCcy = xFinCcy;
	}

	public String getMesgFrmtName() {
		return mesgFrmtName;
	}

	public void setMesgFrmtName(String mesgFrmtName) {
		if (mesgFrmtName == null) {
			mesgFrmtName = "";
		}
		this.mesgFrmtName = mesgFrmtName;
	}

	public String getMesgStatus() {
		return mesgStatus;
	}

	public void setMesgStatus(String mesgStatus) {
		if (mesgStatus == null) {
			mesgStatus = "";
		}
		this.mesgStatus = mesgStatus;
	}

	public String getMesgMesgUserGroup() {
		return mesgMesgUserGroup;
	}

	public void setMesgMesgUserGroup(String mesgMesgUserGroup) {
		this.mesgMesgUserGroup = mesgMesgUserGroup;
	}

	public String getInstRpName() {
		return instRpName;
	}

	public void setInstRpName(String instRpName) {
		if (instRpName == null) {
			instRpName = "";
		}
		this.instRpName = instRpName;
	}

	public String getInstReceiverX1() {
		return instReceiverX1;
	}

	public void setInstReceiverX1(String instReceiverX1) {
		this.instReceiverX1 = instReceiverX1;
	}

	public String getEmiIAppName() {
		return emiIAppName;
	}

	public void setEmiIAppName(String emiIAppName) {
		if (emiIAppName == null) {
			emiIAppName = "";
		}
		this.emiIAppName = emiIAppName;
	}

	public Integer getEmiSessionNbr() {
		return emiSessionNbr;
	}

	@JsonIgnore
	public String getEmiSessionNbrFormatted() {
		if (emiSessionNbr == null) {
			return "";
		}
		return String.format("%0" + 4 + "d", emiSessionNbr);
	}

	public void setEmiSessionNbr(Integer emiSessionNbr) {
		this.emiSessionNbr = emiSessionNbr;
	}

	public String getEmiSequenceNbr() {
		return emiSequenceNbr;
	}

	@JsonIgnore
	public String getEmiSequenceNbrFormatted() {
		if (emiSequenceNbr == null || emiSequenceNbr.isEmpty() || !NumberUtils.isNumber(emiSequenceNbr)) {
			return "";
		}
		return String.format("%0" + 6 + "d", Integer.parseInt(emiSequenceNbr));
	}

	public void setEmiSequenceNbr(String emiSequenceNbr) {
		if (emiSequenceNbr == null) {
			emiSequenceNbr = "";
		}
		this.emiSequenceNbr = emiSequenceNbr;
	}

	public String getEmiNetworkDeliveryStatus() {
		return emiNetworkDeliveryStatus;
	}

	public String getEmiNetworkDeliveryStatusFormatted() {
		if (!StringUtils.isEmpty(emiNetworkDeliveryStatus)) {
			if (StringUtils.equalsIgnoreCase(emiNetworkDeliveryStatus, "DLV_N_A"))
				return "N/A";
			if (StringUtils.equalsIgnoreCase(emiNetworkDeliveryStatus, "DLV_WAITING_ACK"))
				return "Waiting Network Ack";
			if (StringUtils.equalsIgnoreCase(emiNetworkDeliveryStatus, "DLV_TIMED_OUT"))
				return "Network Time out";
			if (StringUtils.equalsIgnoreCase(emiNetworkDeliveryStatus, "DLV_ACKED"))
				return "Network Ack";
			if (StringUtils.equalsIgnoreCase(emiNetworkDeliveryStatus, "DLV_NACKED"))
				return "Network Nack";
			if (StringUtils.equalsIgnoreCase(emiNetworkDeliveryStatus, "DLV_REJECTED_LOCALLY"))
				return "Rejected Locally";
			if (StringUtils.equalsIgnoreCase(emiNetworkDeliveryStatus, "DLV_ABORTED"))
				return "Network Aborted";
		}
		// else
		return "-";
	}

	public void setEmiNetworkDeliveryStatus(String emiNetworkDeliveryStatus) {
		if (emiNetworkDeliveryStatus == null) {
			emiNetworkDeliveryStatus = "";
		}
		this.emiNetworkDeliveryStatus = emiNetworkDeliveryStatus;
	}

	public String getRecIAppName() {
		return recIAppName;
	}

	public void setRecIAppName(String recIAppName) {
		if (recIAppName == null) {
			recIAppName = "";
		}
		this.recIAppName = recIAppName;
	}

	public Integer getRecSessionNbr() {
		return recSessionNbr;
	}

	@JsonIgnore
	public String getRecSessionNbrFormatted() {
		if (recSessionNbr == null) {
			return "";
		}
		return String.format("%0" + 4 + "d", recSessionNbr);
	}

	public void setRecSessionNbr(Integer recSessionNbr) {
		this.recSessionNbr = recSessionNbr;
	}

	public String getRecSequenceNbr() {
		return recSequenceNbr;
	}

	@JsonIgnore
	public String getRecSequenceNbrFormatted() {
		if (recSequenceNbr == null || recSequenceNbr.isEmpty() || !NumberUtils.isNumber(recSequenceNbr)) {
			return "";
		}
		return String.format("%0" + 6 + "d", Integer.parseInt(recSequenceNbr));
	}

	public void setRecSequenceNbr(String recSequenceNbr) {
		this.recSequenceNbr = recSequenceNbr;
	}

	public String getMessageUMID() {

		String result = String.format("%04X%04X", this.mesgUmidl, mesgUmidh);

		return result;
	}

	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(int timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public Integer getMesgUumidSuffix() {
		return mesgUumidSuffix;
	}

	public void setMesgUumidSuffix(Integer mesgUumidSuffix) {
		this.mesgUumidSuffix = mesgUumidSuffix;
	}

	public String getMesgIdentifier() {
		return mesgIdentifier;
	}

	public void setMesgIdentifier(String mesgIdentifier) {
		this.mesgIdentifier = mesgIdentifier;
	}

	public String getMesgReference() {
		if (mesgFrmtName != null && ("Swift".equalsIgnoreCase(mesgFrmtName.trim()) || "Internal".equalsIgnoreCase(mesgFrmtName.trim()))) {
			return mesgTrnRef;
		}
		return mesgUserReferenceText;
	}

	public String getMesgSyntaxTableVer() {
		return mesgSyntaxTableVer;
	}

	public void setMesgSyntaxTableVer(String mesgSyntaxTableVer) {
		this.mesgSyntaxTableVer = mesgSyntaxTableVer;
	}

	public String getCachedText() {
		return cachedText;
	}

	public void setCachedText(String cachedText) {
		this.cachedText = cachedText;
	}

	public String getAlliance_instance() {
		return alliance_instance;
	}

	public void setAlliance_instance(String alliance_instance) {
		if (alliance_instance == null) {
			alliance_instance = "";
		}
		this.alliance_instance = alliance_instance;
	}

	public String getUetr() {
		return uetr;
	}

	public void setUetr(String uetr) {
		if (uetr == null) {
			uetr = "";
		}
		this.uetr = uetr;
	}

	public String getSlaId() {
		return slaId;
	}

	public void setSlaId(String slaId) {
		if (slaId == null) {
			slaId = "";
		}
		this.slaId = slaId;
	}

	public String getMesgRelatedReference() {
		return mesgRelatedReference;
	}

	public void setMesgRelatedReference(String mesgRelatedReference) {
		if (mesgRelatedReference == null) {
			mesgRelatedReference = "";
		}
		this.mesgRelatedReference = mesgRelatedReference;
	}

	public String getmXKeyword1() {
		return mXKeyword1;
	}

	public void setmXKeyword1(String mXKeyword1) {
		if (mXKeyword1 == null) {
			mXKeyword1 = "";
		}
		this.mXKeyword1 = mXKeyword1;
	}

	public String getmXKeyword2() {
		return mXKeyword2;
	}

	public void setmXKeyword2(String mXKeyword2) {
		if (mXKeyword2 == null) {
			mXKeyword2 = "";
		}
		this.mXKeyword2 = mXKeyword2;
	}

	public String getmXKeyword3() {
		return mXKeyword3;
	}

	public void setmXKeyword3(String mXKeyword3) {
		if (mXKeyword3 == null) {
			mXKeyword3 = "";
		}
		this.mXKeyword3 = mXKeyword3;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getXmlText() {
		return xmlText;
	}

	public void setXmlText(String xmlText) {
		this.xmlText = xmlText;
	}

	public BigDecimal getSortableXfinAmount() {
		return sortableXfinAmount;
	}

	public void setSortableXfinAmount(BigDecimal sortableXfinAmount) {
		if (sortableXfinAmount == null) {
			this.sortableXfinAmount = new BigDecimal(0.00);
		} else {
			this.sortableXfinAmount = sortableXfinAmount;
		}

	}

	public boolean isViewerAmountZerosPadding() {
		return viewerAmountZerosPadding;
	}

	public void setViewerAmountZerosPadding(boolean viewerAmountZerosPadding) {
		this.viewerAmountZerosPadding = viewerAmountZerosPadding;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getThousandAmountFormat() {
		return thousandAmountFormat;
	}

	public void setThousandAmountFormat(String thousandAmountFormat) {
		this.thousandAmountFormat = thousandAmountFormat;
	}

	public String getDecimalAmountFormat() {
		return decimalAmountFormat;
	}

	public void setDecimalAmountFormat(String decimalAmountFormat) {
		this.decimalAmountFormat = decimalAmountFormat;
	}

	public String getOrderingCustomer() {
		return orderingCustomer;
	}

	public void setOrderingCustomer(String orderingCustomer) {
		this.orderingCustomer = orderingCustomer;
	}

	public String getOrderingInstitution() {
		return orderingInstitution;
	}

	public void setOrderingInstitution(String orderingInstitution) {
		this.orderingInstitution = orderingInstitution;
	}

	public String getBeneficiaryCustomer() {
		return beneficiaryCustomer;
	}

	public void setBeneficiaryCustomer(String beneficiaryCustomer) {
		this.beneficiaryCustomer = beneficiaryCustomer;
	}

	public String getAccountWithInstitution() {
		return accountWithInstitution;
	}

	public void setAccountWithInstitution(String accountWithInstitution) {
		this.accountWithInstitution = accountWithInstitution;
	}

	public String getDetailsOfcharges() {
		return detailsOfcharges;
	}

	public void setDetailsOfcharges(String detailsOfcharges) {
		this.detailsOfcharges = detailsOfcharges;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonCodes() {
		return reasonCodes;
	}

	public void setReasonCodes(String reasonCodes) {
		this.reasonCodes = reasonCodes;
	}

	public String getStatusOriginatorBIC() {
		return statusOriginatorBIC;
	}

	public void setStatusOriginatorBIC(String statusOriginatorBIC) {
		this.statusOriginatorBIC = statusOriginatorBIC;
	}

	public String getForwardedToAgent() {
		return forwardedToAgent;
	}

	public void setForwardedToAgent(String forwardedToAgent) {
		this.forwardedToAgent = forwardedToAgent;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getNAKCode() {
		return NAKCode;
	}

	public void setNAKCode(String nAKCode) {
		NAKCode = nAKCode;
	}

	public String getGpiCur() {
		return gpiCur;
	}

	public void setGpiCur(String gpiCur) {
		this.gpiCur = gpiCur;
	}

	public String getDeducts() {
		return deducts;
	}

	public void setDeducts(String deducts) {
		this.deducts = deducts;
	}

	public void setDeductsFormatted(String deductsFormatted) {
		this.deductsFormatted = deductsFormatted;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getValidationFlag() {
		return validationFlag;
	}

	public void setValidationFlag(String validationFlag) {
		this.validationFlag = validationFlag;
	}

	public String getMxKeyBoard1() {
		return mxKeyBoard1;
	}

	public void setMxKeyBoard1(String mxKeyBoard1) {
		this.mxKeyBoard1 = mxKeyBoard1;
	}

	public String getMxKeyBoard2() {
		return mxKeyBoard2;
	}

	public void setMxKeyBoard2(String mxKeyBoard2) {
		this.mxKeyBoard2 = mxKeyBoard2;
	}

	public String getMxKeyBoard3() {
		return mxKeyBoard3;
	}

	public void setMxKeyBoard3(String mxKeyBoard3) {
		this.mxKeyBoard3 = mxKeyBoard3;
	}

	public String getSenderCorr() {
		return senderCorr;
	}

	public void setSenderCorr(String senderCorr) {
		this.senderCorr = senderCorr;
	}

	public String getReceiverCorr() {
		return receiverCorr;
	}

	public void setReceiverCorr(String receiverCorr) {
		this.receiverCorr = receiverCorr;
	}

	public String getReimbursementInst() {
		return reimbursementInst;
	}

	public void setReimbursementInst(String reimbursementInst) {
		this.reimbursementInst = reimbursementInst;
	}

	public String getMoneyFormat() {
		return moneyFormat;
	}

	public void setMoneyFormat(String moneyFormat) {
		this.moneyFormat = moneyFormat;
	}

	public String getDeductsCurr() {
		return deductsCurr;
	}

	public void setDeductsCurr(String deductsCurr) {
		this.deductsCurr = deductsCurr;
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

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getNotifDateTime() {
		return notifDateTime;
	}

	public void setNotifDateTime(String notifDateTime) {
		this.notifDateTime = notifDateTime;
	}

	public BigDecimal getInstructedAmount() {
		return instructedAmount;
	}

	public void setInstructedAmount(BigDecimal instructedAmount) {
		this.instructedAmount = instructedAmount;
	}

	@JsonProperty("instructedAmount")
	public String getInstructedAmountFormatted() {

		if (instructedAmount == null)
			return "";

		if (instructedAmount.toString().endsWith(".00"))
			return instructedAmount.toString().substring(0, instructedAmount.toString().length() - 3);

		return instructedAmount.toString();
	}

	public String getMessageId() {
		return "{" + getAid() + "," + getMesgUmidl() + "," + getMesgUmidh() + "}";
	}

	public boolean isMessageMasked() {
		return messageMasked;
	}

	public void setMessageMasked(boolean messageMasked) {
		this.messageMasked = messageMasked;
	}

	public String getMessagePK() {
		return "(" + aid + "," + mesgUmidl + "," + mesgUmidh + ")";
	}

	public boolean isNotValidMsg() {
		return notValidMsg;
	}

	public void setNotValidMsg(boolean notValidMsg) {
		this.notValidMsg = notValidMsg;
	}

	public String getMesgHistory() {
		return mesgHistory;
	}

	public void setMesgHistory(String mesgHistory) {
		this.mesgHistory = mesgHistory;
	}
}
