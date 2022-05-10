package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class TextPart extends Text {

	private static final long serialVersionUID = -7020740158022176083L;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "X_CREA_DATE_TIME_MESG", nullable = true)
	private Date mesgCreaDateTime;

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	@Override
	public String toString() {
		return "TextPart [mesgCreaDateTime=" + mesgCreaDateTime + "] " + super.toString();
	}

}
