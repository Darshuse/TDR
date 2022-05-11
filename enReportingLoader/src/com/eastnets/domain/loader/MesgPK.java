package com.eastnets.domain.loader;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RMESG database table.
 * 
 */
@Embeddable
public class MesgPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7900502751936082702L;

	private long aid;

	@Column(name="MESG_S_UMIDH")
	private long mesgSUmidh;

	@Column(name="MESG_S_UMIDL")
	private long mesgSUmidl;

	public MesgPK() {
	}
	public long getAid() {
		return this.aid;
	}
	public void setAid(long aid) {
		this.aid = aid;
	}
	public long getMesgSUmidh() {
		return this.mesgSUmidh;
	}
	public void setMesgSUmidh(long mesgSUmidh) {
		this.mesgSUmidh = mesgSUmidh;
	}
	public long getMesgSUmidl() {
		return this.mesgSUmidl;
	}
	public void setMesgSUmidl(long mesgSUmidl) {
		this.mesgSUmidl = mesgSUmidl;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MesgPK)) {
			return false;
		}
		MesgPK castOther = (MesgPK)other;
		return 
			(this.aid == castOther.aid)
			&& (this.mesgSUmidh == castOther.mesgSUmidh)
			&& (this.mesgSUmidl == castOther.mesgSUmidl);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.aid ^ (this.aid >>> 32)));
		hash = hash * prime + ((int) (this.mesgSUmidh ^ (this.mesgSUmidh >>> 32)));
		hash = hash * prime + ((int) (this.mesgSUmidl ^ (this.mesgSUmidl >>> 32)));
		
		return hash;
	}
	@Override
	public String toString() {
		return "MesgPK [aid=" + aid + ", mesgSUmidh=" + mesgSUmidh + ", mesgSUmidl=" + mesgSUmidl + "]";
	}
	
}