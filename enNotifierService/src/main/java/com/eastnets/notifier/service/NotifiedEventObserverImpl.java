package com.eastnets.notifier.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.notifier.domain.EventData;
import com.eastnets.notifier.repository.ENEventsDAO;
import com.eastnets.notifier.util.NotifierUtil;

/**
 * the aim of this class to check for all already notified messages(LIVE) to prevent sending duplicate events
 * 
 * @author AHammad
 *
 */
@Service
public class NotifiedEventObserverImpl implements NotifiedEventObserver {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotifiedEventObserverImpl.class);

	@Autowired
	private ENEventsDAO eventsDAO;

	@Autowired
	private Map<String, List<String>> notifiedEventsMap;

	/**
	 * A convenience method to remove duplicate event which already sent for same message
	 * 
	 * @author AHammad
	 * @since 22/04/2020
	 * @return List of all non sent events before
	 */
	@Override
	public List<EventData> removeAlreadySentEvent(List<EventData> allEvent) {

		LOGGER.debug("check for all notified messages if there is a duplicate ");

		if (notifiedEventsMap.isEmpty() || allEvent.isEmpty())
			return allEvent;

		List<EventData> removedEvent = allEvent.stream()
				.filter(ev -> (ev.getMessage() != null && ev.getMessage().getInstanceStatus() != null && !ev.getMessage().getInstanceStatus().contentEquals(NotifierUtil.COMPLETE_MESSAGE_STATUS))
						&& (notifiedEventsMap.containsKey(ev.getPrimaryKey().toString() + ev.getInstanceNumber()) && notifiedEventsMap.get(ev.getPrimaryKey().toString() + ev.getInstanceNumber()).contains(ev.getMessage().getQuqueStatus())))
				.collect(Collectors.toList());
		List<EventData> completeEvents = new ArrayList<EventData>();
		for (EventData e : removedEvent) {
			NotifierUtil.getRoutingPointStatus(e).orElse("");
			if (e.getMessage().getInstanceStatus().contentEquals(NotifierUtil.COMPLETE_MESSAGE_STATUS)) {
				completeEvents.add(e);
			}
		}
		removedEvent.removeAll(completeEvents);

		allEvent.removeAll(removedEvent);

		allEvent.stream().forEach(e -> {
			if (!e.getInstanceNumber().equals("0")) {
				List<String> queues = notifiedEventsMap.get(e.getPrimaryKey().toString() + e.getInstanceNumber());
				if (queues == null) {
					queues = new ArrayList<String>();
				}
				queues.add(e.getMessage().getQuqueStatus());
				notifiedEventsMap.put(e.getPrimaryKey().toString() + e.getInstanceNumber(), queues);
			}

		});

		if (!removedEvent.isEmpty()) {
			LOGGER.debug("there is a duplicate event, will be remove  ");
			eventsDAO.updateEvents(removedEvent, MessageStatus.SENT.getStatus());
			try {
				eventsDAO.removeSentMessages();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		LOGGER.debug("No duplicate event found");
		return allEvent;

	}

}
