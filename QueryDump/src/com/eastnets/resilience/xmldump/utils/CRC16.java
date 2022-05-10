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

/**
 * CRC16 implementation
 * 
 * @author EHakawati
 * 
 */
public class CRC16 {

	/**
	 * used in CRC16 calculation
	 */
	private static final int[] ODD_PARITY = { 0, 1, 1, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0 };

	/**
	 * Private constructor to prevent instantiation.
	 */
	private CRC16() {
	}

	/**
	 * Perform the CRC16 on the data element based on a zero seed.
	 * <p>
	 * CRC16 is based on the polynomial = X^16 + X^15 + X^2 + 1.
	 * 
	 * @param dataToCrc
	 *            data element on which to perform the CRC16
	 * @return CRC16 value
	 */
	public static int compute(int dataToCrc) {
		return compute(dataToCrc, 0);
	}

	/**
	 * Perform the CRC16 on the data element based on the provided seed.
	 * <p>
	 * CRC16 is based on the polynomial = X^16 + X^15 + X^2 + 1.
	 * 
	 * @param dataToCrc
	 *            data element on which to perform the CRC16
	 * @return CRC16 value
	 */
	public static int compute(int dataToCrc, int seed) {
		int dat = ((dataToCrc ^ (seed & 0xFF)) & 0xFF);

		seed = (seed & 0xFFFF) >>> 8;

		int indx1 = (dat & 0x0F);
		int indx2 = (dat >>> 4);

		if ((ODD_PARITY[indx1] ^ ODD_PARITY[indx2]) == 1)
			seed = seed ^ 0xC001;

		dat = (dat << 6);
		seed = seed ^ dat;
		dat = (dat << 1);
		seed = seed ^ dat;
		return seed;
	}

	/**
	 * Perform the CRC16 on an array of data elements based on a zero seed.
	 * <p>
	 * CRC16 is based on the polynomial = X^16 + X^15 + X^2 + 1.
	 * 
	 * @param dataToCrc
	 *            array of data elements on which to perform the CRC16
	 * 
	 * @return CRC16 value
	 */
	public static int compute(byte dataToCrc[]) {
		return compute(dataToCrc, 0, dataToCrc.length, 0);
	}

	/**
	 * Perform the CRC16 on an array of data elements based on a zero seed.
	 * <p>
	 * CRC16 is based on the polynomial = X^16 + X^15 + X^2 + 1.
	 * 
	 * @param dataToCrc
	 *            array of data elements on which to perform the CRC16
	 * @param off
	 *            offset into the data array
	 * @param len
	 *            length of data to CRC16
	 * 
	 * @return CRC16 value
	 */
	public static int compute(byte dataToCrc[], int off, int len) {
		return compute(dataToCrc, off, len, 0);
	}

	/**
	 * Perform the CRC16 on an array of data elements based on the provided
	 * seed.
	 * <p>
	 * CRC16 is based on the polynomial = X^16 + X^15 + X^2 + 1.
	 * 
	 * @param dataToCrc
	 *            array of data elements on which to perform the CRC16
	 * @param off
	 *            offset into the data array
	 * @param len
	 *            length of data to CRC16
	 * @param seed
	 *            seed to use for CRC16
	 * 
	 * @return CRC16 value
	 */
	public static int compute(byte dataToCrc[], int off, int len, int seed) {

		// loop to do the CRC on each data element
		for (int i = 0; i < len; i++) {
			seed = compute(dataToCrc[i + off], seed);
		}

		return seed;
	}

	/**
	 * Perform the CRC16 on an array of data elements based on the provided
	 * seed.
	 * <p>
	 * CRC16 is based on the polynomial = X^16 + X^15 + X^2 + 1.
	 * 
	 * @param dataToCrc
	 *            array of data elements on which to perform the CRC16
	 * @param seed
	 *            seed to use for CRC16
	 * @return CRC16 value
	 */
	public static int compute(byte dataToCrc[], int seed) {
		return compute(dataToCrc, 0, dataToCrc.length, seed);
	}
}