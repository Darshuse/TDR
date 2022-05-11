package com.eastnets.service.loader.loaderReaderServiceDelegates;

import java.util.ArrayList;
import java.util.List;

import com.eastnets.dao.loader.LoaderDAO;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.LoaderMessage.MessageSource;

/**
 * 
 * @author OAlKhalili
 *
 */
public class UnqueueMessagesDelegate {

	private LoaderDAO loaderDAO;

	public void removeProcessingRows(List<LoaderMessage> list, String aid) {

		List<LoaderMessage> databaseSourcedMessagesList = new ArrayList<LoaderMessage>();
		List<LoaderMessage> mqSourcedMessagesList = new ArrayList<LoaderMessage>();

		for (LoaderMessage loaderMessage : list) {
			if (loaderMessage.getMessageSource().equals(MessageSource.DATABASE)) {
				databaseSourcedMessagesList.add(loaderMessage);
			} else if (loaderMessage.getMessageSource().equals(
					MessageSource.XML)) {
				mqSourcedMessagesList.add(loaderMessage);
			}
		}

		handleDBMessageUnqueue(databaseSourcedMessagesList, aid);
		handleMQMessageUnqueue(mqSourcedMessagesList, aid);
	}

	private void handleMQMessageUnqueue(
			List<LoaderMessage> mqSourcedMessagesList, String aid) {
		if (!mqSourcedMessagesList.isEmpty()) {
			Long id=new Long(0);
			loaderDAO.removeMQSourcedProcessingRows(mqSourcedMessagesList, aid,id);
		}
	}

	private void handleDBMessageUnqueue(
			List<LoaderMessage> databaseSourcedMessagesList, String aid) {
		if (!databaseSourcedMessagesList.isEmpty()) {
			loaderDAO.removeDatabaseSourcedProcessingRows(
					databaseSourcedMessagesList, aid);
		}
	}

	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}
}
