package com.eastnets.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.entities.GlobalSettings;
import com.eastnets.repositories.GlobalSettingsRepository;

@Service
public class CommonService {

	@Autowired
	private GlobalSettingsRepository globalSettingsRepository;

	@Transactional(readOnly = true)
	public List<GlobalSettings> getGlobalSettings() {
		return globalSettingsRepository.findAll();
	}

	public GlobalSettingsRepository getGlobalSettingsRepository() {
		return globalSettingsRepository;
	}

	public void setGlobalSettingsRepository(GlobalSettingsRepository globalSettingsRepository) {
		this.globalSettingsRepository = globalSettingsRepository;
	}

}
