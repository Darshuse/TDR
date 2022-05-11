package com.eastnets.domain.dataAnalyzer;

public enum Repository {
	RESOURCES("/resources"),
    PERMISSIONS("/permissions"),
    EXPORT("/export"),
    IMPORT("/import");
	
	private final String repoUrl;

	private Repository(String repoUrl) {
		this.repoUrl = repoUrl;
	}

	public String getRepoUrl() {
		return repoUrl;
	}

	 
	
	
}
