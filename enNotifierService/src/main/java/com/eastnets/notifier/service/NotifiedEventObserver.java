package com.eastnets.notifier.service;

import java.util.List;

import com.eastnets.notifier.domain.EventData;

public interface NotifiedEventObserver {

	public List<EventData> removeAlreadySentEvent(List<EventData> allEvent);

}
