/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */
package com.eastnets.resilience.xmldump.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * thread pooling + blocking queue so that execution will halt when max pool
 * arrived
 * 
 * @author EHakawati
 * 
 */
public class WorkQueue {

	private final BlockingQueue<Runnable> blockingQueue;
	private final RejectedExecutionHandler rejectedExecutionHandler;
	private final ExecutorService executorService;

	private final int maxThread;

	/**
	 * Constructor
	 * 
	 * @param maxThread
	 */
	public WorkQueue(int maxThread) {
		this.maxThread = maxThread;

		// Execution blocker
		blockingQueue = new ArrayBlockingQueue<Runnable>(1);
		rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
		executorService = new ThreadPoolExecutor(1, this.maxThread, 0L, TimeUnit.MILLISECONDS, blockingQueue,
				rejectedExecutionHandler);

	}

	/**
	 * Add new runnable to the thread pool
	 * 
	 * @param runnable
	 */
	public void execute(Runnable runnable) {

		if (!executorService.isShutdown()) {
			executorService.submit(runnable);
		} else {
			// -- TODO: throw exception
		}

	}

	/**
	 * Join the execution via wait the pooled threads to finish
	 * 
	 * @throws InterruptedException
	 */
	public void joinExecution() throws InterruptedException {

		executorService.shutdown();
		while (executorService.isTerminated() == false) {
			executorService.awaitTermination(1, TimeUnit.SECONDS);
		}

	}
}
