package com.eastnets.notifier.util;

import static org.apache.commons.lang.StringUtils.substringsBetween;

import java.util.Optional;

import com.eastnets.notifier.domain.EventData;

public class NotifierUtil {

	private static final String NACKED = "Nacked";
	private static final String INTERVINTION_TEXT_CREATED = "Created at";
	private static final String INTERVINTION_TEXT_ROUTED = "Routed from";
	private static final String INTERVINTION_TEXT_MANUALLY_DISPOSED = "Manually disposed from";
	private static final String INTERVINTION_TEXT_DISPOSED = "Disposed from";
	private static final String INTERVINTION_TEXT_COMPLETED = "Completed in";
	private static final String INTERVINTION_TEXT_COMPLETED_AT = "Completed at";
	private static final String INTERVINTION_TEXT_REACTIVATED = "Reactivated in";
	private static final String INTERVINTION_TEXT_MANUALLY_COMPLETED = "Manually completed";
	public static final String LIVE_MESSAGE_STATUS = "LIVE";
	public static final String COMPLETE_MESSAGE_STATUS = "COMPLETED";

	public static Optional<String> getRoutingPointStatus(EventData event) {
		String[] betweenStr;
		if (event.getMessage().getMergedText().startsWith(INTERVINTION_TEXT_CREATED)) {
			betweenStr = substringsBetween(event.getMessage().getMergedText(), "[", "]");
			event.getMessage().setInstanceStatus(LIVE_MESSAGE_STATUS);
			return Optional.of(betweenStr[0]);
		} else if (event.getMessage().getMergedText().startsWith(INTERVINTION_TEXT_ROUTED)) {
			betweenStr = substringsBetween(event.getMessage().getMergedText(), "[", "]");
			event.getMessage().setInstanceStatus(LIVE_MESSAGE_STATUS);
			return Optional.of(betweenStr[1]);
		} else if (event.getMessage().getMergedText().startsWith(INTERVINTION_TEXT_MANUALLY_DISPOSED)) {
			betweenStr = substringsBetween(event.getMessage().getMergedText(), "[", "]");
			event.getMessage().setInstanceStatus(LIVE_MESSAGE_STATUS);
			return Optional.of(betweenStr[1]);
		} else if (event.getMessage().getMergedText().startsWith(INTERVINTION_TEXT_COMPLETED)) {
			betweenStr = substringsBetween(event.getMessage().getMergedText(), "[", "]");
			event.getMessage().setInstanceStatus(COMPLETE_MESSAGE_STATUS);
			return Optional.of(betweenStr[0]);
		} else if (event.getMessage().getMergedText().startsWith(INTERVINTION_TEXT_DISPOSED)) {
			betweenStr = substringsBetween(event.getMessage().getMergedText(), "[", "]");
			event.getMessage().setInstanceStatus(LIVE_MESSAGE_STATUS);
			return Optional.of(betweenStr[1]);
		} else if (event.getMessage().getMergedText().startsWith(INTERVINTION_TEXT_MANUALLY_COMPLETED)) {
			betweenStr = substringsBetween(event.getMessage().getMergedText(), "[", "]");
			event.getMessage().setInstanceStatus(COMPLETE_MESSAGE_STATUS);
			return Optional.of(betweenStr[0]);
		} else if (event.getMessage().getMergedText().startsWith(INTERVINTION_TEXT_REACTIVATED)) {
			betweenStr = substringsBetween(event.getMessage().getMergedText(), "[", "]");
			event.getMessage().setInstanceStatus(LIVE_MESSAGE_STATUS);
			return Optional.of(betweenStr[0]);
		} else if (event.getMessage().getMergedText().startsWith(INTERVINTION_TEXT_COMPLETED_AT)) {
			betweenStr = substringsBetween(event.getMessage().getMergedText(), "[", "]");
			event.getMessage().setInstanceStatus(COMPLETE_MESSAGE_STATUS);
			return Optional.of(betweenStr[0]);
		}
		return Optional.empty();
	}

	public static void main(String[] args) {

		String s = "Completed";
		System.out.println(s.startsWith(COMPLETE_MESSAGE_STATUS));
	}

	public static Optional<String> getNackedFromIntervintionText(String interventionText) {
		if (interventionText.contains(NACKED)) {
			return Optional.of("NACK");
		}
		return Optional.empty();

	}

}
