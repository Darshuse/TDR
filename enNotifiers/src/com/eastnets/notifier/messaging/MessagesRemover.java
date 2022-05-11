package com.eastnets.notifier.messaging;

import java.io.Serializable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.eastnets.dao.events.ENEventsDAO;

public class MessagesRemover implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6860914824717873521L;
	/*
	 * This for end user in hours
	 */
	private String sleepTime;
	private long sleepTimeInMils;
	private ENEventsDAO eventsDAO;
	private boolean notFinished = true;
	private static final Logger LOGGER = LogManager.getLogger(MessagesRemover.class);

	public void init() {
		int sleepTimeInHours = Integer.parseInt(sleepTime);
		sleepTimeInMils = sleepTimeInHours * 60 * 60 * 1000;
	}

	public String getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}

	public long getSleepTimeInMils() {
		return sleepTimeInMils;
	}

	public void setSleepTimeInMils(long sleepTimeInMils) {
		this.sleepTimeInMils = sleepTimeInMils;
	}

	public ENEventsDAO getEventsDAO() {
		return eventsDAO;
	}

	public void setEventsDAO(ENEventsDAO eventsDAO) {
		this.eventsDAO = eventsDAO;
	}

	public void destory() {
		LOGGER.info("Message Remover Stop working ");
		notFinished = false;
	}

	@Override
	public void run() {
		while (notFinished) {
			try {
				LOGGER.debug(" :: Removing Messages :: ");
				eventsDAO.removeSentMessages();
				Thread.sleep(sleepTimeInMils);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
