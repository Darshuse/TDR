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

package com.eastnets.domain.admin;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.springframework.beans.BeanUtils;

/**
 * LoaderConnection POJO
 * @author EastNets
 * @since August 29, 2012
 */
public class LoaderConnection implements Serializable,Diffable<LoaderConnection>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4802427284307286200L;
	private Long aid; 					//alliance ID (ID Field)
	private Long port = null; 					//Saxs Port Number (Port Field)
	private Long timeOut = null; 				// Time out in seconds (Time_out Field)
	private String allianceInfo; 		//connection description (alliance_Info field) 
	private String serverAddress; 		//Saxs IP number (SRV_Address field)
	private String allianceInstance; 	//Instance Name (alliance_instance field)
	private Integer returenStatus; //the procedure return status
	private Long lastUmidl;
	private Long lastUmidh;
	private String filesLocalPath = "";
	private BigDecimal lastProcessedSequenceNo; // for tracing process when the messages extracted from the source in form of batches


	public String getFilesLocalPath() {
		return filesLocalPath;
	}

	public void setFilesLocalPath(String filesLocalPath) {
		if (filesLocalPath == null) {
			filesLocalPath = "";
		}

		filesLocalPath = filesLocalPath.trim();
		if (filesLocalPath.length() > 256) {
			filesLocalPath = filesLocalPath.substring(0, 255);
		}
		this.filesLocalPath = filesLocalPath;
	}

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
	}

	public Long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Long timeOut) {
		this.timeOut = timeOut;
	}

	public String getAllianceInfo() {
		return allianceInfo;
	}

	public void setAllianceInfo(String allianceInfo) {
		this.allianceInfo = allianceInfo;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getAllianceInstance() {
		return allianceInstance;
	}

	public void setAllianceInstance(String allianceInstance) {

		this.allianceInstance = (allianceInstance == null)? "N/S" : allianceInstance;
	}

	public Integer getReturenStatus() {
		return returenStatus;
	}

	public void setReturenStatus(Integer returenStatus) {
		this.returenStatus = returenStatus;
	}

	public void setLastUmidl(Long lastUmidl) {
		this.lastUmidl = lastUmidl;
	}

	public Long getLastUmidl() {
		return lastUmidl;
	}

	public void setLastUmidh(Long lastUmidh) {
		this.lastUmidh = lastUmidh;
	}

	public Long getLastUmidh() {
		return lastUmidh;
	}

	public BigDecimal getLastProcessedSequenceNo() {
		return lastProcessedSequenceNo;
	}

	public void setLastProcessedSequenceNo(BigDecimal lastProcessedSequenceNo) {
		this.lastProcessedSequenceNo = lastProcessedSequenceNo;
	}



	public LoaderConnection shallowClone() {
		LoaderConnection loaderConnectionOld = new LoaderConnection();
		BeanUtils.copyProperties(this, loaderConnectionOld);
		return loaderConnectionOld;
	}


	/*
	 * 
	 * 	private static final long serialVersionUID = -4802427284307286200L;
	private Long aid; 					//alliance ID (ID Field)
	private Long port = null; 					//Saxs Port Number (Port Field)
	private Long timeOut = null; 				// Time out in seconds (Time_out Field)
	private String allianceInfo; 		//connection description (alliance_Info field) 
	private String serverAddress; 		//Saxs IP number (SRV_Address field)
	private String allianceInstance; 	//Instance Name (alliance_instance field)
	private Integer returenStatus; //the procedure return status
	private Long lastUmidl;
	private Long lastUmidh;
	private String filesLocalPath = "";
	private BigDecimal lastProcessedSequenceNo; // for tracing process when the messages extracted from the source in form of batches

	 * 
	 * */
	@Override
	public DiffResult diff(LoaderConnection oldLoaderConnection) { 
		DiffBuilder compare = new DiffBuilder(this, oldLoaderConnection, null,false);
		compare.append("Description",this.getAllianceInfo(),oldLoaderConnection.getAllianceInfo());
		compare.append("Server Name",this.getServerAddress(),oldLoaderConnection.getServerAddress());
		compare.append("Socket Port",this.getPort(),oldLoaderConnection.getPort());
		compare.append("Timeout",this.getTimeOut(),oldLoaderConnection.getTimeOut()); 
		compare.append("Files Local Path",this.getFilesLocalPath(),oldLoaderConnection.getFilesLocalPath());

		return compare.build();

	}


}
