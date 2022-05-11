package com.eastnets.enGpiLoader.notification;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiLoader.DAO.LoaderDAO;
import com.eastnets.enGpiLoader.config.AppConfigBean; 
import com.eastnets.enGpiLoader.parser.RjeMessageParser;
import com.eastnets.enGpiLoader.utility.DataSourceParser;
import com.eastnets.service.ServiceLocator;

public class Notifaction {
	private static final Logger LOGGER = Logger.getLogger(Notifaction.class);

	public void notify(DataSourceParser dataSourceParser,List<NotifierMessage> resultMessages,ServiceLocator serviceLocator, LoaderDAO loaderDAO, AppConfigBean appConfigBean,RjeMessageParser rjeMessageParser) throws Exception { 
		if(!dataSourceParser.getxMLNotifierConfigHelper().isEnabelWriteGpiConfirmation() && !dataSourceParser.getxMLNotifierConfigHelper().isEnabelMailNotify()){ 
			LOGGER.info("At least one action must be enabled : Notify by Email or Message confirmation");
			System.exit(1);
		}  
		LOGGER.debug("Bulk Size : "+dataSourceParser.getxMLNotifierConfigHelper().getBulkSize());   
		if(dataSourceParser.getxMLNotifierConfigHelper().isEnabelWriteGpiConfirmation()){
			ConfirmNotifaction confirmNotifaction=new ConfirmNotifaction(serviceLocator,loaderDAO,appConfigBean,rjeMessageParser);
			confirmNotifaction.notifyByWriteConfirmation(resultMessages, dataSourceParser); 
		}
		if(dataSourceParser.getxMLNotifierConfigHelper().isEnabelMailNotify()){
			MailNotifaction mailNotifaction=new MailNotifaction(serviceLocator, loaderDAO, appConfigBean,rjeMessageParser);
			mailNotifaction.notifyBySendingEmail(resultMessages,dataSourceParser); 
		} 
	}



}
