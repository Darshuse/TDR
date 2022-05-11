package com.eastnets.resilience;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnLogger {
	final static String log_tag= "enXmlDump";
	static BufferedWriter bufferedWriter;

	public static void setLevel( Level value ){
		if ( bufferedWriter == null )
			Logger.getLogger(log_tag).setLevel( value );
	}
	
	public static void logI( String value ){
		Logger.getLogger(log_tag).info(value);
		if ( bufferedWriter != null ){
			try {
				bufferedWriter.write("I: " + value + "\r\n");
			} catch (IOException e) {
			}
		}
	}
	
	public static void logW( String value ){
		Logger.getLogger(log_tag).warning(value);
		if ( bufferedWriter != null ){
			try {
				bufferedWriter.write("W: " + value + "\r\n");
			} catch (IOException e) {
			}
		}
	}

	public static void logE( String value ){
		Logger.getLogger(log_tag).severe(value);
		if ( bufferedWriter != null ){
			try {
				bufferedWriter.write("E: " + value + "\r\n");
			} catch (IOException e) {
			}
		}
	}

	public static void print(String value) {
		if ( bufferedWriter == null ){
			return;
		}
		try {
			bufferedWriter.write( value + "\r\n");
		} catch (IOException e) {}
		
	}

	public static void setBufferWriter(BufferedWriter bufferedWriter) {
		EnLogger.bufferedWriter= bufferedWriter;
		
	}

	public static void closeFile() {
		if ( EnLogger.bufferedWriter != null ){
			try {
				EnLogger.bufferedWriter.close();
			} catch (Exception e) {
			}
		}
		EnLogger.bufferedWriter = null;

	}

	public static void printLn(String value) {
		System.out.println(value);
		if ( bufferedWriter != null ){
			try {
				bufferedWriter.write(  value + "\r\n");
			} catch (IOException e) {
			}
		}
		
	}
}
