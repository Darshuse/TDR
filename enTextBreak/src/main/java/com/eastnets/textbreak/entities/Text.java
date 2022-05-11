
package com.eastnets.textbreak.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

//@Entity
//@Table(name = "RTEXT")
public class Text implements Serializable {

	private static final long serialVersionUID = -9070400619994537863L;

	// @EmbeddedId
	private TextPK id;

	// @Lob
	// @Column(name = "TEXT_DATA_BLOCK")
	private String textDataBlock;

	// @Column(name = "TEXT_DATA_BLOCK_LEN")
	private BigDecimal textDataBlockLen;

	// @Column(name = "TEXT_DATA_LAST")
	private BigDecimal textDataLast;

	// @Column(name = "TEXT_SWIFT_BLOCK_5")
	private String textSwiftBlock5;

	// @Column(name = "TEXT_SWIFT_BLOCK_U")
	private String textSwiftBlockU;

	// @Column(name = "TEXT_SWIFT_PROMPTED")
	private String textSwiftPrompted;

	// @Column(name = "TEXT_TOKEN")
	private BigDecimal textToken = BigDecimal.ZERO;

	// @Column(name = "X_TEXT_CHECKSUM")
	private BigDecimal xTextChecksum = BigDecimal.ZERO;

	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "X_CREA_DATE_TIME_MESG", nullable = true)
	private Date mesgCreaDateTime;

	// @Column(name = "decomposed")
	private Integer decomposed;

	public Integer getDecomposed() {
		return decomposed;
	}

	public void setDecomposed(Integer decomposed) {
		this.decomposed = decomposed;
	}

	public Text() {
	}

	public TextPK getId() {
		return id;
	}

	public void setId(TextPK id) {
		this.id = id;
	}

	public String getTextDataBlock() {
		return textDataBlock;
	}

	public void setTextDataBlock(String textDataBlock) {
		this.textDataBlock = textDataBlock;
	}

	public BigDecimal getTextDataBlockLen() {
		return textDataBlockLen;
	}

	public void setTextDataBlockLen(BigDecimal textDataBlockLen) {
		this.textDataBlockLen = textDataBlockLen;
	}

	public BigDecimal getTextDataLast() {
		return textDataLast;
	}

	public void setTextDataLast(BigDecimal textDataLast) {
		this.textDataLast = textDataLast;
	}

	public String getTextSwiftBlock5() {
		return textSwiftBlock5;
	}

	public void setTextSwiftBlock5(String textSwiftBlock5) {
		this.textSwiftBlock5 = textSwiftBlock5;
	}

	public String getTextSwiftBlockU() {
		return textSwiftBlockU;
	}

	public void setTextSwiftBlockU(String textSwiftBlockU) {
		this.textSwiftBlockU = textSwiftBlockU;
	}

	public String getTextSwiftPrompted() {
		return textSwiftPrompted;
	}

	public void setTextSwiftPrompted(String textSwiftPrompted) {
		this.textSwiftPrompted = textSwiftPrompted;
	}

	public BigDecimal getTextToken() {
		return textToken;
	}

	public void setTextToken(BigDecimal textToken) {
		this.textToken = textToken;
	}

	public BigDecimal getxTextChecksum() {
		return xTextChecksum;
	}

	public void setxTextChecksum(BigDecimal xTextChecksum) {
		this.xTextChecksum = xTextChecksum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((textDataBlock == null) ? 0 : textDataBlock.hashCode());
		result = prime * result + ((textDataBlockLen == null) ? 0 : textDataBlockLen.hashCode());
		result = prime * result + ((textDataLast == null) ? 0 : textDataLast.hashCode());
		result = prime * result + ((textSwiftBlock5 == null) ? 0 : textSwiftBlock5.hashCode());
		result = prime * result + ((textSwiftBlockU == null) ? 0 : textSwiftBlockU.hashCode());
		result = prime * result + ((textSwiftPrompted == null) ? 0 : textSwiftPrompted.hashCode());
		result = prime * result + ((textToken == null) ? 0 : textToken.hashCode());
		result = prime * result + ((xTextChecksum == null) ? 0 : xTextChecksum.hashCode());
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
		Text other = (Text) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;

		if (textDataBlock == null) {
			if (other.textDataBlock != null)
				return false;
		} else if (!textDataBlock.equals(other.textDataBlock))
			return false;
		if (textDataBlockLen == null) {
			if (other.textDataBlockLen != null)
				return false;
		} else if (!textDataBlockLen.equals(other.textDataBlockLen))
			return false;
		if (textDataLast == null) {
			if (other.textDataLast != null)
				return false;
		} else if (!textDataLast.equals(other.textDataLast))
			return false;
		if (textSwiftBlock5 == null) {
			if (other.textSwiftBlock5 != null)
				return false;
		} else if (!textSwiftBlock5.equals(other.textSwiftBlock5))
			return false;
		if (textSwiftBlockU == null) {
			if (other.textSwiftBlockU != null)
				return false;
		} else if (!textSwiftBlockU.equals(other.textSwiftBlockU))
			return false;
		if (textSwiftPrompted == null) {
			if (other.textSwiftPrompted != null)
				return false;
		} else if (!textSwiftPrompted.equals(other.textSwiftPrompted))
			return false;
		if (textToken == null) {
			if (other.textToken != null)
				return false;
		} else if (!textToken.equals(other.textToken))
			return false;
		if (xTextChecksum == null) {
			if (other.xTextChecksum != null)
				return false;
		} else if (!xTextChecksum.equals(other.xTextChecksum))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Text [id=" + id + ", textDataBlock=" + textDataBlock + ", textDataBlockLen=" + textDataBlockLen + ", textDataLast=" + textDataLast + ", textSwiftBlock5=" + textSwiftBlock5 + ", textSwiftBlockU=" + textSwiftBlockU
				+ ", textSwiftPrompted=" + textSwiftPrompted + ", textToken=" + textToken + ", xTextChecksum=" + xTextChecksum + ", message=" + "" + "]";
	}

}
