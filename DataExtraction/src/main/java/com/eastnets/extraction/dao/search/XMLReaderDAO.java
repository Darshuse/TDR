package com.eastnets.extraction.dao.search;

import java.util.List;

import org.springframework.stereotype.Component;

import com.eastnets.extraction.bean.XMLMessage;

@Component
public interface XMLReaderDAO {
	public List<XMLMessage> getXMLMessage(String compsiteKeyString, boolean mxMessage);

}
