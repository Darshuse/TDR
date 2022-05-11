package com.eastnets.domain.loader;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the RTEXT database table.
 * 
 */
@Entity
@Table(name = "RTEXT")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

@NamedQuery(name = "Text.findAll", query = "SELECT t FROM Text t")
@Cacheable(value = false)
public class Text extends AbstractReportingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5499643605533808806L;

	@EmbeddedId
	private TextPK id;

	@Lob
	@Column(name = "TEXT_DATA_BLOCK")
	private String textDataBlock;

	@Column(name = "TEXT_DATA_BLOCK_LEN")
	private BigDecimal textDataBlockLen;

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

	@Column(name = "decomposed")
	private Integer decomposed = 0;

	// bi-directional one-to-one association to Mesg
	@OneToOne
	@JoinColumns({ @JoinColumn(name = "AID", referencedColumnName = "AID", insertable = false, updatable = false), @JoinColumn(name = "TEXT_S_UMIDH", referencedColumnName = "MESG_S_UMIDH", insertable = false, updatable = false),
			@JoinColumn(name = "TEXT_S_UMIDL", referencedColumnName = "MESG_S_UMIDL", insertable = false, updatable = false) })
	private Mesg rmesg;

	public Text() {
	}

	public TextPK getId() {
		return this.id;
	}

	public void setId(TextPK id) {
		this.id = id;
	}

	public String getTextDataBlock() {
		return this.textDataBlock;
	}

	public void setTextDataBlock(String textDataBlock) {
		this.textDataBlock = textDataBlock;
	}

	public BigDecimal getTextDataBlockLen() {
		return this.textDataBlockLen;
	}

	public void setTextDataBlockLen(BigDecimal textDataBlockLen) {
		this.textDataBlockLen = textDataBlockLen;
	}

	public BigDecimal getTextDataLast() {
		return this.textDataLast;
	}

	public void setTextDataLast(BigDecimal textDataLast) {
		this.textDataLast = textDataLast;
	}

	public String getTextSwiftBlock5() {
		return this.textSwiftBlock5;
	}

	public void setTextSwiftBlock5(String textSwiftBlock5) {
		this.textSwiftBlock5 = textSwiftBlock5;
	}

	public String getTextSwiftBlockU() {
		return this.textSwiftBlockU;
	}

	public void setTextSwiftBlockU(String textSwiftBlockU) {
		this.textSwiftBlockU = textSwiftBlockU;
	}

	public String getTextSwiftPrompted() {
		return this.textSwiftPrompted;
	}

	public void setTextSwiftPrompted(String textSwiftPrompted) {
		this.textSwiftPrompted = textSwiftPrompted;
	}

	public BigDecimal getTextToken() {
		return this.textToken;
	}

	public void setTextToken(BigDecimal textToken) {
		this.textToken = textToken;
	}

	public BigDecimal getXTextChecksum() {
		return this.xTextChecksum;
	}

	public void setXTextChecksum(BigDecimal xTextChecksum) {
		this.xTextChecksum = xTextChecksum;
	}

	public Mesg getRmesg() {
		return this.rmesg;
	}

	public void setRmesg(Mesg rmesg) {
		this.rmesg = rmesg;
	}

	@Override
	public String toString() {
		return "Text [id=" + id + ", textDataBlock=" + textDataBlock + ", textDataBlockLen=" + textDataBlockLen + ", textDataLast=" + textDataLast + ", textSwiftBlock5=" + textSwiftBlock5 + ", textSwiftBlockU=" + textSwiftBlockU
				+ ", textSwiftPrompted=" + textSwiftPrompted + ", textToken=" + textToken + ", xTextChecksum=" + xTextChecksum + ", rmesg=" + rmesg + "]";
	}

}