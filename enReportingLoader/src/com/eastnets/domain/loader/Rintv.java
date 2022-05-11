package com.eastnets.domain.loader;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "RINTV")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQuery(name = "Rintv.findAll", query = "SELECT r FROM Rintv r")
@Cacheable(value = false)
public class Rintv  extends AbstractReportingEntity implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private RintvPK id;
	@Column(name = "INTV_INTY_NUM")
	private long intvIntyNum=20;
	@Column(name = "INTV_INTY_NAME")
	private String intvIntyName="Instance created";
	@Column(name = "INTV_INTY_CATEGORY")
	private String intvIntyCategory="INTY_ROUTING";
	@Column(name = "INTV_OPER_NICKNAME")
	private String intvOperNickName="SYSTEM";
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INTV_APPE_DATE_TIME")
	private Date intvAppeDateTime;
	@Column(name = "INTV_APPE_SEQ_NBR")
	private long intvAppSeqNbr=0;
	@Column(name = "INTV_LENGTH")
	private long intvLength=71;
	@Column(name = "INTV_TOKEN")
	private long intvToken=0;
	@Column(name = "INTV_APPL_SERV_NAME")
	private String intvApplServName="Mesg Creation";
	@Column(name = "INTV_MPFN_NAME")
	private String intvMpfnName="mpc";
	@Column(name = "INTV_TEXT")
	private String intvText;
	@Column(name = "INTV_MERGED_TEXT")
	private String intvMergedText; 

	//bi-directional many-to-one association to Inst
	    @ManyToOne
	    @JoinColumns({
		@JoinColumn(name="AID", referencedColumnName="AID",insertable=false, updatable=false),
		@JoinColumn(name="INTV_INST_NUM", referencedColumnName="INST_NUM",insertable=false, updatable=false),
		@JoinColumn(name="INTV_S_UMIDH", referencedColumnName="INST_S_UMIDH",insertable=false, updatable=false),
		@JoinColumn(name="INTV_S_UMIDL", referencedColumnName="INST_S_UMIDL",insertable=false, updatable=false)
		})
	private Inst rinst;

public Rintv() {
 
}

public Inst getRinst() {
	return rinst;
}
public void setRinst(Inst rinst) {
	this.rinst = rinst;
}
	public long getIntvIntyNum() {
		return intvIntyNum;
	}
	public void setIntvIntyNum(long intvIntyNum) {
		this.intvIntyNum = intvIntyNum;
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
	public String getIntvOperNickName() {
		return intvOperNickName;
	}
	public void setIntvOperNickName(String intvOperNickName) {
		this.intvOperNickName = intvOperNickName;
	}
	public Date getIntvAppeDateTime() {
		return intvAppeDateTime;
	}
	public void setIntvAppeDateTime(Date intvAppeDateTime) {
		this.intvAppeDateTime = intvAppeDateTime;
	}
	public long getIntvAppSeqNbr() {
		return intvAppSeqNbr;
	}
	public void setIntvAppSeqNbr(long intvAppSeqNbr) {
		this.intvAppSeqNbr = intvAppSeqNbr;
	}
	public long getIntvLength() {
		return intvLength;
	}
	public void setIntvLength(long intvLength) {
		this.intvLength = intvLength;
	}
	public long getIntvToken() {
		return intvToken;
	}
	public void setIntvToken(long intvToken) {
		this.intvToken = intvToken;
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

	public RintvPK getId() {
		return id;
	}
	public void setId(RintvPK id) {
		this.id = id;
	}
	
 
	
	@Override
	public String toString() {
		return "Rintv [id=" + id + ", intvIntyNum=" + intvIntyNum + ", intvIntyName=" + intvIntyName + ", intvIntyCategory="
				+ intvIntyCategory + ", intvOperNickName=" + intvOperNickName + ", intvAppeDateTime=" + intvAppeDateTime + ", intvAppSeqNbr="
				+ intvAppSeqNbr + ", intvLength=" + intvLength + ", intvToken=" + intvToken
				+ ", intvApplServName=" + intvApplServName + ", intvApplServName=" + intvApplServName + ", intvMpfnName="
				+ intvMpfnName + ", intvText=" + intvText + ", xCreaDateTimeMesg=" + ""
				+ ", intvMergedText=" + intvMergedText  + ", rinst=" + rinst + "]";
	}

}
