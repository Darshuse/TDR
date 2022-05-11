
package com.eastnets.notifier.repository;

import java.util.List;

import com.eastnets.notifier.domain.EventData;
import com.eastnets.notifier.domain.NotifierEventObserver;

public interface ENEventsDAO {

	/**
	 * Fetch new message starting from last read sequence number
	 * 
	 * @param lastReadSequence
	 * @param bulkSize
	 * @return list of events meta data
	 */
	public List<EventData> fetchNewEvents(int bulkSize);

	public boolean updateEvents(List<EventData> eventMetadataList, int status);

	public void bulkInsertEventHistory(List<EventData> eventMetadataList);

	public void bulkInsertNotifierEventObserver(List<NotifierEventObserver> eventObserverList);

	public void getAllRoutingPointByMsgPrimaryKey();

	public boolean removetNotifierEventObserver(int aid, int umidl, int umidh);

	public boolean deleteNonIntervDataFromRUpdateNotifier();

	/**
	 * 
	 * @return
	 */
	public boolean removeSentMessages();

	/*
	 * This method will called when the application stopped
	 */
	public void changeProcessingToNewEvents();

	public boolean upadteNotifierStatusBeforePullMessages();

}
