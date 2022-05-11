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

package com.eastnets.domain.swing;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

/**
 * UserFin POJO
 * @author EastNets
 * @since July 11, 2012
 */
public class UserFin implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2884180800862157509L;
	private Date internalTimeStamp;
	private String finSwiftIO;
	private String finCorr;
	private String finOwnDist;
	private String finMsgType;
	private String finTrnRef;
	private String finAmount;
	private String finCurr;
	private Date finValueDate;
	private String finMesgStatus;
	private Date finMesgCreaDateTime;
	private String finMesgNetwStatus;
	private String finIsnOsn;
	private Clob data;

	public Date getInternalTimeStamp() {
		return internalTimeStamp;
	}

	public void setInternalTimeStamp(Date internalTimeStamp) {
		this.internalTimeStamp = internalTimeStamp;
	}

	public String getFinSwiftIO() {
		return finSwiftIO;
	}

	public void setFinSwiftIO(String finSwiftIO) {
		this.finSwiftIO = finSwiftIO;
	}

	public String getFinCorr() {
		return finCorr;
	}

	public void setFinCorr(String finCorr) {
		this.finCorr = finCorr;
	}

	public String getFinOwnDist() {
		return finOwnDist;
	}

	public void setFinOwnDist(String finOwnDist) {
		this.finOwnDist = finOwnDist;
	}

	public String getFinMsgType() {
		return finMsgType;
	}

	public void setFinMsgType(String finMsgType) {
		this.finMsgType = finMsgType;
	}

	public String getFinTrnRef() {
		return finTrnRef;
	}

	public void setFinTrnRef(String finTrnRef) {
		this.finTrnRef = finTrnRef;
	}

	public String getFinAmount() {
		return finAmount;
	}

	public void setFinAmount(String finAmount) {
		this.finAmount = finAmount;
	}

	public String getFinCurr() {
		return finCurr;
	}

	public void setFinCurr(String finCurr) {
		this.finCurr = finCurr;
	}

	public Date getFinValueDate() {
		return finValueDate;
	}

	public void setFinValueDate(Date finValueDate) {
		this.finValueDate = finValueDate;
	}

	public String getFinMesgStatus() {
		return finMesgStatus;
	}

	public void setFinMesgStatus(String finMesgStatus) {
		this.finMesgStatus = finMesgStatus;
	}



	public Date getFinMesgCreaDateTime() {
		return finMesgCreaDateTime;
	}

	public void setFinMesgCreaDateTime(Date finMesgCreaDateTime) {
		this.finMesgCreaDateTime = finMesgCreaDateTime;
	}

	public String getFinMesgNetwStatus() {
		return finMesgNetwStatus;
	}

	public void setFinMesgNetwStatus(String finMesgNetwStatus) {
		this.finMesgNetwStatus = finMesgNetwStatus;
	}

	public String getFinIsnOsn() {
		return finIsnOsn;
	}

	public void setFinIsnOsn(String finIsnOsn) {
		this.finIsnOsn = finIsnOsn;
	}

	public Clob getData() {
		return data;
	}

	public void setData(Clob data) {
		this.data = data;
	}

}
