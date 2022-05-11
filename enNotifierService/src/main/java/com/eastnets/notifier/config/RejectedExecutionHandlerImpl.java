package com.eastnets.notifier.config;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RejectedExecutionHandlerImpl.class);

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

		try {
			LOGGER.debug("try to put task to queue");
			executor.getQueue().put(r);
		} catch (InterruptedException e) {
			throw new RejectedExecutionException("There was an unexpected InterruptedException whilst waiting to add a Runnable in the executor's blocking queue", e);
		}

	}

}
