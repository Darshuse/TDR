package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class JrnlPK implements Serializable {

	private static final long serialVersionUID = -6026273091351383432L;

	@Column(name = "AID")
	private Integer aid;

	@Column(name = "JRNL_REV_DATE_TIME")
	private Integer jrnlRevDateTime;

	@Column(name = "JRNL_SEQ_NBR")
	private Long jrnlSeqNumber;

	public JrnlPK() {
	}

	public JrnlPK(Integer aid, Integer jrnlRevDateTime, Long jrnlSeqNumber) {
		super();
		this.aid = aid;
		this.jrnlRevDateTime = jrnlRevDateTime;
		this.jrnlSeqNumber = jrnlSeqNumber;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getJrnlRevDateTime() {
		return jrnlRevDateTime;
	}

	public void setJrnlRevDateTime(Integer jrnlRevDateTime) {
		this.jrnlRevDateTime = jrnlRevDateTime;
	}

	public Long getJrnlSeqNumber() {
		return jrnlSeqNumber;
	}

	public void setJrnlSeqNumber(Long jrnlSeqNumber) {
		this.jrnlSeqNumber = jrnlSeqNumber;
	}

	@Override
	public String toString() {
		return "JrnlPK [aid=" + aid + ", jrnlRevDateTime=" + jrnlRevDateTime + ", jrnlSeqNumber=" + jrnlSeqNumber + "]";
	}

}
