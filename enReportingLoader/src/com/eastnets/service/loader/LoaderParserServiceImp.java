
/**
 * 
 */
package com.eastnets.service.loader;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.eastnets.beans.AMPData.AMP;
import com.eastnets.dao.loader.LoaderDAO;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.LoaderMessage.MessageSource;
import com.eastnets.domain.loader.LoaderMessage.MessageStatus;
import com.eastnets.mx.mapping.DataPDU;
import com.eastnets.resilience.mtutil.GenerateMTTypes;
import com.eastnets.resilience.mtutil.MTType;
import com.eastnets.service.ServiceLocator;
import com.eastnets.service.loader.exceptions.MessageParsingException;
import com.eastnets.service.loader.helper.DataSourceParser;
import com.eastnets.service.loader.helper.XMLDataSouceHelper;
import com.eastnets.service.loader.helper.jms.JMSSender;
import com.eastnets.service.loader.parser.AmhMessageParser;
import com.eastnets.service.loader.parser.MxMessageParser;
import com.eastnets.service.loader.parser.RjeMessageParser;
import com.eastnets.service.viewer.ViewerService;

/**
 * @author MKassab
 * 
 */
public class LoaderParserServiceImp extends LoaderServiceImp implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(LoaderParserServiceImp.class);
	private LoaderDAO loaderDAO;
	private List<Future<LoaderMessage>> parsingTasks;
	private ServiceLocator serviceLocator;
	private XMLDataSouceHelper xmlDataSouceHelper;
	private JMSSender moveToErrorQSender;

	@SuppressWarnings("unused") // it is called from spring, when creating this object.
	private void init() {
		DataSourceParser dataSourceParser = null;
		try {
			dataSourceParser = new DataSourceParser(getAppConfigBean().getDbConfigFilePath());
		} catch (Exception e) {
			// TODO: handle exception
		}
		this.xmlDataSouceHelper = dataSourceParser.getXmlDataSource();
	}

	@Override
	public void run() {
		LoaderMessage loaderMesg = null;
		ViewerService viewerService = baseServiceLocator.getViewerService();
		MxMessageParser mxParser = null;
		RjeMessageParser rjeParser = null;
		AmhMessageParser amhMessageParser = null;
		Callable<LoaderMessage> parsingTask;
		LOGGER.info("Initializing Parser Service");
		ExecutorService executorService = Executors.newFixedThreadPool(50);
		Future<LoaderMessage> underProcessingMessage;
		/*
		 * load MT types,syntax version and viewer service before creating RJ Message to save memory
		 */
		Map<String, MTType> mtTypesMap = null;
		String syntaxVersion = viewerService.getLatestInstalledSyntaxVer("");

		JAXBContext jaxbCont;
		Unmarshaller umarsh = null;
		Unmarshaller umarshAMH = null;
		JAXBContext jaxbContextAMH = null;
		List<String> sepaXSD = new ArrayList<String>();

		try {
			mtTypesMap = GenerateMTTypes.getMtTypesMap();
			// For Mx

			jaxbCont = JAXBContext.newInstance(DataPDU.class);
			umarsh = jaxbCont.createUnmarshaller();

			// For AMH
			jaxbContextAMH = JAXBContext.newInstance(AMP.class);
			umarshAMH = jaxbContextAMH.createUnmarshaller();

			sepaXSD = loaderDAO.getXSDIdentifier();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		// SimpleDateFormat dateF = new SimpleDateFormat("mm:ss:S");
		while (true) {

			// check if there is async future task finished
			for (int index = 0; index < parsingTasks.size(); index++) {
				underProcessingMessage = parsingTasks.get(index);
				if (underProcessingMessage.isDone()) {
					try {
						// remove message from parsing tasks list
						parsingTasks.remove(underProcessingMessage);
						// put the parsed message in the output queue to be
						// eligible for persisting in DB
						// date = new Date();
						// LOGGER.info(":::: BEFORE GET FROM FETURE :::::
						// "+dateF.format(date));

						LoaderMessage loaderMessage = underProcessingMessage.get();
						// date = new Date();
						// LOGGER.info(":::: AFTER GET FROM FETURE :::::
						// "+dateF.format(date));
						outputBlockingQueue.put(loaderMessage);
						// LOGGER.info("OUTPUT SIZE :::::
						// "+outputBlockingQueue.size());

					} catch (Exception e) {
						if (e instanceof MessageParsingException) {
							MessageParsingException messageException = (MessageParsingException) e;
							catchCorruptedMessage(messageException.getLoaderMessage(), messageException.getLoaderMessage().getMessageSequenceNo(), messageException.getLoaderMessage().getMessageSource(), appConfigBean.getSAAAid());
						}

						if (e.getCause() instanceof MessageParsingException) {
							MessageParsingException messageException = (MessageParsingException) e.getCause();
							catchCorruptedMessage(messageException.getLoaderMessage(), messageException.getLoaderMessage().getMessageSequenceNo(), messageException.getLoaderMessage().getMessageSource(), appConfigBean.getSAAAid());
						}
					}
				}
			}

			try {
				loaderMesg = inputBlockingQueue.poll();
				// LOGGER.info("INPUT SIZE :::: " + inputBlockingQueue.size());
				if (loaderMesg != null && (loaderMesg.getMesgType() != null && !loaderMesg.getMesgType().isEmpty())) {
					if (loaderMesg.getMesgType().equals("XML")) {
						mxParser = new MxMessageParser(loaderMesg, umarsh, sepaXSD);
						parsingTask = mxParser;
						LOGGER.debug("MX Message :: MX Parser responsible to parsing this message");
					} else if ((loaderMesg.getMesgType() != null && xmlDataSouceHelper.getFormatType() != null) && (loaderMesg.getMesgType().equals("AMH") || xmlDataSouceHelper.getFormatType().equalsIgnoreCase("AMH"))) {
						amhMessageParser = new AmhMessageParser(loaderMesg, umarshAMH, sepaXSD);
						parsingTask = amhMessageParser;
						LOGGER.debug("AMH Message :: AMH Parser responsible to parsing this message");
					} else {
						LOGGER.debug("RJ Message :: RJE Parser responsible to parsing this message");
						rjeParser = new RjeMessageParser(viewerService, syntaxVersion, mtTypesMap, loaderMesg);
						parsingTask = rjeParser;
					}

					// date = new Date();
					// LOGGER.info("::::::::: BEFORE SUBMIT :::::::
					// "+dateF.format(date));
					if (parsingTask != null) {
						Future<LoaderMessage> futureMessage = executorService.submit(parsingTask);
						// date = new Date();
						// LOGGER.info("::::::::: AFTER SUBMIT :::::::
						// "+dateF.format(date));

						parsingTasks.add(futureMessage);
					}
				}
				if (loaderMesg == null && parsingTasks.isEmpty()) {
					LOGGER.debug("NO Messages ready for parsing");
					Thread.sleep(10000);
				}

			} catch (Exception e) {
				LOGGER.error(e);
				LOGGER.error("Error while parsing message :: " + loaderMesg.getMessageSequenceNo());
				MessageParsingException messageException = (MessageParsingException) e;
				catchCorruptedMessage(messageException.getLoaderMessage(), messageException.getLoaderMessage().getMessageSequenceNo(), messageException.getLoaderMessage().getMessageSource(), appConfigBean.getSAAAid());
			}
		}
	}

	private void moveToErrorQueue(String message) {
		if (message != null) {
			boolean sentSuccessfully = moveToErrorQSender.sendMesage(message);
			if (sentSuccessfully) {
				LOGGER.info("Message Moved To Error Queue");
			}
		}
	}

	public static void main(String[] args) throws JAXBException {
		try {
			File file = new File("D:\\enGpiNonfier\\MX_AMPexample.txt");
			JAXBContext jaxbContext = JAXBContext.newInstance(AMP.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			AMP que = (AMP) jaxbUnmarshaller.unmarshal(file);
			System.out.println(que.getData());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private void catchCorruptedMessage(LoaderMessage loaderMessage, BigDecimal messageSeqNumber, MessageSource messageSource, String aid) {
		String errMessageText = (loaderMessage.getRowData().length() >= 1000) ? loaderMessage.getRowData().substring(0, 999) : loaderMessage.getRowData();
		if (messageSource.equals(MessageSource.XML)) {
			loaderDAO.updateMessageStatusForMQ(messageSeqNumber, MessageStatus.CORRUPTED, aid);
			LOGGER.error("Error While parsing message no :: " + messageSeqNumber);
			loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "Parsing Message", errMessageText, "");
			if (xmlDataSouceHelper.isUseErrorQueue()) {
				moveToErrorQueue(loaderMessage.getMessageTextFromMQ());
			}
		} else {
			loaderDAO.updateMessageStatus(messageSeqNumber, MessageStatus.CORRUPTED, aid);
			loaderDAO.insertIntoErrorld("DB Connector", "Failed", "Parsing Message", errMessageText, "");
		}

	}

	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}

	public List<Future<LoaderMessage>> getParsingTasks() {
		return parsingTasks;
	}

	public void setParsingTasks(List<Future<LoaderMessage>> parsingTasks) {
		this.parsingTasks = parsingTasks;
	}

	public XMLDataSouceHelper getXmlDataSouceHelper() {
		return xmlDataSouceHelper;
	}

	public void setXmlDataSouceHelper(XMLDataSouceHelper xmlDataSouceHelper) {
		this.xmlDataSouceHelper = xmlDataSouceHelper;
	}

	public JMSSender getMoveToErrorQSender() {
		return moveToErrorQSender;
	}

	public void setMoveToErrorQSender(JMSSender moveToErrorQSender) {
		this.moveToErrorQSender = moveToErrorQSender;
	}

	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public LoaderParserServiceImp() {
		super();
		parsingTasks = new ArrayList<Future<LoaderMessage>>();
	}

	public LoaderParserServiceImp(BlockingQueue<LoaderMessage> inblockingQueue, BlockingQueue<LoaderMessage> outblockingQueue) {
		super(inblockingQueue, outblockingQueue);
	}

}
