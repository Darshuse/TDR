package com.eastnets.enGPIParser.messageparsing;

import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.GPIMesgFields;
import com.eastnets.domain.viewer.MessageKey;
import com.eastnets.domain.viewer.MessageParsingResult;
import com.eastnets.domain.viewer.TextFieldData;

public class SRPParser implements MessageParser {

	GPIMesgFields gpiMessageFields = new GPIMesgFields();

	private static final Logger LOGGER = Logger.getLogger(CovParser.class);

	public GPIMesgFields parse(List<TextFieldData> fields, MessageKey key) {

		try {

			LOGGER.debug("gSRP Parsing Method");
			String value = null;

			if (fields.isEmpty()) {
				LOGGER.error("MAKE SURE THAT MESSAGES ARE DECOMPOSED. Text Fields are Empty, The message will NOT be parsed   Aid= " + key.getAid() + " UMIDH= " + key.getUmidh() + " UMIDL= " + key.getUnidl());
				gpiMessageFields.setMessageParsingResult(MessageParsingResult.NOTDECOMPOSED);
				return gpiMessageFields;
			}

			Integer fieldCode = 0;

			for (TextFieldData field : fields) {
				if (field.getFieldCode() == 79 || field.getFieldCode() == 76) {
					value = field.getValue();
					fieldCode = field.getFieldCode();
				}
			}

			if (value == null) {

				gpiMessageFields.setMessageParsingResult(MessageParsingResult.NOTCONFIRMATION);

				if (fieldCode == 79)
					LOGGER.warn("Field 79 is Empty, The message will NOT be parsed");
				else if (fieldCode == 76)
					LOGGER.warn("Field 76 is Empty, The message will NOT be parsed");

				return gpiMessageFields;
			}

			String[] lines = value.split("\n");

			int numberOfLines = lines.length;

			// For Tracker Response on SRP - 199
			if ((key.getMesgType().equalsIgnoreCase("192") && key.getMesgSubFormat().equalsIgnoreCase("OUTPUT") && key.getMesgSender().contains("TRCKCHZ"))
					|| ((key.getMesgType().equalsIgnoreCase("199") && key.getMesgSubFormat().equalsIgnoreCase("INPUT") && !key.getMesgReciver().contains("TRCKCHZ"))
							|| (key.getMesgType().equalsIgnoreCase("192") && key.getMesgSubFormat().equalsIgnoreCase("INPUT") && !key.getMesgReciver().contains("TRCKCHZ")))) {
				lines[0] = lines[0].replaceAll("\r", "");
				lines[1] = lines[1].replaceAll("\r", "");
				String[] statusReasonLine = lines[0].split("/");
				gpiMessageFields.setReasonCode(statusReasonLine[1]);
				String[] bicsLine = lines[1].split("/");
				gpiMessageFields.setStatusOriginator(bicsLine[0]);
			} else if (numberOfLines < 2) {
				String[] valueSplitted = lines[0].split("/");
				if (valueSplitted.length > 2) {
					gpiMessageFields.setStatusCode(valueSplitted[1]);
					gpiMessageFields.setReasonCode(valueSplitted[2]);
				} else {

					// FOR Cancellation Reason - 192 or 199
					String reasonValue = "";
					for (int i = 0; i < valueSplitted.length; i++) {
						if (!valueSplitted[i].isEmpty()) {
							reasonValue += valueSplitted[i];
						}
					}
					gpiMessageFields.setReasonCode(reasonValue);
				}
			} else if (numberOfLines >= 2) {
				lines[0] = lines[0].replaceAll("\r", "");
				lines[1] = lines[1].replaceAll("\r", "");
				String[] statusReasonLine = lines[0].split("/");
				gpiMessageFields.setStatusCode(statusReasonLine[1]);
				if (statusReasonLine.length > 2)
					if (!statusReasonLine[2].isEmpty())
						gpiMessageFields.setReasonCode(statusReasonLine[2]);

				String[] bicsLine = lines[1].split("/");
				gpiMessageFields.setStatusOriginator(bicsLine[0]);
				if (bicsLine.length >= 2)
					gpiMessageFields.setForwardedTo(bicsLine[1]);
			}

			gpiMessageFields.setMessageParsingResult(MessageParsingResult.PARSED);
			gpiMessageFields.setUpdateFinMessageStatus(false);
			return gpiMessageFields;

		} catch (Exception e) {

			gpiMessageFields.setMessageParsingResult(MessageParsingResult.WRONGFIELDS);
			LOGGER.error("WRONG FIELD VALUES. Message Parsing FAILED For This Confimration Message, MESG_PARSING_DATE_TIME Column will be Updated to Skip This Message.");
			e.printStackTrace();
			return gpiMessageFields;
		}

	}

}
