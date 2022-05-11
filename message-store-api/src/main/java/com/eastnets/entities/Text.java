package com.eastnets.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "RTEXT")
public class Text implements Serializable {

	private static final long serialVersionUID = -9070400619994537863L;

	@EmbeddedId
	private PKText textId;

	@Lob
	@Column(name = "TEXT_DATA_BLOCK")
	private String textDataBlock;

	@Column(name = "TEXT_DATA_BLOCK_LEN")
	private Integer textDataBlockLen=0;

	@Column(name = "TEXT_DATA_LAST")
	private BigDecimal textDataLast;

	@Column(name = "TEXT_SWIFT_BLOCK_5")
	private String textSwiftBlock5;

	@Column(name = "TEXT_SWIFT_BLOCK_U")
	private String textSwiftBlockU;

	@Column(name = "TEXT_SWIFT_PROMPTED")
	private String textSwiftPrompted;

	@Column(name = "TEXT_TOKEN")
	private BigDecimal textToken = BigDecimal.ZERO;

	@Column(name = "X_TEXT_CHECKSUM")
	private BigDecimal xTextChecksum = BigDecimal.ZERO;

	// @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	// @PrimaryKeyJoinColumns({ @PrimaryKeyJoinColumn(name = "AID",
	// referencedColumnName = "AID"),
	// @PrimaryKeyJoinColumn(name = "TEXT_S_UMIDH", referencedColumnName =
	// "MESG_S_UMIDH"),
	// @PrimaryKeyJoinColumn(name = "TEXT_S_UMIDL", referencedColumnName =
	// "MESG_S_UMIDL") })
	// private Mesg mesg;

	public String getTextDataBlock() {
		return textDataBlock;
	}

	public void setTextDataBlock(String textDataBlock) {
		this.textDataBlock = textDataBlock;
	}

	public Integer getTextDataBlockLen() {
		return textDataBlockLen;
	}

	public void setTextDataBlockLen(Integer textDataBlockLen) {
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

	public PKText getTextId() {
		return textId;
	}

	public void setTextId(PKText textId) {
		this.textId = textId;
	}

	@Override
	public String toString() {
		return "Text [textId=" + textId + ", textDataBlock=" + textDataBlock + ", textDataBlockLen=" + textDataBlockLen
				+ ", textDataLast=" + textDataLast + ", textSwiftBlock5=" + textSwiftBlock5 + ", textSwiftBlockU="
				+ textSwiftBlockU + ", textSwiftPrompted=" + textSwiftPrompted + ", textToken=" + textToken
				+ ", xTextChecksum=" + xTextChecksum + ", getTextDataBlock()=" + getTextDataBlock()
				+ ", getTextDataBlockLen()=" + getTextDataBlockLen() + ", getTextDataLast()=" + getTextDataLast()
				+ ", getTextSwiftBlock5()=" + getTextSwiftBlock5() + ", getTextSwiftBlockU()=" + getTextSwiftBlockU()
				+ ", getTextSwiftPrompted()=" + getTextSwiftPrompted() + ", getTextToken()=" + getTextToken()
				+ ", getxTextChecksum()=" + getxTextChecksum() + ", getTextId()=" + getTextId() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
