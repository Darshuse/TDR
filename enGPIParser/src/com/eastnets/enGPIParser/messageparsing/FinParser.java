package com.eastnets.enGPIParser.messageparsing;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.GPIMesgFields;
import com.eastnets.domain.viewer.MessageKey;
import com.eastnets.domain.viewer.MessageParsingResult;
import com.eastnets.domain.viewer.TextFieldData;

public class FinParser implements MessageParser {

	GPIMesgFields gpiMessageFields = new GPIMesgFields();

	private static final Logger LOGGER = Logger.getLogger(FinParser.class);

	boolean senderDeducts;

	public GPIMesgFields parse(List<TextFieldData> fields, MessageKey key) {

		senderDeducts = false;

		LOGGER.debug("Fin Parsing Method for messages 103");

		if (fields.isEmpty()) {
			LOGGER.error("MAKE SURE THAT MESSAGES ARE DECOMPOSED. Text Fields are Empty, This message will NOT be parsed   Aid= " + key.getAid() + " UMIDH = " + key.getUmidh() + " UMIDL= " + key.getUnidl());
			gpiMessageFields.setMessageParsingResult(MessageParsingResult.NOTDECOMPOSED);
			return gpiMessageFields;
		}
		try {
			for (TextFieldData fieldData : fields) {
				if (fieldData.getFieldCode().intValue() == 33 && (fieldData.getFieldOption() != null && fieldData.getFieldOption().equalsIgnoreCase("B"))) {
					preperField33B(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 50) {
					preperField50A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 51 && (fieldData.getFieldOption() != null && fieldData.getFieldOption().equalsIgnoreCase("a"))) {
					preperField51A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 52) {
					preperField52A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 53) {
					preperField53A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 36) {
					preperField36(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 71 && (fieldData.getFieldOption() != null && fieldData.getFieldOption().equalsIgnoreCase("a"))) {
					preperField71A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 54) {
					preperField54A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 55) {
					preperField55A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 56) {
					preperField56A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 57) {
					preperField57A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 59) {
					preperField59A(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 71 && (fieldData.getFieldOption() != null && fieldData.getFieldOption().equalsIgnoreCase("F"))) {
					preperField71F(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 71 && (fieldData.getFieldOption() != null && fieldData.getFieldOption().equalsIgnoreCase("G"))) {
					preperField71G(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 70) {
					preperField70(fieldData.getValue());
				} else if (fieldData.getFieldCode().intValue() == 72) {
					prepareField72(fieldData.getValue());
				}
			}

			if (!senderDeducts)
				setDefaultSenderDeducts();
			gpiMessageFields.setNakCode(key.getAppeNacReason());
			gpiMessageFields.setMessageParsingResult(MessageParsingResult.PARSED);
			return gpiMessageFields;

		} catch (Exception e) {

			LOGGER.error("WRONG FIELD VALUES. Message Parsing FAILED For This Fin Message, MESG_PARSING_DATE_TIME Column will be Updated to Skip This Message. ");

			gpiMessageFields.setMessageParsingResult(MessageParsingResult.WRONGFIELDS);
			e.printStackTrace();
			return gpiMessageFields;
		}

	}

	private void setDefaultSenderDeducts() {

		gpiMessageFields.setMesgSndChargesAmount("0");
	}

	private void preperField33B(String value) {
		gpiMessageFields.setMesgInstrAmount(new BigDecimal(value.substring(3, value.length()).replace(",", ".")));
		gpiMessageFields.setMesgInstrCcy(value.substring(0, 3));
	}

	private void preperField50A(String value) {
		gpiMessageFields.setMesgOrderCus(value);
	}

	private void preperField51A(String value) {
		gpiMessageFields.setMesgSendingInst(value);
	}

	private void preperField52A(String value) {
		gpiMessageFields.setMesgOrderingInst(value);
	}

	private void preperField53A(String value) {
		gpiMessageFields.setMesgSnd_Corr(value);
	}

	private void preperField36(String value) {
		gpiMessageFields.setMesgExchangeRate(value);
	}

	private void preperField71A(String value) {
		gpiMessageFields.setMesgCharges(value);
	}

	private void preperField54A(String value) {
		gpiMessageFields.setMesgRcvrCorr(value);
	}

	private void preperField55A(String value) {
		gpiMessageFields.setMesgReimbursInst(value);
	}

	private void preperField56A(String value) {
		gpiMessageFields.setMesgIntermInst(value);
	}

	private void preperField57A(String value) {
		gpiMessageFields.setMesgAccountInst(value);
	}

	private void preperField59A(String value) {
		gpiMessageFields.setMesgBenCust(value);
	}

	private void preperField71F(String value) {
		senderDeducts = true;
		String cur = value.substring(0, 3);
		String amount = value.substring(3, value.length()).replace(',', '.');
		String updatedAmount;
		if (gpiMessageFields.getMesgSndChargesCurr().equals("")) {
			gpiMessageFields.setMesgSndChargesCurr(cur);
		} else {
			gpiMessageFields.setMesgSndChargesCurr(gpiMessageFields.getMesgSndChargesCurr() + "," + cur);
		}

		if (amount.indexOf(".") != amount.length() - 1)
			updatedAmount = amount;
		else
			updatedAmount = amount.substring(0, amount.length() - 1);

		if (gpiMessageFields.getMesgSndChargesAmount().equals("")) {

			gpiMessageFields.setMesgSndChargesAmount(updatedAmount);

		} else {

			gpiMessageFields.setMesgSndChargesAmount(gpiMessageFields.getMesgSndChargesAmount() + "," + updatedAmount);
		}

	}

	private void preperField71G(String value) {
		String cur = value.substring(0, 3);
		String amount = value.substring(3, value.length()).replace(',', '.');
		String updatedAmount;

		if (amount.indexOf(".") != amount.length() - 1)
			updatedAmount = amount;
		else
			updatedAmount = amount.substring(0, amount.length() - 1);

		gpiMessageFields.setMesgRcvChargesAmount(new BigDecimal(updatedAmount));
		gpiMessageFields.setMesgRcvChargesCurr(cur);
	}

	private void preperField70(String value) {
		gpiMessageFields.setMesgRemitInfo(value);

	}

	private void prepareField72(String value) {

		String rejecttReturn = null;
		String rejecttReturnFieldCuasing = null;
		String rejecttReturnReasonCode = null;
		String rejecttReturnReff = null;

		String[] arrayOf72Values = value.split("\r\n");

		int rejtRetnLastIndex = arrayOf72Values[0].substring(1).indexOf('/');

		if (rejtRetnLastIndex != -1) {
			rejecttReturn = arrayOf72Values[0].substring(1, rejtRetnLastIndex + 1);
			if (rejecttReturn.equals("REJT") || rejecttReturn.equals("RETN")) {

				int fieldCuasingBeginIndex = arrayOf72Values[0].lastIndexOf('/');
				if (fieldCuasingBeginIndex != -1) {
					rejecttReturnFieldCuasing = arrayOf72Values[0].substring(fieldCuasingBeginIndex + 1);
				}

				rejecttReturnReasonCode = arrayOf72Values[1].substring(1);

				int reffBeginIndex = arrayOf72Values[2].lastIndexOf('/');
				if (reffBeginIndex != -1) {
					rejecttReturnReff = arrayOf72Values[2].substring(reffBeginIndex + 1);
				}

			}
		}

		gpiMessageFields.setSenderToReceiver(rejecttReturn);
		gpiMessageFields.setSenderToReceiverFieldCuasing(rejecttReturnFieldCuasing);
		gpiMessageFields.setSenderToReceiverReasonCode(rejecttReturnReasonCode);
		gpiMessageFields.setSenderToReceiverReff(rejecttReturnReff);
	}

}
