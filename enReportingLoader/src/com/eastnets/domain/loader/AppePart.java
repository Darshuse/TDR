package com.eastnets.domain.loader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author MKassab
 * 
 */

@Entity
@Table(name = "RAPPE")
@NamedQuery(name = "AppePart.findAll", query = "SELECT a FROM AppePart a")
@Cacheable(value = false)
public class AppePart extends Appe implements Serializable {

	private static final long serialVersionUID = -7203462883876484801L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "X_CREA_DATE_TIME_MESG", nullable = true)
	private Date mesgCreaDateTime;

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

}
