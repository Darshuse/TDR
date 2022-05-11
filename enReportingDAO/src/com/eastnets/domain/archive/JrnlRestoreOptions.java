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

package com.eastnets.domain.archive;

/**
 * JrnlRestoreOptions POJO
 * @author EastNets
 * @since September 20, 2012
 */
public class JrnlRestoreOptions extends RestoreOptions{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3251474999216514229L;
	private Long transactionSize;

	/**
	 * @param transactionSize the transactionSize to set
	 */
	public void setTransactionSize(Long transactionSize) {
		this.transactionSize = transactionSize;
	}

	/**
	 * @return the transactionSize
	 */
	public Long getTransactionSize() {
		return transactionSize;
	}
	
}
