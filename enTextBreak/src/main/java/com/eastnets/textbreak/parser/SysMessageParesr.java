
package com.eastnets.textbreak.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.textbreak.bean.DAOFactoryTb;
import com.eastnets.textbreak.bean.ParsedData;
import com.eastnets.textbreak.bean.SourceData;
import com.eastnets.textbreak.bean.TextBreakConfig;
import com.eastnets.textbreak.dao.TextBreakDaoImpl;
import com.eastnets.textbreak.entities.SystemTextField;
import com.eastnets.textbreak.entities.SystemTextFieldPK;
import com.eastnets.textbreak.enums.DecomposeStatus;
import com.eastnets.textbreak.service.TextBreakRepositoryService;
import com.eastnets.textbreak.utility.Constants;
import com.eastnets.textbreak.utility.IntegrityChecker;

@Service
public class SysMessageParesr implements MessageParser {

	@Autowired
	TextBreakDaoImpl textBreakDaoImpl;
	@Autowired
	private TextBreakRepositoryService textBreakRepositoryService;

	@Autowired
	DAOFactoryTb daoFactory;

	@Autowired
	private IntegrityChecker integrityChecker;
	private static final Logger LOGGER = Logger.getLogger(SysMessageParesr.class);
	@Autowired
	private TextBreakConfig textBreakConfig;
	String sysTextBlock = "";
	Pattern p = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

	boolean sysMessagesHaveBraces = true;

	public SysMessageParesr(TextBreakRepositoryService textBreakRepositoryService) {
		this.setTextBreakRepositoryService(textBreakRepositoryService);
	}

	public SysMessageParesr() {
		// TODO Auto-generated constructor stub
	}

	private static final Pattern systemTextPattern = Pattern.compile("\\{(.*?):(.*?)\\}", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

	Matcher m = null;
	List<String> feildList = null;
	List<String[]> feildListLines = null;

	@Override
	public ParsedData parse(SourceData data) {
		sysMessagesHaveBraces = true;
		ParsedData parsedData = new ParsedData();
		try {
			// LOGGER.debug("parsing System message >> " + " AID = " + data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh());

			parsedData.setSysMessages(true);
			sysTextBlock = data.getTextDataBlock();

			// here to handling when textDataBlock isEmpty
			if (sysTextBlock == null || sysTextBlock.isEmpty()) {
				LOGGER.debug("Warning ::: this Message has empaty text_data_block  >> " + " AID = " + data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh() + " STX >> " + data.getStxVersion());
				parsedData.setFaildParsing(true);
				return parsedData;
			}

			feildList = getSysMesgBlock(sysTextBlock);
			parsedData.setSysTextFieldList(getSystemTextFeilds(feildList, data));
			if (textBreakConfig.isCheckTextIntegrity()) {
				boolean checkSysMessageIntegrity = integrityChecker.checkSysMessageIntegrity(sysTextBlock, parsedData.getSysTextFieldList(), sysMessagesHaveBraces);
				if (!checkSysMessageIntegrity) {
					LOGGER.debug("Unable to decomposed message >> " + " AID = " + data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh());
					textBreakDaoImpl.updateParsinStatus(data, DecomposeStatus.PARSING_FAILED.getStatus());
					daoFactory.getTextBreakDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "", "Error when parseing messages   :: " + " Syntax = " + data.getStxVersion() + " AID = "
							+ data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh() + "   Error Messages : " + " Text Integrity Error");

					parsedData.setFaildParsing(true);
					return parsedData;
				}
			}

			fillParsedData(data, parsedData);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "";
			}
			LOGGER.debug("Unable to decomposed message >> " + " Syntax = " + data.getStxVersion() + " AID = " + data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh());
			textBreakDaoImpl.updateParsinStatus(data, DecomposeStatus.PARSING_FAILED.getStatus());
			daoFactory.getTextBreakDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "", "Error when parseing messages   :: " + " Syntax = " + data.getStxVersion() + " AID = "
					+ data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh() + "   Error Messages : " + ((message.length() < 400) ? message : message.substring(0, 399)));

