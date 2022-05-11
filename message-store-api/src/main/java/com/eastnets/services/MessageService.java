package com.eastnets.services;

import com.eastnets.entities.Mesg;
import com.eastnets.models.MessageRequest;
import com.eastnets.models.MessageResponse;

public interface MessageService {
	
	public Mesg getMesgs();

	public MessageResponse saveMessage(MessageRequest message) throws Exception;
}
