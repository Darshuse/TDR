package com.eastnets.service.loader.helper.jms;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.XMLConstants;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.eastnets.dao.loader.LoaderDAO;
import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.LoaderMessage.MessageSource;
import com.eastnets.enReportingLoader.Main;
import com.eastnets.enReportingLoader.config.AppConfigBean;
import com.eastnets.service.ServiceLocator;
import com.eastnets.service.loader.helper.DataSourceParser;
import com.eastnets.service.loader.helper.XMLDataSouceHelper;
import com.eastnets.service.loader.inputMessage.InputMessage;
import com.eastnets.service.loader.loaderReaderServiceDelegates.QueueMessagesDelegate;

public class JMSReader extends Observable implements MessageListener {

	private static final Logger LOGGER = Logger.getLogger(JMSReader.class);

	private LinkedBlockingQueue<LoaderMessage> readerOutQueue;
	private JMSSender moveToErrorQSender;
	private QueueMessagesDelegate queueMessagesDelegate;
	private LoaderDAO loaderDAO;
	private LoaderConnection loaderConnection;
	private BigDecimal msgID;
	private ServiceLocator serviceLocator;
	protected AppConfigBean appConfigBean = null;
	private XMLDataSouceHelper xmlDataSouceHelper;
	final String MSG_HEADER_HISTORY = "------------------------------- History Log ---------------------------------";

