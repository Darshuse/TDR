package com.eastnets.enGpiLoader.notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.DataSource;
import com.eastnets.domain.viewer.DaynamicMsgDetailsParam;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiLoader.DAO.LoaderDAO;
import com.eastnets.enGpiLoader.bulder.MessageBulder;
import com.eastnets.enGpiLoader.config.AppConfigBean;
import com.eastnets.enGpiLoader.parser.RjeMessageParser;
import com.eastnets.enGpiLoader.service.GpiService;
import com.eastnets.enGpiLoader.utility.DataSourceParser;
import com.eastnets.service.ServiceLocator;
import com.eastnets.service.viewer.helper.ViewerServiceUtils;
import com.google.common.collect.Lists;

public class MailNotifaction  {
	private static final Logger LOGGER = Logger.getLogger(MailNotifaction.class);

	private ServiceLocator serviceLocator;
	private LoaderDAO loaderDAO;
	private AppConfigBean appConfigBean = null;
	private RjeMessageParser rjeMessageParser;
 
	public MailNotifaction(ServiceLocator serviceLocator, LoaderDAO loaderDAO, AppConfigBean appConfigBean,RjeMessageParser rjeMessageParser) {
		super();
		this.serviceLocator = serviceLocator;
		this.loaderDAO = loaderDAO;
		this.appConfigBean = appConfigBean;
		this.rjeMessageParser=rjeMessageParser;
	}


	public void notifyBySendingEmail(List<NotifierMessage> messagesList,DataSourceParser dataSourceParser) throws InterruptedException{ 	
		List<NotifierMessage> notifierMessages=getMessagesForMail(messagesList,dataSourceParser);
		if(!notifierMessages.isEmpty()){
			for (List<NotifierMessage> messages : Lists.partition(notifierMessages, dataSourceParser.getxMLNotifierConfigHelper().getBulkSize())) {

				LOGGER.debug("Start building Mail notification bulk");  
				notifyBulkMail(messages, dataSourceParser); 
				LOGGER.debug("End building Mail notification bulk"); 	 

			}			
		}


	}


	private List<NotifierMessage> getMessagesForMail(List<NotifierMessage> messages,DataSourceParser dataSourceParser) {
		List<NotifierMessage> notifierMessages=new ArrayList<NotifierMessage>();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		String curuntDate = ""; 

		for(NotifierMessage message:messages){
			curuntDate = formater.format(new Date());
			if(message.getMailAtempt() >= dataSourceParser.getxMLNotifierConfigHelper().getMailAttempts() || !ViewerServiceUtils.comparinDuration(message.getMailAttemptsDateTimeStr(),curuntDate,dataSourceParser.getxMLNotifierConfigHelper().getDurationTime())){	
				continue;
			}else{
				notifierMessages.add(message);
			}

		}
		return notifierMessages;

	}

	private void notifyBulkMail(List<NotifierMessage> messages,DataSourceParser dataSourceParser) throws InterruptedException{ 
	 
	 
		String mailStatus=serviceLocator.getViewerService().mailMessagesForGpi(dataSourceParser.getxMLNotifierConfigHelper().getMailAttempts(),appConfigBean.getUsername(), appConfigBean.getMailSubject(), appConfigBean.getMailTo() , messages, true, false,0,dataSourceParser.getxMLNotifierConfigHelper().getDurationTime(),getmessagesDetails(messages)); 

	    

		if(mailStatus.isEmpty()){ 
			LOGGER.debug("Start sending Emails");
			LOGGER.debug("End sending Emails");  
			LOGGER.debug("Start updating on ldGpiNotifersHistory");	 
			updateldGpiNotifersHistoryForMail(messages,dataSourceParser);  
			LOGGER.debug("End updating on ldGpiNotifersHistory"); 
		}
		else{ 
			if(!mailStatus.equalsIgnoreCase("NOT Valid")){
				for(NotifierMessage notifierMessage:messages){
					notifierMessage.setSendMail(false);
				}
				LOGGER.info("while mailMessagesForGpi  :" + mailStatus );
				serviceLocator.getViewerService().insertIntoErrorld("GPINotifier", "Failed", "GPINotifier", "while mailMessagesForGpi()  :" + mailStatus , null); 
			}
		}

	}


 	private List<String> getmessagesDetails(List<NotifierMessage> messages) { 
		List<String> rjeMessageText=new ArrayList<String>();
		for (NotifierMessage message : messages) {  
			try { 
				
				if(message.getDataSource().equals(DataSource.SAA)){
				 return null;			
				}else{  
					rjeMessageText.add(message.getMessageText()); 	
				} 

			} catch (Exception ex) { 
				ex.printStackTrace();
			} 
		
		}
		return rjeMessageText;
	}


	private void updateldGpiNotifersHistoryForMail(List<NotifierMessage> resultMessages,DataSourceParser dataSourceParser){ 
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		String curuntDate = "";
		for(NotifierMessage entity:resultMessages){
			curuntDate = formater.format(new Date());
			if(entity.isSendMail() && GpiService.isMsgExceededDuration(entity.getMailAttemptsDateTimeStr(),curuntDate,dataSourceParser.getxMLNotifierConfigHelper().getDurationTime())){
				try {
					loaderDAO.insertMailAttempt(entity);	
				} catch (Exception e) {
					if(e.getMessage().contains("Violation of PRIMARY KEY constraint") || e.getMessage().contains("unique constraint (SIDE.PK_LDGPINOTIFERSHISTORY) violated")){
						
						loaderDAO.updateMailAttempt(entity);
					}
				}
			}
		}
	}


	public RjeMessageParser getRjeMessageParser() {
		return rjeMessageParser;
	}


	public void setRjeMessageParser(RjeMessageParser rjeMessageParser) {
		this.rjeMessageParser = rjeMessageParser;
	}



}
