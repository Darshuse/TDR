package com.eastnets.domain.loader;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RINST database table.
 * 
 */
@Embeddable
public class InstPK extends AbstractReportingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8345091215780147254L;

	@Column
	private long aid;

	@Column(name="INST_S_UMIDH")
	private long instSUmidh;

	@Column(name="INST_S_UMIDL")
	private long instSUmidl;

	@Column(name="INST_NUM")
	private long instNum = 0 ;

	public InstPK() {
	}
	public InstPK(int aid, int umidl, int umidh, int instnum) {
		this.aid = aid;
		instSUmidl = umidl;
		instSUmidh = umidh;
		instNum = instnum;
	}
	public long getAid() {
		return this.aid;
	}
	public void setAid(long aid) {
		this.aid = aid;
	}
	public long getInstSUmidh() {
		return this.instSUmidh;
	}
	public void setInstSUmidh(long instSUmidh) {
		this.instSUmidh = instSUmidh;
	}
	public long getInstSUmidl() {
		return this.instSUmidl;
	}
	public void setInstSUmidl(long instSUmidl) {
		this.instSUmidl = instSUmidl;
	}
	public long getInstNum() {
		return this.instNum;
	}
	public void setInstNum(long instNum) {
		this.instNum = instNum;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof InstPK)) {
			return false;
		}
		InstPK castOther = (InstPK)other;
		return 
			(this.aid == castOther.aid)
			&& (this.instSUmidh == castOther.instSUmidh)
			&& (this.instSUmidl == castOther.instSUmidl)
			&& (this.instNum == castOther.instNum);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.aid ^ (this.aid >>> 32)));
		hash = hash * prime + ((int) (this.instSUmidh ^ (this.instSUmidh >>> 32)));
		hash = hash * prime + ((int) (this.instSUmidl ^ (this.instSUmidl >>> 32)));
		hash = hash * prime + ((int) (this.instNum ^ (this.instNum >>> 32)));
		
		return hash;
	}
	@Override
	public String toString() {
		return "InstPK [aid=" + aid + ", instSUmidh=" + instSUmidh + ", instSUmidl=" + instSUmidl + ", instNum=" + instNum + "]";
	}
}