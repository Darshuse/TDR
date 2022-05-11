package com.eastnets.domain.loader;


import java.io.Serializable;
import java.util.Date; 
import javax.persistence.*;

/**
 * The primary key class for the RintvPK database table.
 * 
 */
@Embeddable 
public class RintvPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "AID")
	private long aid ;

	@Column(name = "INTV_S_UMIDL")
	private long iNTVSUMIDL ;

	@Column(name = "INTV_S_UMIDH")
	private long iNTVSUMIDH ;

	@Column(name = "INTV_INST_NUM")
	private long intvInstNum=0 ;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INTV_DATE_TIME")
	private Date intvDateTime;

	@Column(name = "INTV_SEQ_NBR")
	private long intvSeqNbr=0; 
	
	public RintvPK() {
		// TODO Auto-generated constructor stub
	}

	public long getAid() {
		return aid;
	}
	public void setAid(long aid) {
		this.aid = aid;
	}
	public long getiNTVSUMIDL() {
		return iNTVSUMIDL;
	}
	public void setiNTVSUMIDL(long iNTVSUMIDL) {
		this.iNTVSUMIDL = iNTVSUMIDL;
	}
	public long getiNTVSUMIDH() {
		return iNTVSUMIDH;
	}
	public void setiNTVSUMIDH(long iNTVSUMIDH) {
		this.iNTVSUMIDH = iNTVSUMIDH;
	}
	public long getIntvInstNum() {
		return intvInstNum;
	}
	public void setIntvInstNum(long intvInstNum) {
		this.intvInstNum = intvInstNum;
	}
	public Date getIntvDateTime() {
		return intvDateTime;
	}
	public void setIntvDateTime(Date intvDateTime) {
		this.intvDateTime = intvDateTime;
	}
	public long getIntvSeqNbr() {
		return intvSeqNbr;
	}
	public void setIntvSeqNbr(long intvSeqNbr) {
		this.intvSeqNbr = intvSeqNbr;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RintvPK)) {
			return false;
		}
		RintvPK castOther = (RintvPK)other;
		return 
				(this.aid == castOther.aid)
				&& (this.iNTVSUMIDL == castOther.iNTVSUMIDL)
				&& (this.iNTVSUMIDH == castOther.iNTVSUMIDH)
				&& (this.intvInstNum == castOther.intvInstNum) 
				&& (this.intvDateTime == castOther.intvDateTime)
				&& (this.intvSeqNbr == castOther.intvSeqNbr)
				;
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.aid ^ (this.aid >>> 32)));
		hash = hash * prime + ((int) (this.iNTVSUMIDH ^ (this.iNTVSUMIDH >>> 32)));
		hash = hash * prime + ((int) (this.iNTVSUMIDL ^ (this.iNTVSUMIDL >>> 32)));
		hash = hash * prime + ((int) (this.intvInstNum ^ (this.intvInstNum >>> 32)));
		hash = hash * prime + this.intvDateTime.hashCode(); 
		hash = hash * prime + ((int) (this.intvSeqNbr ^ (this.intvSeqNbr >>> 32)));


		return hash;
	}
	@Override
	public String toString() {
		return "RintvPK [aid=" + aid + ", mesgSUmidh=" + iNTVSUMIDH + ", mesgSUmidl=" + iNTVSUMIDL + "]";
	}

}
