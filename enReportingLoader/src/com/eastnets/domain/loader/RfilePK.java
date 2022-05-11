package com.eastnets.domain.loader;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RfilePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6653671510189719419L;

	@Column(name = "AID")
	private long aid;

	@Column(name = "FILE_S_UMIDL")
	private long fileSUmidl;

	@Column(name = "FILE_S_UMIDH")
	private long fileSUmidh;

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getFileSUmidl() {
		return fileSUmidl;
	}

	public void setFileSUmidl(long fileSUmidl) {
		this.fileSUmidl = fileSUmidl;
	}

	public long getFileSUmidh() {
		return fileSUmidh;
	}

	public void setFileSUmidh(long fileSUmidh) {
		this.fileSUmidh = fileSUmidh;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (aid ^ (aid >>> 32));
		result = prime * result + (int) (fileSUmidh ^ (fileSUmidh >>> 32));
		result = prime * result + (int) (fileSUmidl ^ (fileSUmidl >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RfilePK other = (RfilePK) obj;
		if (aid != other.aid)
			return false;
		if (fileSUmidh != other.fileSUmidh)
			return false;
		if (fileSUmidl != other.fileSUmidl)
			return false;
		return true;
	}

}
