package com.eastnets.service.loader.helper.jms;

import java.util.Observable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.eastnets.dao.loader.LoaderDAO;

public class JMSSender extends Observable {
	private static final Logger LOGGER = Logger.getLogger(JMSSender.class);
	
	private JmsTemplate jmsTemplate;
	private LoaderDAO loaderDAO;
	
	/**
	 * Create a JMS Message from passed messageText and send it to error queue
	 * @param messageText
	 */
	public boolean sendMesage(final String messageText) {

		try{ 
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message = (Message) session.createTextMessage(messageText);
					return message;
				}
			});
			return true;
		} catch(Exception e){
			LOGGER.error("JMSSender: Exception ("+e.getClass().getName()+") while moving jms messasge to error queue: " + e.getMessage());
			String errMessage="Failed";
			if(e.getMessage() != null){
				errMessage= (e.getMessage().length() >= 200) ? e.getMessage().substring(0, 199) : e.getMessage();
			}

			loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessage, "");	
			return false;
		}
	}
	
	/**
	 * Forward the input Message to error queue
	 * @param message
	 */
	public boolean sendMesage(final Message message) {
		try{
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return message;
				}
			});
			return true;
		} catch(Exception e){
			LOGGER.error("JMSSender: Exception ("+e.getClass().getName()+") while moving jms messasge to error queue: " + e.getMessage());
			return false;
		}
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}

}