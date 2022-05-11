package com.eastnets.enGpiLoader.source;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiLoader.DAO.LoaderDAO;
import com.eastnets.enGpiLoader.bulder.GpiQueryBulder;
import com.eastnets.enGpiLoader.config.AppConfigBean;
import com.eastnets.enGpiLoader.notification.Notifaction;
import com.eastnets.enGpiLoader.service.GpiService;
import com.eastnets.enGpiLoader.utility.DataSourceParser;
import com.eastnets.enGpiLoader.utility.GpiDirectory;
import com.eastnets.service.ServiceLocator;
public class SaaSource implements MessageSource {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 5275049801457064994L;
	private AppConfigBean appConfigBean = null;
	private ServiceLocator serviceLocator;
	private LoaderDAO loaderDAO;
	private  Map<String, GpiDirectory> gpiDirectoryMap ;
	private static final String GPI_PRODUCT_ID = "25";

	String query = "";
	private static final Logger LOGGER = Logger.getLogger(SaaSource.class);

	public void init() {
		try {
			//gpiDirectoryMap=loaderDAO.getGpiDirectory();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void startSourceMonotoring(DataSourceParser dataSourceParser ) throws Exception {
		checkGPILicense();
		LOGGER.info("Start gpi Notifier Job ....");
		query = GpiQueryBulder.buildQuery(dataSourceParser,appConfigBean.getEnableDebugMode()); 
		LOGGER.debug("End build Query ... ");   
		LOGGER.debug("Start getting Pending Message From DB ... "); 

		List<NotifierMessage> gpiMessegesPendenig =null;
		List<NotifierMessage> resultMessages=null;
		while (true) {
			try {
				gpiMessegesPendenig = serviceLocator.getViewerService().getPendingGpiMesg(query,dataSourceParser.getxMLNotifierConfigHelper().getMailAttempts(),dataSourceParser.getxMLNotifierConfigHelper().getConfirmAttempts(),dataSourceParser.getxMLNotifierConfigHelper().getTimeInterval());
			} catch (Exception e) {
				serviceLocator.getViewerService().insertIntoErrorld("GPINotifier", "Failed", "GPINotifier", "while getPendingGpiMesg() SQL Exception ", null);
				e.printStackTrace(); 
			}
			try {
				resultMessages = GpiService.compareGpiMessages(gpiMessegesPendenig,gpiDirectoryMap,dataSourceParser.getxMLNotifierConfigHelper().getDurationTime()); 
			} catch (Exception e) {
				serviceLocator.getViewerService().insertIntoErrorld("GPINotifier", "Failed", "GPINotifier", "while compareGpiMessages()  Exception ", null);
				e.printStackTrace();
			} 

			if(resultMessages != null  && !resultMessages.isEmpty()){ 
				LOGGER.debug("Start notification cycle for pending messages    ...");  
				new Notifaction().notify(dataSourceParser,resultMessages,serviceLocator,loaderDAO,appConfigBean,null); 
				LOGGER.debug("End notification cycle for pending Messages    ...");

			}else{ 
				LOGGER.debug("No message pending in queue ..."); 

			}

			try {  
				Long sleepPeriod=dataSourceParser.getxMLNotifierConfigHelper().getSleepPeriod();
				LOGGER.info("Notifier  will sleep for " + sleepPeriod + " Seconds");
				Thread.sleep((sleepPeriod) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



	public void checkGPILicense() {
		if (!serviceLocator.getLicenseService().checkLicense()) {
			serviceLocator.getViewerService().insertIntoErrorld("GPINotifier", "Failed", "License", "NO LICENCSE", null);
			LOGGER.info("gpi Notifier is not licensed.");
			System.exit(1);
			return;
		}
		if (!serviceLocator.getLicenseService().checkProduct(GPI_PRODUCT_ID)) {
			serviceLocator.getViewerService().insertIntoErrorld("GPINotifier", "Failed", "License", "NO LICENCSE", null);
			LOGGER.info("GPI Notifier is not licensed.");
			System.exit(1);
			return;
		} 
		LOGGER.info("gpi Notifier license is checked successfuly..."); 

	}
	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}



	public Map<String, GpiDirectory> getGpiDirectoryMap() {
		return gpiDirectoryMap;
	}



	public void setGpiDirectoryMap(Map<String, GpiDirectory> gpiDirectoryMap) {
		this.gpiDirectoryMap = gpiDirectoryMap;
	}





}
