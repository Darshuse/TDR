package com.eastnets.service.loader.helper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.eastnets.domain.loader.LoaderMessage;

public class MQMessageReader implements MessageReader {

	private LinkedBlockingQueue<LoaderMessage> readerInQueue;

	@Override
	public void init() {
	}

	/**
	 * read messages from MQ (Example: IBM MQ, Apache Active MQ)
	 */
	@Override
	public List<LoaderMessage> readMessages(Connection lockCon, String aid) throws Exception {

		int readSize = readerInQueue.size();
		List<LoaderMessage> mesgList = new ArrayList<LoaderMessage>(readSize);

		while (!readerInQueue.isEmpty() && mesgList.size() <= readSize) {
			LoaderMessage message = readerInQueue.poll();
			mesgList.add(message);
		}

		return mesgList;
	}

	/**
	 * Restore messages from our cache tables in-case of shutdown in the middle of integration
	 */
	@Override
	public List<LoaderMessage> restoreMessages(List<BigDecimal> messagesIds) throws Exception {
		throw new Exception("Restore in case of MQ connector should be done from LoaderDAOImp.restoreMQProcessingRows");
	}

	@Override
	public void finish() {
	}

	public LinkedBlockingQueue<LoaderMessage> getReaderInQueue() {
		return readerInQueue;
	}

	public void setReaderInQueue(LinkedBlockingQueue<LoaderMessage> readerInQueue) {
		this.readerInQueue = readerInQueue;
	}
}
