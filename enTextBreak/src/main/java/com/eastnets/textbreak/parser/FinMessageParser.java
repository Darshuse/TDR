
package com.eastnets.textbreak.parser;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.bean.ParsedField;
import com.eastnets.resilience.textparser.bean.ParsedLoop;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.syntax.Message;
import com.eastnets.textbreak.bean.DAOFactoryTb;
import com.eastnets.textbreak.bean.ParsedData;
import com.eastnets.textbreak.bean.SourceData;
import com.eastnets.textbreak.bean.TextBreakConfig;
import com.eastnets.textbreak.dao.TextBreakDaoImpl;
import com.eastnets.textbreak.entities.TextField;
import com.eastnets.textbreak.entities.TextFieldLoop;
import com.eastnets.textbreak.entities.TextFieldLoopPK;
import com.eastnets.textbreak.entities.TextFieldPK;
import com.eastnets.textbreak.enums.DecomposeStatus;
import com.eastnets.textbreak.service.TextBreakRepositoryService;
import com.eastnets.textbreak.utility.Constants;
import com.eastnets.textbreak.utility.IntegrityChecker;

/**
 * @author MKassab
 * 
 */

@Service
public class FinMessageParser implements MessageParser {
	private static final Logger LOGGER = Logger.getLogger(FinMessageParser.class);

	@Autowired
	private DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	TextBreakRepositoryService textBreakRepositoryService;

	@Autowired
	TextBreakDaoImpl textBreakDaoImpl;

	@Autowired
	DAOFactoryTb daoFactory;

	@Autowired
	private TextBreakConfig textBreakConfig;

	@Autowired
	private IntegrityChecker integrityChecker;

	// the edge between value and value_memo
	private static final int MEMO_SIZE = 1750;
	Connection expandConn = null;
	ParsedMessage parsedMessage;
	Message messageParser;

