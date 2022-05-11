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
 * MessageArchiveSettings POJO
 * @author EastNets
 * @since September 19, 2012
 */
public class MessageArchiveSettings extends ArchiveSettings{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3393390917863327962L;
	private String archivePath;
	private String swiftArchivePath;
	
	
	
	public String getArchivePath() {
		if(archivePath == null){
			return "";
		}
		
		return archivePath;
	}
	public void setArchivePath(String archivePath) {
		if(archivePath == null){
			archivePath =  "";
		}
		this.archivePath = archivePath;
	}
	public String getSwiftArchivePath() {
		if(swiftArchivePath == null){
			return "";
		}
		return swiftArchivePath;
	}
	public void setSwiftArchivePath(String swiftArchivePath) {
		if(swiftArchivePath == null){
			swiftArchivePath = "";
		}
		this.swiftArchivePath = swiftArchivePath;
	}

}
