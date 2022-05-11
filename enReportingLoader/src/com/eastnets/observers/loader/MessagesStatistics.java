package com.eastnets.observers.loader;

import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import com.eastnets.dao.loader.LoaderDAO;
import com.eastnets.domain.monitoring.UpdatedMessage;
import com.eastnets.enReportingLoader.Main;
import com.eastnets.enReportingLoader.config.AppConfigBean;

public class MessagesStatistics implements Observer, Runnable {

	private static int MINUTE = 60000;
	private static Logger LOGGER = Logger.getLogger(MessagesStatistics.class);
	private int numberOfMessages;
	private LoaderDAO loaderDAO;
	private AppConfigBean appConfigBean;

	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}

	@Override
	public void update(Observable o, Object arg) {
		numberOfMessages++;
	}

	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

	@Override
	public void run() {
		LOGGER.info("Start Messages Processing Estimator ");
		while (true) {
			LOGGER.info("Number of Processing Rows Per " + MINUTE + " :: " + numberOfMessages);

			loaderDAO.addNewStatistics(fillStatisticsMessage(),appConfigBean.getDatabaseType());
			// reset number of persisted message counter
			numberOfMessages = 0;
			try {
				Thread.sleep(MINUTE);
			} catch (InterruptedException e) {
				LOGGER.error("MessagesProcessingEstimator Thread had interpted");
				String errMessage="Failed";
				if(e.getMessage() != null){
					errMessage= (e.getMessage().length() >= 200) ? e.getMessage().substring(0, 199) : e.getMessage();
				}
				
				
				if(Main.isDbReader()){
					loaderDAO.insertIntoErrorld("DB Connector", "Failed", "", errMessage, "");		

				}else{
					loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessage, "");			

				}
				e.printStackTrace();
			}
		}
	}

	private UpdatedMessage fillStatisticsMessage() {

		UpdatedMessage message = new UpdatedMessage();
		message.setNewMsgCount(numberOfMessages + "");
		message.setID(appConfigBean.getSAAAid());
		message.setTotalTime(MINUTE / 60);
		// the following not important for the loader
		message.setOverrun("0");
		message.setUpdateMsgCount("0");
		message.setOnTimeCount("0");
		message.setNotifiedMsgCount("0");
		message.setErrorCount("0");
		message.setWarningCount("0");
		message.setFailedCount("0");
		message.setJrnlMsgCount("0");
		message.setOrigin("1");
		return message;
	}
}
