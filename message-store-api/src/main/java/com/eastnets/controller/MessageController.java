package com.eastnets.controller;

import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eastnets.models.MessageRequest;
import com.eastnets.models.MessageResponse;
import com.eastnets.services.MessageService;

@RestController
@RequestMapping("/messageStoreAPI")
public class MessageController {

	@Autowired
	MessageService messageService;

	private static final Logger LOGGER = LogManager.getLogger(MessageController.class);

	@PutMapping(value = "/saveMessage", consumes = { MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<MessageResponse> saveMessage(@Valid @RequestBody MessageRequest messageRequest) {
		MessageResponse messageResponser = new MessageResponse();
		try {
			LOGGER.info("received " + messageRequest.getSource() + " message with ID: " + messageRequest.getId());
			LOGGER.info("strat saving the Message " + messageRequest.getId());

			messageResponser = messageService.saveMessage(messageRequest);

			return new ResponseEntity<>(messageResponser, messageResponser.getStatusCode());
		} catch (Exception e) {
			LOGGER.error("an error occured " + e.getMessage());

			messageResponser.setStatusCode(HttpStatus.EXPECTATION_FAILED);
			messageResponser.setStatusMsg("an error occured " + e.getMessage());

			return new ResponseEntity<>(messageResponser, messageResponser.getStatusCode());
		}

	}

}
