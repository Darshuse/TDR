package com.eastnets.watchdog.dumper;

import java.util.concurrent.BlockingQueue;

import com.eastnets.entities.Jrnl;
import com.eastnets.entities.Mesg;
import com.eastnets.watchdog.config.WatchdogConfiguration;

public class DBDumper implements Runnable {

	private BlockingQueue<Mesg> inputBlockingQueue;
	private BlockingQueue<Jrnl> outputBlockingQueue;
	private WatchdogConfiguration configBean;

	public DBDumper() {
	}

	public BlockingQueue<Mesg> getInputBlockingQueue() {
		return inputBlockingQueue;
	}

	public void setInputBlockingQueue(BlockingQueue<Mesg> inputBlockingQueue) {
		this.inputBlockingQueue = inputBlockingQueue;
	}

	public BlockingQueue<Jrnl> getOutputBlockingQueue() {
		return outputBlockingQueue;
	}

	public void setOutputBlockingQueue(BlockingQueue<Jrnl> outputBlockingQueue) {
		this.outputBlockingQueue = outputBlockingQueue;
	}

	public WatchdogConfiguration getConfigBean() {
		return configBean;
	}

	public void setConfigBean(WatchdogConfiguration configBean) {
		this.configBean = configBean;
	}

	public void run() {
		System.out.println("DBDumper Run");
	}

}
