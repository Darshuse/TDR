package com.eastnets.service.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminUtils {

	public static boolean isValidEmail(String mailTo) {

		Pattern pattern = Pattern
				.compile("^([A-Za-z0-9][_A-Za-z0-9-\\+'\\!#\\^~]*(\\.[_A-Za-z0-9-\\+'\\!#\\^~]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,}))$");

		Matcher matcher = pattern.matcher(mailTo);
		return matcher.matches();

	}
}
