package com.eastnets.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class AppendixPart extends Appendix {

	private static final long serialVersionUID = 8775715803832522272L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "X_CREA_DATE_TIME_MESG", nullable = true)
	private Date mesgCreaDateTime;

	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "appeRecordIdGenerator")
	@SequenceGenerator(name = "appeRecordIdGenerator", sequenceName = "seq_appe_record_id")
	@Column(name = "APPE_RECORD_ID")
	private BigDecimal appeRecordId;

	public BigDecimal getAppeRecordId() {
		return appeRecordId;
	}

	public void setAppeRecordId(BigDecimal appeRecordId) {
		this.appeRecordId = appeRecordId;
	}

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	@Override
	public String toString() {
		return "AppendixPart [mesgCreaDateTime=" + mesgCreaDateTime + ", appeRecordId=" + appeRecordId + "]";
	}

}
