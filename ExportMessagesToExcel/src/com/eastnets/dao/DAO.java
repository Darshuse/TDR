package com.eastnets.dao;

 
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import com.eastnets.beans.DBType;


public class DAO {

    private static final String DB_PROPERTY_FILE = "./dbconfig.properties";

    private static final String DB_HOST = "dbHost";
    private static final String DB_PORT = "dbPort";
    private static final String DB_NAME = "dbName";
    private static final String DB_USER = "dbUser";
    private static final String DB_PASSWORD = "dbPassword";
    private static final String DB_TYPE = "dbType";

    private static String dbHost = "";
    private static String dbPort = "";
    private static String dbName = "";
    private static String dbUser = "";
    private static String dbPassword = "";
    private static String dbType = "";

    private static boolean dbPropLoaded = false;
 
    
    public DAO() {
        super();
    }

    
    private static void loadDBPropertyFile() throws Exception {
        
    	Properties dbProbFile = new Properties();
        dbProbFile.load(DAO.class.getResourceAsStream(DB_PROPERTY_FILE));

        dbHost = dbProbFile.getProperty(DB_HOST);
        dbPort = dbProbFile.getProperty(DB_PORT);
        dbName = dbProbFile.getProperty(DB_NAME);
        dbUser = dbProbFile.getProperty(DB_USER);
        dbPassword = dbProbFile.getProperty(DB_PASSWORD);
        dbType = dbProbFile.getProperty(DB_TYPE);

        dbPropLoaded = true;
            
        if ("".equals(dbHost) || "".equals(dbPort) ||
            "".equals(dbName) || "".equals(dbType) ||
            "".equals(dbUser) || "".equals(dbPassword)) {
        	
            throw new Exception("Database parameters is Missing in configuration file.");
        }

    }

    
    public static Connection getDBConnection() throws Exception {

    	String str= null;
        
    	if (!dbPropLoaded) {
            loadDBPropertyFile();
        }

        if(dbType.equalsIgnoreCase(DBType.ORACLE.toString())) { //Oracle connection string
	        
	        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
	        str = "jdbc:oracle:thin:" + dbUser + "/" + dbPassword + "@" + dbHost + ":" + dbPort + ":" + dbName ;
	       
        } else if (dbType.equalsIgnoreCase(DBType.MSSQL.toString())) { //MSSQL connection string
        	
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        	str = "jdbc:sqlserver://" + dbHost + ":" + dbPort + "/" + dbName +     	
        				 ";instanceName=SQLEXPRESS;user=" + dbUser + ";password=" + dbPassword;
        }
        
        Connection con = DriverManager.getConnection(str);
        return con;
    }

}

    

