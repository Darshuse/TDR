package com.eastnets.service.loader;


import java.util.Observable;
import java.util.concurrent.BlockingQueue;


import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.enReportingLoader.config.AppConfigBean;
import com.eastnets.service.ServiceLocator;

public abstract class LoaderServiceImp extends Observable implements LoaderService {

	protected BlockingQueue<LoaderMessage> inputBlockingQueue;
	protected BlockingQueue<LoaderMessage> outputBlockingQueue;
	protected AppConfigBean appConfigBean = null;
	protected ServiceLocator baseServiceLocator;


	public LoaderServiceImp() {
		inputBlockingQueue = null;
		outputBlockingQueue = null;
	}

	public LoaderServiceImp(BlockingQueue<LoaderMessage> inblockingQueue, BlockingQueue<LoaderMessage> outblockingQueue) {
		this.inputBlockingQueue = inblockingQueue;
		this.outputBlockingQueue = outblockingQueue;
	}

	public BlockingQueue<LoaderMessage> getInputBlockingQueue() {
		return inputBlockingQueue;
	}

	public void setInputBlockingQueue(BlockingQueue<LoaderMessage> inputBlockingQueue) {
		this.inputBlockingQueue = inputBlockingQueue;
	}

	public BlockingQueue<LoaderMessage> getOutputBlockingQueue() {
		return outputBlockingQueue;
	}

	public void setOutputBlockingQueue(BlockingQueue<LoaderMessage> outputBlockingQueue) {
		this.outputBlockingQueue = outputBlockingQueue;
	}

	public ServiceLocator getBaseServiceLocator() {
		return baseServiceLocator;
	}

	public void setBaseServiceLocator(ServiceLocator baseServiceLocator) {
		this.baseServiceLocator = baseServiceLocator;
	}

	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}
}
