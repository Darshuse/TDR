
package com.eastnets.textbreak.factory;

import java.sql.Connection;

import com.eastnets.textbreak.enums.ParserType;
import com.eastnets.textbreak.parser.MessageParser;
import com.eastnets.textbreak.service.TextBreakRepositoryService;

public class ParserFactory {

	private ParserFactory() {
		throw new IllegalStateException("ParserFactory class");
	}

	public static MessageParser createParser(ParserType parserType, Connection conn, TextBreakRepositoryService textBreakRepositoryService) {
		MessageParser messageParser = null;

		return messageParser;
	}

}
