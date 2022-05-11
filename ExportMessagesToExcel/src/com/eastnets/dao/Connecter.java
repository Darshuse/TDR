package com.eastnets.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;


public class Connecter {

    
    private static Connection con;
    private static ResultSet rs;
    private static PreparedStatement updateStmt;
    private static PreparedStatement insrtStmt;
    private static PreparedStatement slctStmt;

    
    /** Creates a new instance of Connecter */
    public Connecter() {

    }

    
    public static void connect() throws Exception {

		con = DAO.getDBConnection();
	}

    
    public static void disconnect() throws Exception {

    	con.close();
    	con = null;
    }

    
    public static ResultSet exceuteSlctQuery(String q, Object[] params) throws Exception {

		slctStmt = con.prepareStatement(q);
	
		for (int i = 1; i <= actLength(params); i++) {
	
		    slctStmt.setObject(i, params[i - 1]);
		}
	
		rs = slctStmt.executeQuery();
		return rs;
    }
    

    public static ResultSet exceuteSlctQuery(String q) throws Exception {

    	init();

		slctStmt = con.prepareStatement(q);
		rs = slctStmt.executeQuery();
	
		return rs;
    }
    

    public static void executeUpdateStatement(String updStmt, Object[] params) throws Exception {

		updateStmt = con.prepareStatement(updStmt);
	
		for (int i = 1; i <= actLength(params); i++) {
	
		    updateStmt.setObject(i, params[i - 1]);
		}
	
		updateStmt.execute();
    }

    
    public static void executeInsertStatement(String insStmt) throws Exception {

		insrtStmt = con.prepareStatement(insStmt);
	
		insrtStmt.execute();
    }

    
    public static void executeInsertStatement(String insStmt, Object[] params) throws Exception {

		insrtStmt = con.prepareStatement(insStmt);
	
		for (int i = 1; i <= actLength(params); i++) {
	
		    insrtStmt.setObject(i, params[i - 1]);
		}
	
		insrtStmt.execute();
    }

    
    private static int actLength(Object[] array) {

		int actLength = 0;
		
		for (int i = 0; i < array.length; i++) {
	
		    if (array[i] != null) {
		    	actLength++;
		    }
		}
	
		return actLength;
    }

    
    private static void init() throws Exception {
		
    	if (rs != null) {
		    rs.close();
		    rs = null;
		}
    }
    
    
}