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
import java.util.List;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.springframework.beans.BeanUtils;

/**
 * LoaderSettings POJO
 * @author EastNets
 * @since July 31, 2012
 */
public class LoaderSettings implements Serializable,Diffable<LoaderSettings> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4167631127007480115L;
	// General
	private Integer activeTime;
	private Integer sleepTime;
	private Integer liveMessageUpdate;
	private String liveSource;
	private Integer updateDelayMin;
	private Integer updateDelayMax;
	private Integer dailyScanTime;

	// Schedule
	private Integer disableUpdate; // 1 > true
	private Integer disableStartTime;
	private Integer disableEndTime;
	private Integer disableDuration;
	private Integer activeDaysMon;
	private Integer activeDaysTue;
	private Integer activeDaysWed;
	private Integer activeDaysThu;
	private Integer activeDaysFri;
	private Integer activeDaysSat;
	private Integer activeDaysSun;

	// Text Decomposition
	private Integer enableOfflineDecomposition;
	private Integer parseTextBlock;
	private List<String> availableMessageTypes;
	private List<String> selectedMessageTypes;

	// Watchdog
	private Integer enableOfflineWatchdog;

	// Advanced
	private Integer skipRoutingInterventions;
	private Integer loadEvents;
	private Integer storeOriginalInstanceOnly;

	//Payload Transfer
	private Integer payloadTryCountMax;
	private Integer payloadTryDelayMin;
	private Integer payloadGetChunkCount;


	public Integer getPayloadTryCountMax() {
		return payloadTryCountMax;
	}
	public void setPayloadTryCountMax(Integer payloadTryCountMax) {

		if(payloadTryCountMax != null)
			this.payloadTryCountMax = payloadTryCountMax;
		else 
			this.payloadTryCountMax = 0;
	}


	public Integer getPayloadTryDelayMin() {
		return payloadTryDelayMin;
	}
	public void setPayloadTryDelayMin(Integer payloadTryDelayMin) {

		if(payloadTryDelayMin != null)
			this.payloadTryDelayMin = payloadTryDelayMin;
		else 
			this.payloadTryDelayMin = 0;
	}


	public Integer getPayloadGetChunkCount() {
		return payloadGetChunkCount;
	}
	public void setPayloadGetChunkCount(Integer payloadGetChunkCount) {

		if(payloadGetChunkCount != null)
			this.payloadGetChunkCount = payloadGetChunkCount;
		else 
			this.payloadGetChunkCount = 0;
	}

	public Integer getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Integer activeTime) {
		this.activeTime = activeTime;
	}

	public Integer getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(Integer sleepTime) {
		this.sleepTime = sleepTime;
	}

	public Integer getLiveMessageUpdate() {
		return liveMessageUpdate;
	}

	public void setLiveMessageUpdate(Integer liveMessageUpdate) {
		this.liveMessageUpdate = liveMessageUpdate;
	}

	public String getLiveSource() {
		return liveSource;
	}

	public void setLiveSource(String liveSource) {
		this.liveSource = liveSource;
	}

	public Integer getUpdateDelayMin() {
		return updateDelayMin;
	}

	public void setUpdateDelayMin(Integer updateDelayMin) {
		this.updateDelayMin = updateDelayMin;
	}

	public Integer getUpdateDelayMax() {
		return updateDelayMax;
	}

	public void setUpdateDelayMax(Integer updateDelayMax) {
		this.updateDelayMax = updateDelayMax;
	}

	public Integer getDailyScanTime() {
		return dailyScanTime;
	}

	public void setDailyScanTime(Integer dailyScanTime) {
		this.dailyScanTime = dailyScanTime;
	}

	public Integer getDisableUpdate() {
		return disableUpdate;
	}

	public void setDisableUpdate(Integer disableUpdate) {
		this.disableUpdate = disableUpdate;
	}

	public Integer getDisableStartTime() {
		return disableStartTime;
	}

	public void setDisableStartTime(Integer disableStartTime) {
		this.disableStartTime = disableStartTime;
	}

	public Integer getDisableEndTime() {
		return disableEndTime;
	}

	public void setDisableEndTime(Integer disableEndTime) {
		this.disableEndTime = disableEndTime;
	}

	public Integer getDisableDuration() {
		return disableDuration;
	}

	public void setDisableDuration(Integer disableDuration) {
		this.disableDuration = disableDuration;
	}

	public Integer getActiveDaysMon() {
		return activeDaysMon;
	}

	public void setActiveDaysMon(Integer activeDaysMon) {
		this.activeDaysMon = activeDaysMon;
	}

	public Integer getActiveDaysTue() {
		return activeDaysTue;
	}

	public void setActiveDaysTue(Integer activeDaysTue) {
		this.activeDaysTue = activeDaysTue;
	}

	public Integer getActiveDaysWed() {
		return activeDaysWed;
	}

	public void setActiveDaysWed(Integer activeDaysWed) {
		this.activeDaysWed = activeDaysWed;
	}

	public Integer getActiveDaysThu() {
		return activeDaysThu;
	}

	public void setActiveDaysThu(Integer activeDaysThu) {
		this.activeDaysThu = activeDaysThu;
	}

	public Integer getActiveDaysFri() {
		return activeDaysFri;
	}

	public void setActiveDaysFri(Integer activeDaysFri) {
		this.activeDaysFri = activeDaysFri;
	}

	public Integer getActiveDaysSat() {
		return activeDaysSat;
	}

	public void setActiveDaysSat(Integer activeDaysSat) {
		this.activeDaysSat = activeDaysSat;
	}

	public Integer getActiveDaysSun() {
		return activeDaysSun;
	}

	public void setActiveDaysSun(Integer activeDaysSun) {
		this.activeDaysSun = activeDaysSun;
	}

	public Integer getEnableOfflineDecomposition() {
		return enableOfflineDecomposition;
	}

	public void setEnableOfflineDecomposition(Integer enableOfflineDecomposition) {
		this.enableOfflineDecomposition = enableOfflineDecomposition;
	}

	public List<String> getAvailableMessageTypes() {
		return availableMessageTypes;
	}

	public void setAvailableMessageTypes(List<String> availableMessageTypes) {
		this.availableMessageTypes = availableMessageTypes;
	}

	public List<String> getSelectedMessageTypes() {
		return selectedMessageTypes;
	}

	public void setSelectedMessageTypes(List<String> selectedMessageTypes) {
		this.selectedMessageTypes = selectedMessageTypes;
	}

	public Integer getEnableOfflineWatchdog() {
		return enableOfflineWatchdog;
	}

	public void setEnableOfflineWatchdog(Integer enableOfflineWatchdog) {
		this.enableOfflineWatchdog = enableOfflineWatchdog;
	}

	public Integer getSkipRoutingInterventions() {
		return skipRoutingInterventions;
	}

	public void setSkipRoutingInterventions(Integer skipRoutingInterventions) {
		this.skipRoutingInterventions = skipRoutingInterventions;
	}

	public Integer getStoreOriginalInstanceOnly() {
		return storeOriginalInstanceOnly;
	}

	public void setStoreOriginalInstanceOnly(Integer storeOriginalInstanceOnly) {
		this.storeOriginalInstanceOnly = storeOriginalInstanceOnly;
	}

	public Integer getLoadEvents() {
		return loadEvents;
	}

	public void setLoadEvents(Integer loadEvents) {
		this.loadEvents = loadEvents;
	}

	public Integer getParseTextBlock() {
		return parseTextBlock;
	}

	public void setParseTextBlock(Integer parseTextBlock) {
		this.parseTextBlock = parseTextBlock;
	}



	// clone method
	public LoaderSettings shallowClone() {
		LoaderSettings loaderSettingsOld = new LoaderSettings();
		BeanUtils.copyProperties(this, loaderSettingsOld);
		return loaderSettingsOld;
	}




	@Override
	public DiffResult diff(LoaderSettings oldLoaderSettings) { 
		DiffBuilder compare = new DiffBuilder(this, oldLoaderSettings, null,false);
		compare.append("Enable Offline Decomposition",this.getEnableOfflineDecomposition(),oldLoaderSettings.getEnableOfflineDecomposition());

		compare.append("Enable Offline Watchdog",this.getEnableOfflineWatchdog(),oldLoaderSettings.getEnableOfflineWatchdog());

		compare.append("Skip Routing Interventions",this.getSkipRoutingInterventions(),oldLoaderSettings.getSkipRoutingInterventions());

		compare.append("Skip Events",this.getLoadEvents(),oldLoaderSettings.getLoadEvents());

		compare.append("Skip Store Original Instance Only",this.getStoreOriginalInstanceOnly(),oldLoaderSettings.getStoreOriginalInstanceOnly()); 
		compare.append("Active Time",this.getActiveTime(),oldLoaderSettings.getActiveTime());

		compare.append("Sleep Time",this.getSleepTime(),oldLoaderSettings.getSleepTime());

		compare.append("Live Message Update",this.getLiveMessageUpdate(),oldLoaderSettings.getLiveMessageUpdate());

		compare.append("Live Status",this.getLiveSource(),oldLoaderSettings.getLiveSource());

		compare.append("Update Delay Min",this.getUpdateDelayMin(),oldLoaderSettings.getUpdateDelayMin());

		compare.append("Update Delay Max",this.getUpdateDelayMax(),oldLoaderSettings.getUpdateDelayMax());

		compare.append("Daily Scan Time",this.getDailyScanTime(),oldLoaderSettings.getDailyScanTime());

		compare.append("Payload Try Count Max",this.getPayloadTryCountMax(),oldLoaderSettings.getPayloadTryCountMax());

		compare.append("Payload Try Delay Min",this.getPayloadTryDelayMin(),oldLoaderSettings.getPayloadTryDelayMin());

		compare.append("Payload Get Chunk Count",this.getPayloadGetChunkCount(),oldLoaderSettings.getPayloadGetChunkCount()); 
		compare.append("Disable database update",this.getDisableUpdate(),oldLoaderSettings.getDisableUpdate());

		compare.append("Active Days Mon",this.getActiveDaysMon(),oldLoaderSettings.getActiveDaysMon());
		compare.append("Active Days Tue",this.getActiveDaysTue(),oldLoaderSettings.getActiveDaysTue());
		compare.append("Active Days Wed",this.getActiveDaysWed(),oldLoaderSettings.getActiveDaysWed());
		compare.append("Active Days Thu",this.getActiveDaysThu(),oldLoaderSettings.getActiveDaysThu());
		compare.append("Active Days Fri",this.getActiveDaysFri(),oldLoaderSettings.getActiveDaysFri());
		compare.append("Active Days Sat",this.getActiveDaysSat(),oldLoaderSettings.getActiveDaysSat());
		compare.append("Active Days Sun",this.getActiveDaysSun(),oldLoaderSettings.getActiveDaysSun());
		compare.append("Text Block Decomposition :Available Message Types ",this.getSelectedMessageTypes(),oldLoaderSettings.getSelectedMessageTypes());
		compare.append("Disable Start Time",this.getDisableStartTime(),oldLoaderSettings.getDisableStartTime());
		compare.append("Disable End Time",this.getDisableEndTime(),oldLoaderSettings.getDisableEndTime());

		return compare.build();

	}




}
