package com.eastnets.textbreak.entities;

import java.io.Serializable;

//@Embeddable
public class TextPK implements Serializable {

	private static final long serialVersionUID = -6299196265296204118L;

	// @Column
	private long aid;

	// @Column(name = "TEXT_S_UMIDH")
	private long textSUmidh;

	// @Column(name = "TEXT_S_UMIDL")
	private long textSUmidl;

	public TextPK() {
	}

	public TextPK(long aid, long textSUmidh, long textSUmidl) {
		super();
		this.aid = aid;
		this.textSUmidh = textSUmidh;
		this.textSUmidl = textSUmidl;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (aid ^ (aid >>> 32));
		result = prime * result + (int) (textSUmidh ^ (textSUmidh >>> 32));
		result = prime * result + (int) (textSUmidl ^ (textSUmidl >>> 32));
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
		TextPK other = (TextPK) obj;
		if (aid != other.aid)
			return false;
		if (textSUmidh != other.textSUmidh)
			return false;
		if (textSUmidl != other.textSUmidl)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TextPK [aid=" + aid + ", textSUmidh=" + textSUmidh + ", textSUmidl=" + textSUmidl + "]";
	}

}
