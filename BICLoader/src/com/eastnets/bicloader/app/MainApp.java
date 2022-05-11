package com.eastnets.bicloader.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {

	public static void main(String[] args) {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		BICLoaderApp obj = (BICLoaderApp) context.getBean("bicLoader");
		obj.startProcess(args);

	}

}
