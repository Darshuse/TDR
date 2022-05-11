
package com.eastnets.textbreak.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.textbreak.bean.ParsedData;
import com.eastnets.textbreak.bean.SourceData;
import com.eastnets.textbreak.dao.TextBreakDaoImpl;
import com.eastnets.textbreak.enums.ParserType;
import com.eastnets.textbreak.parser.FinMessageParser;
import com.eastnets.textbreak.parser.MessageParser;
import com.eastnets.textbreak.parser.SysMessageParesr;

@Service
public class TextBreakParserService extends TextBreakService {
	private static final Logger LOGGER = Logger.getLogger(TextBreakParserService.class);

	MessageParser messageParser;
	Connection conn = null;

	@Autowired
	TextBreakDaoImpl textBreakDaoImpl;

	@Autowired
	private FinMessageParser finMessageParser;

	@Autowired
	private SysMessageParesr sysMessageParesr;

	public List<ParsedData> parseMessages(List<SourceData> sourceDataList) {
		List<ParsedData> parsedDateList = new ArrayList<>();
		for (SourceData data : sourceDataList) {
			if (data.getMessageParserType().equals(ParserType.FIN_MESSAGE_PARSER)) {
				ParsedData finMessagesDataParsed = finMessageParser.parse(data);
				parsedDateList.add(finMessagesDataParsed);
			} else if (data.getMessageParserType().equals(ParserType.SYS_MESSAGE_PARSER)) {
				ParsedData sysMessagesDataParsed = sysMessageParesr.parse(data);
				parsedDateList.add(sysMessagesDataParsed);
			}
		}

		return parsedDateList;

	}

	public MessageParser getFinMessageParser() {
		return finMessageParser;
	}

	public MessageParser getSysMessageParesr() {
		return sysMessageParesr;
	}

}
