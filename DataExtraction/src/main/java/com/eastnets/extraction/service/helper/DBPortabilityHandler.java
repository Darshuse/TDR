package com.eastnets.extraction.service.helper;

/**
 * Data Base Portability Handler, Used as adapter to handle differences between
 * Oracle and MsSql Database
 */
public class DBPortabilityHandler {

	public static final int DB_TYPE_ORACLE = 0;
	public static final int DB_TYPE_MSSQL = 1;
	private static int dbType;
	private static boolean partitioned;

	public static int getDbType() {
		return dbType;
	}

	public static void setDbType(int DBType) {
		dbType = DBType;
	}

	public static String getDate(String date, String pattern) {
		if (dbType == DB_TYPE_MSSQL) {
			return "'" + date + "'";
		}

		return "To_DATE('" + date + "', '" + pattern + "')";
	}

	public static boolean isPartitionedDB() {
		return partitioned;
	}
	
	public static String getDataLengthFn() {
		if(getDbType() ==  DB_TYPE_MSSQL) {	
	        return "DataLength" ;
		}
		
		return "dbms_lob.getlength" ;
	}

}
