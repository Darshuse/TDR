package com.eastnets.service.loader;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.enReportingLoader.Main;
import com.eastnets.service.loader.helper.DataSourceParser;
import com.eastnets.service.loader.helper.XMLDataSouceHelper;
import com.eastnets.service.loader.helper.jms.JMSSender;
import com.eastnets.service.loader.loaderReaderServiceDelegates.MQLoaderDelegate;
import com.eastnets.service.loader.loaderReaderServiceDelegates.QueueMessagesDelegate;

public class MQLoaderReaderServiceImp extends LoaderServiceImp
		implements
			Runnable {

	private static final Logger LOGGER = Logger
			.getLogger(LoaderServiceImp.class);

	private MQLoaderDelegate mqLoaderDelegate;
	private QueueMessagesDelegate queueMessagesDelegate;
	private DefaultMessageListenerContainer dmlc;
	private XMLDataSouceHelper xmlDataSouceHelper;
	private Connection lockCon;
	private JMSSender moveToErrorQSender;

	public MQLoaderReaderServiceImp(
			BlockingQueue<LoaderMessage> inblockingQueue,
			BlockingQueue<LoaderMessage> outblockingQueue) {
		super(inblockingQueue, outblockingQueue);
	}

	public MQLoaderReaderServiceImp() {
		super();
	}

	public void run() {
		try {
			// moveToErrorQSender.sendMesage("AA");
			DataSourceParser dataSourceParser = new DataSourceParser(
					getAppConfigBean().getDbConfigFilePath());
			this.xmlDataSouceHelper = dataSourceParser.getXmlDataSource();
			if (!xmlDataSouceHelper.isIbmMQIntegrationEnabled()
					&& !xmlDataSouceHelper.isApacheMqIntegrationEnabled()) {
				return;
			}

			queueMessagesDelegate.init(appConfigBean.getSAAAid(),
					inputBlockingQueue, outputBlockingQueue);

			List<LoaderMessage> messagesList = mqLoaderDelegate
					.restoreMessages(appConfigBean.getSAAAid());
			if (!messagesList.isEmpty()) {

				queueMessagesDelegate.queueMessages(messagesList,
						"RestoreWithData", lockCon);

			}

			dmlc.start();

			/**
			 * Queue coding will be done by JMS Reader as a listener service
			 */

			// while (true) {
			// try{
			// messagesList.clear();
			// messagesList = mqLoaderDelegate.readMessages();
			//
			// if (!messagesList.isEmpty()) {
			// queueMessagesDelegate.queueMessages(messagesList,
			// "NormalWithData");
			// } else {
			// Thread.sleep(5000);
			// }
			// }catch (InterruptedException e) {
			// LOGGER.error(e);
			// e.printStackTrace();
			// } catch (Exception e) {
			// LOGGER.error(e);
			// e.printStackTrace();
			// }
			// }

		} catch (InterruptedException e) {
			LOGGER.error(e);
			String errMessage = "Failed";
			if (e.getMessage() != null) {
				errMessage = (e.getMessage().length() >= 200)
						? e.getMessage().substring(0, 199)
						: e.getMessage();
			}

			if (Main.isDbReader()) {
				mqLoaderDelegate.getLoaderDAO().insertIntoErrorld(
						"DB Connector", "Failed", "", errMessage, "");

			} else {
				mqLoaderDelegate.getLoaderDAO().insertIntoErrorld(
						"MQ Connector", "Failed", "", errMessage, "");

			}
			e.printStackTrace();
		} catch (Exception e) {
			String errMessage = "Failed";
			if (e.getMessage() != null) {
				errMessage = (e.getMessage().length() >= 200)
						? e.getMessage().substring(0, 199)
						: e.getMessage();
			}

			LOGGER.error(e);
			if (Main.isDbReader()) {
				mqLoaderDelegate.getLoaderDAO().insertIntoErrorld(
						"DB Connector", "Failed", "", errMessage, "");

			} else {
				mqLoaderDelegate.getLoaderDAO().insertIntoErrorld(
						"MQ Connector", "Failed", "", errMessage, "");

			}
			e.printStackTrace();
		}

	}

	public QueueMessagesDelegate getQueueMessagesDelegate() {
		return queueMessagesDelegate;
	}

	public void setQueueMessagesDelegate(
			QueueMessagesDelegate queueMessagesDelegate) {
		this.queueMessagesDelegate = queueMessagesDelegate;
	}

	public MQLoaderDelegate getMqLoaderDelegate() {
		return mqLoaderDelegate;
	}

	public void setMqLoaderDelegate(MQLoaderDelegate mqLoaderDelegate) {
		this.mqLoaderDelegate = mqLoaderDelegate;
	}

	public XMLDataSouceHelper getXmlDataSouceHelper() {
		return xmlDataSouceHelper;
	}

	public void setXmlDataSouceHelper(XMLDataSouceHelper xmlDataSouceHelper) {
		this.xmlDataSouceHelper = xmlDataSouceHelper;
	}

	public Connection getLockCon() {
		return lockCon;
	}

	public void setLockCon(Connection lockCon) {
		this.lockCon = lockCon;
	}

	public DefaultMessageListenerContainer getDmlc() {
		return dmlc;
	}

	public void setDmlc(DefaultMessageListenerContainer dmlc) {
		this.dmlc = dmlc;
	}

	public JMSSender getMoveToErrorQSender() {
		return moveToErrorQSender;
	}

	public void setMoveToErrorQSender(JMSSender moveToErrorQSender) {
		this.moveToErrorQSender = moveToErrorQSender;
	}
}
