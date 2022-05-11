package com.eastnets.domain.viewer;

import java.util.Date;

public class GpiAgent {
	private String name;
	private String messageBIC;
	private String status;
	private String primaryKey;
	private String senderName;
	private String senderBic;
	private String reciverName;
	private String reciverBic;
	private String detailsReason;
	private Date creationDate;
	private String mesg_trn_ref;
	private MessageDetails details;
	private String umdhAndRef;
	private boolean orderingCustomer;
	private boolean beneficiaryCustomer;
	private boolean coverPayment;
	private EndPointStatus endPointStatus;
	private LineStatus lineStatus;
	private boolean firstGpiAgent;
	private boolean firstCoveGpiAgent;
	private boolean disableReceiveTimeIcone = false;
	private boolean disableSendTimeIcone;
	private boolean nonGpiAgent;
	private String endPointFormatted;
	private String countryName;
	private String cityName;
	private String takenTime;
	private String msgSendAmount;
	private String msgSendCurr;
	private String arriveDate = "";
	private String sendDate = "";
	private boolean lastElement;
	private LineStatus lineStatusForLastAgent = LineStatus.NOT_ARRIVED_COVE;
	private boolean gSRPParty = false;
	private String gSRPMsg;
	private Boolean filed52 = false;
	private Boolean filed52NotA = false;
	private String marketInfrastructure;

	private String mesgCharges;

	private String mesgDirction;

	private boolean showMarketInfrastructure = false;

	public MessageDetails getDetails() {
		return details;
	}

	public void setDetails(MessageDetails details) {
		this.details = details;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessageBIC() {
		return messageBIC;
	}

	public void setMessageBIC(String messageBIC) {
		this.messageBIC = messageBIC;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getUmdhAndRef() {
		return umdhAndRef;
	}

	public void setUmdhAndRef(String umdhAndRef) {
		this.umdhAndRef = umdhAndRef;
	}

	public EndPointStatus getEndPointStatus() {
		return endPointStatus;
	}

	public void setEndPointStatus(EndPointStatus endPointStatus) {
		this.endPointStatus = endPointStatus;
	}

	public LineStatus getLineStatus() {
		return lineStatus;
	}

	public void setLineStatus(LineStatus lineStatus) {
		this.lineStatus = lineStatus;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getMesg_trn_ref() {
		return mesg_trn_ref;
	}

	public void setMesg_trn_ref(String mesg_trn_ref) {
		this.mesg_trn_ref = mesg_trn_ref;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderBic() {
		return senderBic;
	}

	public void setSenderBic(String senderBic) {
		this.senderBic = senderBic;
	}

	public String getReciverName() {
		return reciverName;
	}

	public void setReciverName(String reciverName) {
		this.reciverName = reciverName;
	}

	public String getReciverBic() {
		return reciverBic;
	}

	public void setReciverBic(String reciverBic) {
		this.reciverBic = reciverBic;
	}

	public String getDetailsReason() {
		return detailsReason;
	}

	public void setDetailsReason(String detailsReason) {
		this.detailsReason = detailsReason;
	}

	public boolean isBeneficiaryCustomer() {
		return beneficiaryCustomer;
	}

	public void setBeneficiaryCustomer(boolean beneficiaryCustomer) {
		this.beneficiaryCustomer = beneficiaryCustomer;
	}

	public boolean isOrderingCustomer() {
		return orderingCustomer;
	}

	public void setOrderingCustomer(boolean orderingCustomer) {
		this.orderingCustomer = orderingCustomer;
	}

	public boolean isFirstGpiAgent() {
		return firstGpiAgent;
	}

	public void setFirstGpiAgent(boolean firstGpiAgent) {
		this.firstGpiAgent = firstGpiAgent;
	}

	public boolean isDisableReceiveTimeIcone() {
		return disableReceiveTimeIcone;
	}

	public void setDisableReceiveTimeIcone(boolean disableReceiveTimeIcone) {
		this.disableReceiveTimeIcone = disableReceiveTimeIcone;
	}

	public boolean isDisableSendTimeIcone() {
		return disableSendTimeIcone;
	}

	public void setDisableSendTimeIcone(boolean disableSendTimeIcone) {
		this.disableSendTimeIcone = disableSendTimeIcone;
	}

	public String getEndPointFormatted() {
		return endPointFormatted;
	}

	public void setEndPointFormatted(String endPointFormatted) {
		this.endPointFormatted = endPointFormatted;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getTakenTime() {
		return takenTime;
	}

	public void setTakenTime(String takenTime) {
		this.takenTime = takenTime;
	}

	public String getMsgSendAmount() {
		return msgSendAmount;
	}

	public void setMsgSendAmount(String msgSendAmount) {
		this.msgSendAmount = msgSendAmount;
	}

	public String getMsgSendCurr() {
		return msgSendCurr;
	}

	public void setMsgSendCurr(String msgSendCurr) {
		this.msgSendCurr = msgSendCurr;
	}

	public boolean isNonGpiAgent() {
		return nonGpiAgent;
	}

	public void setNonGpiAgent(boolean nonGpiAgent) {
		this.nonGpiAgent = nonGpiAgent;
	}

	public boolean isCoverPayment() {
		return coverPayment;
	}

	public void setCoverPayment(boolean coverPayment) {
		this.coverPayment = coverPayment;
	}

	public boolean isLastElement() {
		return lastElement;
	}

	public void setLastElement(boolean lastElement) {
		this.lastElement = lastElement;
	}

	public LineStatus getLineStatusForLastAgent() {
		return lineStatusForLastAgent;
	}

	public void setLineStatusForLastAgent(LineStatus lineStatusForLastAgent) {
		this.lineStatusForLastAgent = lineStatusForLastAgent;
	}

	public boolean isFirstCoveGpiAgent() {
		return firstCoveGpiAgent;
	}

	public void setFirstCoveGpiAgent(boolean firstCoveGpiAgent) {
		this.firstCoveGpiAgent = firstCoveGpiAgent;
	}

	public boolean isgSRPParty() {
		return gSRPParty;
	}

	public void setgSRPParty(boolean gSRPParty) {
		this.gSRPParty = gSRPParty;
	}

	public String getgSRPMsg() {
		return gSRPMsg;
	}

	public void setgSRPMsg(String gSRPMsg) {
		this.gSRPMsg = gSRPMsg;
	}

	public Boolean getFiled52() {
		return filed52;
	}

	public void setFiled52(Boolean filed52) {
		this.filed52 = filed52;
	}

	public String getMarketInfrastructure() {
		return marketInfrastructure;
	}

	public void setMarketInfrastructure(String marketInfrastructure) {
		this.marketInfrastructure = marketInfrastructure;
	}

	public boolean isShowMarketInfrastructure() {
		return showMarketInfrastructure;
	}

	public void setShowMarketInfrastructure(boolean showMarketInfrastructure) {
		this.showMarketInfrastructure = showMarketInfrastructure;
	}

	public Boolean getFiled52NotA() {
		return filed52NotA;
	}

	public void setFiled52NotA(Boolean filed52NotA) {
		this.filed52NotA = filed52NotA;
	}

	public String getMesgDirction() {
		return mesgDirction;
	}

	public void setMesgDirction(String mesgDirction) {
		this.mesgDirction = mesgDirction;
	}

	public String getMesgCharges() {
		return mesgCharges;
	}

	public void setMesgCharges(String mesgCharges) {
		this.mesgCharges = mesgCharges;
	}

}
