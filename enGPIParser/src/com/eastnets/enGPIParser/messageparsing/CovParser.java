package com.eastnets.enGPIParser.messageparsing;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.GPIMesgFields;
import com.eastnets.domain.viewer.MessageKey;
import com.eastnets.domain.viewer.MessageParsingResult;
import com.eastnets.domain.viewer.TextFieldData;

public class CovParser implements MessageParser {

	GPIMesgFields gpiMessageFields = new GPIMesgFields();

	private static final Logger LOGGER = Logger.getLogger(CovParser.class);

	public GPIMesgFields parse(List<TextFieldData> fields, MessageKey key) {

		LOGGER.debug("COV Parsing Method for COV Messages");

		if (fields.isEmpty()) {
			LOGGER.error(
					"MAKE SURE THAT MESSAGES ARE DECOMPOSED. Text Fields are Empty, This message will NOT be parsed   Aid= "
							+ key.getAid() + " UMIDH = " + key.getUmidh() + " UMIDL= " + key.getUnidl());
			gpiMessageFields.setMessageParsingResult(MessageParsingResult.NOTDECOMPOSED);
			return gpiMessageFields;
		}
		try {
			for (TextFieldData fieldData : fields) {
				if (fieldData.getFieldCode().intValue() == 50) {
					preperField50A(fieldData.getValue(), fieldData.getSequence());
				} else if (fieldData.getFieldCode().intValue() == 52) {
					preperField52A(fieldData.getValue(), fieldData.getSequence());
				} else if (fieldData.getFieldCode().intValue() == 53) {
					preperField53A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 54) {
					preperField54(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 56
						&& (fieldData.getFieldOption() != null && fieldData.getFieldOption().equalsIgnoreCase("A"))) {
					preperField56A(fieldData.getValue(), fieldData.getSequence());
				} else if (fieldData.getFieldCode().intValue() == 57) {
					preperField57A(fieldData.getValue(), fieldData.getSequence());
				} else if (fieldData.getFieldCode().intValue() == 58) {
					preperField58A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 59) {
					preperField59A(fieldData.getValue(), fieldData.getSequence());
				} else if (fieldData.getFieldCode().intValue() == 70) {
					preperField70(fieldData.getValue(), fieldData.getSequence());
				} else if (fieldData.getFieldCode().intValue() == 33) {
					preperField33(fieldData.getValue(), fieldData.getSequence());
				}
			}

			gpiMessageFields.setNakCode(key.getAppeNacReason());
			gpiMessageFields.setMessageParsingResult(MessageParsingResult.PARSED);
			return gpiMessageFields;

		} catch (Exception e) {

			LOGGER.error(
					"WRONG FIELD VALUES. Message Parsing FAILED For This COV Message, MESG_PARSING_DATE_TIME Column will be Updated to Skip This Message. ");

			gpiMessageFields.setMessageParsingResult(MessageParsingResult.WRONGFIELDS);
			e.printStackTrace();
			return gpiMessageFields;
		}
	}

	private void preperField50A(String value, String seq) {
		if (seq.trim().equals("B"))
			gpiMessageFields.setSbOrderingCus(value);

	}

	private void preperField52A(String value, String seq) {

		if (seq.trim().equals("B"))
			gpiMessageFields.setSbOrderingInst(value);
		else
			gpiMessageFields.setMesgOrderingInst(value);

	}

	private void preperField53A(String value) {
		gpiMessageFields.setMesgSnd_Corr(value);

	}

	private void preperField54(String value) {
		gpiMessageFields.setMesgRcvrCorr(value);

	}

	private void preperField56A(String value, String seq) {
		if (seq.trim().equals("B"))
			gpiMessageFields.setSbIntermediaryInst(value);
		else
			gpiMessageFields.setMesgIntermInst(value);

	}

	private void preperField57A(String value, String seq) {
		if (seq.trim().equals("B"))
			gpiMessageFields.setSbAccountWithInst(value);
		else
			gpiMessageFields.setMesgAccountInst(value);

	}

	private void preperField58A(String value) {
		gpiMessageFields.setMesgBeneficiaryInst(value);

	}

	private void preperField59A(String value, String seq) {
		if (seq.trim().equals("B"))
			gpiMessageFields.setSbBeneficiaryCustomer(value);
		else
			gpiMessageFields.setMesgBenCust(value);

	}

	private void preperField70(String value, String seq) {
		if (seq.trim().equals("B"))
			gpiMessageFields.setSbRemittanceInfo(value);

	}

	private void preperField33(String value, String seq) {
		if (seq.trim().equals("B")) {
			gpiMessageFields
					.setSbInstructedAmount(new BigDecimal(value.substring(3, value.length()).replace(",", ".")));
			gpiMessageFields.setSbInstructedCurr(value.substring(0, 3));
		}
	}

}
