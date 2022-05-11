
package com.eastnets.notifier.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobExecutor implements DisposableBean {

	@Autowired
	private EventsObserver eventsObserver;

	public void start() {
		// taskExecutor.execute(messagesRemover);
	}

	public EventsObserver getEventsObserver() {
		return eventsObserver;
	}

	public void setEventsObserver(EventsObserver eventsObserver) {
		this.eventsObserver = eventsObserver;
	}

	@Override
	public void destroy() throws Exception {
		eventsObserver.changeEventsToNew();
	}

}
