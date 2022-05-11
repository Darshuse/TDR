package com.eastnets.notifier.domain;

import java.util.List;

public class Text {

	private List<String> chargesDetails; // field 71
	private String narrativeDescription; // field 79
	private String fullText;

	public List<String> getChargesDetails() {
		return chargesDetails;
	}

	public void setChargesDetails(List<String> chargesDetails) {
		this.chargesDetails = chargesDetails;
	}

	public String getNarrativeDescription() {
		return narrativeDescription;
	}

	public void setNarrativeDescription(String narrativeDescription) {
		this.narrativeDescription = narrativeDescription;
	}

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	@Override
	public String toString() {
		return "Text [chargesDetails=" + chargesDetails + ", narrativeDescription=" + narrativeDescription
				+ ", fullText=" + fullText + "]";
	}

}
