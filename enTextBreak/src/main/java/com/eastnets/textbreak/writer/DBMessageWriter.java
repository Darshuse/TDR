
package com.eastnets.textbreak.writer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.textbreak.dao.TextBreakDaoImpl;
import com.eastnets.textbreak.entities.SystemTextField;
import com.eastnets.textbreak.entities.TextField;
import com.eastnets.textbreak.entities.TextFieldLoop;
import com.eastnets.textbreak.enums.DecomposeStatus;
import com.eastnets.textbreak.service.TextBreakRepositoryService;

@Service
public class DBMessageWriter implements MessageWriter {

	private static final Logger LOGGER = Logger.getLogger(DBMessageWriter.class);

	@Autowired
	private TextBreakRepositoryService textBreakRepositoryService;

	@Autowired
	TextBreakDaoImpl textBreakDaoImpl;

	/**
	 * Responsible for Insert Into RtextField and RtextFieldLoop also we added Transactional in case of any exceptions accrued , if every thing is fine the rtext status column will be update to 1
	 */
	@Transactional
	@Override
	public void persistFinMessages(List<TextField> textFieldList, List<TextFieldLoop> textFieldLoopList) {
		if (textFieldList != null && !textFieldList.isEmpty()) {
			textBreakDaoImpl.fillTextField(textFieldList, null);
		}
		if (textFieldLoopList != null && !textFieldLoopList.isEmpty()) {
			textBreakDaoImpl.fillTextLoop(textFieldLoopList, null);
		}
		textBreakDaoImpl.updateFinMessageStatus(textFieldList, DecomposeStatus.DECOMPOSED.getStatus());
	}

	/**
	 * Responsible for Insert Into rSystemTextField also we added Transactional in case of any exceptions accrued , if every thing is fine the rtext status column will be update to 1
	 */
	@Transactional
	@Override
	public void persistSystemMessages(List<SystemTextField> sysTextFieldList) {
		textBreakDaoImpl.fillSystemTextField(sysTextFieldList, null);
		textBreakDaoImpl.updateSysMessageStatus(sysTextFieldList, DecomposeStatus.DECOMPOSED.getStatus());
	}

	public TextBreakRepositoryService getTextBreakRepositoryService() {
		return textBreakRepositoryService;
	}

	public void setTextBreakRepositoryService(TextBreakRepositoryService textBreakRepositoryService) {
		this.textBreakRepositoryService = textBreakRepositoryService;
	}

}
