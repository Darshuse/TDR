package com.eastnets.extraction.bean;

import java.util.List;

public class XmlExportResult {

	private List<String> xmlMessage;
	private int missingXMLContentCount;

	public List<String> getXmlMessage() {
		return xmlMessage;
	}

	public void setXmlMessage(List<String> xmlMessage) {
		this.xmlMessage = xmlMessage;
	}

	public int getMissingXMLContentCount() {
		return missingXMLContentCount;
	}

	public void setMissingXMLContentCount(int missingXMLContentCount) {
		this.missingXMLContentCount = missingXMLContentCount;
	}

}
