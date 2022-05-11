package com.eastnets.service.viewer.report;

import java.util.Date;

import com.eastnets.domain.viewer.SearchResultEntity;

public class ViewerSearchReportEntity {
	private SearchResultEntity message;
	private String text ;
	
	public ViewerSearchReportEntity(SearchResultEntity message  ){
		this.setMessage(message);
	}

	public SearchResultEntity getMessage() {
		return message;
	}

	public void setMessage(SearchResultEntity message) {
		this.message = message;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getSubFormat(){
		return message.getMesgSubFormat();
	}

	public String getSender(){
		return message.getMesgSenderX1();
	}

	public String getReceiver(){
		return message.getInstReceiverX1();
	}

	public Date getCreaDateTime(){
		return message.getMesgCreaDateTime();
	}
	

	public String getIsn(){
		if(message.getMesgSubFormat().equalsIgnoreCase("INPUT"))
			return message.getEmiSequenceNbrFormatted();
		return message.getRecSequenceNbrFormatted();
	}	
}
