package com.eastnets.resilience.toolmonitor;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Globals {
	public static String product_name = "enToolMonitor";
	public static String product_version = "1.0.0 build 1";

	public static String getDateString() {
		SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(new Date() ) + " : ";
	}

}
