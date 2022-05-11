package com.eastnets.notifier.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.notifier.domain.EventData;
import com.eastnets.notifier.repository.ENEventsDAO;

@Service
public class BlankMessagesServiceHandler {

	private static final String MALFORMEDHEADER = "<MALFORMEDDATAMESSAGE><![CDATA[";// </MALFORMEDHEADER>
	private static final String END_MALFORMEDHEADER = "]]></MALFORMEDDATAMESSAGE>";

	private static final Logger LOGGER = LoggerFactory.getLogger(BlankMessagesServiceHandler.class);

	private static final String XXXXXX = "XXXXXX";// it's means internal messages so we must remove it and don't send it to IBM Queue
	@Autowired
	private ENEventsDAO eventsDAO;

	public List<EventData> handleBlankMessagesEvent(List<EventData> allEvent) {

		LOGGER.info("start checking for Blank messages to prevent sending them");

		List<EventData> filteredEvent = allEvent.stream().filter(e -> e.getMessage().getMessageType() == null || e.getMessage().getSenderBIC().startsWith(XXXXXX)).collect(Collectors.toList());
		allEvent.removeAll(filteredEvent);

		if (!filteredEvent.isEmpty()) {

			filteredEvent.stream().forEach(e -> {
				e.setCreatedMessageEvent(MALFORMEDHEADER + e.getMessage().toString() + END_MALFORMEDHEADER);
			});
			LOGGER.debug("there is a Blank messages");
			eventsDAO.updateEvents(filteredEvent, MessageStatus.SENT.getStatus());
			eventsDAO.bulkInsertEventHistory(filteredEvent);
		} else {
			LOGGER.info("No Blank Messages ");
		}

		return allEvent;

	}

}
