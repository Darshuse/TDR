package com.eastnets.dao.events;

import java.util.List;

import com.eastnets.domain.events.ENEventMetadata;

public interface ENEventsDAO {

	/**
	 * Fetch new message starting from last read sequence number
	 * 
	 * @param lastReadSequence
	 * @param bulkSize
	 * @return list of events meta data
	 */
	public List<ENEventMetadata> fetchNewEvents(int bulkSize);

	public void updateEvents(List<ENEventMetadata> eventMetadataList, int status);

	public void bulkInsertEventHistory(List<ENEventMetadata> eventMetadataList);

	/**
	 * 
	 * This Method will remove the events from the history if and only if case of sending failure happened.
	 * 
	 * @param eventMetadataList
	 * @return
	 */
	public void removeHistoryEvents(List<ENEventMetadata> eventMetadataList);

	public void removeSentMessages();

	/*
	 * This method will called when the application stopped
	 */
	public void changeProcessingToNewEvents();

}
