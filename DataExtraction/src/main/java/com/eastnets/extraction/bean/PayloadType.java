package com.eastnets.extraction.bean;

public class PayloadType {

	public static final String BINARY_TAG = "b";
	public static final String TEXT_TAG = "t";

	private boolean notLoaded;
	private boolean binary;
	private boolean text;

	public PayloadType(String type) {
		if (type == null) {
			type = "";
		}

		binary = type.equalsIgnoreCase(BINARY_TAG);
		text = type.equalsIgnoreCase(TEXT_TAG);
		notLoaded = !(binary || text);

	}

	public boolean isBinary() {
		return binary;
	}

	public boolean isText() {
		return text;
	}

	public void setBinary(boolean binary) {
		this.binary = binary;
	}

	public void setText(boolean text) {
		this.text = text;
	}

	public boolean isNotLoaded() {
		return notLoaded;
	}

	public void setNotLoaded(boolean notLoaded) {
		this.notLoaded = notLoaded;
	}

}
