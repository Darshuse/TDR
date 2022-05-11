package com.eastnets.extraction.service.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.eastnets.extraction.bean.InterventionDetails;
import com.eastnets.extraction.bean.SearchResult;

public class SearchUtils {

	public static String checkAnd(StringBuilder base, String ext) {
		if (base.length() != 0) {
			return " and " + ext;
		}
		return ext;
	}

	private static String getFormattedDate(Date date, Boolean withTime) {
		if (date == null) {
			return "";
		}
		String dateTime = "";
		if (withTime) {
			dateTime = " HH:mm:ss";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd" + dateTime);
		String dateStr = formatter.format(date);
		String pattern = "YYYYMMDD";
		if (withTime) {
			pattern += " HH24:MI:SS";
		}
		return DBPortabilityHandler.getDate(dateStr, pattern);
	}

	public static String DateToSqlStr(Date date) {
		String sqlDateStr = "";
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.HOUR_OF_DAY, -1 * 0);// we are
														// searching in
														// the database,
														// so we remove
														// the time zone
														// offset, if we
														// are viewing
														// we will add
														// the offset
			sqlDateStr = getFormattedDate(calendar.getTime(), true);
		}
		return sqlDateStr;
	}

	public static String sqlEscapeString(String str) {
		return StringUtils.replace(str, "'", "''");
	}

	public static boolean IsSystemMessageServiceIdinfier(String mesg_type) {
		if (!StringUtils.equalsIgnoreCase(mesg_type, "XXX")) {
			// Substitute value for NULL.
			if (NumberUtils.isNumber(mesg_type)) {
				// Contains only numeric characters
				Integer val = new Integer(mesg_type);
				if (val != null) {
					String strVal = String.valueOf(val);
					if (strVal.equalsIgnoreCase("05") || strVal.equalsIgnoreCase("5")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String convertClob2String(java.sql.Clob clobInData) {
		if (clobInData == null)
			return null;
		String stringClob = null;
		try {
			int clobLength = (int) clobInData.length();
			stringClob = clobInData.getSubString(1, clobLength);

			if (stringClob != null) {
				stringClob = stringClob.replaceAll("\\\\n", "\n").replaceAll("\\r", "").replaceAll("\r\n", "\n").replaceAll("\r", "");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return stringClob;
	}

	public static String checkForWildCard(String str) {
		if (StringUtils.contains(str, '%') || StringUtils.contains(str, '*') || StringUtils.contains(str, '_') || StringUtils.contains(str, '?')) {
			String tmp = StringUtils.replace(str, "*", "%");
			tmp = StringUtils.replace(str, "?", "_");
			return " like '" + tmp + "'";
		}
		return "  = '" + str + "'";
	}

	public static boolean containsFileMessage(List<SearchResult> messages) {
		for (SearchResult message : messages) {
			if ("file".equalsIgnoreCase(message.getMesgFrmtName())) {
				return true;
			}
		}
		return false;
	}

	public static boolean IsSystemMessage(String mesg_type) {
		if (!StringUtils.equalsIgnoreCase(mesg_type, "XXX")) {
			// Substitute value for NULL.
			if (NumberUtils.isNumber(mesg_type)) {
				// Contains only numeric characters
				Integer val = new Integer(mesg_type);
				if (val != null && val > 0 && val < 100) {
					return true;
				}
			}
		}
		return false;
	}

	public static String formatXML(String unformattedXml) {
		if (unformattedXml == null || unformattedXml.trim().length() == 0)
			return "";

		int stack = 0;
		StringBuilder pretty = new StringBuilder();
		String[] rows = unformattedXml.trim().replaceAll(">", ">\n").replaceAll("<", "\n<").split("\n");

		boolean prevRowEndTag = false;
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] == null || rows[i].trim().length() == 0)
				continue;

			String row = rows[i].trim();
			if (row.startsWith("<?")) {
				// xml version tag
				pretty.append(row + "\n");
				prevRowEndTag = false;
			} else if (row.startsWith("</")) {
				--stack;
				if (prevRowEndTag) {
					String indent = repeatString("    ", stack);
					pretty.append("\n" + indent);
				}
				pretty.append(row);
				prevRowEndTag = true;
			} else if (row.startsWith("<")) {
				// starting tag
				String indent = repeatString("    ", stack++);
				pretty.append("\n" + indent + row);
				prevRowEndTag = false;
			} else {
				// tag data
				pretty.append(row);
				prevRowEndTag = false;
			}
		}

		return pretty.toString().trim();
	}

	private static String repeatString(String string, int identation) {
		String str = "";
		for (int i = 0; i < identation; ++i) {
			str += string;
		}
		return str;
	}

	public static String formatMessageInputReference(Date appeDateTime, String appeSessionHolder, Integer appeSessionNbr, Integer appeSequenceNbr, String appeIAppName, String appeType, String appeAckNackText) {
		String result = "";
		if (StringUtils.equalsIgnoreCase(appeIAppName, "SWIFT") && StringUtils.equalsIgnoreCase(appeType, "APPE_EMISSION")) {
			if (!StringUtils.isEmpty(appeAckNackText)) {
				String textblock = appeAckNackText;
				int index = StringUtils.indexOf(textblock, "{177:");
				if (index >= 0) {
					result = StringUtils.substring(textblock, index + 5, index + 11);
				}
			}
		}

		result = result + StringUtils.left(appeSessionHolder, 12);
		result = result + String.format("%04d", appeSessionNbr);
		result = result + String.format("%06d", appeSequenceNbr);
		return result;
	}

	public static String formatMessageOutputReference(Date appeLocalOutputTime, String appeSessionHolder, Integer appeSessionNbr, Integer appeSequenceNbr) {
		String result = "";
		if (appeLocalOutputTime != null) {
			SimpleDateFormat df = new SimpleDateFormat("HHmm yyMMdd");
			result = df.format(appeLocalOutputTime);
		}
		if (appeSessionHolder != null) {
			result = result + StringUtils.left(appeSessionHolder, 12);
		}
		if (appeSessionNbr != null) {
			result = result + String.format("%04d", appeSessionNbr);
		}

		if (appeSequenceNbr != null) {
			result = result + String.format("%06d", appeSequenceNbr);
		}

		return result;
	}

	public static String formatAckNak(String status) {
		if (StringUtils.equalsIgnoreCase(status, "DLV_ACKED")) {
			return "ACK";
		}
		if (StringUtils.equalsIgnoreCase(status, "DLV_NACKED")) {
			return "NAK";
		}
		return "";
	}

	public static String formatAckSts(String status, String app_name) {
		if (!StringUtils.isEmpty(status)) {
			if (StringUtils.equalsIgnoreCase(status, "DLV_N_A"))
				return "N/A";
			if (StringUtils.equalsIgnoreCase(status, "DLV_WAITING_ACK"))
				return "Waiting " + StringUtils.defaultString(app_name) + " Ack";
			if (StringUtils.equalsIgnoreCase(status, "DLV_TIMED_OUT"))
				return StringUtils.defaultString(app_name) + " Time out";
			if (StringUtils.equalsIgnoreCase(status, "DLV_ACKED"))
				return StringUtils.defaultString(app_name) + " Ack";
			if (StringUtils.equalsIgnoreCase(status, "DLV_NACKED"))
				return StringUtils.defaultString(app_name) + " Nack";
			if (StringUtils.equalsIgnoreCase(status, "DLV_REJECTED_LOCALLY"))
				return "Rejected Locally";
			if (StringUtils.equalsIgnoreCase(status, "DLV_ABORTED"))
				return StringUtils.defaultString(app_name) + " Aborted";
		}
		return "-";
	}

	public static String formatIntvStatus(InterventionDetails interventionDetails, long iIntvNo) {
		String str = convertClob2String(interventionDetails.getIntvText());
		if (!StringUtils.isEmpty(str)) {
			return "By " + StringUtils.defaultString(interventionDetails.getIntvOperNickname()) + " : " + StringUtils.defaultString(str) + "\n";
		}
		return "--- Invertention " + String.format("%04d", iIntvNo) + ": Text not accessible ---\n";
	}

	public static String formatAuthResult(String sts) {
		if (StringUtils.equalsIgnoreCase(sts, "DLV_N_A"))
			return "";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_N_A"))
			return "";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_SUCCESS"))
			return "Success";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_SUCCESS_OLD_KEY"))
			return "Old Key";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_SUCCESS_FUTURE_KEY"))
			return "Future KEY";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_FAILURE"))
			return "Failure";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_NO_KEY"))
			return "No Key";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_BYPASSED"))
			return "Bypassed";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_NO_RMA_REC"))
			return "No RMA Rec";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_RMA_NOT_ENABLED"))
			return "RMA Not Enabled";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_RMA_NOT_IN_VALID_PERIOD"))
			return "RMA not in valid period";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_INVALID_DIGEST"))
			return "Invalid Digest";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_INVALID_SIGN_DN"))
			return "Invalid Sign Onx";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_SIG_FAILURE"))
			return "Sig Failure";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_INVALID_CERT_ID"))
			return "Invalid Cert ID";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_NO_RMAU_BKEY"))
			return "No RMAU BKEY";
		return "";
	}

	public static String formatRmaCheckResult(String sts) {
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_N_A"))
			return "-";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_SUCCESS"))
			return "Success";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_FAILURE"))
			return "Failure";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_BYPASSED"))
			return "Bypassed";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NO_REC"))
			return "No Record";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NOT_ENABLED"))
			return "Not Enabled";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NOT_IN_VALID_PERIOD"))
			return "Invalid Period";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NOT_AUTHORISED"))
			return "Not allowed";
		return "";
	}
}
