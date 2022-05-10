package com.eastnets.watchdog.dumper;

import org.springframework.beans.factory.annotation.Autowired;

import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.service.WatchDogRepositoryService;

public abstract class Dumper implements Runnable {

	@Autowired
	WatchDogRepositoryService watchDogService;

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

}
