package com.eastnets.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RINTV")
public class Intervention implements Serializable {

	private static final long serialVersionUID = -1391899949730939271L;

	@EmbeddedId
	private IntvPK intvId;

	@Column(name = "INTV_INTY_NUM")
	private Integer intvIntyNo;

	@Column(name = "INTV_INTY_NAME")
	private String intvIntyName;

	@Column(name = "INTV_INTY_CATEGORY")
	private String intvIntyCategory;

	@Column(name = "INTV_OPER_NICKNAME")
	private String intvOperNickname;

	@Column(name = "INTV_APPL_SERV_NAME")
	private String intvApplServName;

	@Column(name = "INTV_MPFN_NAME")
	private String intvMpfnName;

	@Column(name = "INTV_APPE_DATE_TIME")
	private LocalDateTime intvAppeDateTime;

	@Column(name = "INTV_APPE_SEQ_NBR")
	private Integer intvAppeSeqNo;

	@Column(name = "INTV_LENGTH")
	private Integer intvLength;

	@Column(name = "INTV_TOKEN")
	private Integer intvToken;

	@Column(name = "INTV_TEXT")
	private String intvText;

	@Column(name = "INTV_MERGED_TEXT")
	private String intvMergedText;

	public Integer getIntvIntyNo() {
		return intvIntyNo;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumns({ @JoinColumn(name = "AID", referencedColumnName = "AID", insertable = false, updatable = false),
			@JoinColumn(name = "INTV_S_UMIDH", referencedColumnName = "MESG_S_UMIDH", insertable = false, updatable = false),
			@JoinColumn(name = "INTV_S_UMIDL", referencedColumnName = "MESG_S_UMIDL", insertable = false, updatable = false) })
	public Mesg mesg;

	public IntvPK getIntvId() {
		return intvId;
	}

	public void setIntvId(IntvPK intvId) {
		this.intvId = intvId;
	}

	public void setIntvIntyNo(Integer intvIntyNo) {
		this.intvIntyNo = intvIntyNo;
	}

	public String getIntvIntyName() {
		return intvIntyName;
	}

	public void setIntvIntyName(String intvIntyName) {
		this.intvIntyName = intvIntyName;
	}

	public String getIntvIntyCategory() {
		return intvIntyCategory;
	}

	public void setIntvIntyCategory(String intvIntyCategory) {
		this.intvIntyCategory = intvIntyCategory;
	}

	public String getIntvOperNickname() {
		return intvOperNickname;
	}

	public void setIntvOperNickname(String intvOperNickname) {
		this.intvOperNickname = intvOperNickname;
	}

	public String getIntvApplServName() {
		return intvApplServName;
	}

	public void setIntvApplServName(String intvApplServName) {
		this.intvApplServName = intvApplServName;
	}

	public String getIntvMpfnName() {
		return intvMpfnName;
	}

	public void setIntvMpfnName(String intvMpfnName) {
		this.intvMpfnName = intvMpfnName;
	}

	public LocalDateTime getIntvAppeDateTime() {
		return intvAppeDateTime;
	}

	public void setIntvAppeDateTime(LocalDateTime intvAppeDateTime) {
		this.intvAppeDateTime = intvAppeDateTime;
	}

	public Integer getIntvAppeSeqNo() {
		return intvAppeSeqNo;
	}

	public void setIntvAppeSeqNo(Integer intvAppeSeqNo) {
		this.intvAppeSeqNo = intvAppeSeqNo;
	}

	public Integer getIntvLength() {
		return intvLength;
	}

	public void setIntvLength(Integer intvLength) {
		this.intvLength = intvLength;
	}

	public Integer getIntvToken() {
		return intvToken;
	}

	public void setIntvToken(Integer intvToken) {
		this.intvToken = intvToken;
	}

	public String getIntvText() {
		return intvText;
	}

	public void setIntvText(String intvText) {
		this.intvText = intvText;
	}

	public String getIntvMergedText() {
		return intvMergedText;
	}

	public void setIntvMergedText(String intvMergedText) {
		this.intvMergedText = intvMergedText;
	}

}
