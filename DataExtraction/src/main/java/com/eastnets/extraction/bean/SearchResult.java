package com.eastnets.extraction.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.eastnets.extraction.service.helper.SearchUtils;

public class SearchResult {

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
	private BigDecimal xFinAmount;
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

	private String emiNetworkDeliveryStatus;
	private String recIAppName;
	private Integer recSessionNbr;
	private String recSequenceNbr;
	private String note;

	private String thousandAmountFormat;

	private String decimalAmountFormat;

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
	private String NAKCode;
	private String gpiCur;
	private BigDecimal instructedAmount;
	private String deductsFormatted;
	private String senderCorr;
	private String receiverCorr;
	private String reimbursementInst;
	private String sattlmentMethod;
	private String clearingSystem;
	private String notifDateTime;
	private List<XMLMessage> xmlMessages;

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public String getAlliance_instance() {
		return alliance_instance;
	}

	public void setAlliance_instance(String alliance_instance) {
		this.alliance_instance = alliance_instance;
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
		this.mesgSubFormat = mesgSubFormat;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
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
		this.mesgTrnRef = mesgTrnRef;
	}

	public String getMesgUserReferenceText() {
		return mesgUserReferenceText;
	}

	public void setMesgUserReferenceText(String mesgUserReferenceText) {
		this.mesgUserReferenceText = mesgUserReferenceText;
	}

	public Date getMesgCreaDateTimeOnDB() {
		return mesgCreaDateTime;
	}

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public Date getxFinValueDate() {
		return xFinValueDate;
	}

	public void setxFinValueDate(Date xFinValueDate) {
		this.xFinValueDate = xFinValueDate;
	}

	public BigDecimal getxFinAmount() {
		return xFinAmount;
	}

	public void setxFinAmount(BigDecimal xFinAmount) {
		this.xFinAmount = xFinAmount;
	}

	public BigDecimal getSortableXfinAmount() {
		return sortableXfinAmount;
	}

	public void setSortableXfinAmount(BigDecimal sortableXfinAmount) {
		this.sortableXfinAmount = sortableXfinAmount;
	}

	public String getxFinCcy() {
		return xFinCcy;
	}

	public void setxFinCcy(String xFinCcy) {
		this.xFinCcy = xFinCcy;
	}

	public String getMesgFrmtName() {
		return mesgFrmtName;
	}

	public void setMesgFrmtName(String mesgFrmtName) {
		this.mesgFrmtName = mesgFrmtName;
	}

	public String getMesgStatus() {
		return mesgStatus;
	}

	public void setMesgStatus(String mesgStatus) {
		this.mesgStatus = mesgStatus;
	}

	public String getMesgMesgUserGroup() {
		return mesgMesgUserGroup;
	}

	public void setMesgMesgUserGroup(String mesgMesgUserGroup) {
		this.mesgMesgUserGroup = mesgMesgUserGroup;
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

	public String getInstRpName() {
		return instRpName;
	}

	public void setInstRpName(String instRpName) {
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
		this.emiIAppName = emiIAppName;
	}

	public Integer getEmiSessionNbr() {
		return emiSessionNbr;
	}

	public void setEmiSessionNbr(Integer emiSessionNbr) {
		this.emiSessionNbr = emiSessionNbr;
	}

	public String getEmiSequenceNbr() {
		return emiSequenceNbr;
	}

	public void setEmiSequenceNbr(String emiSequenceNbr) {
		this.emiSequenceNbr = emiSequenceNbr;
	}

	public String getEmiNetworkDeliveryStatus() {
		return emiNetworkDeliveryStatus;
	}

	public void setEmiNetworkDeliveryStatus(String emiNetworkDeliveryStatus) {
		this.emiNetworkDeliveryStatus = emiNetworkDeliveryStatus;
	}

	public String getRecIAppName() {
		return recIAppName;
	}

	public void setRecIAppName(String recIAppName) {
		this.recIAppName = recIAppName;
	}

	public Integer getRecSessionNbr() {
		return recSessionNbr;
	}

	public void setRecSessionNbr(Integer recSessionNbr) {
		this.recSessionNbr = recSessionNbr;
	}

	public String getRecSequenceNbr() {
		return recSequenceNbr;
	}

	public void setRecSequenceNbr(String recSequenceNbr) {
		this.recSequenceNbr = recSequenceNbr;
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

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
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

	public String getDeducts() {
		return deducts;
	}

	public void setDeducts(String deducts) {
		this.deducts = deducts;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
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

	public BigDecimal getInstructedAmount() {
		return instructedAmount;
	}

	public void setInstructedAmount(BigDecimal instructedAmount) {
		this.instructedAmount = instructedAmount;
	}

	public String getDeductsFormatted() {
		return deductsFormatted;
	}

	public void setDeductsFormatted(String deductsFormatted) {
		this.deductsFormatted = deductsFormatted;
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

	public String getNotifDateTime() {
		return notifDateTime;
	}

	public void setNotifDateTime(String notifDateTime) {
		this.notifDateTime = notifDateTime;
	}

	public String getDateTimeForQuery() {
		return SearchUtils.DateToSqlStr(mesgCreaDateTime);
	}

	public List<XMLMessage> getXmlMessages() {
		return xmlMessages;
	}

	public void setXmlMessages(List<XMLMessage> xmlMessages) {
		this.xmlMessages = xmlMessages;
	}

}