			parsedData.setFaildParsing(true);
		}

		return parsedData;

	}

	private List<String> extractParsedMessage(String text) {
		final Pattern pattern = Pattern.compile(":(\\w+?):(.*)", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
		List<String> sysFeildList = new ArrayList<>();

		/*
		 * fixed for the following bugs 32142 Dynamic Report: MT586 Message Expand Failed 32140 Dynamic Report: MT565 Message Expand Failed work arround to ignore last space char in text
		 */
		text = text.replaceAll("\\s+$", "");
		// line splitter
		String[] fields = text.split("\\\\r\\\\n:|\\r\\n:|\n:");

		// create ParsedField and attach it to the ParsedMessage object
		for (String field : fields) {
			Matcher matcher = pattern.matcher(":" + field);
			if (matcher.find()) {
				String code = matcher.group(1);
				String value = matcher.group(2);
				sysFeildList.add(code + ":" + value);
			}
		}

		return sysFeildList;
	}

	private void fillParsedData(SourceData data, ParsedData parsedData) {
		parsedData.setAid(data.getAid());
		parsedData.setMesgUmidh(data.getMesgUmidh());
		parsedData.setMesgUmidl(data.getMesgUmidl());
		parsedData.setMesgCreaDateTime(data.getMesgCreaDateTime());
		parsedData.setMesgType(data.getMesgType());
	}

	private List<String> getSysMesgBlock(String textBlock) {
		List<String> sysFeildList = new ArrayList<>();
		m = p.matcher(textBlock.trim());
		while (m.find()) {
			sysFeildList.add(m.group(1));
		}

		if (sysFeildList.isEmpty()) {
			sysMessagesHaveBraces = false;
			sysFeildList = extractParsedMessage(textBlock);
		}
		return sysFeildList;
	}

	public List<String[]> parseSystemText(String dataBlock) {

		try {

			if (dataBlock != null) {
				List<String[]> systemText = new LinkedList<String[]>();

				// Regex parsing
				Matcher matcher = systemTextPattern.matcher(dataBlock);
				while (matcher.find()) {
					String[] array = new String[2];
					array[0] = matcher.group(1);
					array[1] = matcher.group(2);

					systemText.add(array);
				}

				return systemText;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	private List<SystemTextField> getSystemTextFeilds(List<String> feildList, SourceData data) {
		List<SystemTextField> sysTextFeilds = new ArrayList<>();
		int feildCnt = 0;

		for (String feildLine : feildList) {
			String[] feildlineArr = feildLine.trim().split(":", 2);
			SystemTextFieldPK systemTextFieldPK = new SystemTextFieldPK();
			SystemTextField systemTextField = new SystemTextField();
			systemTextFieldPK.setAid(data.getAid());
			systemTextFieldPK.setTextSUmidh(data.getMesgUmidh());
			systemTextFieldPK.setTextSUmidl(data.getMesgUmidl());
			systemTextFieldPK.setFieldCnt(feildCnt);
			feildCnt++;
			systemTextField.setId(systemTextFieldPK);
			systemTextField.setValue(feildlineArr[1]);
			systemTextField.setFieldCode(Long.parseLong(feildlineArr[0]));
			systemTextField.setMesgCreaDateTime(data.getMesgCreaDateTime());
			sysTextFeilds.add(systemTextField);
		}
		return sysTextFeilds;
	}

	private List<SystemTextField> getSystemTextFeildsAnotherWay(List<String[]> feildList, SourceData data) {
		List<SystemTextField> sysTextFeilds = new ArrayList<>();
		int feildCnt = 0;
		for (String[] feildLine : feildList) {
			SystemTextFieldPK systemTextFieldPK = new SystemTextFieldPK();
			SystemTextField systemTextField = new SystemTextField();
			systemTextFieldPK.setAid(data.getAid());
			systemTextFieldPK.setTextSUmidh(data.getMesgUmidh());
			systemTextFieldPK.setTextSUmidl(data.getMesgUmidl());
			systemTextFieldPK.setFieldCnt(feildCnt);
			feildCnt++;
			systemTextField.setId(systemTextFieldPK);
			systemTextField.setValue(feildLine[1]);
			systemTextField.setFieldCode(Long.parseLong(feildLine[0]));
			sysTextFeilds.add(systemTextField);
		}
		return sysTextFeilds;
	}

	public TextBreakRepositoryService getTextBreakRepositoryService() {
		return textBreakRepositoryService;
	}

	public void setTextBreakRepositoryService(TextBreakRepositoryService textBreakRepositoryService) {
		this.textBreakRepositoryService = textBreakRepositoryService;
	}

	public IntegrityChecker getIntegrityChecker() {
		return integrityChecker;
	}

	public void setIntegrityChecker(IntegrityChecker integrityChecker) {
		this.integrityChecker = integrityChecker;
	}

	public TextBreakConfig getTextBreakConfig() {
		return textBreakConfig;
	}

	public void setTextBreakConfig(TextBreakConfig textBreakConfig) {
		this.textBreakConfig = textBreakConfig;
	}

}