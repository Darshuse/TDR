package com.eastnets.message.summary.enumDesc;

public enum SwiftBics {
	SWFTXXXXXXX("SWFTXXXXXXX"), SWHQHKHKXXX("SWHQHKHKXXX"), SWHQNLNLXXX("SWHQNLNLXXX"), SWHQUSUSXXX("SWHQUSUSXXX"), SWIFTXXXXXX("SWIFTXXXXXX");

	private final String bic;

	SwiftBics(String bic) {
		this.bic = bic;
	}

	public String getBic() {
		return bic;
	}
	
}
