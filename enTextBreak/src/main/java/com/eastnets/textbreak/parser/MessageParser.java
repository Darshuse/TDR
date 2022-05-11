
package com.eastnets.textbreak.parser;

import com.eastnets.textbreak.bean.ParsedData;
import com.eastnets.textbreak.bean.SourceData;

public interface MessageParser {

	ParsedData parse(SourceData data);

}
