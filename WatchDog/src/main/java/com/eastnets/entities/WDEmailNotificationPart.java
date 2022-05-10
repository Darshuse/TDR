package com.eastnets.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "WDEMAILNOTIFICATION")
@Cacheable(value = false)
public class WDEmailNotificationPart extends WDEmailNotification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3743119177333927788L;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MESG_CREA_DATE_TIME", nullable = true)
	private Date mesgCreaDateTime;

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

}
