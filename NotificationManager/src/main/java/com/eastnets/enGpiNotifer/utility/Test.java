package com.eastnets.enGpiNotifer.utility;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Test {
  
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(Test.class);

    logger.info("---myArrayListMultimap----------");
    logger.info("Duplicates allowed, Not Sorted...");
    Multimap<String,String> myArrayListMultimap = ArrayListMultimap.create();
    myArrayListMultimap.put("777", "Amaury Valdes");
    myArrayListMultimap.put("777", "Walter White");
    myArrayListMultimap.put("777", "John Smith");
    myArrayListMultimap.put("777", "Eric Hamlin");
    myArrayListMultimap.put("777", "Amaury Valdes");
  }
}