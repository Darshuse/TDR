package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "WDEVENTREQUESTRESULT")
@Cacheable(value = false)
public class WDEventRequestResultPart extends WDEventRequestResult {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "jrnl_date_time", nullable = true)
	private Date jrnlDateTime;

	public Date getJrnlDateTime() {
		return jrnlDateTime;
	}

	public void setJrnlDateTime(Date jrnlDateTime) {
		this.jrnlDateTime = jrnlDateTime;
	}

}
