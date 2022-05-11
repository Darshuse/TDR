package com.eastnets.domain.loader;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RTEXT database table.
 * 
 */
@Embeddable
public class TextPK extends AbstractReportingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3588053934055595194L;

	@Column
	private long aid;

	@Column(name="TEXT_S_UMIDH")
	private long textSUmidh;

	@Column(name="TEXT_S_UMIDL")
	private long textSUmidl;

	public TextPK() {
	}
	public TextPK(int i, int j, int k) {
		aid = i;
		textSUmidh = j;
		textSUmidl = k;
	}
	public long getAid() {
		return this.aid;
	}
	public void setAid(long aid) {
		this.aid = aid;
	}
	public long getTextSUmidh() {
		return this.textSUmidh;
	}
	public void setTextSUmidh(long textSUmidh) {
		this.textSUmidh = textSUmidh;
	}
	public long getTextSUmidl() {
		return this.textSUmidl;
	}
	public void setTextSUmidl(long textSUmidl) {
		this.textSUmidl = textSUmidl;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TextPK)) {
			return false;
		}
		TextPK castOther = (TextPK)other;
		return 
			(this.aid == castOther.aid)
			&& (this.textSUmidh == castOther.textSUmidh)
			&& (this.textSUmidl == castOther.textSUmidl);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.aid ^ (this.aid >>> 32)));
		hash = hash * prime + ((int) (this.textSUmidh ^ (this.textSUmidh >>> 32)));
		hash = hash * prime + ((int) (this.textSUmidl ^ (this.textSUmidl >>> 32)));
		
		return hash;
	}
	@Override
	public String toString() {
		return "TextPK [aid=" + aid + ", textSUmidh=" + textSUmidh + ", textSUmidl=" + textSUmidl + "]";
	}
	
}