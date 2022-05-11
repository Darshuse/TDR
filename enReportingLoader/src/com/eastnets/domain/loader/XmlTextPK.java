package com.eastnets.domain.loader;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class XmlTextPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1332380551432451890L;

	@Column(name = "AID")
	private long aid;

	@Column(name = "XMLTEXT_S_UMIDL")
	private long textSUMIDL;

	@Column(name = "XMLTEXT_S_UMIDH")
	private long textSUMIDH;

	@Column(name = "XMLTEXT_MESG_ORDER")
	private long textMsgOrder;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MESG_CREA_DATE_TIME")
	private Date createDate;

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getTextSUMIDL() {
		return textSUMIDL;
	}

	public void setTextSUMIDL(long textSUMIDL) {
		this.textSUMIDL = textSUMIDL;
	}

	public long getTextSUMIDH() {
		return textSUMIDH;
	}

	public void setTextSUMIDH(long textSUMIDH) {
		this.textSUMIDH = textSUMIDH;
	}

	public long getTextMsgOrder() {
		return textMsgOrder;
	}

	public void setTextMsgOrder(long textMsgOrder) {
		this.textMsgOrder = textMsgOrder;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (aid ^ (aid >>> 32));
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + (int) (textMsgOrder ^ (textMsgOrder >>> 32));
		result = prime * result + (int) (textSUMIDH ^ (textSUMIDH >>> 32));
		result = prime * result + (int) (textSUMIDL ^ (textSUMIDL >>> 32));
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
		XmlTextPK other = (XmlTextPK) obj;
		if (aid != other.aid)
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (textMsgOrder != other.textMsgOrder)
			return false;
		if (textSUMIDH != other.textSUMIDH)
			return false;
		if (textSUMIDL != other.textSUMIDL)
			return false;
		return true;
	}

}
