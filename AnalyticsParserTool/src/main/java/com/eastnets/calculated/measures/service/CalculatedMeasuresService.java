package com.eastnets.calculated.measures.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.calculated.measures.dao.CalculatedMeasureWriterDAO;
import com.eastnets.message.summary.Bean.MessageSummaryDTO;
import com.eastnets.message.summary.configuration.ReaderConfigDAO;

@Service
public class CalculatedMeasuresService {

	private static final Logger LOGGER = LogManager.getLogger(CalculatedMeasuresService.class);

	@Autowired
	public CalculatedMeasureWriterDAO calculatedMeasureWriterDAO;

	@Autowired
	public ReaderConfigDAO readerConfigDAO;

	public void startCalculatedMeasures() {

		List<MessageSummaryDTO> messageSummaryList = new ArrayList<>();

		try {
			messageSummaryList = readerConfigDAO.getCalculatedMeasuresDAO().getMessageSummaryTextFields();
			if (messageSummaryList != null && !messageSummaryList.isEmpty()) {
				calculatedMeasureWriterDAO.writeCalculatedMeasures(messageSummaryList);
			}

		} catch (Exception e) {
			LOGGER.trace("No data returned from MessageSummary");
			LOGGER.error(e);

		}

	}
}
