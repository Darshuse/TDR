package com.eastnets.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
	private static final DateTimeFormatter DATETIME_NUMBER_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");
	private static final DateTimeFormatter DATE_NUMBER_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

	public static Timestamp convertStringToTimestamp(String strDate) throws Exception {

		LocalDateTime dateTime = LocalDateTime.parse(strDate, DATETIME_FORMATTER);
		Timestamp timestamp = Timestamp.valueOf(dateTime);
		return timestamp;

	}

	public static LocalDateTime convertStringToLocalDateTime(String strDate) throws Exception {

		LocalDateTime localDateTime = LocalDateTime.parse(strDate, DATETIME_NUMBER_FORMATTER);
		return localDateTime;

	}

	public static LocalDate convertStringToLocalDate(String strDate) throws Exception {

		LocalDate localDate = LocalDate.parse(strDate, DATE_NUMBER_FORMATTER);
		return localDate;
	}

}
