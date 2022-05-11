package com.eastnets.service.loader;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.enReportingLoader.Main;
import com.eastnets.service.loader.helper.DataSourceParser;
import com.eastnets.service.loader.helper.XMLDataSouceHelper;
import com.eastnets.service.loader.loaderReaderServiceDelegates.DatabaseLoaderDelegate;
import com.eastnets.service.loader.loaderReaderServiceDelegates.QueueMessagesDelegate;

public class DBLoaderReaderServiceImp extends LoaderServiceImp implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(LoaderServiceImp.class);
	
	private DatabaseLoaderDelegate dbLoaderDelegate;
	private QueueMessagesDelegate queueMessagesDelegate;
	private Connection lockCon;
	
	private XMLDataSouceHelper xmlDataSouceHelper;
	
	public DBLoaderReaderServiceImp(BlockingQueue<LoaderMessage> inblockingQueue, BlockingQueue<LoaderMessage> outblockingQueue) {
		super(inblockingQueue, outblockingQueue);
	}

	public DBLoaderReaderServiceImp() {
		super();
	}

	public void run() {
		
		try { 
			 
			DataSourceParser dataSourceParser = new DataSourceParser(getAppConfigBean().getDbConfigFilePath());
			this.xmlDataSouceHelper = dataSourceParser.getXmlDataSource();
			if(!xmlDataSouceHelper.isDbIntegrationEnabled()){ 
				return;
			} 
			queueMessagesDelegate.init(appConfigBean.getSAAAid(),inputBlockingQueue, outputBlockingQueue);
			
			List<LoaderMessage> messagesList = dbLoaderDelegate.restoreMessages(appConfigBean.getSAAAid());
			if (!messagesList.isEmpty()) {
				queueMessagesDelegate.queueMessages(messagesList, "Restore",lockCon);
			}
			
			while (true) {
				try{
					messagesList.clear();
					messagesList = dbLoaderDelegate.readMessages(lockCon,appConfigBean.getSAAAid());
					
					if (!messagesList.isEmpty()) {
						queueMessagesDelegate.queueMessages(messagesList, "Normal",messagesList.get(messagesList.size()-1).getLockCon()); 
					} else {
						Thread.sleep(5000);
					}
				}catch (InterruptedException e) {
					LOGGER.error(e);
					e.printStackTrace();
				} catch (Exception e) {
					LOGGER.error(e);
					e.printStackTrace();
				}
			}
		
		} catch (InterruptedException e) {
			LOGGER.error(e);
			String errMessage="Failed";
			if(e.getMessage() != null){
				errMessage= (e.getMessage().length() >= 200) ? e.getMessage().substring(0, 199) : e.getMessage();
			}
			

			if(Main.isDbReader()){
				dbLoaderDelegate.getLoaderDAO().insertIntoErrorld("DB Connector", "Failed", "", errMessage, "");		

			}else{
				dbLoaderDelegate.getLoaderDAO().insertIntoErrorld("MQ Connector", "Failed", "", errMessage, "");			

			}
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error(e);
			String errMessage="Failed";
			if(e.getMessage() != null){
				errMessage= (e.getMessage().length() >= 200) ? e.getMessage().substring(0, 199) : e.getMessage();
			}
			
			if(Main.isDbReader()){
				dbLoaderDelegate.getLoaderDAO().insertIntoErrorld("DB Connector", "Failed", "", errMessage, "");		

			}else{
				dbLoaderDelegate.getLoaderDAO().insertIntoErrorld("MQ Connector", "Failed", "", errMessage, "");			

			}
			e.printStackTrace();
		}

	}

	public DatabaseLoaderDelegate getDbLoaderDelegate() {
		return dbLoaderDelegate;
	}

	public void setDbLoaderDelegate(DatabaseLoaderDelegate dbLoaderDelegate) {
		this.dbLoaderDelegate = dbLoaderDelegate;
	}
	
	public QueueMessagesDelegate getQueueMessagesDelegate() {
		return queueMessagesDelegate;
	}

	public void setQueueMessagesDelegate(QueueMessagesDelegate queueMessagesDelegate) {
		this.queueMessagesDelegate = queueMessagesDelegate;
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
}
