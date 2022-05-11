package com.eastnets.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class AdvancedDate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8742763508837944681L;

	public static final int TYPE_DATE = 0;
	public static final int TYPE_DAYS = 1;
	public static final int TYPE_WEEKS = 2;
	public static final int TYPE_MONTHS = 3;

	private Date date;
	private Integer days = 0;
	private Integer weeks = 0;
	private Integer months = 0;

	private Date secondDate;

	private Date timeOne;
	private Date timeTow;

	private int type = TYPE_DATE;
	private boolean includeCurrent = false;

	private static String MONTH_STR = "MONTH";
	private static String WEEK_STR = "WEEK";
	private static String DAY_STR = "DAY";

	public void clear() {
		date = null;
		days = null;
		weeks = null;
		months = null;
		type = TYPE_DATE;
	}

	public AdvancedDate() {

	}

	public Date getDate() {
		return date;
	}

	public Timestamp getDateAsTimeStamp() {
		if (date == null) {
			return null;
		}
		Timestamp timeStampDate = new Timestamp(date.getTime());
		return timeStampDate;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getWeeks() {
		return weeks;
	}

	public void setWeeks(Integer weeks) {
		this.weeks = weeks;
	}

	public Integer getMonths() {
		return months;
	}

	public void setMonths(Integer months) {
		this.months = months;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getValue() {
		if (type == TYPE_DAYS) {
			return getDays();
		}
		if (type == TYPE_WEEKS) {
			return getWeeks();
		}
		if (type == TYPE_MONTHS) {
			return getMonths();
		}
		return null;
	}

	public void setValue(Integer value) {
		if (type == TYPE_DAYS) {
			setDays(value);
		} else if (type == TYPE_WEEKS) {
			setWeeks(value);
		} else if (type == TYPE_MONTHS) {
			setMonths(value);
		}
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	private LocalDateTime getDataWithTime(Date time, Date date) {
		try {
			String timeFrom = new SimpleDateFormat("HH:mm:ss").format(time);
			String dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(date);
			LocalDate datePart = LocalDate.parse(dateFrom);
			LocalTime timePart = LocalTime.parse(timeFrom);
			LocalDateTime dt = LocalDateTime.of(datePart, timePart);
			return dt;
		} catch (Exception e) {
			return null;
		}

	}

	public Date getDateValue(boolean resetTime) {
		if (type == TYPE_DATE) {
			LocalDateTime dt = null;
			if (timeOne != null)
				dt = getDataWithTime(timeOne, date);

			if (resetTime) {
				return getDate((dt != null) ? asDate(dt) : date, 0, 0, 0);
			}
			return (dt != null) ? asDate(dt) : date;
		}

		int field = Calendar.DATE;
		Calendar calendar = Calendar.getInstance();

		if (type == TYPE_MONTHS) {
			field = Calendar.MONTH;
		} else if (type == TYPE_WEEKS) {
			field = Calendar.WEEK_OF_YEAR;
		} else if (type == TYPE_DAYS) {
			field = Calendar.DATE;
		}
		Integer value = getValue();
		if (value == null) {
			return null;
		}
		int amount = -1 * value;
		calendar.add(field, amount);
		if (field == Calendar.WEEK_OF_YEAR) {
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek < calendar.getFirstDayOfWeek()) {
				dayOfWeek += 7;
			}
			calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - dayOfWeek);
		}
		if (field == Calendar.MONTH) {
			calendar.set(Calendar.DATE, 1);
		}

		Date theDate = calendar.getTime();
		theDate = getDate(theDate, 0, 0, 0);

		return theDate;
	}

	public static Date getToday(int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hour, minute, second);
		return cal.getTime();
	}

	public static Date getDate(Date date, int hour, int minute, int second) {
		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hour, minute, second);
		return cal.getTime();
	}

	public Date getSecondDate() {
		return secondDate;
	}

	public void setSecondDate(Date secondDate) {
		this.secondDate = secondDate;
	}

	public Date getDateFrom() {
		return getDateValue(false);
	}

	private Date getsecondDateWithTime(Date secondDate) {
		try {
			if (secondDate == null) {
				return null;
			}
			String dateFrom = new SimpleDateFormat("yyyy/MM/dd").format(secondDate);
			String toDateStr = dateFrom + " 23:59:59";
			return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(toDateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Date getDateTo() {
		if (type == TYPE_DATE) {
			LocalDateTime dt = null;
			if (timeTow != null) {
				dt = getDataWithTime(timeTow, secondDate);
			}
			return (dt != null) ? asDate(dt) : getsecondDateWithTime(secondDate);
		}

		Integer v = getValue();
		if (includeCurrent || v == null || v == 0) {
			return getToday(23, 59, 59);
		}

		Calendar calendar = Calendar.getInstance();

		// if its a week, get back to the first day of the week
		if (type == TYPE_WEEKS) {
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek < calendar.getFirstDayOfWeek()) {
				dayOfWeek += 7;
			}
			calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - dayOfWeek);
		}

		// if its a month get back to the first day of the month
		if (type == TYPE_MONTHS) {
			calendar.set(Calendar.DATE, 1);
		}

		// get back one day
		calendar.add(Calendar.DATE, -1);

		Date theDate = calendar.getTime();
		theDate = getDate(theDate, 23, 59, 59);// reset the time

		return theDate;
	}

	public boolean parseFormatted(String advancedDateStr) {
		if (advancedDateStr == null || advancedDateStr.trim().isEmpty()) {
			return false;
		}
		clear();

		String[] split = advancedDateStr.split("-");
		if (split.length >= 2 && (MONTH_STR.equalsIgnoreCase(split[0]) || WEEK_STR.equalsIgnoreCase(split[0]) || DAY_STR.equalsIgnoreCase(split[0]))) {
			setType(AdvancedDate.TYPE_DAYS);
			if (split[0].equals(MONTH_STR)) {
				setType(AdvancedDate.TYPE_MONTHS);
			} else if (split[0].equals(WEEK_STR)) {
				setType(AdvancedDate.TYPE_WEEKS);
			} else if (split[0].equals(DAY_STR)) {
				setType(AdvancedDate.TYPE_DAYS);
			}
			String[] split2 = split[1].split("_");

			includeCurrent = false;
			if (split2.length > 1) {
				includeCurrent = split2[1].trim().equals("1");
			}

			setValue(Integer.parseInt(split2[0]));
			return true;
		}

		// the format is datePattern;;from_date;;to_date
		split = advancedDateStr.split(";;");
		if (split.length != 3) {
			return false;
		}
		String datePattern = split[0].trim().replace("\\;", ";");

		setType(AdvancedDate.TYPE_DATE);
		Date dateValue = null;
		DateFormat dateTimeFormat = new SimpleDateFormat(datePattern);
		try {
			dateValue = dateTimeFormat.parse(split[1].trim().replace("\\;", ";"));
		} catch (ParseException e) {
		}
		setDate(dateValue);

		dateValue = null;
		try {
			dateValue = dateTimeFormat.parse(split[2].trim().replace("\\;", ";"));
		} catch (ParseException e) {
		}

		setSecondDate(dateValue);

		return true;
	}

	public void parseTimeFrom(String timeStr) {
		try {
			SimpleDateFormat formatWriter = new SimpleDateFormat("HH:mm:ss");
			Date parse = formatWriter.parse(timeStr);
			setTimeOne(parse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public void parseTimeTo(String timeStr) {
		try {
			SimpleDateFormat formatWriter = new SimpleDateFormat("HH:mm:ss");
			Date parse = formatWriter.parse(timeStr);
			setTimeTow(parse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public String formatTimeFrom() {
		try {
			if (getTimeOne() == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String datrFromformated = sdf.format(getTimeOne());
			return datrFromformated;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String formatTimeTo() {
		try {
			if (getTimeTow() == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String datrToformated = sdf.format(getTimeTow());
			return datrToformated;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String format(String datePattern) {
		if ((getType() == TYPE_DAYS && getDays() == null) || (getType() == TYPE_WEEKS && getWeeks() == null) || (getType() == TYPE_MONTHS && getMonths() == null)) {
			return "";
		}
		if (getType() == TYPE_DATE && (getDate() == null || getSecondDate() == null)) {
			return "";
		}

		if (getType() == TYPE_DAYS) {
			return DAY_STR + "-" + getDays() + "_" + (includeCurrent ? "1" : "0");
		}

		if (getType() == TYPE_WEEKS) {
			return WEEK_STR + "-" + getWeeks() + "_" + (includeCurrent ? "1" : "0");
		}
		if (getType() == TYPE_MONTHS) {
			return MONTH_STR + "-" + getMonths() + "_" + (includeCurrent ? "1" : "0");
		}
		if (datePattern == null || datePattern.isEmpty()) {
			return "";
		}

		// the format is datePattern;;from_date;;to_date
		String value = datePattern.replace(";", "\\;");
		value += ";;";
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		try {
			value += sdf.format(getDate()).replace(";", "\\;");
		} catch (Exception ex) {
		}

		value += ";;";
		try {
			value += sdf.format(getSecondDate()).replace(";", "\\;");
		} catch (Exception ex) {
		}

		return value;
	}

	public boolean isIncludeCurrent() {
		return includeCurrent;
	}

	public void setIncludeCurrent(boolean includeCurrent) {
		this.includeCurrent = includeCurrent;
	}

	public Date getTimeOne() {
		return timeOne;
	}

	public void setTimeOne(Date timeOne) {
		this.timeOne = timeOne;
	}

	public Date getTimeTow() {
		return timeTow;
	}

	public void setTimeTow(Date timeTow) {
		this.timeTow = timeTow;
	}
}
