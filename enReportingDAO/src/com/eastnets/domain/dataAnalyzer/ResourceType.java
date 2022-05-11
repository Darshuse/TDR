package com.eastnets.domain.dataAnalyzer;

public enum ResourceType {

	ADHOC("adhocDataView"),
	REPORT("reportUnit"),
	DASHBOARD("dashboard");
	
	private final String resourceType;
	
	private ResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceType() {
		return resourceType;
	}
 
 
	
}
