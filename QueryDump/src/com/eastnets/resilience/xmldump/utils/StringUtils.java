/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */
package com.eastnets.resilience.xmldump.utils;

import com.eastnets.resilience.textparser.util.SwiftFormatUtils;

/**
 * General common string functions
 * 
 * @author EHakawati
 * 
 */
public class StringUtils {

	/**
	 * extract the umidl from he sumid
	 * 
	 * @param sumid
	 * @return umidl
	 */
	public static int getUmidl(String sumid) {
		String high = sumid.substring(0, 8);
		return (int) Long.parseLong(high, 16);
	}

	/**
	 * extract the umidh from he sumid
	 * 
	 * @param sumid
	 * @return umidh
	 */
	public static int getUmidh(String sumid) {
		String low = sumid.substring(8, 16);
		return (int) Long.parseLong(low, 16);
	}

	/**
	 * Compute CRC16 of a string
	 * 
	 * @param data
	 * @return
	 */
	public static int computeCRC(String data) {

		// back word compatibility
		data = data.replaceAll("\r\n", "\\\\r\\\\n");
		// compute CRC
		return CRC16.compute(data.getBytes());

	}

	/**
	 * replace each \n by \\r\\n for database importing use
	 * 
	 * @param string
	 * @return
	 */
	public static String forceStringCarriageReturn(String string) {
		return string.replaceAll("\n", "\\\\r\\\\n");

	}

	/**
	 * replace each \n by \r\n
	 * 
	 * @param string
	 * @return
	 */
	public static String forceWindowsCarriageReturn(String data) {
		// force the \r as Java skips it
		return data.replaceAll("\n", "\r\n");
	}

	/**
	 * Format amount
	 * 
	 * @param amount
	 * @return
	 */
	public static String commafyAmount(String amount) {

		if (amount != null) {
			String finAmount = amount.trim().replaceAll("[ ]{2,}", " ");
			String[] finAmountParts = finAmount.split(" ");
			String[] fraction = finAmountParts[1].split(",");

			return finAmountParts[0] + " "
					+ SwiftFormatUtils.getNumber(fraction[0]).toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,") + "."
					+ (fraction.length == 1 ? "" : fraction[1]);
		}
		return amount;

	}

}
