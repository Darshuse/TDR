package com.eastnets.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

@Embeddable
public class IntvPK implements Serializable {

	private static final long serialVersionUID = -7945971486688308760L;

	@Column(name = "AID")
	private Long aid;

	@Column(name = "INTV_S_UMIDL")
	private Long intvSUmidl;

	@Column(name = "INTV_S_UMIDH")
	private Long intvSUmidh;

	@Column(name = "INTV_INST_NUM")
	private Integer intvInstNum;

	@Column(name = "INTV_DATE_TIME")
	private LocalDateTime intvDateTime;

	@Column(name = "INTV_SEQ_NBR")
	private Integer intvSeqNo;

	public IntvPK() {

	}

	public IntvPK(Long aid, Long intvSUmidl, Long intvSUmidh, Integer intvInstNum, LocalDateTime intvDateTime,
			Integer intvSeqNo) {
		super();
		this.aid = aid;
		this.intvSUmidl = intvSUmidl;
		this.intvSUmidh = intvSUmidh;
		this.intvInstNum = intvInstNum;
		this.intvDateTime = intvDateTime;
		this.intvSeqNo = intvSeqNo;
	}

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	public Long getIntvSUmidl() {
		return intvSUmidl;
	}

	public void setIntvSUmidl(Long intvSUmidl) {
		this.intvSUmidl = intvSUmidl;
	}

	public Long getIntvSUmidh() {
		return intvSUmidh;
	}

	public void setIntvSUmidh(Long intvSUmidh) {
		this.intvSUmidh = intvSUmidh;
	}

	public Integer getIntvInstNum() {
		return intvInstNum;
	}

	public void setIntvInstNum(Integer intvInstNum) {
		this.intvInstNum = intvInstNum;
	}

	public LocalDateTime getIntvDateTime() {
		return intvDateTime;
	}

	public void setIntvDateTime(LocalDateTime intvDateTime) {
		this.intvDateTime = intvDateTime;
	}

	public Integer getIntvSeqNo() {
		return intvSeqNo;
	}

	public void setIntvSeqNo(Integer intvSeqNo) {
		this.intvSeqNo = intvSeqNo;
	}

}