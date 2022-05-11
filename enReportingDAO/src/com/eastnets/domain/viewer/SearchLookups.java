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

package com.eastnets.domain.viewer;

import java.io.Serializable;
import java.util.List;

import com.eastnets.domain.Alliance;

/**
 * SearchLookups POJO
 * 
 * @author EastNets
 * @since September 20, 2012
 */
public class SearchLookups implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9136278977743593773L;

	private List<String> sourceSearchFile;
	private List<Alliance> sourceAvailableSAA;
	private List<Currency> sourceAvailablecurancy;
	private List<Country> sourceAvailableCountry;
	private List<String> sourceAvailableMsgTy;
	private List<String> umidFormat;
	private List<String> contentNature;
	private List<String> queuesAvilable;
	private List<String> unitsAvailable;
	private List<Identifier> messageNames;
	private List<String> detailOfChangeList;
	private List<String> qualifierList;
	private List<StatusCode> statusCodeList;
	private List<StatusReason> reasonCodes;
	private List<StatusReason> gSRPReasonCodes;
	private List<StatusCode> statusList;
	private List<String> sattlmentMethodList;
	private List<String> clearingSystemList;
	private List<String> serviceTypeList;

	public void setSourceSearchFile(List<String> sourceSearchFile) {
		this.sourceSearchFile = sourceSearchFile;
	}

	public List<String> getSourceSearchFile() {
		return sourceSearchFile;
	}

	public void setSourceAvailableSAA(List<Alliance> sourceAvailableSAA) {
		this.sourceAvailableSAA = sourceAvailableSAA;
	}

	public List<Alliance> getSourceAvailableSAA() {
		return sourceAvailableSAA;
	}

	public void setUmidFormat(List<String> umidFormat) {
		this.umidFormat = umidFormat;
	}

	public List<String> getUmidFormat() {
		return umidFormat;
	}

	public void setContentNature(List<String> contentNature) {
		this.contentNature = contentNature;
	}

	public List<String> getContentNature() {
		return contentNature;
	}

	public void setQueuesAvilable(List<String> queuesAvilable) {
		this.queuesAvilable = queuesAvilable;
	}

	public List<String> getQueuesAvilable() {
		return queuesAvilable;
	}

	public void setUnitsAvailable(List<String> unitsAvailable) {
		this.unitsAvailable = unitsAvailable;
	}

	public List<String> getUnitsAvailable() {
		return unitsAvailable;
	}

	public List<Identifier> getMessageNames() {
		return messageNames;
	}

	public void setMessageNames(List<Identifier> messageNames) {
		this.messageNames = messageNames;
	}

	public List<Currency> getSourceAvailablecurancy() {
		return sourceAvailablecurancy;
	}

	public void setSourceAvailablecurancy(List<Currency> sourceAvailablecurancy) {
		this.sourceAvailablecurancy = sourceAvailablecurancy;
	}

	public List<Country> getSourceAvailableCountry() {
		return sourceAvailableCountry;
	}

	public void setSourceAvailableCountry(List<Country> sourceAvailableCountry) {
		this.sourceAvailableCountry = sourceAvailableCountry;
	}

	public List<String> getSourceAvailableMsgTy() {
		return sourceAvailableMsgTy;
	}

	public void setSourceAvailableMsgTy(List<String> sourceAvailableMsgTy) {
		this.sourceAvailableMsgTy = sourceAvailableMsgTy;
	}

	public List<String> getDetailOfChangeList() {
		return detailOfChangeList;
	}

	public void setDetailOfChangeList(List<String> detailOfChangeList) {
		this.detailOfChangeList = detailOfChangeList;
	}

	public List<String> getQualifierList() {
		return qualifierList;
	}

	public void setQualifierList(List<String> qualifierList) {
		this.qualifierList = qualifierList;
	}

	public List<StatusCode> getStatusCodeList() {
		return statusCodeList;
	}

	public void setStatusCodeList(List<StatusCode> statusCodeList) {
		this.statusCodeList = statusCodeList;
	}

	public List<StatusReason> getReasonCodes() {
		return reasonCodes;
	}

	public void setReasonCodes(List<StatusReason> reasonCodes) {
		this.reasonCodes = reasonCodes;
	}

	public List<StatusCode> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<StatusCode> statusList) {
		this.statusList = statusList;
	}

	public List<StatusReason> getgSRPReasonCodes() {
		return gSRPReasonCodes;
	}

	public void setgSRPReasonCodes(List<StatusReason> gSRPReasonCodes) {
		this.gSRPReasonCodes = gSRPReasonCodes;
	}

	public List<String> getSattlmentMethodList() {
		return sattlmentMethodList;
	}

	public void setSattlmentMethodList(List<String> sattlmentMethodList) {
		this.sattlmentMethodList = sattlmentMethodList;
	}

	public List<String> getClearingSystemList() {
		return clearingSystemList;
	}

	public void setClearingSystemList(List<String> clearingSystemList) {
		this.clearingSystemList = clearingSystemList;
	}

	public List<String> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<String> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

}
