package com.eastnets.domain.dataAnalyzer;


public enum HttpApiUrls {
 
	FLOW_HTML("/flow.html"),
	DOMAIN_DESINGER("/domaindesigner.html"),
	DASHBOARD_VIEW("/dashboard/viewer.html"),
	DASHBOARD_EDIT("/dashboard/designer.html"),
	SCHEDULER("/scheduler/main.html");
	
	private final String HttpApiUrl;
	
	private HttpApiUrls(String HttpApiUrl) {
		this.HttpApiUrl = HttpApiUrl;
	}

	public String getHttpApiUrl() {
		return HttpApiUrl;
	}
	 
}
