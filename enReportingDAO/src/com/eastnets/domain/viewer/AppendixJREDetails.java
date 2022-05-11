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
import java.sql.Clob;
import java.util.Date;

/**
 * AppendixDetails POJO
 * @author EastNets
 * @since September 25, 2012
 */
public class AppendixJREDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7504605339509071844L;
	private Integer appeSessionNbr;
	private Integer appeSequenceNbr;
	private String 	appeSessionHolder;
	private Date 	appeLocalOutputTime;
	private Date 	appeRemoteInputTime;
	private String 	appeRemoteInputReference;
	private Clob 	appeAckNackText;
	private String 	appeChecksumValue;
	private String 	appeChecksumResult;
	private Clob 	appeAuthValue;
	private String 	appeAuthResult;
	
	public void setAppeSessionNbr(Integer appeSessionNbr) {
		this.appeSessionNbr = appeSessionNbr;
	}
	public Integer getAppeSessionNbr() {
		return appeSessionNbr;
	}
	public void setAppeSequenceNbr(Integer appeSequenceNbr) {
		this.appeSequenceNbr = appeSequenceNbr;
	}
	public Integer getAppeSequenceNbr() {
		return appeSequenceNbr;
	}
	public void setAppeSessionHolder(String appeSessionHolder) {
		this.appeSessionHolder = appeSessionHolder;
	}
	public String getAppeSessionHolder() {
		return appeSessionHolder;
	}
	public void setAppeLocalOutputTime(Date appeLocalOutputTime) {
		this.appeLocalOutputTime = appeLocalOutputTime;
	}
	public Date getAppeLocalOutputTime() {
		return appeLocalOutputTime;
	}
	public void setAppeRemoteInputTime(Date appeRemoteInputTime) {
		this.appeRemoteInputTime = appeRemoteInputTime;
	}
	public Date getAppeRemoteInputTime() {
		return appeRemoteInputTime;
	}
	public void setAppeRemoteInputReference(String appeRemoteInputReference) {
		this.appeRemoteInputReference = appeRemoteInputReference;
	}
	public String getAppeRemoteInputReference() {
		return appeRemoteInputReference;
	}
	public void setAppeAckNackText(Clob appeAckNackText) {
		this.appeAckNackText = appeAckNackText;
	}
	public Clob getAppeAckNackText() {
		return appeAckNackText;
	}
	public void setAppeChecksumValue(String appeChecksumValue) {
		this.appeChecksumValue = appeChecksumValue;
	}
	public String getAppeChecksumValue() {
		return appeChecksumValue;
	}
	public void setAppeChecksumResult(String appeChecksumResult) {
		this.appeChecksumResult = appeChecksumResult;
	}
	public String getAppeChecksumResult() {
		return appeChecksumResult;
	}
	public void setAppeAuthValue(Clob appeAuthValue) {
		this.appeAuthValue = appeAuthValue;
	}
	public Clob getAppeAuthValue() {
		return appeAuthValue;
	}
	public void setAppeAuthResult(String appeAuthResult) {
		this.appeAuthResult = appeAuthResult;
	}
	public String getAppeAuthResult() {
		return appeAuthResult;
	}	
}
