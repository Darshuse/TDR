package com.eastnets.service.loader.helper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import com.eastnets.domain.loader.LoaderMessage;

public interface MessageReader {

	public void init()   ;

	public List<LoaderMessage> readMessages(Connection lockCon,String aid) throws Exception;

	/**
	 * 
	 * @param messagesIds
	 *            it's unique Id for loader message
	 * 
	 * @return list of messages that stored in processing table and still they
	 *         exist there, this well happened only if the loader still working
	 *         and suddenly exist for any reason
	 * 
	 */
	public List<LoaderMessage> restoreMessages(List<BigDecimal> messagesIds) throws Exception;

	public void finish();
 
}
