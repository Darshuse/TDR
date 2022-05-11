package com.eastnets.enGPIParser.messageparsing;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.GPIMesgFields;
import com.eastnets.domain.viewer.MessageKey;
import com.eastnets.domain.viewer.MessageParsingResult;
import com.eastnets.domain.viewer.TextFieldData;

public class ConfirmationParser implements MessageParser {

	GPIMesgFields gpiMessageFields = new GPIMesgFields();

	private static final Logger LOGGER = Logger.getLogger(ConfirmationParser.class);

	boolean senderDeducts;

	public static void main(String[] args) {

		// new ConfirmationParser().testParse();
	}

	public GPIMesgFields parse(List<TextFieldData> fields, MessageKey key) {

		senderDeducts = false;
		try {
			// ** DON'T REMOVE THESE COMMENTED VARIABLES
			// String fromCurrency = "";
			// String toCurrency = "";

			LOGGER.debug("Confirmation Parsing Method for 199 or 299 messages");
			String value = null;

			if (fields.isEmpty()) {
				LOGGER.error(" MAKE SURE THAT MESSAGES ARE DECOMPOSED. Text Fields are Empty, The message will NOT be parsed   Aid= " + key.getAid() + " UMIDH= " + key.getUmidh() + " UMIDL= " + key.getUnidl());
				gpiMessageFields.setMessageParsingResult(MessageParsingResult.NOTDECOMPOSED);
				return gpiMessageFields;
			}

			for (TextFieldData field : fields) {
				if (field.getFieldCode() == 79)
					value = field.getValue();
			}

			if (value == null) {
				gpiMessageFields.setMessageParsingResult(MessageParsingResult.NOTCONFIRMATION);
				LOGGER.warn("Field 79 is Empty, The message will NOT be parsed");

				return gpiMessageFields;
			}

			// String value =
			// "//1612020918+0100//ACSP/002//BANKBEB0//USD1000,//EXCH//USD/EUR/0.91//:71F:USD20,//:71F:EUR10,";

			/*
			 * Create array of field 79 lines Lines in field 79 are separated by //
			 */
			value = value.replace("//CLRG", "/CLRG");
			value = value.replace("//COVE", "/COVE");
			value = value.replace("//INDA", "/INDA");
			value = value.replace("//INGA", "/INGA");
			value = value.replaceAll("\n", "");
			value = value.replaceAll("\r", "");
			value = value.replaceAll(",", ".");
			String[] lines = value.split("//");

			int numberOfLines = lines.length;

			// First line of 79 is a TimeStamp
			gpiMessageFields.setNotifyTime(lines[1]);

			/*
			 * Line 2 of field 79 contains message status and an optional reason code separated by /
			 */
			String[] statusReason = lines[2].split("/");
			gpiMessageFields.setStatusCode(statusReason[0].trim());

			// add the reason if it included
			if (statusReason.length == 2)
				gpiMessageFields.setReasonCode(statusReason[1].trim());

			/*
			 * Line 2 of field 79 is a combination of a mandatory Bank bic ( Originator ) and an optional forwarded-to bank bic Separated by /
			 */

			String[] bics = lines[3].split("/");

			for (String line : bics) {
				if (gpiMessageFields.getStatusOriginator() == null || gpiMessageFields.getStatusOriginator().isEmpty()) {
					gpiMessageFields.setStatusOriginator(line);
				} else if (checkIfSettlementMethod(line)) {
					gpiMessageFields.setSettlementMethod(line);
				} else if (line.length() == 3) {
					gpiMessageFields.setClearingSystem(line);
				} else {
					gpiMessageFields.setForwardedTo(line);
				}
			}
			/*
			 * gpiMessageFields.setStatusOriginator(bics[0].trim());
			 * 
			 * if (bics.length > 1) if (checkIfSettlementMethod(bics[1].trim())) { gpiMessageFields.setSettlementMethod(bics[1].trim()); if (bics.length == 3)
			 * gpiMessageFields.setClearingSystem(bics[2].trim()); } else { gpiMessageFields.setForwardedTo(bics[1].trim()); if (bics.length == 3) gpiMessageFields.setSettlementMethod(bics[2].trim());
			 * 
			 * if (bics.length == 4) { gpiMessageFields.setSettlementMethod(bics[2].trim()); gpiMessageFields.setClearingSystem(bics[3].trim()); }
			 * 
			 * }
			 */

			// line 4 of field 79 is the Amount
			String[] currencyAmountCharges = lines[4].split("/");
			String currencyAmount = currencyAmountCharges[0];
			gpiMessageFields.setMesgCreditCcy(currencyAmount.substring(0, 3));
			gpiMessageFields.setMesgCreditAmount(currencyAmount.substring(3));

			if (currencyAmountCharges.length == 2) {
				String detailsofCharges = currencyAmountCharges[1];
				gpiMessageFields.setDetailsofCharges(detailsofCharges);
				gpiMessageFields.setMesgCharges(detailsofCharges);
			}
			/*
			 * The rest of field 79 message lines are optional so we will check if the message contains more than four lines.
			 * 
			 * NOTE THAT all the following are optional.
			 */
			if (numberOfLines > 4) {

				for (int i = 5; i < numberOfLines; i++) {

					/*
					 * Normally if EXCHANGE RATE is included in the message it's added in line number 5 --> EXCH//USD/EUR/0.91 We will store only the last part, "the percentage"
					 */

					if (i == 5 && lines[i].contains("EXCH")) {

						String[] exchLine = lines[6].split("/");

						String fromCurrency = exchLine[0].trim();
						String toCurrency = exchLine[1].trim();

						gpiMessageFields.setMesgExchangeRate(exchLine[exchLine.length - 1]);
						gpiMessageFields.setMesgExchangeCurrFrom(fromCurrency);
						gpiMessageFields.setMesgExchangeCurrTo(toCurrency);
					}

					/*
					 * There are 1 to n Charges included either by receiver 71G or sender 71F and so we will store the last one always as the others are for sure included in previous GPI messages.
					 * 
					 * NOTE: we considered that sender charges and receiver charges can't be included in the same message.
					 */

					if (i == numberOfLines - 1 && lines[i].contains("71F")) {
						senderDeducts = true;
						String[] chargesLine = lines[i].split(":");

						String charges = chargesLine[chargesLine.length - 1];

						gpiMessageFields.setMesgSndChargesCurr(charges.substring(0, 3));

						if (charges.indexOf('.') != charges.length() - 1)
							gpiMessageFields.setMesgSndChargesAmount(charges.substring(3));

						else
							gpiMessageFields.setMesgSndChargesAmount(charges.substring(3, charges.length() - 1));

					}

					if (i == numberOfLines - 1 && lines[i].contains("71G")) {
						String[] chargesLine = lines[i].split(":");
						String charges = chargesLine[chargesLine.length - 1];

						gpiMessageFields.setMesgRcvChargesCurr(charges.substring(0, 3));

						if (charges.indexOf('.') != charges.length() - 1)
							gpiMessageFields.setMesgRcvChargesAmount(new BigDecimal(charges.substring(3)));

						else
							gpiMessageFields.setMesgRcvChargesAmount(new BigDecimal(charges.substring(3, charges.length() - 1)));

					}
				}
			}

			fillAllDeductsCompleted(lines);

			if (!senderDeducts)
				gpiMessageFields.setMesgSndChargesAmount("0");

			gpiMessageFields.setNakCode(key.getAppeNacReason());
			gpiMessageFields.setMessageParsingResult(MessageParsingResult.PARSED);
			gpiMessageFields.setUpdateFinMessageStatus(true);
			return gpiMessageFields;

		} catch (Exception e) {

			gpiMessageFields.setMessageParsingResult(MessageParsingResult.WRONGFIELDS);
			LOGGER.error("WRONG FIELD VALUES. Message Parsing FAILED For This Confimration Message, MESG_PARSING_DATE_TIME Column will be Updated to Skip This Message.");
			e.printStackTrace();
			return gpiMessageFields;
		}

	}

