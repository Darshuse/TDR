package com.eastnets.textbreak.utility;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.eastnets.textbreak.entities.SystemTextField;
import com.eastnets.textbreak.entities.TextField;

@Service
public class IntegrityChecker {

	final String separator = System.getProperty("line.separator");

	public boolean checkFinMessageIntegrity(String orginalMessageText, List<TextField> textFields) {

		Pair<String, Boolean> buildUnexpandedText = buildUnexpandedText(textFields);
		String messageTextAfterDecomposed = removingExtraChar(buildUnexpandedText.getKey()).trim();
		if (messageTextAfterDecomposed.equalsIgnoreCase(removingExtraChar(orginalMessageText).trim())) {
			return true;
		}
		return false;

	}

	public boolean checkSysMessageIntegrity(String orginalMessageText, List<SystemTextField> textFields, boolean sysMessagesHaveBraces) {
		Pair<String, Boolean> buildSysUnexpandedText = buildSysUnexpandedText(textFields, sysMessagesHaveBraces);
		String messageTextAfterDecomposed = removingExtraChar(buildSysUnexpandedText.getKey()).trim();
		if (messageTextAfterDecomposed.equalsIgnoreCase(removingExtraChar(orginalMessageText).trim())) {
			return true;
		}
		return false;
	}

	private Pair<String, Boolean> buildSysUnexpandedText(List<SystemTextField> textFields, boolean sysMessagesHaveBraces) {
		Pair<String, Boolean> result = new Pair<String, Boolean>();
		if (textFields == null || textFields.size() == 0) {
			return null;// error: Record Set is null !
		}
		result.setKey("");
		for (int i = 0; i < textFields.size(); ++i) {

			String str = textFields.get(i).getValue();
			str = StringUtils.replace(str, "\\r\\n", "\r\n"); // added
			if (sysMessagesHaveBraces) {
				result.setKey(result.getKey() + "{" + String.valueOf(textFields.get(i).getFieldCode()) + ":" + StringUtils.defaultString(str) + "}");
			} else {

				result.setKey(result.getKey() + "\n:" + String.valueOf(textFields.get(i).getFieldCode()) + ":" + StringUtils.defaultString(str));
			}
			result.setValue(false);
		}

		return result;

	}

	private String removingExtraChar(String mesgText) {
		String newMesgText = StringUtils.replace(mesgText, "\\r\\n", "\r\n");
		newMesgText = StringUtils.replace(mesgText, "\\n", "\n");
		newMesgText = StringUtils.replace(mesgText, "\r\n", "\n");
		return newMesgText;
	}

	private Pair<String, Boolean> buildUnexpandedText(List<TextField> textFieldData) {
		Pair<String, Boolean> result = new Pair<String, Boolean>();
		result.setValue(true);
		result.setKey("");
		for (int i = 0; i < textFieldData.size(); ++i) {

			long iCode = textFieldData.get(i).getId().getFieldCode();
			if (iCode == 0) {
				String valueMemo = "";
				if (textFieldData.get(i).getValueMemo() != null) {
					valueMemo = textFieldData.get(i).getValueMemo();
				}
				result.setKey(result.getKey() + "\n" + StringUtils.defaultString(valueMemo));
			} else if (iCode > 0 && iCode < 100) {
				String valueMemo = "";
				if (textFieldData.get(i).getValueMemo() != null) {
					valueMemo = textFieldData.get(i).getValueMemo();
				}
				result.setKey(result.getKey() + "\n:" + iCode + StringUtils.defaultString(textFieldData.get(i).getFieldOption()) + ":");
				if (!StringUtils.isEmpty(textFieldData.get(i).getValue())) {
					result.setKey(result.getKey() + StringUtils.defaultString(textFieldData.get(i).getValue()));
				} else {
					result.setKey(result.getKey() + valueMemo);
				}
			} else if (iCode == 255) {
				result.setValue(false);
				String valueMemo = "";
				if (textFieldData.get(i).getValueMemo() != null) {
					valueMemo = textFieldData.get(i).getValueMemo();
				}
				result.setKey(result.getKey() + valueMemo);
			}
		}
		return result;
	}

	public String convertClob2String(java.sql.Clob clobInData) {
		if (clobInData == null)
			return null;
		String stringClob = null;
		try {
			long i = 1;
			int clobLength = (int) clobInData.length();
			stringClob = clobInData.getSubString(i, clobLength);

			if (stringClob != null)
				stringClob = stringClob.replaceAll("\\\\n", separator).replace("\\r", "");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return stringClob;
	}
}
