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
@Table(name="RTEXT")
@NamedQuery(name="TextPart.findAll", query="SELECT t FROM TextPart t")
@Cacheable(value = false)
public class TextPart extends Text implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7781192308364178756L;
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
