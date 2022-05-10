package com.eastnets.watchdog.checker;

import org.apache.log4j.Logger;

public class CheckerService {
	private static final Logger LOGGER = Logger.getLogger(CheckerService.class);
	//
	//
	// BlockingQueue<Object> preProcessingQueue;
	// BlockingQueue<WDSearchResult> postProcessingQueue;
	// BlockingQueue<WDEmailNotification> emailsToBeDumpedQueue;
	//// BlockingQueue<EmailNotification> emailsToBeSentQueue;
	//
	// WatchdogConfiguration configBean;
	//
	// @Override
	// public void run() {
	// LOGGER.info("CheckerService service started");
	//
	// try {
	// if (configBean.getMode() == 0) {
	// LOGGER.info("Running CheckerService in mode 0");
	//
	// emailsToBeDumpedQueue.add((WDEmailNotification) preProcessingQueue.poll());
	//
	// // Events Only
	// } else if (configBean.getMode() == 1) {
	// LOGGER.info("Running CheckerService in mode 1");
	// while (true) {
	//
	// WDJrnlKey wdJrnlKey = (WDJrnlKey) preProcessingQueue.take();
	// checkJrnlKey(wdJrnlKey);
	//
	// }
	// // Messages Only
	// } else if (configBean.getMode() == 2) {
	// LOGGER.info("Running CheckerService in mode 2");
	// while (true) {
	//
	// WDSuperKey wdKey = (WDSuperKey) preProcessingQueue.take();
	// checkWDKey(wdKey);
	//
	// }
	//
	// // Email Notifications Only
	// } else if (configBean.getMode() == 3) {
	// LOGGER.info("Running CheckerService in mode 3");
	// while (true) {
	//// emailsToBeSentQueue.addAll(getAvailableEmailNotifications());
	// System.out.println("");
	// }
	// // Everything
	// }
	//
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// private List<MessageEmailNotification> getAvailableEmailNotifications() {
	// LOGGER.info("getting available email notifications ");
	// return serviceLocator.getWatchDogService().getAvailableMessagesEmailNotifications();
	// }
	//
	// private void checkWDKey(WDSuperKey wdKey) {
	//
	// }
	//
	// private void checkJrnlKey(WDJrnlKey wdJrnlKey) {
	// LOGGER.info("Checking jrnl key");
	// List<JrnlSearchRequest> requests = serviceLocator.getWatchDogService()
	// .checkWDJrnlKey(wdJrnlKey.getJrnlCompName(), wdJrnlKey.getJrnlEventNumber());
	//
	// for (JrnlSearchRequest searchRequest : requests) {
	// LOGGER.info("Checking jrnl key");
	// postProcessingQueue.add(prepareEventRequestResult(searchRequest, wdJrnlKey));
	// if (searchRequest.getEmail() != null) {
	// emailsToBeDumpedQueue.add(prepareEmailNotification(searchRequest, wdJrnlKey));
	// }
	// }
	// }
	//
	// private WDEmailNotification prepareEmailNotification(JrnlSearchRequest searchRequest, WDJrnlKey wdJrnlKey) {
	// WDEmailNotification emailNotification = new WDEmailNotification();
	// emailNotification.setAid(wdJrnlKey.getJrnlPK().getAid());
	// emailNotification.setDescription(searchRequest.getDescription());
	// emailNotification.setUsername(searchRequest.getEmail());
	// emailNotification.setUmidh(wdJrnlKey.getJrnlPK().getJrnlRevDateTime());
	// emailNotification.setUmidhl(wdJrnlKey.getJrnlPK().getJrnlSeqNumber());
	// emailNotification.setMessageType("EVT");
	// emailNotification.setWdID(searchRequest.getRequestID());
	// return emailNotification;
	// }
	//
	// private WDEventRequestResult prepareEventRequestResult(JrnlSearchRequest searchRequest, WDJrnlKey wdJrnlKey) {
	// WDEventRequestResult eventRequestResult = new WDEventRequestResult();
	// // eventRequestResult.setId(eventRequestResult.getId() + 1);
	// eventRequestResult.setAid(wdJrnlKey.getJrnlPK().getAid());
	// eventRequestResult.setDescription(searchRequest.getDescription());
	// eventRequestResult.setJrnlRevDateTime(wdJrnlKey.getJrnlPK().getJrnlRevDateTime());
	// eventRequestResult.setJrnlSeqNumber(wdJrnlKey.getJrnlPK().getJrnlSeqNumber());
	// eventRequestResult.setUsername(searchRequest.getUsername());
	// eventRequestResult.setProcessed(0);
	// eventRequestResult.setRequestId(searchRequest.getRequestID());
	// return eventRequestResult;
	// }
	//
	// public BlockingQueue<WDSearchResult> getpostProcessingQueue() {
	// return postProcessingQueue;
	// }
	//
	// public void setpostProcessingQueue(BlockingQueue<WDSearchResult> postProcessingQueue) {
	// this.postProcessingQueue = postProcessingQueue;
	// }
	//
	// public BlockingQueue<WDEmailNotification> getEmailsToBeDumpedQueue() {
	// return emailsToBeDumpedQueue;
	// }
	//
	// public void setEmailsToBeDumpedQueue(BlockingQueue<WDEmailNotification> emailsToBeDumpedQueue) {
	// this.emailsToBeDumpedQueue = emailsToBeDumpedQueue;
	// }
	//
	// public ServiceLocator getServiceLocator() {
	// return serviceLocator;
	// }
	//
	// public void setServiceLocator(ServiceLocator serviceLocator) {
	// this.serviceLocator = serviceLocator;
	// }
	//
	// public BlockingQueue<Object> getpreProcessingQueue() {
	// return preProcessingQueue;
	// }
	//
	// public void setpreProcessingQueue(BlockingQueue<Object> preProcessingQueue) {
	// this.preProcessingQueue = preProcessingQueue;
	// }
	//
	// public WatchdogConfiguration getConfigBean() {
	// return configBean;
	// }
	//
	// public void setConfigBean(WatchdogConfiguration configBean) {
	// this.configBean = configBean;
	// }
	//
	// public BlockingQueue<EmailNotification> getEmailsToBeSentQueue() {
	// return emailsToBeSentQueue;
	// }
	//
	// public void setEmailsToBeSentQueue(BlockingQueue<EmailNotification> emailsToBeSentQueue) {
	// this.emailsToBeSentQueue = emailsToBeSentQueue;
	// }
	//
	// public BlockingQueue<Object> getPreProcessingQueue() {
	// return preProcessingQueue;
	// }
	//
	// public void setPreProcessingQueue(BlockingQueue<Object> preProcessingQueue) {
	// this.preProcessingQueue = preProcessingQueue;
	// }
	//
	// public BlockingQueue<WDSearchResult> getPostProcessingQueue() {
	// return postProcessingQueue;
	// }
	//
	// public void setPostProcessingQueue(BlockingQueue<WDSearchResult> postProcessingQueue) {
	// this.postProcessingQueue = postProcessingQueue;
	// }

}
