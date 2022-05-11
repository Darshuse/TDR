package com.eastnets.reportingserver.reportUtil;


import java.text.SimpleDateFormat;
import java.util.Date;


public class Util {

	public static String formatNumberto2Digit(int number) throws Exception {
		
		java.text.DecimalFormat nf = new java.text.DecimalFormat("#00.###");  
		nf.setDecimalSeparatorAlwaysShown(false);  
		return nf.format(number);
	}
	

	public static String formatDate(Date curDate) throws Exception {
		
		SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss" );
		return format.format(curDate).replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
	}
}
