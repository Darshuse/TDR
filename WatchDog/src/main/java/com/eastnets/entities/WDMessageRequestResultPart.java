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
@Table(name = "WDUSERREQUESTRESULT")
@Cacheable(value = false)
public class WDMessageRequestResultPart extends WDMessageRequestResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7781192308364178756L;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "X_CREA_DATE_TIME_MESG", nullable = true)
	private Date xMesgCreaDateTime;

	public Date getxMesgCreaDateTime() {
		return xMesgCreaDateTime;
	}

	public void setxMesgCreaDateTime(Date xMesgCreaDateTime) {
		this.xMesgCreaDateTime = xMesgCreaDateTime;
	}

}
