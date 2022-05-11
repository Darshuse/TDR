package com.eastnets.config;

import java.io.Serializable;

public enum AuthMethod implements Serializable {

	DB("DB"), LDAP("LDAP");

	private final String name;

	private AuthMethod(String s) {
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
