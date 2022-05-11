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

import java.util.List;

/**
 * JrnlArchiveOptions POJO
 * @author EastNets
 * @since September 19, 2012
 */
public class JrnlArchiveOptions extends ArchiveOptions{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6171384656430235672L;

	private Long transactionSize;
		
	private List<JrnlArchiveSettings> jrnlArchiveSettings;

	@Override
	public Long getTransactionSize() {
		return transactionSize;
	}

	@Override
	public void setTransactionSize(Long transactionSize) {
		this.transactionSize = transactionSize;
	}

	public List<JrnlArchiveSettings> getJrnlArchiveSettings() {
		return jrnlArchiveSettings;
	}

	public void setJrnlArchiveSettings(List<JrnlArchiveSettings> jrnlArchiveSettings) {
		this.jrnlArchiveSettings = jrnlArchiveSettings;
	}
	
	
	
}
