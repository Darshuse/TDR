package com.eastnets.service.loader.loaderReaderServiceDelegates;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.eastnets.dao.loader.LoaderDAO;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.service.loader.helper.DatabaseMessageReader;

/**
 * Class created to contain all database reading operations from client database in case of database reader
 * @author OAlKhalili
 *
 */
public class DatabaseLoaderDelegate extends LoaderBase {
	
	private   LoaderDAO loaderDAO;
	protected DatabaseMessageReader reader;
	
	/**
	 * Make sure if there is any messages have not being processed
	 * @return
	 * @throws Exception
	 */
	public List<LoaderMessage> restoreMessages(String aid) throws Exception {
		List<LoaderMessage> messagesList = new ArrayList<LoaderMessage>();
		List<BigDecimal> messagesIds = loaderDAO.restoreProcessingRows(aid);
		if (!messagesIds.isEmpty()) {
			messagesList = reader.restoreMessages(messagesIds);
		}
		return messagesList;
	}
	
	public List<LoaderMessage> readMessages(Connection lockCon,String aid) throws Exception{
		return reader.readMessages(lockCon,aid);
	}
	
	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}
	
	public DatabaseMessageReader getReader() {
		return reader;
	}

	public void setReader(DatabaseMessageReader reader) {
		this.reader = reader;
	}
}
