package com.eastnets.service.loader.helper;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DataSourceParser {

	private String filePath;
	private Document dom;
	private XMLDataSouceHelper xmlDataSource = new XMLDataSouceHelper();
	private Map<String, XMLQueryHelper> xmlQueryHelperMap;
	private static final Logger LOGGER = Logger.getLogger(DataSourceParser.class);

	public DataSourceParser(String filePath) throws Exception {
		LOGGER.info("Start parsing input data source details");
		this.filePath = filePath;
		xmlQueryHelperMap = new HashMap<String, XMLQueryHelper>();
		startParsing();
		LOGGER.info("End parsing input data source details");
	}

	public Map<String, XMLQueryHelper> getXmlQueryHelperMap() {
		return xmlQueryHelperMap;
	}

	public void setXmlQueryHelperMap(Map<String, XMLQueryHelper> xmlQueryHelperMap) {
		this.xmlQueryHelperMap = xmlQueryHelperMap;
	}

	public XMLDataSouceHelper getXmlDataSource() {
		return xmlDataSource;
	}

	private void startParsing() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		dom = db.parse(filePath);
		parseDBConnectionFromDom();
		parseIbmMqConnectionFromDom();
		parseApacheMqConnectionFromDom();
		parseQueryFromDom();
	}

	private void parseQueryFromDom() throws Exception {
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("queryconfig");
		XMLQueryHelper xmlQueryHelper = null;
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				xmlQueryHelper = getQueryHelper(el);
				xmlQueryHelperMap.put(xmlQueryHelper.getQueryName(), xmlQueryHelper);
			}
		}
	}

	private XMLQueryHelper getQueryHelper(Element el) throws Exception {
		String tableName = getTextValue(el, "tablename");
		String seqNumb = getTextValue(el, "sequencenumber");
		String txtCol = getTextValue(el, "textcolum");
		String historyCol = getTextValue(el, "historycolum");
		String mesgType = getTextValue(el, "messagetype");
		String queryName = getTextValue(el, "queryname");
		if (tableName == null || seqNumb == null || txtCol == null || mesgType == null || queryName == null) {
			throw new RuntimeException("Error in Query Helper, One or more Paramter(s) missing ");
		}

		String unitName = getTextValue(el, "unitname");
		String creationDate = getTextValue(el, "creationdate");
		String creationOperation = getTextValue(el, "creationoperator");

		String where = getTextValue(el, "wherestatment");
		return new XMLQueryHelper(queryName, tableName, seqNumb, txtCol, historyCol, mesgType, where, unitName, creationDate, creationOperation);
	}

	private void parseIbmMqConnectionFromDom() throws Exception {
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("ibmMqconnection");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				updateXMLDataSourceWithIbmMqConnection(el);
			}
		}
	}

	private void parseApacheMqConnectionFromDom() throws Exception {
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("apacheMqconnection");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				updateXMLDataSourceWithApacheMqConnection(el);
			}
		}
	}

	private void parseDBConnectionFromDom() throws Exception {
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("dbconnection");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				updateXMLDataSourceWithDBConnection(el);
			}
		}
	}

	private void updateXMLDataSourceWithDBConnection(Element el) throws Exception {

		String ip = getTextValue(el, "ip");
		String dbType = getTextValue(el, "dbtype");
		String dbName = getTextValue(el, "dbname");
		String serviceName = getTextValue(el, "servicename");
		String port = getTextValue(el, "port");
		try {
			Integer.parseInt(port);
		} catch (Exception e) {
			throw new RuntimeException("Error in port number");
		}
		String username = getTextValue(el, "username");
		String password = getTextValue(el, "password");
		int bulkSize = Integer.parseInt(getTextValue(el, "bulksize"));

		xmlDataSource.setDbIntegrationEnabled(true);
		xmlDataSource.initDBConfig(ip, dbType, dbName, serviceName, port, username, password, bulkSize);
	}

	private void updateXMLDataSourceWithIbmMqConnection(Element el) throws Exception {

		String ip = getTextValue(el, "ip");
		String username = getTextValue(el, "username");
		String password = getTextValue(el, "password");
		String channel = getTextValue(el, "channel");
		String queueManager = getTextValue(el, "queueManager");
		String inputQueueName = getTextValue(el, "inputQueueName");
		String errorQueueName = getTextValue(el, "errorQueueName");
		if (errorQueueName == null) {
			errorQueueName = "null";
			xmlDataSource.setUseErrorQueue(false);
		}
		String formatType = getTextValue(el, "formatType");
		String port = "";
		int bulkSize = 0;
		try {
			port = getTextValue(el, "port");
			Integer.parseInt(port);
		} catch (Exception e) {
			throw new RuntimeException("port must be numeric");
		}

		String sslKeyStoreType = empatyIfNull(getTextValue(el, "sslKeyStoreType"));
		String sslKeyStore = empatyIfNull(getTextValue(el, "sslKeyStore"));
		String sslKeyStorePassword = empatyIfNull(getTextValue(el, "sslKeyStorePassword"));
		String sslTrustStoreType = empatyIfNull(getTextValue(el, "sslTrustStoreType"));
		String sslTrustStore = empatyIfNull(getTextValue(el, "sslTrustStore"));
		String sslTrustStorePassword = empatyIfNull(getTextValue(el, "sslTrustStorePassword"));
		String sslCipherSuite = empatyIfNull(getTextValue(el, "sslCipherSuite"));
		String sslPeerName = empatyIfNull(getTextValue(el, "sslPeerName"));
		String SSLCipherSpec = empatyIfNull(getTextValue(el, "SSLCipherSpec"));
		String useIBMCipherMappings = empatyIfNull(getTextValue(el, "useIBMCipherMappings"));
		boolean noAuthentication = Boolean.parseBoolean(getTextValue(el, "noAuthentication"));
		boolean enableFIPS = Boolean.parseBoolean(getTextValue(el, "enableFIPS"));
		boolean appId = Boolean.parseBoolean(getTextValue(el, "Appid"));

		xmlDataSource.setIbmMQIntegrationEnabled(true);
		xmlDataSource.initIbmMqConfig(ip, "ibm", username, password, port, channel, queueManager, inputQueueName, errorQueueName, bulkSize, formatType, sslKeyStoreType, sslKeyStore, sslKeyStorePassword, sslTrustStoreType, sslTrustStore,
				sslTrustStorePassword, sslCipherSuite, sslPeerName, SSLCipherSpec, useIBMCipherMappings, noAuthentication, enableFIPS, appId);
	}

	private String empatyIfNull(String value) {
		if (value == null)
			return "";

		return value;

	}

	private void updateXMLDataSourceWithApacheMqConnection(Element el) throws Exception {

		String ip = getTextValue(el, "ip");

		if (ip == null || ip.isEmpty()) {
			return;
		}
		String inputQueueName = getTextValue(el, "inputQueueName");
		String errorQueueName = getTextValue(el, "errorQueueName");
		if (errorQueueName == null) {
			errorQueueName = "null";
			xmlDataSource.setUseErrorQueue(false);
		}
		String formatType = getTextValue(el, "formatType");

		String port = "";
		int bulkSize = 0;
		try {
			port = getTextValue(el, "port");
			Integer.parseInt(port);
		} catch (Exception e) {
			throw new RuntimeException("port must be numeric");
		}

		xmlDataSource.setApacheMqIntegrationEnabled(true);
		xmlDataSource.initApacheMqConfig(ip, port, "apache", inputQueueName, errorQueueName, bulkSize, formatType);
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
