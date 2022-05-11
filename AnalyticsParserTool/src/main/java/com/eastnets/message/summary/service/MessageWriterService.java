package com.eastnets.message.summary.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.message.summary.Bean.MessageSummaryDTO;
import com.eastnets.message.summary.dao.MessageWriterDAO;

@Service
public class MessageWriterService {

	private static final Logger LOGGER = Logger.getLogger(MessageWriterService.class);
	@Autowired
	MessageWriterDAO messageWriterDAO;

	@Transactional
	public void writeMessages(List<MessageSummaryDTO> messageSummaryDTOs) {

		try {

			LOGGER.debug("Inserting and Updating Messages");

			List<MessageSummaryDTO> messagesToBeInserted = new ArrayList<>();
			List<MessageSummaryDTO> messagesToBeUpdated = new ArrayList<>();

			for (MessageSummaryDTO messageSummaryDTO : messageSummaryDTOs) {

				if (messageSummaryDTO.getFetchStatus() == 0) {
					messagesToBeInserted.add(messageSummaryDTO);
				} else if (messageSummaryDTO.getFetchStatus() == 2) {
					messagesToBeUpdated.add(messageSummaryDTO);
				}
			}

			if (!messagesToBeInserted.isEmpty()) {
				messageWriterDAO.insertMessages(messagesToBeInserted);

			}
			if (!messagesToBeUpdated.isEmpty()) {
				messageWriterDAO.updateMessages(messagesToBeUpdated);

			}

			updateMessageStatus(messageSummaryDTOs);
		} catch (Exception e) {
			LOGGER.error("Error in Wrtting messages service");
			e.printStackTrace();
		}
	}

	public void updateMessageStatus(List<MessageSummaryDTO> messageSummaryDTOs) {

		LOGGER.trace("Update Messages Status");
		messageWriterDAO.updateMessageStatus(messageSummaryDTOs);
	}

}