	@Override
	public ParsedData parse(SourceData data) {
		ParsedData parsedData = new ParsedData();
		try {
			// LOGGER.debug("Parsing Fin message >> " + " AID = " + data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh());

			String textDataBlock = data.getTextDataBlock();
			// here to handling when textDataBlock isEmpty
			if (textDataBlock == null || textDataBlock.isEmpty()) {
				LOGGER.debug("Warning ::: this Message has empaty text_data_block  >> " + " AID = " + data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh() + " STX >> " + data.getStxVersion());
				parsedData.setFaildParsing(true);
				return parsedData;
			}

			try {
				if (!textBreakConfig.isOnlinedecompos() && (expandConn == null || expandConn.isClosed())) {
					expandConn = jdbcTemplate.getDataSource().getConnection();
				}
			} catch (Exception e) {
				// e.printStackTrace();

			}

			parsedData.setSysMessages(false);
			messageParser = Syntax.getParser(data.getStxVersion(), data.getMesgType(), (!textBreakConfig.isOnlinedecompos()) ? expandConn : textBreakConfig.getExpandConnConfig());
			parsedMessage = messageParser.parseMessageText(data.getTextDataBlock());
			// fill rtexttextFieldsLoop List
			parsedData.setTextFieldLoopList(fillMessageTextFieldsLoop(data));
			// fill rtexttextFields List
			parsedData.setTextFieldList(fillMessageTextFields(data));
			if (textBreakConfig.isCheckTextIntegrity()) {
				boolean checkFinMessageIntegrity = integrityChecker.checkFinMessageIntegrity(textDataBlock, parsedData.getTextFieldList());
				if (!checkFinMessageIntegrity) {
					textBreakDaoImpl.updateParsinStatus(data, DecomposeStatus.PARSING_FAILED.getStatus());
					parsedData.setFaildParsing(true);
					LOGGER.debug("Unable to decomposed message >> " + " Syntax = " + data.getStxVersion() + " AID = " + data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh());
					daoFactory.getTextBreakDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "", "Error when parseing messages   :: " + " Syntax = " + data.getStxVersion() + " AID = "
							+ data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh() + "   Error Messages : " + " Text Integrity Error");
					return parsedData;
				}
			}

			fillParsedData(data, parsedData);
		} catch (Exception e) {
			textBreakDaoImpl.updateParsinStatus(data, DecomposeStatus.PARSING_FAILED.getStatus());
			String message = e.getMessage();
			if (message == null) {
				message = "";
			}
			parsedData.setFaildParsing(true);
			LOGGER.debug("Unable to decomposed message >> " + " Syntax = " + data.getStxVersion() + " AID = " + data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh());
			daoFactory.getTextBreakDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "", "Error when parseing messages   :: " + " Syntax = " + data.getStxVersion() + " AID = "
					+ data.getAid() + " UMIDL = " + data.getMesgUmidl() + " UMIDH = " + data.getMesgUmidh() + "   Error Messages : " + ((message.length() < 400) ? message : message.substring(0, 399)));

		}

		return parsedData;

	}

	private void fillParsedData(SourceData data, ParsedData parsedData) {
		parsedData.setAid(data.getAid());
		parsedData.setMesgUmidh(data.getMesgUmidh());
		parsedData.setMesgUmidl(data.getMesgUmidl());
		parsedData.setMesgCreaDateTime(data.getMesgCreaDateTime());
		parsedData.setMesgType(data.getMesgType());
	}

	private List<TextFieldLoop> fillMessageTextFieldsLoop(SourceData data) {
		Map<Integer, ParsedLoop> parsedLoop = parsedMessage.getParsedLoops();
		List<TextFieldLoop> textFieldsLoopList = new ArrayList<>();
		for (Integer key : parsedLoop.keySet()) {
			ParsedLoop loop = parsedLoop.get(key);
			TextFieldLoop fieldLoop = new TextFieldLoop();
			TextFieldLoopPK fieldLoopPK = new TextFieldLoopPK();
			fieldLoopPK.setAid(data.getAid());
			fieldLoopPK.setTextSUmidh(data.getMesgUmidh());
			fieldLoopPK.setTextSUmidl(data.getMesgUmidl());
			fieldLoopPK.setGroupIdx(loop.getGroupIdx());
			fieldLoopPK.setSequenceId((loop.getSequence() == null || loop.getSequence().isEmpty()) ? "0" : loop.getSequence());
			fieldLoop.setId(fieldLoopPK);
			fieldLoop.setGroupCnt(loop.getGroupCnt());
			fieldLoop.setGroupId(Long.parseLong(loop.getGroupId()));
			fieldLoop.setParentGroupIGdx(loop.getParentGroupIdx());
			fieldLoop.setMesgCreaDateTime(data.getMesgCreaDateTime());
			textFieldsLoopList.add(fieldLoop);
		}

		return textFieldsLoopList;
	}

	private List<TextField> fillMessageTextFields(SourceData data) {
		List<ParsedField> parsedFields = parsedMessage.getParsedFields();
		List<TextField> textFieldsList = new ArrayList<>();

		int cntCounter = 0;
		for (ParsedField field : parsedFields) {
			TextFieldPK fieldPK = new TextFieldPK();
			TextField textField = new TextField();
			fieldPK.setAid(data.getAid());
			fieldPK.setTextSUmidh(data.getMesgUmidh());
			fieldPK.setTextSUmidl(data.getMesgUmidl());
			fieldPK.setFieldCode((field.getFieldCode() != null) ? Long.parseLong(field.getFieldCode().replaceAll("\\D+", "")) : 0L);
			fieldPK.setFieldCodeId(field.getCodeId());
			fieldPK.setSequenceId((field.getSequence() == null || field.getSequence().isEmpty()) ? "0" : field.getSequence());
			fieldPK.setGroupIdx(field.getGroupIdx());
			textField.setId(fieldPK);
			textField.setFieldOption(field.getFieldOption());
			if (field.getMaxTextSize() >= MEMO_SIZE || fieldPK.getFieldCode() == 0) {
				textField.setValueMemo(field.getValue());
			} else {
				textField.setValue(field.getValue());
			}
			textField.setFieldCnt(cntCounter);
			textField.setMesgCreaDateTime(data.getMesgCreaDateTime());
			textFieldsList.add(textField);
			cntCounter++;
		}

		return textFieldsList;

		// data.setTextFieldList(textFieldsList);

	}

	public TextBreakConfig getTextBreakConfig() {
		return textBreakConfig;
	}

	public void setTextBreakConfig(TextBreakConfig textBreakConfig) {
		this.textBreakConfig = textBreakConfig;
	}

	public IntegrityChecker getIntegrityChecker() {
		return integrityChecker;
	}

	public void setIntegrityChecker(IntegrityChecker integrityChecker) {
		this.integrityChecker = integrityChecker;
	}

}
