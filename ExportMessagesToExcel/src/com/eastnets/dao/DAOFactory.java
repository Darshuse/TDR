package com.eastnets.dao;


public class DAOFactory {

	private static Connecter dbConnector ;
	
	public static Connecter getConnector() {
		
		if(dbConnector == null) {
			dbConnector = new Connecter();
		}
		
		return dbConnector;
	}
		
}
