package com.eastnets.enGpiLoader.jms;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage; 
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate; 
public class JMSBrowse {
	private JmsTemplate jmsTemplate;
	private Destination destination;
	private String queueName; 


	public Map<String,String> browse() throws JMSException {
		final Map<String,String> mesgMap=new HashMap<String,String>();

		int count = this.jmsTemplate.browse(this.queueName, new BrowserCallback<Integer>() {
			public Integer doInJms(final Session session, final QueueBrowser browser) throws JMSException {
				Enumeration enumeration = browser.getEnumeration();
				int counter = 0;
				while (enumeration.hasMoreElements()) {
					Message msg = (Message) enumeration.nextElement();
					if (msg instanceof TextMessage) {
						TextMessage textMessage = (TextMessage) msg;
						String text = textMessage.getText();
						mesgMap.put(getMessageInsertionTime(msg)+","+msg.getJMSMessageID(),text);
					}
				}
				return counter;
			}
		});
		return mesgMap;
	}


	public String getMessageInsertionTime(Message message) throws JMSException {
		long creation = message.getJMSTimestamp(); 
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return df.format(new Date(creation));  

	}

	/*	public void browseWithJava8() throws JMSException {
		final List<String> mesgList=new ArrayList<String>();
		BrowserCallback<Integer> action= (session,browser) -> {
			Enumeration enumeration = browser.getEnumeration();
			int counter = 0;
			while (enumeration.hasMoreElements()) {
				Message msg = (Message) enumeration.nextElement();
				if (msg instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) msg;
					String text = textMessage.getText();
					mesgList.add(text);
				}
			}
			return counter;	
		};

		this.jmsTemplate.browse(this.queueName, action);
		mesgList.stream().forEach(mesgText ->{
			System.out.println(mesgText);
		});

	}*/






	public String getQueueName() {
		return queueName;
	}


	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

}
