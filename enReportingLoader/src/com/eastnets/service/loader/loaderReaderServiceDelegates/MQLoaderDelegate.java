package com.eastnets.service.loader.loaderReaderServiceDelegates;

import java.sql.Connection;
import java.util.List;

import com.eastnets.dao.loader.LoaderDAO;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.service.loader.helper.MQMessageReader;

/**
 * Class created to contain all database reading operations from client MQ in case of MQ reader
 * @author OAlKhalili
 *
 */
public class MQLoaderDelegate extends LoaderBase {
	
	private LoaderDAO loaderDAO;
	protected MQMessageReader reader;
	
	public List<LoaderMessage> restoreMessages(String aid) throws Exception {
		return loaderDAO.restoreMQProcessingRows(aid);
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
	
	public MQMessageReader getReader() {
		return reader;
	}

	public void setReader(MQMessageReader reader) {
		this.reader = reader;
	}
}
