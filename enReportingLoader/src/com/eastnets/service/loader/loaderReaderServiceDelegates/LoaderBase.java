package com.eastnets.service.loader.loaderReaderServiceDelegates;

import java.sql.Connection;
import java.util.List;

import com.eastnets.domain.loader.LoaderMessage;

public abstract class LoaderBase {

	public abstract List<LoaderMessage> restoreMessages(String aid) throws Exception;
	public abstract List<LoaderMessage> readMessages(Connection lockCon,String aid) throws Exception;
}