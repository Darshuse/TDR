package com.eastnets.domain.dataAnalyzer;

public enum LoginAuth {
	GET_ENCRYPTION_KEY("/GetEncryptionKey"),
    LOGIN("/login"),
    SPRING_SECURITY_CHECK("/j_spring_security_check"),
    LOGOUT("/logout.html"),
    SERVER_INFO("/serverInfo");
	
	private final String authUrl;

	private LoginAuth(String authUrl) {
		this.authUrl = authUrl;
	}

	public String getAuthUrl() {
		return authUrl;
	}
	
	

}
