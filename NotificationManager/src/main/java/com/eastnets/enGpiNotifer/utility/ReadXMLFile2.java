package com.eastnets.enGpiNotifer.utility;


import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXMLFile2 {
    static String nodeInstiveName="";
  static  Multimap<String,String> myArrayListMultimap = ArrayListMultimap.create();
    
    static String[] valus={"staff","firstname","lastname","nickname","salary","company","queues","queue"};
  public static void main(String[] args) {

    try {

	File file = new File("C:\\staff.xml");

	DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                             .newDocumentBuilder();

	Document doc = dBuilder.parse(file);

	printNote(doc.getChildNodes(),valus);
	
	for(String key:myArrayListMultimap.keySet()){
		System.out.println(myArrayListMultimap.get(key));
		
	}

    } catch (Exception e) {
	System.out.println(e.getMessage());
    }

    Multimap<String, String> map = ArrayListMultimap.create();
    map.put("key1", "value2");
    map.put("key1", "value1");
  }

  private static void printNote(NodeList nodeList,String[] valus) {
    for (int count = 0; count < nodeList.getLength(); count++) {

	    Node tempNode = nodeList.item(count);

	// make sure it's element node.
	if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
		for(String s:valus){
			if(s.equalsIgnoreCase(tempNode.getNodeName())){
				
				myArrayListMultimap.put(s, tempNode.getNodeName());
				
			}		
		}
	
		if (tempNode.hasChildNodes()) {
			printNote(tempNode.getChildNodes(),valus);
		}


	}

    }
  }
  
 


}