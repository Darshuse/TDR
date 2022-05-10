package com.eastnets.watchdog.reader;

import java.util.List;

import com.eastnets.entities.WDMessageSearchRequest;

public class MQReader extends Reader {

	public void init() {
		System.out.println("MQ Reader");
	}

	@Override
	public void run() {
		System.out.println("MQ Reader Run");
	}

	@Override
	List<WDMessageSearchRequest> getWDMessagesSearchRequests() {
		return null;
	}

}
