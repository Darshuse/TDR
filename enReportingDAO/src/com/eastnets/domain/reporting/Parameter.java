package com.eastnets.domain.reporting;

import java.io.Serializable;

import com.eastnets.domain.BaseEntity;

public abstract class Parameter extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7570501717024336445L;
	private String name;
	private Object value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}	
}
