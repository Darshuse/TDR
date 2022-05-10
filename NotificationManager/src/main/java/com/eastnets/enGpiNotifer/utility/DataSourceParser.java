package com.eastnets.enGpiNotifer.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private XMLQueryHelper queryHelper;
	private XMLNotifierConfigHelper xMLNotifierConfigHelper;
	private XMLMailConfigHelper xmlMailConfigHelper;
	private static final Logger LOGGER = Logger.getLogger(DataSourceParser.class);

	public DataSourceParser(String filePath) throws Exception {
		LOGGER.debug("Start parsing input data source details");
		this.filePath = filePath;
		queryHelper = new XMLQueryHelper();
		startParsing();
		LOGGER.debug("End parsing input data source details");
	}

	public XMLDataSouceHelper getXmlDataSource() {
		return xmlDataSource;
	}

	private void startParsing() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		dom = db.parse(filePath);
		parseTDRDBConnectionFromDom();
		parseIbmMqConnectionFromDom();
		parseDBVieweFromDom();
		parseNotifierConfigFromDom();
		parseMailConfigFromDom();
	}

	private void parseDBVieweFromDom() throws Exception {
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("DBView");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				updateXMLDataSourceWithDBView(el);
			}
		}
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

	private void parseTDRDBConnectionFromDom() throws Exception {
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("TDRDbConfig");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				updateXMLDataSourceWithDBConnection(el);
			}
		}
	}

	private void parseNotifierConfigFromDom() throws Exception {
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("notifierConfig");
		XMLNotifierConfigHelper xmlNotifierConfigHelper = null;
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				xmlNotifierConfigHelper = getXMLNotifierConfigHelper(el);
			}
			setxMLNotifierConfigHelper(xmlNotifierConfigHelper);
		}
	}

	private void parseMailConfigFromDom() throws Exception {
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("mailConfig");
		XMLMailConfigHelper xmlMailConfigHelper = null;
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				xmlMailConfigHelper = getXMLMailConfigHelper(el);
			}
			setXmlMailConfigHelper(xmlMailConfigHelper);
		}
	}

	private XMLNotifierConfigHelper getXMLNotifierConfigHelper(Element el) throws Exception {
		boolean enabelMailNotifyBoll = false;
		boolean enabelWriteGpiConfirmation = false;
		boolean enabelUC = false;
		Integer backTime = null;
		Integer mailAttempts = null;
		Integer confirmAttempts = null;
		String tableName = getTextValue(el, "tablename");
		String where = getTextValue(el, "wherestatment");
		String filePathDirectory = getTextValue(el, "MsgConfirmationDirectory");
		String timeInterval = getTextValue(el, "SearchWithin");
		String enabelMailNotify = getTextValue(el, "MailNotification");
		String enabelWriteRJE = getTextValue(el, "MsgConfirmation");
		String trnRefRejex = getTextValue(el, "MsgRef");
		String environmentType = getTextValue(el, "environmentType");
		String delegatBic = getTextValue(el, "DelegateBIC");
		String mesgDirection = getTextValue(el, "MsgDirection");
		String reasonCode = getTextValue(el, "reasonCode");
		String settlementMethod = getTextValue(el, "settlementMethod");
		String statusCode = getTextValue(el, "statusCode");
		String enabelUCStr = getTextValue(el, "enabelUC");

		String durationTime = "";
		Integer bulkSize = null;
		Long sleepPeriod = null;
		try {
			durationTime = getTextValue(el, "durationTime");
			if (!new Time24HoursValidator().validate(durationTime))
				throw new Exception();
		} catch (Exception e) {
			LOGGER.info("please insert valid duration time HH:MM");
			throw new Exception();
		}

		try {
			sleepPeriod = Long.parseLong(getTextValue(el, "sleepPeriod"));
		} catch (Exception e) {
			LOGGER.info("sleep Period Should be Number");
			throw new Exception();
		}

		try {
			bulkSize = Integer.parseInt(getTextValue(el, "bulkSize"));
		} catch (Exception e) {
			LOGGER.info("bulkSize Should be Number");
			throw new Exception();
		}

		try {
			mailAttempts = Integer.parseInt(getTextValue(el, "mailAttempts"));
		} catch (Exception e) {
			LOGGER.info("numberOfAttempt Should be Number");
			throw new Exception();
		}

		try {
			confirmAttempts = Integer.parseInt(getTextValue(el, "MsgAttempts"));
		} catch (Exception e) {
			LOGGER.info("numberOfAttempt Should be Number");
			throw new Exception();
		}

		if (enabelMailNotify != null && !enabelMailNotify.isEmpty()) {
			if (enabelMailNotify.equalsIgnoreCase("true") || enabelMailNotify.equalsIgnoreCase("false")) {
				enabelMailNotifyBoll = Boolean.parseBoolean(enabelMailNotify);
			} else {
				LOGGER.info("enabelMailNotify Should be true or false");
				throw new Exception();
			}

		}
		if (enabelWriteRJE != null && !enabelWriteRJE.isEmpty()) {
			if (enabelWriteRJE.equalsIgnoreCase("true") || enabelWriteRJE.equalsIgnoreCase("false")) {
				enabelWriteGpiConfirmation = Boolean.parseBoolean(enabelWriteRJE);
			} else {
				LOGGER.info("enabelWriteGpiConfirmation should be true or false");
				throw new Exception();
			}
		}

		if (timeInterval != null && !timeInterval.isEmpty()) {
			try {
				backTime = Integer.parseInt(timeInterval);
			} catch (Exception e) {
				LOGGER.info("timeInterval Should be Number");
				throw new Exception();
			}

		}

		enabelUC = Boolean.parseBoolean(enabelUCStr);

		return new XMLNotifierConfigHelper(filePathDirectory, tableName, where, getTextValue(el, "filePathRJE"), getQueuesFromXml(el, "Queue"), enabelMailNotifyBoll, enabelWriteGpiConfirmation, backTime, bulkSize, getQueuesAsMapFromXml(el, "Queue"),
				sleepPeriod, trnRefRejex, environmentType, delegatBic, mailAttempts, confirmAttempts, durationTime, mesgDirection, reasonCode, settlementMethod, statusCode, enabelUC);
	}

	private XMLMailConfigHelper getXMLMailConfigHelper(Element el) throws Exception {
		String mailHost = getTextValue(el, "mailHost");
		String mailPort = getTextValue(el, "mailPort");

		// check if port in number
		try {
			Integer.parseInt(mailPort);

		} catch (Exception e) {
			LOGGER.info("Mail Port Should be Number ");
			throw new Exception();
		}
		String mailUsername = getTextValue(el, "mailUsername");
		String mailPassword = getTextValue(el, "mailPassword");
		String mailFrom = getTextValue(el, "mailFrom");
		String mailTo = getTextValue(el, "mailTo");
		if (!NotifierHelper.isValidMail(mailFrom)) {
			LOGGER.info("Mail From  Should be Email Format ");
			throw new Exception();
		}

		String mailSubject = getTextValue(el, "mailSubject");
		return new XMLMailConfigHelper(mailHost, mailPort, mailUsername, mailPassword, mailTo, mailSubject, mailFrom);
	}

	private void updateXMLDataSourceWithDBConnection(Element el) throws Exception {

		String ip = getTextValue(el, "ip");
		String dbType = getTextValue(el, "dbtype");
		String dbName = getTextValue(el, "dbname");
		String serviceName = getTextValue(el, "servicename");
		String instanceName = getTextValue(el, "instancename");
		String port = getTextValue(el, "port");
		String aid = getTextValue(el, "aid");
		boolean partitioned = Boolean.parseBoolean(getTextValue(el, "partitioned"));
		if (port != null && !port.isEmpty()) {

			Integer.parseInt(port);
		}
		String username = getTextValue(el, "username");
		String password = getTextValue(el, "password");

		// tns Enable
		boolean tnsEnabled = Boolean.parseBoolean(getTextValue(el, "tnsEnabled"));
		String tnsPath = getTextValue(el, "tnsPath");

		xmlDataSource.initDBConfig(ip, dbType, dbName, serviceName, port, username, password, instanceName, aid, partitioned, DataSource.SAA, tnsEnabled, tnsPath);
	}

	private void updateXMLDataSourceWithDBView(Element el) throws Exception {
		String ip = getTextValue(el, "ip");
		String dbType = getTextValue(el, "dbtype");
		String dbName = getTextValue(el, "dbname");
		String serviceName = getTextValue(el, "servicename");
		String instanceName = getTextValue(el, "instancename");
		String port = getTextValue(el, "port");
		boolean partitioned = Boolean.parseBoolean(getTextValue(el, "partitioned"));
		if (port != null && !port.isEmpty()) {
			Integer.parseInt(port);
		}
		String username = getTextValue(el, "username");
		String password = getTextValue(el, "password");
		String tabelName = getTextValue(el, "tabelName");
		String msgTextColumn = getTextValue(el, "msgTextColumn");
		String msgIDCloumn = getTextValue(el, "msgIDCloumn");
		String msgCreationDataColumn = getTextValue(el, "msgCreationDataColumn");
		String msgQueueNameColumn = getTextValue(el, "msgQueueColumn");
		// tns Enable
		boolean tnsEnabled = Boolean.parseBoolean(getTextValue(el, "tnsEnabled"));
		String tnsPath = getTextValue(el, "tnsPath");
		xmlDataSource.initDBViewConfig(ip, dbType, dbName, serviceName, port, username, password, instanceName, partitioned, tabelName, msgTextColumn, msgIDCloumn, msgCreationDataColumn, msgQueueNameColumn, DataSource.EXT_DB, tnsEnabled, tnsPath);
	}

	private void updateXMLDataSourceWithIbmMqConnection(Element el) throws Exception {

		String ip = getTextValue(el, "ip");
		String username = getTextValue(el, "username");
		String password = getTextValue(el, "password");
		String channel = getTextValue(el, "channel");
		String queueManager = getTextValue(el, "queueManager");
		String inputQueueName = getTextValue(el, "inputQueueName");
		String errorQueueName = getTextValue(el, "errorQueueName");
		String port = "1414";
		try {
			port = getTextValue(el, "port");
			Integer.parseInt(port);
		} catch (Exception e) {
			throw new RuntimeException("port must be numeric");
		}

		// tns Enable
		boolean tnsEnabled = Boolean.parseBoolean(getTextValue(el, "tnsEnabled"));
		String tnsPath = getTextValue(el, "tnsPath");
		xmlDataSource.initIbmMqConfig(ip, username, password, port, channel, queueManager, inputQueueName, errorQueueName, DataSource.MQ, tnsEnabled, tnsPath);
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

	private List<Queue> getQueuesFromXml(Element ele, String tagName) {
		NodeList nl = ele.getElementsByTagName(tagName);
		List<Queue> quList = new ArrayList<Queue>();
		if (nl != null) {
			int length = nl.getLength();
			for (int i = 0; i < length; i++) {
				Element el = (Element) nl.item(i);
				Queue queue = new Queue();
				queue.setQueueName(el.getElementsByTagName("queueName").item(0).getTextContent());
				try {
					queue.setReasonCode(el.getElementsByTagName("reasonCode").item(0).getTextContent());
				} catch (Exception e) {
					queue.setReasonCode(null);
				}
				queue.setSettlementMethod(el.getElementsByTagName("settlementMethod").item(0).getTextContent());
				try {

					queue.setStatusCode(el.getElementsByTagName("statusCode").item(0).getTextContent());
				} catch (Exception e) {
					queue.setStatusCode(null);
				}
				quList.add(queue);
			}
		}
		return quList;
	}

	private Map<String, Queue> getQueuesAsMapFromXml(Element ele, String tagName) {
		NodeList nl = ele.getElementsByTagName(tagName);
		Map<String, Queue> queueMap = new HashMap<String, Queue>();
		if (nl != null) {
			int length = nl.getLength();
			for (int i = 0; i < length; i++) {
				Element el = (Element) nl.item(i);
				Queue queue = new Queue();
				queue.setQueueName(el.getElementsByTagName("queueName").item(0).getTextContent());
				try {

					queue.setReasonCode(el.getElementsByTagName("reasonCode").item(0).getTextContent());
				} catch (Exception e) {
					queue.setReasonCode(null);
				}
				queue.setSettlementMethod(el.getElementsByTagName("settlementMethod").item(0).getTextContent());
				try {

					queue.setStatusCode(el.getElementsByTagName("statusCode").item(0).getTextContent());
				} catch (Exception e) {
					queue.setStatusCode(null);
				}
				queueMap.put(el.getElementsByTagName("queueName").item(0).getTextContent(), queue);
			}
		}
		return queueMap;
	}

	public XMLQueryHelper getQueryHelper() {
		return queryHelper;
	}

	public void setQueryHelper(XMLQueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}

	public XMLNotifierConfigHelper getxMLNotifierConfigHelper() {
		return xMLNotifierConfigHelper;
	}

	public void setxMLNotifierConfigHelper(XMLNotifierConfigHelper xMLNotifierConfigHelper) {
		this.xMLNotifierConfigHelper = xMLNotifierConfigHelper;
	}

	public XMLMailConfigHelper getXmlMailConfigHelper() {
		return xmlMailConfigHelper;
	}

	public void setXmlMailConfigHelper(XMLMailConfigHelper xmlMailConfigHelper) {
		this.xmlMailConfigHelper = xmlMailConfigHelper;
	}
}
