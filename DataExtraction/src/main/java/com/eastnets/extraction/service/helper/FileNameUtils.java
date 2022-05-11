package com.eastnets.extraction.service.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.eastnets.extraction.bean.SearchParam;

public class FileNameUtils {

	public static String getFormatedFileName(SearchParam searchParam) {

		String fileName = searchParam.getFileName();

		if (fileName == null || fileName.length() == 0) {
			fileName = "TDR_%Y%M%D%H%m%s_" + (searchParam.getAid().size() > 0 ? searchParam.getAid().get(0) : "Aid")
					+ "_" + searchParam.getMode();
		}

		fileName = replaceYearTimeStamp(fileName);
		fileName = replaceMonthTimeStamp(fileName);
		fileName = replaceDayTimeStamp(fileName);
		fileName = replaceHourTimeStamp(fileName);
		fileName = replaceMinuteTimeStamp(fileName);
		fileName = replaceSecondTimeStamp(fileName);
		fileName = replaceAM_PMTimeStamp(fileName);
		fileName = replaceFullDateFormatTimeStamp(fileName);
		fileName = replaceTTimeStamp(fileName);
		fileName = replaceTAM_PMTimeStamp(fileName);
		return fileName;
	}
	
	public static String getFormatedFileName(String fileName) {

		fileName = replaceYearTimeStamp(fileName);
		fileName = replaceMonthTimeStamp(fileName);
		fileName = replaceDayTimeStamp(fileName);
		fileName = replaceHourTimeStamp(fileName);
		fileName = replaceMinuteTimeStamp(fileName);
		fileName = replaceSecondTimeStamp(fileName);
		fileName = replaceAM_PMTimeStamp(fileName);
		fileName = replaceFullDateFormatTimeStamp(fileName);
		fileName = replaceTTimeStamp(fileName);
		fileName = replaceTAM_PMTimeStamp(fileName);
		return fileName;
	}

	public static String replaceYearTimeStamp(String fileName) {
		fileName = fileName.replaceAll("%Y", getYYYY());
		fileName = fileName.replaceAll("%y", getYY());
		return fileName;
	}

	public static String replaceMonthTimeStamp(String fileName) {
		return fileName.replaceAll("%M", getMM());

	}

	public static String replaceDayTimeStamp(String fileName) {
		return fileName.replaceAll("%D", getDD());
	}

	public static String replaceHourTimeStamp(String fileName) {
		fileName = fileName.replaceAll("%H", getHH24());
		fileName = fileName.replaceAll("%h", getHH12());
		return fileName;
	}

	public static String replaceMinuteTimeStamp(String fileName) {
		return fileName.replaceAll("%m", getMinutes());
	}

	public static String replaceSecondTimeStamp(String fileName) {
		return fileName.replaceAll("%s", getSeconds());
	}

	public static String replaceAM_PMTimeStamp(String fileName) {
		return fileName.replaceAll("%a", getPM_AM());
	}

	public static String replaceFullDateFormatTimeStamp(String fileName) {
		return fileName.replaceAll("%F", getFullDateFormat());
	}

	public static String replaceTTimeStamp(String fileName) {
		return fileName.replaceAll("%T", getFullTimeFormat24());
	}

	public static String replaceTAM_PMTimeStamp(String fileName) {
		return fileName.replaceAll("%t", getFullTimeFormat12());
	}

	public static String getYYYY() {
		return new SimpleDateFormat("yyyy").format(new Date());
	}

	public static String getYY() {
		return new SimpleDateFormat("yy").format(new Date());
	}

	public static String getMM() {
		return new SimpleDateFormat("MM").format(new Date());
	}

	public static String getDD() {
		return new SimpleDateFormat("dd").format(new Date());
	}

	public static String getHH24() {
		return new SimpleDateFormat("HH").format(new Date());
	}

	public static String getHH12() {
		return new SimpleDateFormat("hh").format(new Date());
	}

	public static String getMinutes() {
		return new SimpleDateFormat("mm").format(new Date());
	}

	public static String getSeconds() {
		return new SimpleDateFormat("ss").format(new Date());
	}

	public static String getPM_AM() {
		return new SimpleDateFormat("a").format(new Date());
	}

	public static String getFullDateFormat() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	public static String getFullTimeFormat24() {
		return new SimpleDateFormat("HHmmss").format(new Date());
	}

	public static String getFullTimeFormat12() {
		return new SimpleDateFormat("hhmmss a").format(new Date());
	}
}
