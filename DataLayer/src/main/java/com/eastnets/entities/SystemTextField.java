package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "rSystemTextField")
@Cacheable(value = false)
public class SystemTextField  implements Serializable{

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
	
	public long getFieldCode() {
		return fieldCode;
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
