package com.eastnets.domain.viewer;

import java.util.Date;

public class DaynamicMsgDetailsParam extends DaynamicViewerParam {

	private int aid;
	private int umidl;
	private int umidh;
	private Date mesg_crea_date;
	private boolean includeHistory;
	private boolean includeMessageNotes;
	private String thousandAmountFormat;
	private String decimalAmountFormat;

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

	public boolean isIncludeMessageNotes() {
		return includeMessageNotes;
	}

	public void setIncludeMessageNotes(boolean includeMessageNotes) {
		this.includeMessageNotes = includeMessageNotes;
	}

	public boolean isIncludeHistory() {
		return includeHistory;
	}

	public void setIncludeHistory(boolean includeHistory) {
		this.includeHistory = includeHistory;
	}

	public Date getMesg_crea_date() {
		return mesg_crea_date;
	}

	public void setMesg_crea_date(Date mesg_crea_date) {
		this.mesg_crea_date = mesg_crea_date;
	}

	public int getUmidh() {
		return umidh;
	}

	public void setUmidh(int umidh) {
		this.umidh = umidh;
	}

	public int getUmidl() {
		return umidl;
	}

	public void setUmidl(int umidl) {
		this.umidl = umidl;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

}
