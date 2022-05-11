package com.eastnets.domain.admin;

public class UserThemePreferences {

	private Long USERID;
	private String menuMode;
	private String inputStyle;
	private String componentTheme;

	public Long getUSERID() {
		return USERID;
	}

	public void setUSERID(Long uSERID) {
		USERID = uSERID;
	}

	public String getMenuMode() {
		return menuMode;
	}

	public void setMenuMode(String menuMode) {
		this.menuMode = menuMode;
	}

	public String getInputStyle() {
		return inputStyle;
	}

	public void setInputStyle(String inputStyle) {
		this.inputStyle = inputStyle;
	}

	public String getComponentTheme() {
		return componentTheme;
	}

	public void setComponentTheme(String componentTheme) {
		this.componentTheme = componentTheme;
	}

}