	@Override
	public void onMessage(Message message) {

		TextMessage textMessage = null;
		try {
			LoaderMessage loaderMessage = null;
			if (message instanceof BytesMessage) {
				LOGGER.info("Start Read BytesMessage From Queue ::");
				BytesMessage bytesMessage = (BytesMessage) message;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buf = new byte[1000000];
				for (int readNum; (readNum = bytesMessage.readBytes(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
				loaderMessage = makeLoaderMessage(bos.toString());
				loaderMessage.setMessageTextFromMQ(bos.toString());

			} else {
				LOGGER.info("Start Read TextMessage From Queue ::");
				textMessage = (TextMessage) message;

				// LOGGER.info("Message Received :: " + textMessage.getText());
				// textMessage.clearProperties();
				if (xmlDataSouceHelper.getFormatType() != null && (xmlDataSouceHelper.getFormatType().equalsIgnoreCase("GTX") || xmlDataSouceHelper.getFormatType().equalsIgnoreCase("AMH"))) {
					loaderMessage = makeLoaderMessage(textMessage.getText());
					loaderMessage.setMessageTextFromMQ(textMessage.getText());
				} else {
					validateXMLSchema(textMessage.getText());
					InputMessage inputMessage = unmarshallMessageXml(textMessage.getText());
					loaderMessage = makeLoaderMessage(inputMessage);
				}
				// readerInQueue.put(loaderMessage);
				/**
				 * Meeting outcomes with Khateeb that I should not queue messages on this for performance reasons, I need to re-discuss this with him because some messages might be lost in case of
				 * crash
				 */
				// writeBeforeConvert(loaderMessage.getRowData());
			}

			convertToUTF8(loaderMessage);
			// writeAfterConvert(loaderMessage.getRowData());
			queueMessagesDelegate.queueMessage(loaderMessage, "NormalWithData");
		} catch (ClassCastException e) {
			LOGGER.error("JMSReader: ClassCastException while casting input message to TextMessage" + e.getMessage());
			moveToErrorQueue(textMessage, e.getMessage());
		} catch (IOException e) {
			LOGGER.error("JMSReader: IOException while validating xml" + e.getMessage());
			moveToErrorQueue(textMessage, e.getMessage());
		} catch (SAXException e) {
			LOGGER.error("JMSReader: SAXException while validating xml" + e.getMessage());
			moveToErrorQueue(textMessage, e.getMessage());
		} catch (JAXBException e) {
			LOGGER.error("JMSReader: JAXBException while parsing jms messasge xml" + e.getMessage());
			moveToErrorQueue(textMessage, e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.error("JMSReader: Exception/Error while reading jms messasge" + e.getMessage());
			moveToErrorQueue(textMessage, e.getMessage());
		}
	}

	private void writeBeforeConvert(String msg) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("E:\\Reporting\\Logs\\beforeCon.txt", "UTF-8");
		writer.println(msg);
		writer.close();

	}

	private void writeAfterConvert(String msg) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("E:\\Reporting\\Logs\\afterCon.txt", "UTF-8");
		writer.println(msg);
		writer.close();

	}

	private LoaderMessage makeLoaderMessageTest(String msg) throws JMSException {
		LoaderMessage loadermesg = new LoaderMessage();
		System.out.println(msg);
		loadermesg.setMessageSource(MessageSource.XML);

		try {
			loadermesg.setRowData(msg);
		}

		catch (RuntimeException e) {
			LOGGER.error("JMSReader: RuntimeException (" + e.getClass().getName() + ") while reading jms message <body>" + e.getMessage());
			throw e;
		}
		try {
			loadermesg.setMesgType("XML");
		} catch (RuntimeException e) {
			LOGGER.error("JMSReader: RuntimeException (" + e.getClass().getName() + ") while reading jms message <type>" + e.getMessage());
			throw e;
		}
		try {
			loadermesg.setMessageSequenceNo(new BigDecimal(11111));
		} catch (RuntimeException e) {
			LOGGER.error("JMSReader: RuntimeException (" + e.getClass().getName() + ") while reading jms message <id>" + e.getMessage());
			throw e;
		}
		return loadermesg;
	}

	// convert from internal Java String format -> UTF-8
	public void convertToUTF8(LoaderMessage loaderMessage) {
		loaderMessage.setRowData(new String(loaderMessage.getRowData().getBytes(), Charset.forName("ISO-8859-6")));
		loaderMessage.setRowHistory(new String(loaderMessage.getRowHistory().getBytes(), Charset.forName("ISO-8859-6")));
	}

	// Java method to create MD5 checksum
	private static String getMD5Hash(String data) {
		String result = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] hash = digest.digest(data.getBytes("UTF-8"));
			return bytesToHex(hash); // make it printable
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	// Java method to create SHA-25 checksum
	private static String getSHA256Hash(String data) {
		String result = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(data.getBytes("UTF-8"));
			return bytesToHex(hash); // make it printable
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private static String bytesToHex(byte[] hash) {
		return DatatypeConverter.printHexBinary(hash).toLowerCase();
	}

	/**
	 * Create LoaderMessage from input
	 * 
	 * @param inputMessage
	 * @return
	 * @throws JMSException
	 */
	private LoaderMessage makeLoaderMessage(InputMessage inputMessage) throws JMSException {
		LoaderMessage loadermesg = new LoaderMessage();
		loadermesg.setMessageSource(MessageSource.XML);

		try {
			loadermesg.setRowData(inputMessage.getBody().trim());
			if (inputMessage.getPayload() != null && !inputMessage.getPayload().isEmpty())
				loadermesg.setPayload(inputMessage.getPayload().trim());

		}

		catch (RuntimeException e) {
			LOGGER.error("JMSReader: RuntimeException (" + e.getClass().getName() + ") while reading jms message <body>" + e.getMessage());
			throw e;
		}
		try {
			loadermesg.setMesgType(inputMessage.getType().trim());
		} catch (RuntimeException e) {
			LOGGER.error("JMSReader: RuntimeException (" + e.getClass().getName() + ") while reading jms message <type>" + e.getMessage());
			throw e;
		}
		try {
			loadermesg.setMessageSequenceNo(new BigDecimal(inputMessage.getId().trim()));
		} catch (RuntimeException e) {
			LOGGER.error("JMSReader: RuntimeException (" + e.getClass().getName() + ") while reading jms message <id>" + e.getMessage());
			throw e;
		}
		return loadermesg;
	}

	private LoaderMessage makeLoaderMessage(String messgeText) throws JMSException {

		LoaderMessage loadermesg = new LoaderMessage();
		try {
			loadermesg.setMessageSource(MessageSource.XML);
			if (!messgeText.contains(MSG_HEADER_HISTORY)) {
				String messageTest = prepareMessageTest(messgeText, false);
				loadermesg.setRowData(messageTest);
			} else {
				String[] messageArr = messgeText.split(MSG_HEADER_HISTORY);
				String messageTest = messageArr[0];
				String messageHistory = messageArr[1];
				loadermesg.setRowData(messageTest);
				loadermesg.setRowHistory(messageHistory);
			}
			if (!xmlDataSouceHelper.getFormatType().equalsIgnoreCase("AMH")) {
				loadermesg.setMesgType("103");
			} else {
				loadermesg.setMesgType("AMH");
			}
		} catch (RuntimeException e) {
			LOGGER.error("JMSReader: RuntimeException");
			throw e;
		}

		return loadermesg;
	}

	private String prepareMessageTest(String messgeText, boolean withMsgHistory) {
		if (!withMsgHistory) {
			return messgeText;
		} else {
			return messgeText.substring(0, messgeText.indexOf("History Log")).replace("-------------------------------", "");
		}
	}

	private String prepareMessageHistoryt(String messgeText) {
		return messgeText.substring(messgeText.indexOf("History Log"), messgeText.length() - 1).replace("History Log ---------------------------------", "");
	}

	/**
	 * Validate XML input message against our XSD
	 * 
	 * @param messageText
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	private void validateXMLSchema(String messageText) throws SAXException, IOException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		if (!messageText.contains("</payload>")) {
			schema = factory.newSchema(JMSReader.class.getResource("/xmlSchema/inputMessageSchema.xsd"));
		} else {
			schema = factory.newSchema(JMSReader.class.getResource("/xmlSchema/inputMessageWithPayloadSchema.xsd"));

		}

		Validator validator = schema.newValidator();
		validator.validate(new StreamSource(new StringReader(messageText)));
	}

	/**
	 * Unpack XML message to a bean using JAXB unmarshalling
	 * 
	 * @param messageText
	 * @return
	 * @throws JAXBException
	 */
	private InputMessage unmarshallMessageXml(String messageText) throws JAXBException {
		JAXBContext jaxbCont = JAXBContext.newInstance(InputMessage.class);
		Unmarshaller unmarshaller = jaxbCont.createUnmarshaller();
		JAXBElement<InputMessage> unmarshal = unmarshaller.unmarshal(new StreamSource(new StringReader(messageText)), InputMessage.class);
		return unmarshal.getValue();
	}

	public static InputMessage unmarshal(String xml) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(InputMessage.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(xml);
		InputMessage person = (InputMessage) jaxbUnmarshaller.unmarshal(reader);
		return person;
	}

	public static void main(String[] args) throws JAXBException {
		String xml = "<?xml version='1.0' encoding='UTF-8'?> <inputMessage> <id>mohmmad kassab</id> <type>kassab kassab</type><body>kassab kassab</body></inputMessage>";
		InputMessage inputMessage = new JMSReader().unmarshal(xml);
	}

	/**
	 * Forward JMS message to error queue
	 * 
	 * @param message
	 * @throws JMSException
	 */
	private void moveToErrorQueue(Message message, String errMessage) {
		errMessage = "Faild";
		if (errMessage != null) {
			errMessage = (errMessage.length() >= 200) ? errMessage.substring(0, 199) : errMessage;
		}
		if (Main.isDbReader()) {
			loaderDAO.insertIntoErrorld("DB Connector", "Failed", "", errMessage, "");
		} else {
			loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessage, "");

		}
	}

	public void initReader() throws Exception {
		LOGGER.info(" :: Message Reader Intialized :: ");
		queueMessagesDelegate.init(appConfigBean.getSAAAid(), null, readerOutQueue);
		DataSourceParser dataSourceParser = new DataSourceParser(getAppConfigBean().getDbConfigFilePath());
		this.xmlDataSouceHelper = dataSourceParser.getXmlDataSource();
	}

	public JMSSender getMoveToErrorQSender() {
		return moveToErrorQSender;
	}

	public void setMoveToErrorQSender(JMSSender moveToErrorQSender) {
		this.moveToErrorQSender = moveToErrorQSender;
	}

	public QueueMessagesDelegate getQueueMessagesDelegate() {
		return queueMessagesDelegate;
	}

	public void setQueueMessagesDelegate(QueueMessagesDelegate queueMessagesDelegate) {
		this.queueMessagesDelegate = queueMessagesDelegate;
	}

	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

	public LinkedBlockingQueue<LoaderMessage> getReaderOutQueue() {
		return readerOutQueue;
	}

	public void setReaderOutQueue(LinkedBlockingQueue<LoaderMessage> readerOutQueue) {
		this.readerOutQueue = readerOutQueue;
	}

	public XMLDataSouceHelper getXmlDataSouceHelper() {
		return xmlDataSouceHelper;
	}

	public void setXmlDataSouceHelper(XMLDataSouceHelper xmlDataSouceHelper) {
		this.xmlDataSouceHelper = xmlDataSouceHelper;
	}

	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}

	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public LoaderConnection getLoaderConnection() {
		return loaderConnection;
	}

	public void setLoaderConnection(LoaderConnection loaderConnection) {
		this.loaderConnection = loaderConnection;
	}

	public BigDecimal getMsgID() {
		return msgID;
	}

	public void setMsgID(BigDecimal msgID) {
		this.msgID = msgID;
	}

}