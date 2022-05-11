package com.eastnets.enGpiLoader.DAO;



import java.util.Map;	

import com.eastnets.dao.DAO;
import com.eastnets.domain.viewer.DataSource;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiLoader.Beans.GbiHistoryBean;
import com.eastnets.enGpiLoader.utility.GpiDirectory;
import com.eastnets.enGpiLoader.utility.XMLNotifierConfigHelper;

public interface LoaderDAO extends DAO {

	public Map<String, GpiDirectory> getGpiDirectory();

	public void insertIntoLdGpiNotifersHistory(NotifierMessage notifierMessage);

	public void updateLdGpiNotifersHistory(NotifierMessage notifierMessage);
	public void getGpiNotifersHistory(NotifierMessage notifierMessage);

	public void  insertConfirmatioAttempt(NotifierMessage notifierMessage);
	public void  updateConfirmatioAttempt(NotifierMessage notifierMessage);
	public void insertMailAttempt(NotifierMessage notifierMessage);
	public void updateMailAttempt(NotifierMessage notifierMessage); 
	
	public GbiHistoryBean getmessageFromHistory(String id,DataSource dataSource);
	
	public Map<String,GbiHistoryBean> getHistoryMap(DataSource dataSource,XMLNotifierConfigHelper   notifierConfigHelper);
	
}
