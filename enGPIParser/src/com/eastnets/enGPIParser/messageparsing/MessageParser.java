package com.eastnets.enGPIParser.messageparsing;

import java.util.List;

import com.eastnets.domain.viewer.GPIMesgFields;
import com.eastnets.domain.viewer.MessageKey;
import com.eastnets.domain.viewer.TextFieldData;

public interface MessageParser {

	public GPIMesgFields parse(List<TextFieldData> fields, MessageKey key);
}
