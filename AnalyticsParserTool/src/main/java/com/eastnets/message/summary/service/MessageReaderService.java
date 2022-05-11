package com.eastnets.message.summary.service;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.audit.service.AuditService;
import com.eastnets.message.summary.Bean.MessageSummaryDTO;
import com.eastnets.message.summary.configuration.ReaderConfigDAO;

@Service
public class MessageReaderService {

	private static final Logger LOGGER = LogManager.getLogger(MessageReaderService.class);

	@Autowired
	ReaderConfigDAO readerConfigDAO;

	@Autowired
	AuditService auditService;

	public List<MessageSummaryDTO> getMessages() {

		LOGGER.trace("MessageReaderService: Getting Messages List");

		try {

			return readerConfigDAO.getMessageReaderDAO().getMessageSummaryMesgInformation();

		} catch (Exception e) {
			LOGGER.trace("Empty Messages List - Returning NULL");
			return null;
		}
	}

}
