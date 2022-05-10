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
package com.eastnets.resilience.xsd.messaging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ConnectionResponseCode")
@XmlEnum
public enum ConnectionResponseCode {
	NONE("CONN_RSP_NONE"), QUEUE_READY("CONN_RSP_QUEUE_READY"), RECEIVED("CONN_RSP_RECEIVED"), ERR_FORMAT(
			"CONN_RSP_ERR_FORMAT"), ERR_SEQUENCE("CONN_RSP_ERR_SEQUENCE"), ERR_BLOCK("CONN_RSP_ERR_BLOCK"), ERR_BLOCK_ONE(
			"CONN_RSP_ERR_BLOCK_ONE"), ERR_BLOCK_TWO("CONN_RSP_ERR_BLOCK_TWO"), ERR_BLOCK_THREE(
			"CONN_RSP_ERR_BLOCK_THREE"), ERR_BLOCK_FOUR("CONN_RSP_ERR_BLOCK_FOUR"), ERR_BLOCK_FIVE(
			"CONN_RSP_ERR_BLOCK_FIVE"), ERR_CHECKSUM("CONN_RSP_ERR_CHECKSUM"), ERR_LENGTH("CONN_RSP_ERR_LENGTH"), ERR_LT(
			"CONN_RSP_ERR_LT"), ERR_LAU("CONN_RSP_ERR_LAU"), ERR_CRC("CONN_RSP_ERR_CRC"), ERR_PROFILE(
			"CONN_RSP_ERR_PROFILE"), ERR_SEND("CONN_RSP_ERR_SEND"), ERR_PROTOCOL("CONN_RSP_ERR_PROTOCOL"), ERR_TIMEOUT(
			"CONN_RSP_ERR_TIMEOUT"), NO_TRANSMISSION("CONN_RSP_NO_TRANSMISSION"), ERR_DECODING("CONN_RSP_ERR_DECODING"), ERR_TAG_MISSING(
			"CONN_RSP_ERR_TAG_MISSING"), ERR_TAG_VALUE("CONN_RSP_ERR_TAG_VALUE"), ERR_DISPOSITION(
			"CONN_RSP_ERR_DISPOSITION"), ERR_INTERNAL("CONN_RSP_ERR_INTERNAL"), ERR_BROADCAST("CONN_RSP_ERR_BROADCAST"), ERR_CAS_VERSION(
			"CONN_RSP_ERR_CAS_VERSION"), ERR_RECOVERY_MSG("CONN_RSP_ERR_RECOVERY_MSG"), ERR_FORMAT_MSG(
			"CONN_RSP_ERR_FORMAT_MSG"), ERR_ACCESS_KEY("CONN_RSP_ERR_ACCESS_KEY"), ERR_NO_ACCESS_KEY(
			"CONN_RSP_ERR_NO_ACCESS_KEY"), ERR_ACCESS_AUTH("CONN_RSP_ERR_ACCESS_AUTH"), MAX("CONN_RSP_MAX"), ERR_SENDER_NOT_LICENSED(
			"CONN_RSP_ERR_SENDER_NOT_LICENSED"), ERR_MQ_SEND("CONN_RSP_ERR_MQ_SEND");

	private final String value;

	private ConnectionResponseCode(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static ConnectionResponseCode fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
