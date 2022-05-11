package com.eastnets.service.common;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.AsynchronousFillHandle;

/**
 * 
 * @author MAlkhateeb This Class Responsible of filling Jasper report in
 *         separate thread by using
 *         net.sf.jasperreports.engine.fill.AsynchronousFillHandle and we will
 *         exploit the advantage of this call that well enable us to cancel
 *         filling the report
 */
public class JasperReportAsyncFillHandler {

	private AsynchronousFillHandle asynchronousFillHandle;

	public AsynchronousFillHandle getAsynchronousFillHandle() {
		return asynchronousFillHandle;
	}

	public void setAsynchronousFillHandle(
			AsynchronousFillHandle asynchronousFillHandle) {
		this.asynchronousFillHandle = asynchronousFillHandle;
	}

	public void startFill() {
		this.asynchronousFillHandle.startFill();
	}

	/*
	 * return the thread that is responsible for filling this report through
	 * asynchronous fill handler
	 */
	private Thread getCurrentThreadExecutor(String fillHandlerName) {
		ThreadGroup root = Thread.currentThread().getThreadGroup();
		while (root.getParent() != null) {
			root = root.getParent();
		}

		final ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
		int nAlloc = mxBean.getThreadCount();
		int n = 0;
		Thread[] threads;
		do {
			nAlloc *= 2;
			threads = new Thread[nAlloc];
			n = root.enumerate(threads, true);
		} while (n == nAlloc);
		threads = java.util.Arrays.copyOf(threads, n);
		for (Thread thread : threads) {
			if (thread.getName().equals(fillHandlerName)) {
				return thread;
			}
		}
		return null;
	}

	public void cancelFill(String fillHandlerName) throws JRException {
		Thread currentThread = getCurrentThreadExecutor(fillHandlerName);
		if (currentThread != null) {
			currentThread.interrupt();
		}
		this.asynchronousFillHandle.cancellFill();
	}

}
