package com.eastnets.notifier.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TagCreator {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	public static String createTag(String tagName, String value) {
		if (value != null) {
			return new StringBuilder().append('<').append(tagName).append('>').append(value).append("</")
					.append(tagName).append('>').toString();
		}
		return new StringBuilder().append('<').append(tagName).append('>').append("</")
				.append(tagName).append('>').toString();
	}

	public static String createTag(String tagName, BigInteger value) {
		if (value != null) {
			return new StringBuilder().append('<').append(tagName).append('>').append(value.toString()).append("</")
					.append(tagName).append('>').toString();
		}
		return new StringBuilder().append('<').append(tagName).append('>').append("</")
				.append(tagName).append('>').toString();
	}

	public static String createTag(String tagName, LocalDateTime value) {
		if (value != null) {
			return new StringBuilder().append('<').append(tagName).append('>').append(value.format(DATE_FORMAT))
					.append("</").append(tagName).append('>').toString();
		}
		return new StringBuilder().append('<').append(tagName).append('>').append("</")
				.append(tagName).append('>').toString();
	}

	public static String createTag(String tagName, BigDecimal value) {
		if (value != null) {
			return new StringBuilder().append('<').append(tagName).append('>').append(value.toString()).append("</")
					.append(tagName).append('>').toString();
		}
		return new StringBuilder().append('<').append(tagName).append('>').append("</")
				.append(tagName).append('>').toString();
	}

	public static String createTag(String tagName, Integer value) {
		if (value != null) {
			return new StringBuilder().append('<').append(tagName).append('>').append(value).append("</")
					.append(tagName).append('>').toString();
		}
		return new StringBuilder().append('<').append(tagName).append('>').append("</")
				.append(tagName).append('>').toString();
	}

	public static String createTag(String tagName, List<String> values) {
		if (values != null && !values.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			values.forEach((value) -> {
				builder.append('<').append(tagName).append('>').append(value).append("</").append(tagName).append('>')
				.toString();
			});
			return builder.toString();
		}
		return new StringBuilder().append('<').append(tagName).append('>').append("</")
				.append(tagName).append('>').toString();
	}

}
