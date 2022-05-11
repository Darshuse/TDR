package com.eastnets.beans;


public enum DBType {
	
	ORACLE ("Oracle"),
	MSSQL ("Sql");

    private final String name;       

    private DBType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }

}