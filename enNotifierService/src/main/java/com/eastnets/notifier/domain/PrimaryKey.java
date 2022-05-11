
package com.eastnets.notifier.domain;

import java.util.Comparator;

public class PrimaryKey implements Comparable<PrimaryKey> {

	private Integer aid;
	private String umidh;
	private String umidl;

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public String getUmidh() {
		return umidh;
	}

	public void setUmidh(String umidh) {
		this.umidh = umidh;
	}

	public String getUmidl() {
		return umidl;
	}

	public void setUmidl(String umidl) {
		this.umidl = umidl;
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
		PrimaryKey other = (PrimaryKey) obj;
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

	@Override
	public String toString() {
		return "(" + aid + "," + umidh + ',' + umidl + ')';
	}

	@Override
	public int compareTo(PrimaryKey o) {
		return Comparator.comparing(PrimaryKey::getAid).thenComparing(PrimaryKey::getUmidl).thenComparing(PrimaryKey::getUmidh).compare(this, o);
	}

}
