
package com.eastnets.textbreak.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.textbreak.bean.DAOFactoryTb;
import com.eastnets.textbreak.bean.ParsedData;
import com.eastnets.textbreak.bean.TextBreakConfig;
import com.eastnets.textbreak.dao.TextBreakDaoImpl;
import com.eastnets.textbreak.entities.SystemTextField;
import com.eastnets.textbreak.entities.TextField;
import com.eastnets.textbreak.entities.TextFieldLoop;
import com.eastnets.textbreak.enums.DecomposeStatus;
import com.eastnets.textbreak.utility.Constants;
import com.eastnets.textbreak.writer.MessageWriter;

@Service
public class TextBreakWriterService extends TextBreakService {
	private static final Logger LOGGER = Logger.getLogger(TextBreakWriterService.class);

	@Autowired
	MessageWriter messageWriter;

	@Autowired
	TextBreakDaoImpl textBreakDaoImpl;

	@Autowired
	private TextBreakConfig textBreakConfig;

	@Autowired
	DAOFactoryTb daoFactory;

	int messgeCounter = 1;

	// list of rtextfield
	private List<TextField> textFieldList = new ArrayList<>();

	// list of rtextfieldLoop
	private List<TextFieldLoop> textFieldLoopList = new ArrayList<>();

	// list of rSystemtextfield
	private List<SystemTextField> sysTextFieldList = new ArrayList<>();

	public void writeMessages(List<ParsedData> parseMessages) {
		categorisData(parseMessages);
		if (sysTextFieldList != null && sysTextFieldList.size() != 0) {
			fillSYSTextFieldList();
		}
		if (textFieldList != null && textFieldList.size() != 0) {
			fillRtexField();
		}
		resetFlags();
	}

	private void categorisData(List<ParsedData> parseMessages) {
		for (ParsedData data : parseMessages) {
			if (data.isFaildParsing()) {
				continue;
			}
			if (data.isSysMessages()) {
				sysTextFieldList.addAll(data.getSysTextFieldList());

			} else {
				textFieldList.addAll(data.getTextFieldList());
				textFieldLoopList.addAll(data.getTextFieldLoopList());

			}
		}
	}

	private void fillRtexField() {
		try {
			TextField textField = textFieldList.get(textFieldList.size() - 1);
			LOGGER.debug("Perssist  RTEXTFIELD  & RTEXTFIELDLOOP >> for  " + " last message was : aid= " + textField.getId().getAid() + " ,umidl= " + textField.getId().getTextSUmidl() + ",umidh= " + textField.getId().getTextSUmidh());
			messageWriter.persistFinMessages(textFieldList, textFieldLoopList);
			daoFactory.getTextBreakDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_I, Constants.PROGRAM_NAME, "",
					" messages successfully decomposed  last message was : aid= " + textField.getId().getAid() + " ,umidl= " + textField.getId().getTextSUmidl() + ",umidh= " + textField.getId().getTextSUmidh());
		} catch (Exception e) {
			textBreakDaoImpl.updateFinMessageStatus(textFieldList, DecomposeStatus.INSERTION_FAILED.getStatus());
			String message = e.getMessage();

			if (message == null) {
				message = "";
			}
			TextField textField = textFieldList.get(textFieldList.size() - 1);
			LOGGER.debug("Exception occurred when persisting  Messages Bulk  INTO  (RTEXTFIELD  & RTEXTFIELDLOOP) >> " + "last message was : aid= " + textField.getId().getAid() + " ,umidl= " + textField.getId().getTextSUmidl() + ",umidh= "
					+ textField.getId().getTextSUmidh());
			daoFactory.getTextBreakDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "",
					"Exception occurred when persisting  Messages Bulk  INTO  (RTEXTFIELD  & RTEXTFIELDLOOP) >> " + "last message was : aid= " + textField.getId().getAid() + " ,umidl= " + textField.getId().getTextSUmidl() + ",umidh= "
							+ textField.getId().getTextSUmidh() + "   Error Messages : " + ((message.length() < 400) ? message : message.substring(0, 399)));
		}

	}

	private void fillSYSTextFieldList() {
		try {
			SystemTextField systemTextField = sysTextFieldList.get(sysTextFieldList.size() - 1);
			LOGGER.debug("Perssist  rTextSysFeild  >> for  " + " last message was : aid= " + systemTextField.getId().getAid() + " ,umidl= " + systemTextField.getId().getTextSUmidl() + ",umidh= " + systemTextField.getId().getTextSUmidh());
			messageWriter.persistSystemMessages(sysTextFieldList);
			daoFactory.getTextBreakDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_I, Constants.PROGRAM_NAME, "",
					"  SYS messages successfully decomposed  last message was : aid= " + systemTextField.getId().getAid() + " ,umidl= " + systemTextField.getId().getTextSUmidl() + ",umidh= " + systemTextField.getId().getTextSUmidh());
		} catch (Exception e) {
			textBreakDaoImpl.updateSysMessageStatus(sysTextFieldList, DecomposeStatus.INSERTION_FAILED.getStatus());
			String message = e.getMessage();
			if (message == null) {
				message = "";
			}
			SystemTextField systemTextField = sysTextFieldList.get(sysTextFieldList.size() - 1);
			LOGGER.debug("Exception occurred when persisting SYS Messages Bulk   >> " + " last message was : aid= " + systemTextField.getId().getAid() + " ,umidl= " + systemTextField.getId().getTextSUmidl() + ",umidh= "
					+ systemTextField.getId().getTextSUmidh());
			daoFactory.getTextBreakDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "",
					"Exception occurred when persisting SYS Messages Bulk INTO   (rTextSysFeild)  >> " + "last message was : aid= " + systemTextField.getId().getAid() + " ,umidl= " + systemTextField.getId().getTextSUmidl() + ",umidh= "
							+ systemTextField.getId().getTextSUmidh() + "   Error Messages : " + ((message.length() < 400) ? message : message.substring(0, 399)));
		}

	}

	public void resetFlags() {
		textFieldList = new ArrayList<>();
		textFieldLoopList = new ArrayList<>();
		sysTextFieldList = new ArrayList<>();
	}

	public MessageWriter getMessageWriter() {
		return messageWriter;
	}

	public void setMessageWriter(MessageWriter messageWriter) {
		this.messageWriter = messageWriter;
	}

	public TextBreakConfig getTextBreakConfig() {
		return textBreakConfig;
	}

	public void setTextBreakConfig(TextBreakConfig textBreakConfig) {
		this.textBreakConfig = textBreakConfig;
	}

}
