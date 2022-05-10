package com.eastnets.resilience.xsd.messaging;

import java.util.Date;

public class MessageInfo {
	private Date mesgCreationDateTime;
	private int intvAppeCount;
	
	public Date getMesgCreationDateTime() {
		return mesgCreationDateTime;
	}
	public void setMesgCreationDateTime(Date mesgCreationDateTime) {
		this.mesgCreationDateTime = mesgCreationDateTime;
	}
	public int getIntvAppeCount() {
		return intvAppeCount;
	}
	public void setIntvAppeCount(int intvAppeCount) {
		this.intvAppeCount = intvAppeCount;
	}

}
