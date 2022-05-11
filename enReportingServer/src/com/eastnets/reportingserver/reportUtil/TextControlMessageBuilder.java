package com.eastnets.reportingserver.reportUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eastnets.domain.reporting.TextControlBean;
import com.eastnets.domain.reporting.TextControlMessage;
import com.eastnets.domain.reporting.TextControlMessagesField;

public class TextControlMessageBuilder {

	public static TextControlBean buildMessage(TextControlMessage textControlMessage, List<TextControlMessagesField> textControlMessagesFields, int rowNo) {
		TextControlBean bean = new TextControlBean();
		try {
			if (textControlMessage.getMesgType().equalsIgnoreCase("202") || textControlMessage.getMesgIdinfier().equalsIgnoreCase("202Cov")) {
				bean.setTextReport(buildMessage202Cove(textControlMessage, textControlMessagesFields, rowNo));
			} else {
				bean.setTextReport(buildMessageFormatTXT(textControlMessage, textControlMessagesFields, rowNo));

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bean;
	}

	private static String buildMessage202Cove(TextControlMessage textControlMessage, List<TextControlMessagesField> textControlMessagesFields, int rowno) {
		String seqAStr = "";
		String seqBStr = "";
		List<TextControlMessagesField> textControlMessagesFieldsSeqA = new ArrayList<TextControlMessagesField>();
		List<TextControlMessagesField> textControlMessagesFieldsSeqB = new ArrayList<TextControlMessagesField>();
		for (TextControlMessagesField controlMessagesField : textControlMessagesFields) {
			if (controlMessagesField.getSequence().equalsIgnoreCase("A")) {
				textControlMessagesFieldsSeqA.add(controlMessagesField);
			} else {
				textControlMessagesFieldsSeqB.add(controlMessagesField);
			}
		}
		if (textControlMessage.getMesgIdinfier().equalsIgnoreCase("fin.202.COV")) {
			seqAStr = "A";
			seqBStr = "B";
		} else {
			seqAStr = "";
			seqBStr = "";
		}

		String messageStr = rowno + "|" + getFormatedMesgIdinfier(textControlMessage.getMesgIdinfier()) + "|" + getFormatedDir(textControlMessage.getMesgSubFormat()) + "|" + textControlMessage.getMesgSender() + "|"
				+ textControlMessage.getMesgReceiver() + "|" + seqAStr + buildMessage202BasedOnSeq(textControlMessagesFieldsSeqA);
		messageStr = messageStr + "|" + seqBStr + buildMessage202BasedOnSeq(textControlMessagesFieldsSeqB);
		return messageStr;
	}

	private static String buildMessage202BasedOnSeq(List<TextControlMessagesField> textControlMessagesFields) {
		String messageFeild = "";
		for (TextControlMessagesField textControlMessagesField : textControlMessagesFields) {
			String feildWithvalue = "";
			if (textControlMessagesField.getValue() == null || textControlMessagesField.getValue().isEmpty()) {
				for (int i = 0; i < textControlMessagesField.getFielRowNum(); i++) {
					feildWithvalue = feildWithvalue + "|";
				}
			} else {
				String lines[] = textControlMessagesField.getValue().split("\\r?\\n");
				for (int i = 0; i <= lines.length - 1; i++) {
					feildWithvalue = feildWithvalue + ("|" + lines[i]);
				}
				int restLine = textControlMessagesField.getFielRowNum() - lines.length;
				for (int i = 0; i < restLine; i++) {
					feildWithvalue = feildWithvalue + "|";
				}
			}
			messageFeild = messageFeild + feildWithvalue;
		}
		return messageFeild;
	}

	private static String buildMessageFormatTXT(TextControlMessage textControlMessage, List<TextControlMessagesField> textControlMessagesFields, int rowno) {
		String messageStr = rowno + "|" + getFormatedMesgIdinfier(textControlMessage.getMesgIdinfier()) + "|" + getFormatedDir(textControlMessage.getMesgSubFormat()) + "|" + textControlMessage.getMesgSender() + "|"
				+ textControlMessage.getMesgReceiver() + "|";
		for (TextControlMessagesField textControlMessagesField : textControlMessagesFields) {

			/*
			 * if(textControlMessagesField.getFieldTag().equalsIgnoreCase("77T") && (textControlMessage.getMesgIdinfier().equalsIgnoreCase("fin.103") ||
			 * textControlMessage.getMesgIdinfier().equalsIgnoreCase("fin.103.STP"))){ continue; } if(textControlMessagesField.getFieldTag().equalsIgnoreCase("70") &&
			 * (textControlMessage.getMesgIdinfier().equalsIgnoreCase("fin.103.REMIT") )){ continue; }
			 */

			String feildWithvalue = "";
			if (textControlMessagesField.getFieldTag().equalsIgnoreCase("50A") && (textControlMessagesField.getSequence() != null && textControlMessagesField.getSequence().equalsIgnoreCase("tempB"))) {
				feildWithvalue = "|";
			}
			if (textControlMessagesField.getValue() == null || textControlMessagesField.getValue().isEmpty()) {
				for (int i = 0; i < textControlMessagesField.getFielRowNum(); i++) {
					feildWithvalue = feildWithvalue + "|";
				}
			} else {
				String lines[] = textControlMessagesField.getValue().split("\\r?\\n");
				if (textControlMessagesField.getFieldTag().equalsIgnoreCase("77T") && lines.length > 5) {
					for (int i = 0; i < 5; i++) {
						feildWithvalue = feildWithvalue + ("|" + lines[i]);
					}
				} else {
					for (int i = 0; i <= lines.length - 1; i++) {
						feildWithvalue = feildWithvalue + ("|" + lines[i]);
					}
					int restLine = textControlMessagesField.getFielRowNum() - lines.length;
					for (int i = 0; i < restLine; i++) {
						feildWithvalue = feildWithvalue + "|";
					}
				}

			}
			messageStr = messageStr + feildWithvalue;
		}
		return messageStr;
	}

	public static String buildSummaryReportCTL(String date, String fileNmae, int numberOfRow) {
		return "SG_SWIFT|S530201_SWIFTINFO|" + date + "|5302|01|S530201_SWIFTINFO_" + fileNmae + ".txt|" + numberOfRow;
	}

	private static String getFormatedDir(String dir) {
		if (dir.equalsIgnoreCase("Input")) {
			return "I";
		} else {
			return "O";
		}
	}

	private static String getFormatedMesgIdinfier(String mesgIdinfier) {
		return mesgIdinfier.replace("fin.", "");

	}

	public static String getFormatedControlFileDate(String format, Date controlFileDate) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		String dateFormated = simpleDateFormat.format(controlFileDate);
		return dateFormated;
	}

}
