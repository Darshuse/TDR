
package com.eastnets.textbreak.readers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.eastnets.resultbean.TextBreakResultBean;
import com.eastnets.textbreak.bean.DAOFactoryTb;
import com.eastnets.textbreak.bean.SourceData;
import com.eastnets.textbreak.bean.TextBreakConfig;
import com.eastnets.textbreak.service.TextBreakRepositoryService;
import com.eastnets.textbreak.utility.Constants;

/**
 * 
 * @author MKassab
 *
 */

@Service
public class DbReader extends DataReader {

	@Autowired
	private TextBreakRepositoryService textBreakRepositoryService;
	@Autowired
	private TextBreakConfig textBreakConfig;

	@Autowired
	DAOFactoryTb daoFactory;

	List<SourceData> sourceDateList = new ArrayList<SourceData>();
	private static final Logger LOGGER = Logger.getLogger(DbReader.class);
	Integer MAX_BATCH_SIZE = 10000;
	Date fromDate = null;

	/**
	 * Responsible for read messages that will parsing,this work for offline Textbreak only
	 * 
	 * @return List<SourceData>
	 */
	@Override
	public List<SourceData> readMessages() {
		List<TextBreakResultBean> textBreakMessages = null;
		sourceDateList = new ArrayList<SourceData>();
		LOGGER.debug("Read messages from Database");
		try {
			Boolean partitioned = textBreakConfig.getPartitioned();
			Integer messageNumber = Integer.parseInt(textBreakConfig.getMessageNumber());
			messageNumber = (messageNumber > MAX_BATCH_SIZE) ? MAX_BATCH_SIZE : messageNumber;
			getFromDate();
			Date toDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(textBreakConfig.getToDate());

			if (textBreakConfig.isRecovery()) {
				textBreakMessages = textBreakRepositoryService.findRecoveryMessages(fromDate, toDate, "Swift", textBreakConfig.getAid(), PageRequest.of(0, messageNumber), partitioned, messageNumber);
			} else {
				textBreakMessages = textBreakRepositoryService.findTextBreakMessages(fromDate, toDate, "Swift", textBreakConfig.getAid(), PageRequest.of(0, messageNumber), Boolean.parseBoolean(textBreakConfig.getAllMessages()), partitioned,
						textBreakConfig.isOnlinedecompos(), messageNumber);
			}
		} catch (ParseException e) {
			LOGGER.debug("Errore while readeing message from DataBase");
			daoFactory.getTextBreakDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "",
					"Error when reading messages from database  :: From Date : " + fromDate + " aid : " + textBreakConfig.getAid());
		}
		fillDataSource(textBreakMessages);
		return sourceDateList;
	}

	public static void main(String[] args) {
		/*
		 * LocalDate today = LocalDate.now(); System.out.println("\nCurrent Date: "+today); System.out.println("10 days before today will be "+today.plusDays(-10));
		 */
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, -1);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		dt = c.getTime();

		String strDate = dateFormat.format(dt);
		System.out.println("Converted String: " + strDate);
	}

	/**
	 * Check if from date Empty will take start data from days number
	 * 
	 * @return Date
	 */
	private Date getFromDate() throws ParseException {
		if (fromDate == null) {
			if (!textBreakConfig.getFromDate().isEmpty()) {
				fromDate = (textBreakConfig.getFromDate() == null || textBreakConfig.getFromDate().isEmpty()) ? new Date() : new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(textBreakConfig.getFromDate());
			} else {
				fromDate = getDateFromDaysNumber();
			}
		}
		return fromDate;
	}

	/**
	 * Get Start Date from Days number
	 * 
	 * @return Date
	 */
	private Date getDateFromDaysNumber() {
		Integer daysBefore = Integer.parseInt(textBreakConfig.getDayNumber());
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, -daysBefore);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		dt = c.getTime();
		return dt;
	}

	/**
	 * Responsible for read messages for restore phase
	 * 
	 * We need this method in case of fauilar , get all message with parsingStatus=0
	 * 
	 * @return List<SourceData>
	 */
	@Override
	public List<SourceData> restoreMessages() {
		LOGGER.debug("Restore messages from Database");
		List<TextBreakResultBean> textBreakRestoreMessages = null;
		sourceDateList = new ArrayList<SourceData>();

		LOGGER.debug("Number of messgaes to restore : " + textBreakRestoreMessages.size());
		fillDataSource(textBreakRestoreMessages);
		return sourceDateList;

	}

	/**
	 * Responsible for fill TextBreakResultBean into SourceData "SourceData is class sharde for every phase (Reader,Parse,Write) "
	 * 
	 * @return void
	 */
	private void fillDataSource(List<TextBreakResultBean> textBreakMessages) {
		textBreakMessages.forEach(msg -> {
			SourceData data = new SourceData();
			data.setAid(msg.getAid());
			data.setMesgUmidh(msg.getUmidh());
			data.setMesgUmidl(msg.getUmidl());
			data.setMesgType(msg.getMesgType());
			data.setMesgCreaDateTime(msg.getMesgCreaDateTime());
			data.setTextDataBlock(msg.getTextDataBlock());
			data.setStxVersion(msg.getMesgSyntaxTableVer());
			sourceDateList.add(data);
		});
	}

	public TextBreakConfig getTextBreakConfig() {
		return textBreakConfig;
	}

	public void setTextBreakConfig(TextBreakConfig textBreakConfig) {
		this.textBreakConfig = textBreakConfig;
	}

	public TextBreakRepositoryService getTextBreakRepositoryService() {
		return textBreakRepositoryService;
	}

	public void setTextBreakRepositoryService(TextBreakRepositoryService textBreakRepositoryService) {
		this.textBreakRepositoryService = textBreakRepositoryService;
	}

}
