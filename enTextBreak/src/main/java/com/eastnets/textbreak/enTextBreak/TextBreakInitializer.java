
package com.eastnets.textbreak.enTextBreak;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.textbreak.bean.DAOFactoryTb;
import com.eastnets.textbreak.bean.ParsedData;
import com.eastnets.textbreak.bean.SourceData;
import com.eastnets.textbreak.bean.TextBreakConfig;
import com.eastnets.textbreak.service.TextBreakParserService;
import com.eastnets.textbreak.service.TextBreakReaderService;
import com.eastnets.textbreak.service.TextBreakWriterService;
import com.eastnets.textbreak.utility.ArgumentHandlerTb;
import com.eastnets.textbreak.utility.Constants;
import com.eastnets.textbreak.utility.ProductCheckerLic;

@Service
public class TextBreakInitializer implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(TextBreakInitializer.class);

	@Autowired
	private TextBreakReaderService textBreakReaderService;
	@Autowired
	private TextBreakParserService textBreakParserService;
	@Autowired
	private TextBreakWriterService textBreakWriterService;

	@Autowired
	private TextBreakConfig textBreakConfig;

	@Autowired
	private ProductCheckerLic productChecker;

	@Autowired
	DAOFactoryTb daoFactory;

	private List<Runnable> serviceList;

	@Autowired
	private ArgumentHandlerTb argumentHandler;

	/*
	 * @Autowired private DefaultMessageListenerContainer jmsContainer;
	 */

	@PostConstruct
	public void init() {
		try {
			LOGGER.debug("ServiceInitiator start");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * Responsible for : (Here we have more checker we must check from C++ Code) 1-Check TextBreak license 2-Check TextBreak product 3- Start >> Reader >> Parser >> Dumber
	 */
	public void startService() {
		// initServiceList();
		// check on TextBreak Config if there is any missing params
		boolean isvalidTextBreakConfig = argumentHandler.isvalidTextBreakConfig();
		if (!textBreakConfig.isOnlinedecompos()) {
			if (!isvalidTextBreakConfig) {
				System.exit(1);
				return;
			}

			LOGGER.debug("start all textBreak service ");

			// Here must check on user Roal (user must have SIDE_LOADER)
			boolean checkUserRoals = productChecker.sCheckRoals();
			if (!checkUserRoals) {
				LOGGER.error("The User is not authorized to run TextBreak ....");
				System.exit(1);
				return;
			}

			// Here must check on user itself
			boolean checkUser = productChecker.checkUser(textBreakConfig.getDbUsername());
			if (!checkUser) {
				LOGGER.error("Check user failed....");
				System.exit(1);
				return;
			}
		}
		LOGGER.info("TextBreak Roles is checked successfuly...");
		daoFactory.getTextBreakDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_I, Constants.PROGRAM_NAME, "",
				"Starting text decomposition (The logging frequency has been set to " + textBreakConfig.getMessageNumber() + ")");

		boolean stopservice = false;
		while (!stopservice) {
			LOGGER.debug("Start Reading Messages ::  For Aid = " + textBreakConfig.getAid());
			// Read Bulk of Messages
			List<SourceData> sourceMessages = textBreakReaderService.readMesages();
			if (sourceMessages == null) {
				LOGGER.debug("End TextBreak Service ..(Please Enable offline decomposition  )");
				System.exit(1);
			}
			LOGGER.debug("Number of messages : " + sourceMessages.size());
			if (sourceMessages.size() != 0) {
				// doParsing for read messages
				LOGGER.debug("Start Parsing Messages ::  ::  For Aid = " + textBreakConfig.getAid() + " Messages number = " + sourceMessages.size());
				List<ParsedData> parseMessages = textBreakParserService.parseMessages(sourceMessages);
				LOGGER.debug("End Parsing Messages ::  ::  For Aid = " + textBreakConfig.getAid());
				// Writing read messages
				LOGGER.debug("Start Writing  Messages ::  ::  For Aid = " + textBreakConfig.getAid() + " Messages number = " + sourceMessages.size());
				textBreakWriterService.writeMessages(parseMessages);
			} else {
				try {
					LOGGER.debug("TextBreak  will sleep for " + 60 + " Seconds");
					Thread.sleep((textBreakConfig.getDelay()) * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// stopservice = true;
		}

	}

	public List<Runnable> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Runnable> serviceList) {
		this.serviceList = serviceList;
	}

	public ArgumentHandlerTb getArgumentHandler() {
		return argumentHandler;
	}

	public void setArgumentHandler(ArgumentHandlerTb argumentHandler) {
		this.argumentHandler = argumentHandler;
	}

	public TextBreakReaderService getTextBreakReaderService() {
		return textBreakReaderService;
	}

	public void setTextBreakReaderService(TextBreakReaderService textBreakReaderService) {
		this.textBreakReaderService = textBreakReaderService;
	}

	public TextBreakParserService getTextBreakParserService() {
		return textBreakParserService;
	}

	public void setTextBreakParserService(TextBreakParserService textBreakParserService) {
		this.textBreakParserService = textBreakParserService;
	}

	public TextBreakWriterService getTextBreakWriterService() {
		return textBreakWriterService;
	}

	public void setTextBreakWriterService(TextBreakWriterService textBreakWriterService) {
		this.textBreakWriterService = textBreakWriterService;
	}

	public ProductCheckerLic getProductChecker() {
		return productChecker;
	}

	public void setProductChecker(ProductCheckerLic productChecker) {
		this.productChecker = productChecker;
	}

	/*
	 * public DefaultMessageListenerContainer getJmsContainer() { return jmsContainer; }
	 * 
	 * 
	 * 
	 * public void setJmsContainer(DefaultMessageListenerContainer jmsContainer) { this.jmsContainer = jmsContainer; }
	 */

	public TextBreakConfig getTextBreakConfig() {
		return textBreakConfig;
	}

	public void setTextBreakConfig(TextBreakConfig textBreakConfig) {
		this.textBreakConfig = textBreakConfig;
	}

	@Override
	public void run() {
		startService();

	}
}
