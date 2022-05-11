
package com.eastnets.textbreak.service;

import org.springframework.beans.factory.annotation.Autowired;

public class TextBreakService {

	@Autowired
	private TextBreakRepositoryService textBreakRepositoryService;

	public TextBreakRepositoryService getTextBreakRepositoryService() {
		return textBreakRepositoryService;
	}

	public void setTextBreakRepositoryService(TextBreakRepositoryService textBreakRepositoryService) {
		this.textBreakRepositoryService = textBreakRepositoryService;
	}

}
