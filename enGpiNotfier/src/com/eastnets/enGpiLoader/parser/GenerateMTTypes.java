package com.eastnets.enGpiLoader.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller; 
import org.apache.log4j.Logger;  

public class GenerateMTTypes {
	private static MTTypes mtTypes = new MTTypes();
	
	private static Map<String, MTType> mtTypesMap = new HashMap<String, MTType>();
	private static final Logger LOGGER = Logger.getLogger(GenerateMTTypes.class);
	static{
		try {
			getMtTypesMap();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	public static Map<String, MTType> getMtTypesMap() throws JAXBException {
		if (mtTypesMap.isEmpty()) {
			LOGGER.debug("Loading Mt Types");
			InputStream mtTypesStream = GenerateMTTypes.class.getResourceAsStream("MT_Types.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(MTTypes.class);
			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			mtTypes = (MTTypes) unMarshaller.unmarshal(mtTypesStream);
			List<MTType> list = mtTypes.mt;
			for (MTType mt : list) {
				mtTypesMap.put(mt.getId(), mt);
			}
			return mtTypesMap;
		}
		return mtTypesMap;
	}

}
