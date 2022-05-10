
package com.eastnets.watchdog.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.watchdog.dao.WatchdogDaoImpl;
import com.eastnets.watchdog.resultbeans.User;

@Service
public class ProductChecker {

	@Autowired
	WatchdogDaoImpl watchdogDaoImpl;

	public boolean checkUser(String username) {
		User user = watchdogDaoImpl.getUserProfileId(username);
		if (user != null) {
			return true;
		}
		return false;
	}

	public boolean sCheckRoals() {
		return watchdogDaoImpl.sCheckRoals();
	}

}
