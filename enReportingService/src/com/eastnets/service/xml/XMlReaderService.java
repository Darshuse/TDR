package com.eastnets.service.xml;

import com.eastnets.domain.viewer.XMLMessage;
import com.eastnets.service.Service;

public interface XMlReaderService extends Service {
	
	public XMLMessage readXMLMessage();

}
