package com.eastnets.domain.dataAnalyzer;

public enum ReportsExecutionsURL {
	REPORTS("/resources"),
	REPORT_EXECUTIONS("/permissions"),
	INPUT_CONTROLS("/inputControls"),
	OPTIONS("/options"),
	JOBS("/jobs");
	
	//http://<host>:<port>/jasperserver-pro/rest_v2/queryExecutor *
	//http://<host>:<port>/jasperserver-pro/rest_v2/caches/vds *
	
		private final String repoUrl;

	private ReportsExecutionsURL(String repoUrl) {
		this.repoUrl = repoUrl;
	}

	public String getRepoUrl() {
		return repoUrl;
	}



}
