package com.eastnets.domain.loader;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "RINTV")
@NamedQuery(name = "RintvPart.findAll", query = "SELECT r FROM RintvPart r")
@Cacheable(value = false)
public class RintvPart extends Rintv implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1740744194377533333L;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "X_CREA_DATE_TIME_MESG",nullable=true)
	private Date mesgCreaDateTime;

 
	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}
	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

}
