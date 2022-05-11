package com.eastnets.textbreak.bean;

public class MyMessageConverter // implements MessageConverter
{
	/*
	 * public Object fromMessage(Message message) throws JMSException,MessageConversionException { MapMessage mapMessage = (MapMessage) message; TextBreakMQBean messageObject = new TextBreakMQBean();
	 * messageObject.setMesgText(mapMessage.getString("mesgText")); return messageObject; }
	 * 
	 * public Message toMessage(Object object, Session session) throws JMSException,MessageConversionException { TextBreakMQBean messageObject = (TextBreakMQBean) object; MapMessage message =
	 * session.createMapMessage(); message.setString("mesgText", messageObject.getMesgText()); return message; }
	 */
}
