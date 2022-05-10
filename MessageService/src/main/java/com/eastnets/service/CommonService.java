package com.eastnets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.entities.AppendixPK;
import com.eastnets.entities.GlobalSettings;
import com.eastnets.entities.LiveMessage;
import com.eastnets.repositories.AppendixRepository;
import com.eastnets.repositories.GlobalSettingsRepository;
import com.eastnets.repositories.LiveMessageRepository;
import com.eastnets.repositories.MesgRepository;
import com.eastnets.response.dto.AppendixDTO;

@Service
public class CommonService {

	@Autowired
	private GlobalSettingsRepository globalSettingsRepository;

	@Autowired
	private AppendixRepository appendixRepository;

	@Autowired
	private MesgRepository mesgRepository;

	@Autowired
	private LiveMessageRepository liveMessageRepository;


	/**
	 * functions that call repositories methods
	 */

	@Transactional(readOnly = true)
	public List<GlobalSettings> getGlobalSettings() {
		return globalSettingsRepository.findAll();
	}

	public List<LiveMessage> getLiveMessages() {
		return liveMessageRepository.getLiveMessages();
	}

	public List<AppendixDTO> getAppendixInfo(AppendixPK id) {
		return appendixRepository.getAppendixInfo(id.getAppeSUmidl(), id.getAppeSUmidh(), id.getAid(),
				id.getAppeInstNum(), id.getAppeDateTime(), id.getAppeSeqNbr());
	}

	public int checkIfMessagesIsArchived(AppendixPK id) {
		return mesgRepository.isMessageArchived(id.getAid(), id.getAppeSUmidl(), id.getAppeSUmidh());
	}

}
