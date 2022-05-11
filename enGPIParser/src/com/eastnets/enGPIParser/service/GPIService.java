
package com.eastnets.enGPIParser.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.GPIMesgFields;
import com.eastnets.domain.viewer.MessageKey;
import com.eastnets.domain.viewer.MessageParsingResult;
import com.eastnets.domain.viewer.TextFieldData;
import com.eastnets.enGPIParser.GPIConfig.AppConfigBean;
import com.eastnets.enGPIParser.messageparsing.ConfirmationParser;
import com.eastnets.enGPIParser.messageparsing.CovParser;
import com.eastnets.enGPIParser.messageparsing.FinParser;
import com.eastnets.enGPIParser.messageparsing.MessageParser;
import com.eastnets.enGPIParser.messageparsing.SRPParser;
import com.eastnets.service.ServiceLocator;

public class GPIService {
	private static final Logger LOGGER = Logger.getLogger(GPIService.class);

	private AppConfigBean appConfigBean = null;
	private ServiceLocator serviceLocator;
	private MessageParser parser;
	private GPIMesgFields gpiMessageFields;
	private static final String GPI_PRODUCT_ID = "25";
	private int messagesParsedNumber = 0;
	String query = "";

	boolean isLicChecked = false;

	public void init() {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int startGPIParsing() {
		// Check License for GPI Parser Tool.
		boolean ucEnabel = appConfigBean.isUcEnabel();
		if (!ucEnabel) {
			checkGPILicense();
		}
		LOGGER.debug("enGPIParser started");
		messagesParsedNumber = 0;
		boolean isPartitionedDatabase = appConfigBean.getDbType().equalsIgnoreCase("Oracle") ? serviceLocator.getCommonService().isPartitionedDatabase() : false;
		isPartitionedDatabase = false;

		List<MessageKey> keysList = null;

		if (ucEnabel) {
			keysList = serviceLocator.getViewerService().getMessageUCKeys(Integer.parseInt(getAppConfigBean().getsAAAid()), isPartitionedDatabase);

		} else {
			keysList = serviceLocator.getViewerService().getMessageKeys(Integer.parseInt(getAppConfigBean().getsAAAid()), isPartitionedDatabase, getAppConfigBean().getFromDate(), getAppConfigBean().getToDate(), getAppConfigBean().getBatchSize());

		}

		if (keysList != null) {
			LOGGER.debug("Start parsing Messages  :: number of messages >>  " + keysList.size());

		}

		List<TextFieldData> messageTextList = new ArrayList<TextFieldData>();
		for (MessageKey key : keysList) {
			messageTextList = serviceLocator.getViewerService().getMessageFields(key, isPartitionedDatabase);
			GPIMesgFields fields = retrieveGPIFields(key.getMesgType(), messageTextList, key);
			switch (fields.getMessageParsingResult()) {
			case WRONGFIELDS:
				serviceLocator.getViewerService().updateGPIFields(key, fields, MessageParsingResult.WRONGFIELDS);
				messagesParsedNumber++;
				break;
			case PARSED:
				serviceLocator.getViewerService().updateGPIFields(key, fields, MessageParsingResult.PARSED);
				if (fields.isUpdateFinMessageStatus())
					serviceLocator.getViewerService().updateTransactionStatus(key, fields, ((ucEnabel) ? "OUTPUT" : "INPUT"));
				messagesParsedNumber++;
				break;
			default: {
				try {
					if ((key.getMesgType().equalsIgnoreCase("202") || key.getMesgType().equalsIgnoreCase("205")) && !key.getMesgIdentifier().contains("COV")) {
						serviceLocator.getViewerService().updateGPIFields(key, fields, MessageParsingResult.WRONGFIELDS);
					}
				} catch (Exception e) {
					serviceLocator.getViewerService().updateGPIFields(key, fields, MessageParsingResult.WRONGFIELDS);
				}

				messagesParsedNumber++;
				break;
			}

			}
		}

		return messagesParsedNumber;

	}

	private GPIMesgFields retrieveGPIFields(String messageType, List<TextFieldData> messageTextList, MessageKey key) {
		if (key.getMesgSLA() != null && key.getMesgSLA().equals("002")) {
			LOGGER.debug("Retrieve gpi Fields Method Started For SLA 002");
			parser = new SRPParser();
			gpiMessageFields = parser.parse(messageTextList, key);// 199
		} else {
			if (messageType.contains("199") || messageType.contains("299")) {
				LOGGER.debug("Retrieve gpi Fields Method Started For Confirmation messages (199,299)");
				parser = new ConfirmationParser();
				gpiMessageFields = parser.parse(messageTextList, key);
			} else if (messageType.contains("103") && !key.getMesgIdentifier().contains("COV")) {
				LOGGER.debug("Retrieve gpi Fields Method Started For For 103");
				parser = new FinParser();
				gpiMessageFields = parser.parse(messageTextList, key);
			} else if (key.getMesgIdentifier().contains("COV")) {
				LOGGER.debug("Retrieve gpi Fields Method Started For COV Messages");
				parser = new CovParser();
				gpiMessageFields = parser.parse(messageTextList, key);
			} else {
				gpiMessageFields = new GPIMesgFields();
				gpiMessageFields.setMessageParsingResult(MessageParsingResult.NOTCOVE);
			}
		}

		return gpiMessageFields;
	}

	public void checkGPILicense() {
		if (!isLicChecked) {
			isLicChecked = true;
			if (!serviceLocator.getLicenseService().checkLicense()) {
				serviceLocator.getViewerService().insertIntoErrorld("GPIParser", "Failed", "GPIParser", "NO LICENCSE", "Not Autherized");
				LOGGER.error("gpi Parser is not licensed.");
				System.exit(1);
				return;
			}
			if (!serviceLocator.getLicenseService().checkProduct(GPI_PRODUCT_ID)) {
				serviceLocator.getViewerService().insertIntoErrorld("GPIParser", "Failed", "GPIParser", "NO LICENCSE", "Not Autherized");
				LOGGER.error("gpi Parser Tool is not licensed.");
				System.exit(1);
				return;
			}
		}

	}

	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

}
