
package com.eastnets.textbreak.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.resultbean.TextBreakResultBean;
import com.eastnets.textbreak.bean.DAOFactoryTb;
import com.eastnets.textbreak.entities.GlobalSettings;
import com.eastnets.textbreak.repositories.GlobalSettingRepository;

@Service
public class TextBreakRepositoryService {

	@Autowired
	private GlobalSettingRepository globalSettingsRepository;

	@Autowired
	DAOFactoryTb daoFactory;

	@Transactional(propagation = Propagation.REQUIRED)

	public void updateText(int aid, Long umidh, Long umidl) {
		// textRepository.updateText(aid, umidh, umidl);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTextStatus(String status, int aid, Long umidh, Long umidl) {
		// textRepository.updateTextStatus(status, aid, umidh, umidl);
	}

	@Transactional(readOnly = true)
	public List<TextBreakResultBean> findTextBreakMessages(Date fromDate, Date toDate, String msgFrmtName, String aid, Pageable pageable, boolean allMode, boolean isPart, boolean onLineDecoomposed, Integer messageNumber) throws ParseException {
		if (onLineDecoomposed) {
			return daoFactory.getTextBreakDAO().getSelectedOnlineTextBreakMessage(fromDate, toDate, msgFrmtName, Integer.parseInt(aid), isPart, onLineDecoomposed, messageNumber);
		}
		if (allMode == true) {
			return daoFactory.getTextBreakDAO().getAllMessages(fromDate, toDate, msgFrmtName, Integer.parseInt(aid), isPart, onLineDecoomposed, messageNumber);
		} else {
			return daoFactory.getTextBreakDAO().getConfiguredMessages(fromDate, toDate, msgFrmtName, Integer.parseInt(aid), isPart, onLineDecoomposed, messageNumber);
		}
	}

	@Transactional(readOnly = true)
	public List<TextBreakResultBean> findRecoveryMessages(Date fromDate, Date toDate, String msgFrmtName, String aid, Pageable pageable, boolean isPart, Integer messageNumber) throws ParseException {
		return daoFactory.getTextBreakDAO().getRecoveryMessages(fromDate, toDate, msgFrmtName, Integer.parseInt(aid), isPart, messageNumber);

	}

	@Transactional(readOnly = true)
	public List<TextBreakResultBean> findRestoreMessages(String msgFrmtName, String aid) throws ParseException {
		// return mesgRepository.findRestoreMessages(msgFrmtName, Integer.parseInt(aid));
		return null;
	}

	@Transactional(readOnly = true)
	public List<GlobalSettings> getGlobelSettings() {
		return globalSettingsRepository.findAll();
	}

}
