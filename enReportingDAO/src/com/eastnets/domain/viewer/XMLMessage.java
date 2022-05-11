package com.eastnets.domain.viewer;

import com.eastnets.domain.BaseEntity;

public class XMLMessage extends BaseEntity {

	private static final long serialVersionUID = 6396161531765201420L;

	public enum XML_FETCH_RESULT {
		FETCH_SUCCESS, FETCH_FAILED, LARGE_SIZE
	}

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

}
