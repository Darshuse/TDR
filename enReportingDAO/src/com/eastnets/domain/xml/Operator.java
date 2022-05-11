package com.eastnets.domain.xml;

public enum Operator {
	
	EQUALS("='", "'"), NOT_EQUAL("<>'", "'"), CONTAINS(" LIKE '%", "%'"), DOES_NOT_CONTAINS(" NOT LIKE '%", "%'"), BEGINS_WITH(" LIKE '", "%'")
	, ENDS_WITH( "LIKE '%", "'"), GREATER_THAN(">", ""), LESS_THAN("<", "");
	
	
	private String prefixMeaning;
	private String postfixMeaning;
	
	Operator(String prefixMeaning,String postfixMeaning) {
		this.prefixMeaning = prefixMeaning;
		this.postfixMeaning = postfixMeaning;
	}

	public String getPrefixMeaning() {
		return prefixMeaning;
	}

	public void setPrefixMeaning(String prefixMeaning) {
		this.prefixMeaning = prefixMeaning;
	}

	public String getPostfixMeaning() {
		return postfixMeaning;
	}

	public void setPostfixMeaning(String postfixMeaning) {
		this.postfixMeaning = postfixMeaning;
	}
	
}
