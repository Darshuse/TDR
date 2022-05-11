package com.eastnets.dao.dbconsistencycheck;

import java.io.Serializable;

public class MessageDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mesg_crea_date_time;
	private String mesg_type;
	private String mesg_frmt_name;
	private String mesg_sender;
	private String mesg_receiver;
	private String mesg_amount;
	private String mesg_currency;
	private String valueDate;
	private String trn_ref;
	private String mesg_createdby;
	private String mesg_sub_format;
	

	// getter & setter
	
	
	public String getMesg_crea_date_time() {
		return mesg_crea_date_time;
	}
	public void setMesg_crea_date_time(String mesg_crea_date_time) {
		this.mesg_crea_date_time = mesg_crea_date_time;
	}
	public String getMesg_type() {
		return mesg_type;
	}
	public void setMesg_type(String mesg_type) {
		this.mesg_type = mesg_type;
	}
	public String getMesg_frmt_name() {
		return mesg_frmt_name;
	}
	public void setMesg_frmt_name(String mesg_frmt_name) {
		this.mesg_frmt_name = mesg_frmt_name;
	}
	public String getMesg_sender() {
		return mesg_sender;
	}
	public void setMesg_sender(String mesg_sender) {
		this.mesg_sender = mesg_sender;
	}
	public String getMesg_receiver() {
		return mesg_receiver;
	}
	public void setMesg_receiver(String mesg_receiver) {
		this.mesg_receiver = mesg_receiver;
	}
	public String getMesg_amount() {
		return mesg_amount;
	}
	public void setMesg_amount(String mesg_amount) {
		this.mesg_amount = mesg_amount;
	}
	public String getMesg_currency() {
		return mesg_currency;
	}
	public void setMesg_currency(String mesg_currency) {
		this.mesg_currency = mesg_currency;
	}
	public String getValueDate() {
		return valueDate;
	}
	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}
	public String getTrn_ref() {
		return trn_ref;
	}
	public void setTrn_ref(String trn_ref) {
		this.trn_ref = trn_ref;
	}
	public String getMesg_createdby() {
		return mesg_createdby;
	}
	public void setMesg_createdby(String mesg_createdby) {
		this.mesg_createdby = mesg_createdby;
	}
	public String getMesg_sub_format() {
		return mesg_sub_format;
	}
	public void setMesg_sub_format(String mesg_sub_format) {
		this.mesg_sub_format = mesg_sub_format;
	}
	
}
