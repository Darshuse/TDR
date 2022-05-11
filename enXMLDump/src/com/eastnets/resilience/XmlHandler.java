package com.eastnets.resilience;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;

public class XmlHandler {
	String newLine= "";
	String tab= "";
	
	public void ProcessMessages( EnXmlDumpConfig config, Connection connection, DatabaseQueries queryProvider ){
		if ( config.isFormattedXml() ){
			newLine= "\r\n";
			tab= "\t";
		}
		try {
			//create the file and start writing to it 
			BufferedWriter out = new BufferedWriter(new FileWriter( config.getFilePath() ));
			writeHeader( out );
			writeMessages( out, connection, queryProvider, config );
			writeFooter( out );
			
			
			out.close();
		} catch (IOException e) {
			EnLogger.logE("IOError: " + e.getMessage() );
		} catch (Exception e) {
			EnLogger.logE("Error: " + e.getMessage() );
			e.printStackTrace();
		}
	}

	private void writeHeader(BufferedWriter out) throws IOException {
		out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + newLine );
		out.write("<Messages xmlns=\"urn:swift:saa:xsd:messaging\">" + newLine );
	}

	private void writeFooter(BufferedWriter out) throws IOException  {
		out.write("</Messages>" + newLine );		
	}
	
	private void writeMessages(BufferedWriter out, Connection connection, DatabaseQueries queryProvider, EnXmlDumpConfig config ) throws IOException, Exception  {
		MessageSearchHandler messageSearchHandler = new MessageSearchHandler(out, connection, queryProvider, config, newLine, tab);
		messageSearchHandler.processMessages();		
	}

}
