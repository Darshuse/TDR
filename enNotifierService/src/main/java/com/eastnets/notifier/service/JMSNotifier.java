
package com.eastnets.notifier.service;

import static org.apache.commons.lang.StringUtils.substringAfter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eastnets.notifier.domain.EventData;
import com.eastnets.notifier.domain.NotifierEventObserver;
import com.eastnets.notifier.exception.NotificationException;
import com.eastnets.notifier.util.NotifierUtil;
import com.eastnets.notifier.util.TagCreator;

@Service
public class JMSNotifier extends AbstractNotifier {

	private static final String SWIFT = "SWIFT";

	private static final String _SI_TO_SWIFT = "_SI_to_SWIFT";

	private static final String FIN = "fin.";

	@Autowired
	private JmsTemplate jmsTemplate;
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	Pattern regex = Pattern.compile("\\[(.*?)\\]");
	private static final Logger LOGGER = LoggerFactory.getLogger(JMSNotifier.class);
	@Value("${ibmQueueName}")
	private String queueName;

	@Autowired
	private RJEExporterService rjeExporterService;

	@Override
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
			jmsTemplate.send(queueName, messageCreator);
			LOGGER.info("Message sent successfully without any issue");
		} catch (RuntimeException e) {
			LOGGER.error(e.getMessage());
			throw new NotificationException(e.getMessage());
		}
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * 
	 * @param eventsObserver
	 * @param events
	 * @return the rest of messages that not sent yet
	 */
	public List<EventData> notify(EventsObserver eventsObserver, List<EventData> events) {
		if (events != null && !events.isEmpty()) {
			LOGGER.info("Sending messages ...");
		}
		Set<NotifierEventObserver> notifiedEvents = new TreeSet<NotifierEventObserver>();
		boolean sendDone = true;
		/*
		 * This list should be used in case of failure happened to tell the Event Observer that sending failure happened and to take care of the messages that not sent yet
		 */

		List<EventData> sentEvents = new ArrayList<EventData>();
		List<EventData> unSentEvents = new ArrayList<EventData>(events);
		int eventsSize = events.size();
		EventData iteratedEvent = null;

		for (int index = 0; index < eventsSize; index++) {

			try {
				iteratedEvent = events.get(index);

				LOGGER.info("prepare to send for message " + iteratedEvent.getPrimaryKey());
				/**
				 * if message is null it means , this message already sent with updated event to customer
				 */
				String createMessageText = "";
				if (iteratedEvent.getMessage() != null) {
					try {
						createMessageText = createMessageText(iteratedEvent, notifiedEvents, eventsObserver);

					} catch (Exception e) {
						createMessageText = "";
						LOGGER.info("Exception when start to Build xml message >> createMessageText(iteratedEvent) EventData is " + ((iteratedEvent == null) ? "" : iteratedEvent.toString()));
					}

					if (createMessageText == null || createMessageText.isEmpty()) {
						// doNothing

					} else {
						sendMessage(createMessageText);
						unSentEvents.remove(iteratedEvent);
						iteratedEvent.setCreatedMessageEvent(createMessageText);
						sentEvents.add(iteratedEvent);
						eventsObserver.updateNotifiedMessages(sentEvents);
						eventsObserver.removeSentMessages();
					}

				}

			} catch (NotificationException e) {
				sendDone = false;
				LOGGER.error("Notification exception  :: " + e.getMessage());
				break;
			}
		}
		if (sendDone) {
			LOGGER.debug("Remove sent events normal mode :: " + sentEvents.size());
			eventsObserver.insertSentEventInHistory(sentEvents);
			eventsObserver.insertSentEventInEventObserver(new ArrayList<>(notifiedEvents));
		} else {
			LOGGER.debug("Remove sent events failure mode :: " + sentEvents.size());
			eventsObserver.updateNotifiedMessages(sentEvents);
		}
		return unSentEvents;
	}

	private String getBrieftNetworkStatus(String networkStatus) {
		String briefNetworkStatus = "";// get the value from column.
		switch (networkStatus) {
		case "DLV_NACKED":
			briefNetworkStatus = "NACK";
			return briefNetworkStatus;

		case "DLV_ABORTED":
			briefNetworkStatus = "ABORTED";
			return briefNetworkStatus;

		case "DLV_ACK":
			briefNetworkStatus = "ACK";
			return briefNetworkStatus;

		case "DLV_ACKED":
			briefNetworkStatus = "ACK";
			return briefNetworkStatus;

		case "DLV_N_A":
			briefNetworkStatus = "-";
			return briefNetworkStatus;

		case "DLV_WAITING_ACK":
			briefNetworkStatus = "WAITING  ACK";
			return briefNetworkStatus;

		case "DLV_TIMED_OUT":
			briefNetworkStatus = "Time Out";
			return briefNetworkStatus;

		case "DLV_REJECTED_LOCALLY":
			briefNetworkStatus = "REJECTED LOCALLY";
			return briefNetworkStatus;

		default:
			return networkStatus;
		}
	}

	private String removeFinFromMessageType(String messageIdentifier) {
		if (messageIdentifier.startsWith(FIN)) {
			return substringAfter(messageIdentifier, FIN);
		}
		return messageIdentifier;
	}

	private String createMessageText(EventData eventData, Set<NotifierEventObserver> notifiedEvents, EventsObserver eventsObserver) {
		StringBuilder xmlFormatedMessage = new StringBuilder();
		xmlFormatedMessage.append("<Mesg_ID>");
		xmlFormatedMessage.append("<AID>").append(eventData.getPrimaryKey().getAid()).append("</AID>");
		xmlFormatedMessage.append("<UMIDL>").append(eventData.getPrimaryKey().getUmidl()).append("</UMIDL>");
		xmlFormatedMessage.append("<UMIDH>").append(eventData.getPrimaryKey().getUmidh()).append("</UMIDH>");
		xmlFormatedMessage.append("<Operation>").append(eventData.getOperationType() == 0 ? "insert" : "update").append("</Operation>");
		xmlFormatedMessage.append("<Table>").append(eventData.getTableName()).append("</Table>");
		xmlFormatedMessage.append("<DATETIME>").append(eventData.getMessage().getInterventionDateTime().format(DATE_FORMAT)).append("</DATETIME>");

		if (eventData.getMessage() == null) {
			LOGGER.error("eventData With null message! With Primary key " + eventData.getPrimaryKey() + "  :: Table name =" + eventData.getTableName());
			return xmlFormatedMessage.toString();
		}

		xmlFormatedMessage.append(TagCreator.createTag("MessageType", removeFinFromMessageType(eventData.getMessage().getMessageIdentifer())));
		xmlFormatedMessage.append(TagCreator.createTag("TranAmtCCY", eventData.getMessage().getAmountStr()));
		xmlFormatedMessage.append(TagCreator.createTag("TranRef", eventData.getMessage().getTransactionReference()));
		xmlFormatedMessage.append(TagCreator.createTag("RelRef", eventData.getMessage().getRelatedReference()));
		xmlFormatedMessage.append(TagCreator.createTag("UETRRef", eventData.getMessage().getUterReferenceNumber()));
		xmlFormatedMessage.append(TagCreator.createTag("ServiceTypeInd", eventData.getMessage().getServiceTypeIdentifier()));

		if (eventData.getMessage().getText() != null) {
			xmlFormatedMessage.append(TagCreator.createTag("ChargesDet", eventData.getMessage().getText().getChargesDetails()));
			xmlFormatedMessage.append(TagCreator.createTag("NarrDesc", eventData.getMessage().getText().getNarrativeDescription()));

		}
		if (eventData.getMessage().getValueDate() != null) {
			xmlFormatedMessage.append(TagCreator.createTag("ValueDate", eventData.getMessage().getValueDate().format(DATE_FORMAT)));// must chane
		} else {
			xmlFormatedMessage.append(TagCreator.createTag("ValueDate", eventData.getMessage().getValueDate()));// must chane

		}

		xmlFormatedMessage.append(TagCreator.createTag("Instancetype", eventData.getInstanceNumber()));

		String queueStatus = NotifierUtil.getRoutingPointStatus(eventData).orElse("");
		xmlFormatedMessage.append(TagCreator.createTag("UnitAssigned", eventData.getMessage().getUnitName()));
		xmlFormatedMessage.append(TagCreator.createTag("MesgStatus", eventData.getMessage().getInstanceStatus()));

		if (!StringUtils.isEmpty(eventData.getMessage().getNetwrokStatus()) && (queueStatus.equalsIgnoreCase(_SI_TO_SWIFT) && eventData.getMessage().getInstanceStatus().contentEquals(NotifierUtil.COMPLETE_MESSAGE_STATUS))) {
			xmlFormatedMessage.append(TagCreator.createTag("NetworkStatus", getBrieftNetworkStatus(eventData.getMessage().getNetwrokStatus())));
		} else {
			// handling the Nacked status for instance 0
			xmlFormatedMessage.append(TagCreator.createTag("NetworkStatus", eventData.getMessage().getIntervintionNetworkStatus()));
		}

		xmlFormatedMessage.append(TagCreator.createTag("QueueStatus", queueStatus));

		// remove notifier Event Observer when we have Linked with new COMPLETE intv
		if (eventData.getMessage().getInstanceStatus().contentEquals(NotifierUtil.COMPLETE_MESSAGE_STATUS)) {
			eventsObserver.removetNotifierEventObserver(eventData.getPrimaryKey().getAid(), Integer.parseInt(eventData.getPrimaryKey().getUmidl()), Integer.parseInt(eventData.getPrimaryKey().getUmidh()));
		}

		// here we will not save the instance 0 in the observer table , because the original instance may return to same queue.
		if (!eventData.getInstanceNumber().equals("0") && !eventData.getMessage().getInstanceStatus().contentEquals(NotifierUtil.COMPLETE_MESSAGE_STATUS)) {
			NotifierEventObserver notifierEventObserver = new NotifierEventObserver();
			notifierEventObserver.setMsgPrimaryKey(eventData.getPrimaryKey());
			notifierEventObserver.setInstanceSequanceNumber(eventData.getInstanceNumber());
			notifierEventObserver.setQueueStatus(eventData.getMessage().getInstanceStatus());
			notifierEventObserver.setRoutingPointName(queueStatus);
			LOGGER.debug("checking the routing point ");
			if (StringUtils.isEmpty(notifierEventObserver.getRoutingPointName())) {
				LOGGER.debug("will use the creation routing point ");
				notifierEventObserver.setRoutingPointName(eventData.getMessage().getMesgCreRpName());
			}
			notifiedEvents.add(notifierEventObserver);
		}
		if (eventData.getMessage().getOperatorNickName() != null && !eventData.getMessage().getOperatorNickName().isEmpty()) {
			xmlFormatedMessage.append(TagCreator.createTag("UserDetails", eventData.getMessage().getOperatorNickName()));
		} else {
			xmlFormatedMessage.append(TagCreator.createTag("UserDetails", ""));
		}

		if (!StringUtils.isEmpty(eventData.getMessage().getSourceEntity()) && eventData.getMessage().getDirection().equalsIgnoreCase("input")) {
			xmlFormatedMessage.append(TagCreator.createTag("SourceEntity", eventData.getMessage().getSourceEntity()));

		} else {
			xmlFormatedMessage.append(TagCreator.createTag("SourceEntity", ""));

		}
		// });

		if (eventData.getOperationType() == 1 && (eventData.getTableName().equalsIgnoreCase("RINST") || eventData.getTableName().equalsIgnoreCase("RAPPE") || eventData.getTableName().equalsIgnoreCase("RINTV"))) {
			if (eventData.getInsertionDateTime() != null) {
				xmlFormatedMessage.append("<ReceivedDate>")

						.append(eventData.getInsertionDateTime().format(DATE_FORMAT)).append("</ReceivedDate>");
			} else {
				xmlFormatedMessage.append("<ReceivedDate>").append("</ReceivedDate>");
			}
		} else {
			if (eventData.getMessage().getMesgModifiedDateTime() != null) {
				xmlFormatedMessage.append("<ReceivedDate>").append(eventData.getOperationType() == 0 ? eventData.getInsertionDateTime().format(DATE_FORMAT) : eventData.getMessage().getMesgModifiedDateTime().format(DATE_FORMAT))
						.append("</ReceivedDate>");
			} else {
				xmlFormatedMessage.append("<ReceivedDate>").append("</ReceivedDate>");
			}
		}

		xmlFormatedMessage.append(TagCreator.createTag("SenderBIC", eventData.getMessage().getSenderBIC()));
		xmlFormatedMessage.append(TagCreator.createTag("ReceiverBIC", eventData.getMessage().getRecieverBIC()));
		xmlFormatedMessage.append(TagCreator.createTag("Direction", eventData.getMessage().getDirection()));
		if (eventData.getMessage().getText() != null) {
			LOGGER.trace("start build RJE message for id " + eventData.getPrimaryKey());
			xmlFormatedMessage.append(TagCreator.createTag("MessageCopy", rjeExporterService.buildRJE(eventData.getMessage(), eventData.getPrimaryKey())));
		}

		xmlFormatedMessage.append("</Mesg_ID>");
		LOGGER.trace("XML message is {}", xmlFormatedMessage.toString());
		return xmlFormatedMessage.toString();
	}

	public RJEExporterService getRjeExporterService() {
		return rjeExporterService;
	}

	public void setRjeExporterService(RJEExporterService rjeExporterService) {
		this.rjeExporterService = rjeExporterService;
	}

}
