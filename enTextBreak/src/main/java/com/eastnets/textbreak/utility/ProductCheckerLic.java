package com.eastnets.textbreak.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.textbreak.dao.TextBreakDaoImpl;
import com.eastnets.textbreak.dao.beans.User;

@Service
public class ProductCheckerLic {

	@Autowired
	TextBreakDaoImpl textBreakDaoImpl;

	public boolean checkUser(String username) {
		User user = textBreakDaoImpl.getUserProfileId(username);
		if (user != null) {
			return true;
		}
		return false;
	}

	public boolean sCheckRoals() {
		return textBreakDaoImpl.sCheckRoals();
	}

}
