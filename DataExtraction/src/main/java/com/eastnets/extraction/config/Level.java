package com.eastnets.extraction.config;

import org.springframework.beans.factory.annotation.Value;

public class Level {

	@Value("${springframework}")
	String springframework;
	@Value("${extraction}")
	String extraction;

	public String getSpringframework() {
		return springframework;
	}

	public void setSpringframework(String springframework) {
		this.springframework = springframework;
	}

	public String getExtraction() {
		return extraction;
	}

	public void setExtraction(String extraction) {
		this.extraction = extraction;
	}
}
