/**
 * 
 */
package com.eastnets.reporting.licensing.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author EastNets
 * @since dDec 4, 2012
 * 
 */
public class ProductUtils implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6740587057458580028L;
	private static Calendar dateRef = null;

	static {
		ProductUtils.dateRef = Calendar.getInstance();
		ProductUtils.dateRef.set(1990, 0, 1, 0, 0, 0);
	}
	
	private static long daysBetween(final Calendar startDate, final Calendar endDate) {
        final Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }
	
	public static long daysUntill(final Date toDate) {
		Calendar toCal = Calendar.getInstance();
		toCal.setTime(toDate);
		toCal.set(Calendar.HOUR_OF_DAY, 0);
		toCal.set(Calendar.MINUTE, 0);
		toCal.set(Calendar.SECOND, 0);
		toCal.set(Calendar.MILLISECOND, 0);
		return ProductUtils.daysBetween(ProductUtils.dateRef, toCal);
	}

}