	private void fillAllDeductsCompleted(String[] lines) {
		String cur = "";
		String amount = "";
		try {
			for (String line : lines) {
				if (line.contains("71F") && (gpiMessageFields.getStatusCode().equalsIgnoreCase("ACCC") || gpiMessageFields.getStatusCode().equalsIgnoreCase("ACSC"))) {
					String[] chargesLine = line.split(":");
					String charges = chargesLine[chargesLine.length - 1];
					cur = cur + (cur.isEmpty() ? "" : ",") + charges.substring(0, 3);
					if (charges.indexOf('.') != charges.length() - 1)
						amount = amount + (amount.isEmpty() ? "" : ",") + charges.substring(3);
					else
						amount = amount + (amount.isEmpty() ? "" : ",") + charges.substring(3, charges.length() - 1);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception }
		}
		if (!amount.isEmpty() && !cur.isEmpty()) {
			gpiMessageFields.setAllDeducts(amount + ":" + cur);
		}

	}

	void testParse() {

		String value = "//1612020918+0100" + "//RETN" + "//BANKBEB0//CLRG/FDW" + "//USD1000,/BEN" + "//EXCH//USD/EUR/0.91//:71F:USD20,//:71F:EUR10,";
		value = value.replace("//CLRG", "/CLRG");
		value = value.replace("//COVE", "/COVE");
		value = value.replace("//INDA", "/INDA");
		value = value.replace("//INGA", "/INGA");
		value = value.replaceAll("\n", "");
		value = value.replaceAll("\r", "");
		value = value.replaceAll(",", ".");
		String[] lines = value.split("//");

		int numberOfLines = lines.length;

		// First line of 79 is a TimeStamp
		gpiMessageFields.setNotifyTime(lines[1]);

		/*
		 * Line 2 of field 79 contains message status and an optional reason code separated by /
		 */
		String[] statusReason = lines[2].split("/");
		gpiMessageFields.setStatusCode(statusReason[0].trim());

		// add the reason if it included
		if (statusReason.length == 2)
			gpiMessageFields.setReasonCode(statusReason[1].trim());

		/*
		 * Line 2 of field 79 is a combination of a mandatory Bank bic ( Originator ) and an optional forwarded-to bank bic Separated by /
		 */
		String[] bics = lines[3].split("/");

		for (String line : bics) {
			if (gpiMessageFields.getStatusOriginator() == null || !gpiMessageFields.getStatusOriginator().isEmpty()) {
				gpiMessageFields.setStatusOriginator(line);
			} else if (checkIfSettlementMethod(line)) {
				gpiMessageFields.setSettlementMethod(line);
			} else if (line.length() == 3) {
				gpiMessageFields.setClearingSystem(line);
			} else {
				gpiMessageFields.setForwardedTo(line);
			}
		}

		/*
		 * if (bics.length > 1) if (checkIfSettlementMethod(bics[1].trim())) { gpiMessageFields.setSettlementMethod(bics[1].trim()); if (bics.length == 3)
		 * gpiMessageFields.setClearingSystem(bics[2].trim()); } else { gpiMessageFields.setForwardedTo(bics[1].trim()); if (bics.length == 3) gpiMessageFields.setSettlementMethod(bics[2].trim());
		 * 
		 * if (bics.length == 4) { gpiMessageFields.setSettlementMethod(bics[2].trim()); gpiMessageFields.setClearingSystem(bics[3].trim()); }
		 * 
		 * }
		 */

		// line 4 of field 79 is the Amount
		String[] currencyAmountCharges = lines[4].split("/");
		String currencyAmount = currencyAmountCharges[0];
		gpiMessageFields.setMesgCreditCcy(currencyAmount.substring(0, 3));
		gpiMessageFields.setMesgCreditAmount(currencyAmount.substring(3));

		if (currencyAmountCharges.length == 2) {
			String detailsofCharges = currencyAmountCharges[1];
			gpiMessageFields.setDetailsofCharges(detailsofCharges);
			gpiMessageFields.setMesgCharges(detailsofCharges);
		}
		/*
		 * The rest of field 79 message lines are optional so we will check if the message contains more than four lines.
		 * 
		 * NOTE THAT all the following are optional.
		 */
		if (numberOfLines > 4) {

			for (int i = 5; i < numberOfLines; i++) {

				/*
				 * Normally if EXCHANGE RATE is included in the message it's added in line number 5 --> EXCH//USD/EUR/0.91 We will store only the last part, "the percentage"
				 */

				if (i == 5 && lines[i].contains("EXCH")) {

					String[] exchLine = lines[6].split("/");

					String fromCurrency = exchLine[0].trim();
					String toCurrency = exchLine[1].trim();

					gpiMessageFields.setMesgExchangeRate(exchLine[exchLine.length - 1]);
					gpiMessageFields.setMesgExchangeCurrFrom(fromCurrency);
					gpiMessageFields.setMesgExchangeCurrTo(toCurrency);
				}

				/*
				 * There are 1 to n Charges included either by receiver 71G or sender 71F and so we will store the last one always as the others are for sure included in previous GPI messages.
				 * 
				 * NOTE: we considered that sender charges and receiver charges can't be included in the same message.
				 */

				if (i == numberOfLines - 1 && lines[i].contains("71F")) {
					senderDeducts = true;
					String[] chargesLine = lines[i].split(":");

					String charges = chargesLine[chargesLine.length - 1];

					gpiMessageFields.setMesgSndChargesCurr(charges.substring(0, 3));

					if (charges.indexOf('.') != charges.length() - 1)
						gpiMessageFields.setMesgSndChargesAmount(charges.substring(3));

					else
						gpiMessageFields.setMesgSndChargesAmount(charges.substring(3, charges.length() - 1));

				}

				if (i == numberOfLines - 1 && lines[i].contains("71G")) {
					String[] chargesLine = lines[i].split(":");
					String charges = chargesLine[chargesLine.length - 1];

					gpiMessageFields.setMesgRcvChargesCurr(charges.substring(0, 3));

					if (charges.indexOf('.') != charges.length() - 1)
						gpiMessageFields.setMesgRcvChargesAmount(new BigDecimal(charges.substring(3)));

					else
						gpiMessageFields.setMesgRcvChargesAmount(new BigDecimal(charges.substring(3, charges.length() - 1)));

				}
			}
		}

	}

	private boolean checkIfSettlementMethod(String value) {
		if (value.equalsIgnoreCase("CLRG")) {
			return true;
		} else if (value.equalsIgnoreCase("COVE")) {
			return true;
		} else if (value.equalsIgnoreCase("INDA")) {
			return true;
		} else if (value.equalsIgnoreCase("INGA")) {
			return true;
		}

		return false;
	}

}
