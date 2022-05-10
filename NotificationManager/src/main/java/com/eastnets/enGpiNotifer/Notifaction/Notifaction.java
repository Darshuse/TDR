package com.eastnets.enGpiNotifer.Notifaction;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiNotifer.DAO.LoaderDAO;
import com.eastnets.enGpiNotifer.config.AppConfigBean;
import com.eastnets.enGpiNotifer.parser.RjeMessageParser;
import com.eastnets.enGpiNotifer.utility.DataSourceParser;
import com.eastnets.service.ServiceLocator;

public class Notifaction {
	private static final Logger LOGGER = Logger.getLogger(Notifaction.class); 
	private ConfirmNotifaction confirmNotifaction;
	private MailNotifaction mailNotifaction;
	
	public void notify(DataSourceParser dataSourceParser,List<NotifierMessage> resultMessages,RjeMessageParser rjeMessageParser) throws Exception { 
		if(!dataSourceParser.getxMLNotifierConfigHelper().isEnabelWriteGpiConfirmation() && !dataSourceParser.getxMLNotifierConfigHelper().isEnabelMailNotify()){ 
			LOGGER.info("At least one action must be enabled : Notify by Email or Message confirmation");
			System.exit(1);
		}  
		LOGGER.debug("Bulk Size : "+dataSourceParser.getxMLNotifierConfigHelper().getBulkSize());   
		if(dataSourceParser.getxMLNotifierConfigHelper().isEnabelWriteGpiConfirmation()){ 
			confirmNotifaction.notifyByWriteConfirmation(resultMessages, dataSourceParser,rjeMessageParser); 
		}
		if(dataSourceParser.getxMLNotifierConfigHelper().isEnabelMailNotify()){ 
			mailNotifaction.notifyBySendingEmail(resultMessages,dataSourceParser,rjeMessageParser); 
		} 
	}
 
	public ConfirmNotifaction getConfirmNotifaction() {
		return confirmNotifaction;
	}

	public void setConfirmNotifaction(ConfirmNotifaction confirmNotifaction) {
		this.confirmNotifaction = confirmNotifaction;
	}

	public MailNotifaction getMailNotifaction() {
		return mailNotifaction;
	}

	public void setMailNotifaction(MailNotifaction mailNotifaction) {
		this.mailNotifaction = mailNotifaction;
	}



}
