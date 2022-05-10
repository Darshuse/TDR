package com.eastnets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.common.service.CommonService;
import com.eastnets.textbreak.service.TextBreakRepositoryService;
import com.eastnets.watchdog.service.WatchDogService;

@Service
public class ServiceLocator {

	@Autowired
	private CommonService commonService;

	@Autowired
	private TextBreakRepositoryService textBreakService;

	@Autowired
	private WatchDogService watchDogService;

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public TextBreakRepositoryService getTextBreakService() {
		return textBreakService;
	}

	public void setTextBreakService(TextBreakRepositoryService textBreakService) {
		this.textBreakService = textBreakService;
	}

	public WatchDogService getWatchDogService() {
		return watchDogService;
	}

	public void setWatchDogService(WatchDogService watchDogService) {
		this.watchDogService = watchDogService;
	}

}
