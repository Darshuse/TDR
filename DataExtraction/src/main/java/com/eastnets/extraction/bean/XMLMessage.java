package com.eastnets.extraction.bean;

public class XMLMessage {

	public enum XML_FETCH_RESULT {
		FETCH_SUCCESS, FETCH_FAILED, LARGE_SIZE
	}

	private Integer aid;
	private Integer mesgUmidl;
	private Integer mesgUmidh;
	private XML_FETCH_RESULT fetchResult;
	private String xmlContent;
	private Integer xmlContentSize;
	private XMLType xmlType;
	private Integer bulkDistinguisherId;

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	public XMLType getXmlType() {
		return xmlType;
	}

	public void setXmlType(XMLType xmlType) {
		this.xmlType = xmlType;
	}

	public Integer getXmlContentSize() {
		return xmlContentSize;
	}

	public void setXmlContentSize(Integer xmlContentSize) {
		this.xmlContentSize = xmlContentSize;
	}

	public XML_FETCH_RESULT getFetchResult() {
		return fetchResult;
	}

	public void setFetchResult(XML_FETCH_RESULT fetchResult) {
		this.fetchResult = fetchResult;
	}

	public Integer getBulkDistinguisherId() {
		return bulkDistinguisherId;
	}

	public void setBulkDistinguisherId(Integer bulkDistinguisherId) {
		this.bulkDistinguisherId = bulkDistinguisherId;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getMesgUmidl() {
		return mesgUmidl;
	}

	public void setMesgUmidl(Integer mesgUmidl) {
		this.mesgUmidl = mesgUmidl;
	}

	public Integer getMesgUmidh() {
		return mesgUmidh;
	}

	public void setMesgUmidh(Integer mesgUmidh) {
		this.mesgUmidh = mesgUmidh;
	}

}
