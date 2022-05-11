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
 * JrnlArchiveSettings POJO
 * @author EastNets
 * @since September 19, 2012
 */
public class JrnlArchiveSettings extends ArchiveSettings{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8314234398367927062L;
	private String jrnlArchivePath;
	private String swiftJrnlArchivePath;
	
	public String getJrnlArchivePath() {
		if(jrnlArchivePath == null){
			return "";
		}
		return jrnlArchivePath;
	}
	public void setJrnlArchivePath(String jrnlArchivePath) {
		if(jrnlArchivePath == null){
			jrnlArchivePath = null;
		}
		this.jrnlArchivePath = jrnlArchivePath;
	}
	public String getSwiftJrnlArchivePath() {
		if(swiftJrnlArchivePath == null){
			return "";
		}
		return swiftJrnlArchivePath;
	}
	public void setSwiftJrnlArchivePath(String swiftJrnlArchivePath) {
		if(swiftJrnlArchivePath == null){
			swiftJrnlArchivePath = "";
		}
		this.swiftJrnlArchivePath = swiftJrnlArchivePath;
	}
}
