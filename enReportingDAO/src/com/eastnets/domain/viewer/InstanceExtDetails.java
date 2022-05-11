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

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

/**
 * InstanceExtDetails POJO
 * @author EastNets
 * @since September 25, 2012
 */
public class InstanceExtDetails extends InstanceDetails {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -816288586899982908L;
	private Integer instNo; 
	 private String instNotificationType;
	 private String instMpfnName;
	 private String instLastMpfnResult;
	 private String instReceiverInstitutionName;
	 private String instReceiverBranchInfo;
	 private String instReceiverCityName;
	 private String instReceiverCtryName;
	 private String instReceiverCtryCode; 
	 private String instRoutingCode;
	 private String instReceiverNetworkIAppNam;
	 private String instOperComment;
	 private String instDispAddressCode;
	 private String instReceiverCorrType; 
	 private String instReceiverX1;
	 private String instReceiverX2;
	 private String instReceiverX3;
	 private String instReceiverX4;
	 private String instMpfnStatus;
	 private String instSm2000Priority;
	 private List<IntvAppe> instIntvList;
	public String getInstNotificationType() {
		return instNotificationType;
	}
	public void   setInstNotificationTypeFormatted(String val){
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getInstNotificationTypeFormatted() {
		return StringUtils.capitalize(StringUtils.lowerCase( StringUtils.substring(	instNotificationType  ,18 ) ));
	}
	public void setInstNotificationType(String instNotificationType) {
		this.instNotificationType = instNotificationType;
	}
	public String getInstMpfnName() {
		return instMpfnName;
	}
	public void setInstMpfnName(String instMpfnName) {
		this.instMpfnName = instMpfnName;
	}
	public String getInstLastMpfnResult() {
		return instLastMpfnResult;
	}
	public void setInstLastMpfnResult(String instLastMpfnResult) {
		this.instLastMpfnResult = instLastMpfnResult;
	}
	public String getInstReceiverInstitutionName() {
		return instReceiverInstitutionName;
	}
	public void setInstReceiverInstitutionName(String instReceiverInstitutionName) {
		this.instReceiverInstitutionName = instReceiverInstitutionName;
	}
	public String getInstReceiverBranchInfo() {
		return instReceiverBranchInfo;
	}
	public void setInstReceiverBranchInfo(String instReceiverBranchInfo) {
		this.instReceiverBranchInfo = instReceiverBranchInfo;
	}
	public String getInstReceiverCityName() {
		return instReceiverCityName;
	}
	public void setInstReceiverCityName(String instReceiverCityName) {
		this.instReceiverCityName = instReceiverCityName;
	}
	public String getInstReceiverCtryName() {
		return instReceiverCtryName;
	}
	public void setInstReceiverCtryName(String instReceiverCtryName) {
		this.instReceiverCtryName = instReceiverCtryName;
	}
	public String getInstReceiverCtryCode() {
		return instReceiverCtryCode;
	}
	public void setInstReceiverCtryCode(String instReceiverCtryCode) {
		this.instReceiverCtryCode = instReceiverCtryCode;
	}
	public String getInstRoutingCode() {
		return instRoutingCode;
	}
	public void setInstRoutingCode(String instRoutingCode) {
		this.instRoutingCode = instRoutingCode;
	}
	public String getInstReceiverNetworkIAppNam() {
		return instReceiverNetworkIAppNam;
	}
	public void setInstReceiverNetworkIAppNam(String instReceiverNetworkIAppNam) {
		this.instReceiverNetworkIAppNam = instReceiverNetworkIAppNam;
	}
	public String getInstOperComment() {
		return instOperComment;
	}
	public void setInstOperComment(String instOperComment) {
		this.instOperComment = instOperComment;
	}
	public String getInstDispAddressCode() {
		return instDispAddressCode;
	}
	public void setInstDispAddressCode(String instDispAddressCode) {
		this.instDispAddressCode = instDispAddressCode;
	}
	public String getInstReceiverCorrType() {
		return instReceiverCorrType;
	}
	public void setInstReceiverCorrType(String instReceiverCorrType) {
		this.instReceiverCorrType = instReceiverCorrType;
	}
	public String getInstReceiverX1() {
		return instReceiverX1;
	}
	public void setInstReceiverX1(String instReceiverX1) {
		this.instReceiverX1 = instReceiverX1;
	}
	public String getInstReceiverX2() {
		return instReceiverX2;
	}
	public void setInstReceiverX2(String instReceiverX2) {
		this.instReceiverX2 = instReceiverX2;
	}
	public String getInstReceiverX3() {
		return instReceiverX3;
	}
	public void setInstReceiverX3(String instReceiverX3) {
		this.instReceiverX3 = instReceiverX3;
	}
	public String getInstReceiverX4() {
		return instReceiverX4;
	}
	public void setInstReceiverX4(String instReceiverX4) {
		this.instReceiverX4 = instReceiverX4;
	}
	public String getInstMpfnStatus() {
		return instMpfnStatus;
	}
	public void setInstMpfnStatus(String instMpfnStatus) {
		this.instMpfnStatus = instMpfnStatus;
	}
	public List<IntvAppe> getInstIntvList() {
		return instIntvList;
	}
	public void setInstIntvList(List<IntvAppe> instIntvList) {
		this.instIntvList = instIntvList;
	}
	public void setInstNo(Integer instNo) {
		this.instNo = instNo;
	}
	public Integer getInstNo() {
		return instNo;
	}
	public String getInstSm2000Priority() {
		return instSm2000Priority;
	}
	public void setInstSm2000Priority(String instSm2000Priority) {
		this.instSm2000Priority = instSm2000Priority;
	}

	 

}
