package com.eastnets.service.loader.exceptions;

import com.eastnets.domain.loader.LoaderMessage;

public class MessageParsingException extends Exception {


	private static final long serialVersionUID = -2410152748303788912L;
	private LoaderMessage loaderMessage;

	public MessageParsingException(Throwable t, LoaderMessage loaderMessage) {
		super(t);
		this.loaderMessage = loaderMessage;
	}

	public LoaderMessage getLoaderMessage() {
		return loaderMessage;
	}

	public void setLoaderMessage(LoaderMessage loaderMessage) {
		this.loaderMessage = loaderMessage;
	}

}
