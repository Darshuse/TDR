/**
 * 
 */
package com.eastnets.reporting.licensing.beans;

import java.io.Serializable;


/**
 * 
 * @author EastNets
 * @since dNov 28, 2012
 * 
 */
public class LicenseValidationInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7866551620886431979L;
	private String data;
	private Integer offset;
	private String key;
	private Integer status;

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the offset
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
}
