package com.eastnets.watchdog.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.service.WatchDogRepositoryService;
import com.eastnets.watchdog.service.WatchDogRestfulService;

@Service
public abstract class Checker implements Runnable {

	@Autowired
	WatchDogRepositoryService watchDogRepositoryService;
	@Autowired
	WatchDogRestfulService watchDogRestfulService;

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

}
