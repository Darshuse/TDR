
package com.eastnets.textbreak.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "rSystemTextField")
@Cacheable(value = false)
public class SystemTextField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4283359179364372958L;
	@EmbeddedId
	private SystemTextFieldPK id;
	@Column(name = "FIELD_CODE")
	private long fieldCode;
	@Column(name = "VALUE")
	private String value;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "X_CREA_DATE_TIME_MESG", nullable = true)
	private Date mesgCreaDateTime;

	public long getFieldCode() {
		return fieldCode;
	}

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public void setFieldCode(long fieldCode) {
		this.fieldCode = fieldCode;
	}

	public SystemTextFieldPK getId() {
		return id;
	}

	public void setId(SystemTextFieldPK id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
