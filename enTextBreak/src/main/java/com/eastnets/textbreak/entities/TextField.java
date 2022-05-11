
package com.eastnets.textbreak.entities;

import java.io.Serializable;
import java.util.Date;

//@Entity
//@Table(name = "RTEXTFIELD")
public class TextField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3659447368331866515L;
	// @EmbeddedId
	private TextFieldPK id;
	// @Column(name = "field_cnt")
	private long fieldCnt;
	// @Column(name = "FIELD_OPTION")
	private String fieldOption;
	// @Column(name = "VALUE")
	private String value;

	// @Lob
	// @Column(name = "VALUE_MEMO")
	private String valueMemo;

	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "X_CREA_DATE_TIME_MESG", nullable = true)
	private Date mesgCreaDateTime;

	public TextFieldPK getId() {
		return id;
	}

	public void setId(TextFieldPK id) {
		this.id = id;
	}

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	@Override
	public String toString() {
		return "TextField [id=" + id + ", fieldCnt=" + fieldCnt + ", fieldOption=" + fieldOption + ", value=" + value + ", valueMemo=" + valueMemo + ", mesgCreaDateTime=" + mesgCreaDateTime + "]";
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
