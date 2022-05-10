package com.eastnets.enGpiNotifer.parser;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement; 
@XmlRootElement(name="JREMessage")
public class MTTypes {

	@XmlElement(type = MTType.class)
	List<MTType> mt;
	
	public MTTypes() {
	mt = new ArrayList<MTType>();
	}
}