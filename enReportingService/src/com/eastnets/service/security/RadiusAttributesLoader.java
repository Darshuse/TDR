package com.eastnets.service.security;


import net.jradius.packet.attribute.AttributeFactory;



public class RadiusAttributesLoader {
	
	public void init(){
		
		AttributeFactory.loadAttributeDictionary("net.jradius.dictionary.AttributeDictionaryImpl");
		
	}

}