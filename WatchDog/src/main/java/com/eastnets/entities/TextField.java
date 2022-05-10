package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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

	// bi-directional many-to-one association to Mesg
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({ @JoinColumn(name = "AID", referencedColumnName = "AID", insertable = false, updatable = false), @JoinColumn(name = "TEXT_S_UMIDH", referencedColumnName = "MESG_S_UMIDH", insertable = false, updatable = false),
			@JoinColumn(name = "TEXT_S_UMIDL", referencedColumnName = "MESG_S_UMIDL", insertable = false, updatable = false) })
	private Mesg mesg;

	public Mesg getMesg() {
		return mesg;
	}

	public void setMesg(Mesg mesg) {
		this.mesg = mesg;
	}

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
