package com.eastnets.calculated.measures.dao;

import java.util.List;

import com.eastnets.message.summary.Bean.MessageSummaryDTO;


public interface CalculatedMeasuresReaderDAO {

	List<MessageSummaryDTO> getMessageSummaryTextFields();

}
