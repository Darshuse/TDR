package com.eastnets.domain.reporting;

public class StxPattern {

	private int patternMinChar;
	private int patternMaxChar;
	private int patternNbNows;
	private String type;

	public int getPatternMinChar() {
		return patternMinChar;
	}

	public void setPatternMinChar(int patternMinChar) {
		this.patternMinChar = patternMinChar;
	}

	public int getPatternMaxChar() {
		return patternMaxChar;
	}

	public void setPatternMaxChar(int patternMaxChar) {
		this.patternMaxChar = patternMaxChar;
	}

	public int getPatternNbNows() {
		return patternNbNows;
	}

	public void setPatternNbNows(int patternNbNows) {
		this.patternNbNows = patternNbNows;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
