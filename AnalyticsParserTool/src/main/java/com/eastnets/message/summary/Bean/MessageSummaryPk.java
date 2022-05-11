package com.eastnets.message.summary.Bean;

public class MessageSummaryPk {

	private long aid;
	private long umidh;
	private long umidl;

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getUmidh() {
		return umidh;
	}

	public void setUmidh(long umidh) {
		this.umidh = umidh;
	}

	public long getUmidl() {
		return umidl;
	}

	public void setUmidl(long umidl) {
		this.umidl = umidl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (aid ^ (aid >>> 32));
		result = prime * result + (int) (umidh ^ (umidh >>> 32));
		result = prime * result + (int) (umidl ^ (umidl >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;

		MessageSummaryPk other = (MessageSummaryPk) obj;

		return !(aid != other.aid || umidh != other.umidh || umidl != other.umidl);

	}

}
