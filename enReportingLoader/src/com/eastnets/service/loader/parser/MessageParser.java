package com.eastnets.service.loader.parser;

import java.util.concurrent.Callable;

import com.eastnets.domain.loader.LoaderMessage;

public interface MessageParser extends Callable<LoaderMessage> {

	LoaderMessage parse() throws Exception;
}
