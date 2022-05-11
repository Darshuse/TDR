package com.eastnets.textbreak.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MesgPK implements Serializable {

	private static final long serialVersionUID = -3187000035816940651L;
	@Column(name = "AID")
	private Integer aid;
	@Column(name = "MESG_S_UMIDL")
	private Integer umidl;
	@Column(name = "MESG_S_UMIDH")
	private Integer umidh;

	public MesgPK() {
	}

	public MesgPK(Integer aid, Integer umidl, Integer umidh) {
		super();
		this.aid = aid;
		this.umidl = umidl;
		this.umidh = umidh;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getUmidl() {
		return umidl;
	}

	public void setUmidl(Integer umidl) {
		this.umidl = umidl;
	}

	public Integer getUmidh() {
		return umidh;
	}

	public void setUmidh(Integer umidh) {
		this.umidh = umidh;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aid == null) ? 0 : aid.hashCode());
		result = prime * result + ((umidh == null) ? 0 : umidh.hashCode());
		result = prime * result + ((umidl == null) ? 0 : umidl.hashCode());
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
		MesgPK other = (MesgPK) obj;
		if (aid == null) {
			if (other.aid != null)
				return false;
		} else if (!aid.equals(other.aid))
			return false;
		if (umidh == null) {
			if (other.umidh != null)
				return false;
		} else if (!umidh.equals(other.umidh))
			return false;
		if (umidl == null) {
			if (other.umidl != null)
				return false;
		} else if (!umidl.equals(other.umidl))
			return false;
		return true;
	}

}
