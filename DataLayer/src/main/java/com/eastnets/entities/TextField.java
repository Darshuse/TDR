package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "RTEXTFIELD")
@Cacheable(value = false)
public class TextField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3659447368331866515L;
	@EmbeddedId
	private TextFieldPK id;
	@Column(name = "field_cnt")
	private long fieldCnt;
	@Column(name = "FIELD_OPTION")
	private String fieldOption;
	@Column(name = "VALUE")
	private String value;
	
	@Lob
	@Column(name = "VALUE_MEMO")
	private String valueMemo;

	public TextFieldPK getId() {
		return id;
	}

	public void setId(TextFieldPK id) {
		this.id = id;
	}

	public long getFieldCnt() {
		return fieldCnt;
	}

	public void setFieldCnt(long fieldCnt) {
		this.fieldCnt = fieldCnt;
	}

	public String getFieldOption() {
		return fieldOption;
	}

	public void setFieldOption(String fieldOption) {
		this.fieldOption = fieldOption;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueMemo() {
		return valueMemo;
	}

	public void setValueMemo(String valueMemo) {
		this.valueMemo = valueMemo;
	}

}
