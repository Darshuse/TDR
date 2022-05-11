package com.eastnets.config;

import java.io.Serializable;

public enum DBType implements Serializable {

	ORACLE("Oracle"), MSSQL("Sql");

	private final String name;

	private DBType(String s) {
		name = s;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : name.equals(otherName);
	}

	@Override
	public String toString() {
		return name;
	}

}
