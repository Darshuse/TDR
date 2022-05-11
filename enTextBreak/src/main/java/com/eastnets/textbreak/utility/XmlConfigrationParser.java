package com.eastnets.textbreak.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory; 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eastnets.textbreak.bean.TextBreakConfig;

public class XmlConfigrationParser {

	private String filePath;
	private Document dom;
	TextBreakConfig textBreakConfig=new TextBreakConfig();


	public XmlConfigrationParser(String filePath) throws Exception { 
		this.filePath = filePath; 
		startParsing(); 
	}


	private void startParsing() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		dom = db.parse(filePath);
		parseDBConnectionFromDom();  
		parseTextBreakFromDom();
		parseActiveMQFromDom();
	}
	
	
	private void parseDBConnectionFromDom() throws Exception { 
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("DBConfig");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				updateXMLDataSourceWithDBConnection(el);
			}
		}
	}


	private void parseTextBreakFromDom() throws Exception { 
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("TextBreakConfig");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				updateXMLDataSourceWithTextBreakConnetion(el);
			}
		}
	}


	private void parseActiveMQFromDom() throws Exception { 
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("activeMQConfig");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				updateXMLDataSourceWithActiveMq(el);
			}
		}
	}

	private void updateXMLDataSourceWithDBConnection(Element el) throws Exception {

		textBreakConfig.setUsername(getTextValue(el, "username"));
		textBreakConfig.setPassword(getTextValue(el, "password"));
		textBreakConfig.setServerName(getTextValue(el, "ip"));
		textBreakConfig.setDatabaseName(getTextValue(el, "dbname"));
		textBreakConfig.setDbType(getTextValue(el, "dbtype"));
		textBreakConfig.setDbServiceName( getTextValue(el, "servicename"));
		textBreakConfig.setInstanceName(getTextValue(el, "instancename"));
		textBreakConfig.setPortNumber(getTextValue(el, "port"));
		textBreakConfig.setPartitioned(Boolean.parseBoolean(getTextValue(el, "partitioned")));   
	}


	private void updateXMLDataSourceWithTextBreakConnetion(Element el) throws Exception {

		textBreakConfig.setMessageNumber(getTextValue(el, "messageNumber"));
		textBreakConfig.setDayNumber(getTextValue(el, "dayNumber"));
		textBreakConfig.setFromDate(getTextValue(el, "fromDate"));
		textBreakConfig.setToDate(getTextValue(el, "toDate"));
		textBreakConfig.setAllMessages(getTextValue(el, "allMessage"));
		textBreakConfig.setEnableDebug(Boolean.parseBoolean(getTextValue(el, "enableDebug")));


	}



	private void updateXMLDataSourceWithActiveMq(Element el) throws Exception {

		textBreakConfig.setApacheMqServerIp(getTextValue(el, "mqServerIp"));
		textBreakConfig.setApacheMqServerPort(getTextValue(el, "mqServerPort"));



	}

	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			Node firstChild = el.getFirstChild();
			if (firstChild != null) {
				textVal = firstChild.getNodeValue();
			} else {
				textVal = "";
			}
		}

		return textVal;
	}


}
