package com.eastnets.notifier.messaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.eastnets.dao.events.ENEventsDAO;
import com.eastnets.dao.xml.XMLReaderDAO;
import com.eastnets.domain.events.ENEventMetadata;
import com.eastnets.domain.viewer.XMLMessage;
import com.eastnets.notifier.exception.NotificationException;

public class JMSNotifier {

	private JmsTemplate jmsTemplate;
	private XMLReaderDAO xmlReaderDAO;
	private ENEventsDAO eventsDAO;
	private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	private static final Logger LOGGER = LogManager.getLogger(JMSNotifier.class);

	public void sendMessage(final String content) throws NotificationException {

		MessageCreator messageCreator = new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage();
				message.setText(content);
				return message;
			}
		};
		try {
			LOGGER.info("Start sending message");
			jmsTemplate.send(messageCreator);
			LOGGER.info("Message sent successfully without any issue");
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			LOGGER.error(e.getMessage());
			throw new NotificationException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param eventsObserver
	 * @param events
	 * @return the rest of messages that not sent yet
	 */
	public List<ENEventMetadata> notify(List<ENEventMetadata> events) {
		if (events != null && !events.isEmpty()) {
			LOGGER.info("Sending messages ...");
		}
		boolean sendDone = true;
		/*
		 * This list should be used in case of failure happened to tell the Event Observer that sending failure happened and to take care of the messages that not sent yet
		 */
		List<ENEventMetadata> sentEvents = new ArrayList<ENEventMetadata>();
		List<ENEventMetadata> unSentEvents = new ArrayList<ENEventMetadata>(events);
		int eventsSize = events.size();
		ENEventMetadata iteratedEvent = null;
		for (int index = 0; index < eventsSize; index++) {
			try {
				iteratedEvent = events.get(index);
				sendMessage(createMessageText(iteratedEvent));
				sentEvents.add(iteratedEvent);
				unSentEvents.remove(iteratedEvent);
			} catch (NotificationException e) {
				System.out.println(e.getMessage());
				sendDone = false;
				LOGGER.error("Notification exception  :: " + e.getMessage());
				break;
			}
		}
		if (sendDone) {
			LOGGER.debug("Remove sent events normal mode :: " + events.size());
			updateNotifiedMessages(events);
		} else {
			LOGGER.debug("Remove sent events failure mode :: " + sentEvents.size());
			updateNotifiedMessages(sentEvents);
		}
		return unSentEvents;
	}

	private void updateNotifiedMessages(List<ENEventMetadata> events) {
		LOGGER.debug("Start update Events ");
		if (events != null && !events.isEmpty()) {
			eventsDAO.updateEvents(events, MessageStatus.SENT.getStatus());
			LOGGER.debug("Events updated");
		}
	}

	private String createMessageText(ENEventMetadata eventMetadata) {
		StringBuilder xmlFormatedMessage = new StringBuilder();

		try {
			xmlFormatedMessage.append("<Mesg_ID>");
			xmlFormatedMessage.append("<AID>").append(eventMetadata.getAid()).append("</AID>");
			xmlFormatedMessage.append("<UMIDL>").append(eventMetadata.getUmidl()).append("</UMIDL>");
			xmlFormatedMessage.append("<UMIDH>").append(eventMetadata.getUmidh()).append("</UMIDH>");
			xmlFormatedMessage.append("<Operation>").append(eventMetadata.getOperationMode() == 0 ? "insert" : "update").append("</Operation>");
			xmlFormatedMessage.append("<Table>").append(eventMetadata.getTableName()).append("</Table>");
			if (eventMetadata.getTableName() != null) {
				if (eventMetadata.getTableName().equals("RINST")) {
					xmlFormatedMessage.append("<INST_NUM>").append(eventMetadata.getInstNumber()).append("</INST_NUM>");
				} else if (eventMetadata.getTableName().equals("RINTV") || eventMetadata.getTableName().equals("RAPPE")) {
					xmlFormatedMessage.append("<INST_NUM>").append(eventMetadata.getInstNumber()).append("</INST_NUM>");
					xmlFormatedMessage.append("<DATE_TIME>").append(dateFormat.format(eventMetadata.getDateTime())).append("</DATE_TIME>");
					xmlFormatedMessage.append("<SEQ_NBR>").append(eventMetadata.getSeqNBR()).append("</SEQ_NBR>");
				}
			}

			// BMO CR TFS #61078 Adding the MX messages
			List<XMLMessage> xmlMessages = xmlReaderDAO.getXMLMessage(eventMetadata.getAid(), Integer.valueOf(eventMetadata.getUmidl()), Integer.valueOf(eventMetadata.getUmidh()), eventMetadata.getCreationDate(), true);
			for (XMLMessage xmlMessage : xmlMessages) {
				if (xmlMessage.getXmlContent() != null && !xmlMessage.getXmlContent().isEmpty()) {
					xmlFormatedMessage.append(xmlMessage.getXmlContent());
				}
			}

			xmlFormatedMessage.append("</Mesg_ID>");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			LOGGER.error("Error in creating the message " + e.getMessage(), e);
		}
		return xmlFormatedMessage.toString();
	}

	public XMLReaderDAO getXmlReaderDAO() {
		return xmlReaderDAO;
	}

	public void setXmlReaderDAO(XMLReaderDAO xmlReaderDAO) {
		this.xmlReaderDAO = xmlReaderDAO;
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public ENEventsDAO getEventsDAO() {
		return eventsDAO;
	}

	public void setEventsDAO(ENEventsDAO eventsDAO) {
		this.eventsDAO = eventsDAO;
	}
}
