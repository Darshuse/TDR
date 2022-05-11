package com.eastnets.domain.dataAnalyzer;

public enum AdministrationUrl  {	
	USERS("/user"),
	ATTRIBUTES("/attributes"),
	ROLES("/roles"), 
	ORGNIZATION("/organizations");
	
	private final String adminUrl;
	
	private AdministrationUrl(String adminUrl) {
		this.adminUrl = adminUrl;
	}
	public String getAdminUrl() {
		return adminUrl;
	}
}
