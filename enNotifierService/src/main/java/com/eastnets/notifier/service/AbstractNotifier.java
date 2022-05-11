package com.eastnets.notifier.service;

import com.eastnets.notifier.exception.NotificationException;

public abstract class AbstractNotifier {

	public abstract void sendMessage(String content) throws NotificationException ;
	
}
