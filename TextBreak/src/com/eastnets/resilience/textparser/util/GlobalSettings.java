package com.eastnets.resilience.textparser.util ;

public class GlobalSettings {
	public static String schemaName = "";
	public static String db_postfix = "";

	public static String getSchemaNameWithDot() {
		if ( schemaName == null || schemaName.isEmpty()){
			return "";
		}
		return schemaName + ".";
	}
	
}
