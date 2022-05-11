package com.eastnets.domain.loader;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "RXMLTEXT")
@NamedQuery(name = "XmlTextMessage.findAll", query = "SELECT x FROM XmlTextMessage x")
@Cacheable(value = false)
public class XmlTextMessage extends AbstractReportingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9190166993248279108L;

	@EmbeddedId
	private XmlTextPK xmlTextPk;

	@Column(name = "XMLTEXT_TOKEN")
	private BigDecimal textToken = BigDecimal.ZERO;

	@Column(name = "MESG_IDENTIFIER")
	private String msgIdentifier;

	@Column(name = "MESG_FRMT_NAME")
	private String msgFrmtName;

	@Column(name = "XMLTEXT_HEADER")
	private String msgHeader;

	@Column(name = "XMLTEXT_DATA")
	private String msgDocument;

	@Column(name = "XMLTEXT_SIZE")
	private BigDecimal msgSize = BigDecimal.ZERO;

	@Column(name = "BLK_FLAG")
	private Integer blkFlag;

	@OneToOne
	@JoinColumns({ @JoinColumn(name = "AID", referencedColumnName = "AID", insertable = false, updatable = false), @JoinColumn(name = "XMLTEXT_S_UMIDH", referencedColumnName = "MESG_S_UMIDH", insertable = false, updatable = false),
			@JoinColumn(name = "XMLTEXT_S_UMIDL", referencedColumnName = "MESG_S_UMIDL", insertable = false, updatable = false) })
	private Mesg xmlRmesg;

	@Transient
	private boolean msgBlkFlag;

	public XmlTextPK getXmlTextPk() {
		return xmlTextPk;
	}

	public void setXmlTextPk(XmlTextPK xmlTextPk) {
		this.xmlTextPk = xmlTextPk;
	}

	public BigDecimal getTextToken() {
		return textToken;
	}

	public void setTextToken(BigDecimal textToken) {
		this.textToken = textToken;
	}

	public BigDecimal getMsgSize() {
		return msgSize;
	}

	public void setMsgSize(BigDecimal msgSize) {
		this.msgSize = msgSize;
	}

	public String getMsgIdentifier() {
		return msgIdentifier;
	}

	public void setMsgIdentifier(String msgIdentifier) {
		this.msgIdentifier = msgIdentifier;
	}

	public String getMsgFrmtName() {
		return msgFrmtName;
	}

	public void setMsgFrmtName(String msgFrmtName) {
		this.msgFrmtName = msgFrmtName;
	}

	public String getMsgHeader() {
		return msgHeader;
	}

	public void setMsgHeader(String msgHeader) {
		this.msgHeader = msgHeader;
	}

	public String getMsgDocument() {
		return msgDocument;
	}

	public void setMsgDocument(String msgDocument) {
		this.msgDocument = msgDocument;
	}

	public Integer getBlkFlag() {
		return blkFlag;
	}

	public void setBlkFlag(Integer blkFlag) {
		this.blkFlag = blkFlag;
	}

	public Mesg getXmlRmesg() {
		return xmlRmesg;
	}

	public void setXmlRmesg(Mesg xmlRmesg) {
		this.xmlRmesg = xmlRmesg;
	}

	public boolean isMsgBlkFlag() {
		return msgBlkFlag;
	}

	public void setMsgBlkFlag(boolean msgBlkFlag) {
		this.msgBlkFlag = msgBlkFlag;
	}

}
