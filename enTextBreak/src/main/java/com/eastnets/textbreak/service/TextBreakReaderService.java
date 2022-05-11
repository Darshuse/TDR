
package com.eastnets.textbreak.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.eastnets.textbreak.bean.DataSourceFactory;
import com.eastnets.textbreak.bean.SourceData;
import com.eastnets.textbreak.bean.TextBreakConfig;
import com.eastnets.textbreak.dao.TextBreakDaoImpl;
import com.eastnets.textbreak.readers.DataReader;

/**
 * 
 * @author MKassab
 *
 */

@Service
public class TextBreakReaderService extends TextBreakService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	DataSourceFactory dataSourceFactory;

	@Autowired
	private TextBreakConfig textBreakConfig;

	@Autowired
	TextBreakDaoImpl textBreakDaoImpl;

	private static final Logger LOGGER = Logger.getLogger(TextBreakReaderService.class);

	/**
	 * Responsible for Start Reader service
	 */
	public List<SourceData> readMesages() {
		List<SourceData> messages = new ArrayList<SourceData>();
		DataReader dataSource = dataSourceFactory.getDataSource();
		if (dataSource == null) {
			LOGGER.debug("You need enable offline TextBreak");
			return null;

		}
		List<SourceData> temp = dataSource.readMessages();

		for (SourceData sourceData : temp) {
			if (sourceData.getTextDataBlock() != null && !sourceData.getTextDataBlock().isEmpty()) {
				messages.add(sourceData);
			}
		}
		return messages;

	}

	public TextBreakConfig getTextBreakConfig() {
		return textBreakConfig;
	}

	public void setTextBreakConfig(TextBreakConfig textBreakConfig) {
		this.textBreakConfig = textBreakConfig;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
