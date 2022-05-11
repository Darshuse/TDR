package com.eastnets.domain.relatedMessage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

public class RelatedMessage {
	
	
	private int aid;
	private int mesg_s_umidl;
	private String MESG_REL_TRN_REF;
	private Date creationDate;
	private Date valueDate;
	private int LEVEL;
	private BigDecimal   xFinAmount;
	private int mesg_s_umidh;
	private String msgType;
	private String   xFinCcy;
	private String mesg_trn_ref;
	private String amountStr;
	private boolean apperAmount;
	private boolean showRelatedRef;
    private String sender;
    private String receiver;
    private Date justDate;
	private boolean apperCurancy;
	private boolean showValueDate;

	private String subFotmat;
	private String usnitName;
	
	

	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public int getMesg_s_umidl() {
		return mesg_s_umidl;
	}
	public void setMesg_s_umidl(int mesg_s_umidl) {
		this.mesg_s_umidl = mesg_s_umidl;
	}
	public int getMesg_s_umidh() {
		return mesg_s_umidh;
	}
	public void setMesg_s_umidh(int mesg_s_umidh) {
		this.mesg_s_umidh = mesg_s_umidh;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMesg_trn_ref() {
		return mesg_trn_ref;
	}
	public void setMesg_trn_ref(String mesg_trn_ref) {
		this.mesg_trn_ref = mesg_trn_ref;
	}
	public String getMESG_REL_TRN_REF() {
		return MESG_REL_TRN_REF;
	}
	public void setMESG_REL_TRN_REF(String mESG_REL_TRN_REF) {
		MESG_REL_TRN_REF = mESG_REL_TRN_REF;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public int getLEVEL() {
		return LEVEL;
	}
	public void setLEVEL(int lEVEL) {
		LEVEL = lEVEL;
	}
	public BigDecimal getxFinAmount() {
		return xFinAmount;
	}
	public void setxFinAmount(BigDecimal xFinAmount) {
		this.xFinAmount = xFinAmount;
	}

	public String getxFinAmountFormatted(){
		if ( xFinAmount == null ){
			return "";
		}
		return new DecimalFormat("0.00##").format(xFinAmount);		
	}
	public String getAmountStr() {
		return amountStr;
	}
	public void setAmountStr(String amountStr) {
		this.amountStr = amountStr;
	}
	public boolean isApperAmount() {
		return apperAmount;
	}
	public void setApperAmount(boolean apperAmount) {
		this.apperAmount = apperAmount;
	}
	public String getxFinCcy() {
		return xFinCcy;
	}
	public void setxFinCcy(String xFinCcy) {
		this.xFinCcy = xFinCcy;
	}
	public boolean isApperCurancy() {
		return apperCurancy;
	}
	public void setApperCurancy(boolean apperCurancy) {
		this.apperCurancy = apperCurancy;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Date getJustDate() {
		return justDate;
	}
	public void setJustDate(Date justDate) {
		this.justDate = justDate;
	}
	public boolean isShowRelatedRef() {
		return showRelatedRef;
	}
	public void setShowRelatedRef(boolean showRelatedRef) {
		this.showRelatedRef = showRelatedRef;
	}
	public String getSubFotmat() {
		return subFotmat;
	}
	public void setSubFotmat(String subFotmat) {
		this.subFotmat = subFotmat;
	}
	public String getUsnitName() {
		return usnitName;
	}
	public void setUsnitName(String usnitName) {
		this.usnitName = usnitName;
	}
	public Date getValueDate() {
		return valueDate;
	}
	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}
	public boolean isShowValueDate() {
		return showValueDate;
	}
	public void setShowValueDate(boolean showValueDate) {
		this.showValueDate = showValueDate;
	}
}
