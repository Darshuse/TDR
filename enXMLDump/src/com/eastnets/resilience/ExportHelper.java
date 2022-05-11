package com.eastnets.resilience;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class ExportHelper {
	public static BufferedWriter out;//the XML file we are writing on
	public static String newLine= "\r\n";
	public static String date_format;
	public static Date maxDate= null;//maximum date allowed
	public static Date minDate= null;//minimum date allowed


	//====================================================================================
	public static void formatFieldStr(ResultSet resultSet, String tabs, String field_name, int startIndex) throws IOException, SQLException {
		String label= StringMapper.fieldLabel(field_name);
		out.write( tabs + "<" + label + ">"
				+ substring( ExportHelper.defaultString( resultSet.getString(field_name) ), startIndex)
				+ "</" + label + ">" + newLine );
	}
	
	//====================================================================================
	public static void formatFieldDate(ResultSet resultSet, String tabs, String field_name) throws IOException, SQLException {
		String label= StringMapper.fieldLabel(field_name);
		out.write( tabs + "<" + label + ">"
				+ formatDate( resultSet.getTimestamp(field_name), date_format, true )
				+ "</" + label + ">" + newLine );
	}

	//====================================================================================
	public static void formatFieldNum(ResultSet resultSet, String tabs, String fielName, int char_count) throws SQLException, IOException {
		String val= "";
		if ( !defaultString( resultSet.getString(fielName)).isEmpty() ){
			val= String.format("%0" + char_count + "d", resultSet.getLong(fielName) );
		}

		out.write( tabs + "<" +    StringMapper.fieldLabel(fielName) + ">"
						+ val
						+ "</" +   StringMapper.fieldLabel(fielName) + ">" + newLine );

	}


	//====================================================================================
	public static void formatOptionalFieldStr( ResultSet resultSet, String tabs, String field_name) throws SQLException, IOException {
		String val= ExportHelper.defaultString( resultSet.getString(field_name));
		if ( !val.isEmpty() ){
			String label= StringMapper.fieldLabel(field_name);
			out.write( tabs + "<" + label + ">"
					+ ExportHelper.defaultString( resultSet.getString(field_name) )
					+ "</" + label + ">" + newLine );
		}

	}

	//====================================================================================
	public static void formatOptionalFieldStr( String tabs, String field_name, String value) throws SQLException, IOException {
		if ( !value.isEmpty() ){
			String label= StringMapper.fieldLabel(field_name);
			out.write( tabs + "<" + label + ">"
					+ value
					+ "</" + label + ">" + newLine );
		}

	}

	//====================================================================================
	public static void formatOptionalFieldStr(ResultSet resultSet, String tabs, String field_name, int start_index) throws SQLException, IOException {
		String val= ExportHelper.defaultString( resultSet.getString(field_name));
		if ( !val.isEmpty() ){
			String label= StringMapper.fieldLabel(field_name);
			out.write( tabs + "<" + label + ">"
					+ ExportHelper.substring( ExportHelper.defaultString( resultSet.getString(field_name) ), start_index)
					+ "</" + label + ">" + newLine );
		}		
	}


	//====================================================================================
	public static void formatFieldStr(ResultSet resultSet, String tabs, String field_name) throws IOException, SQLException {
		String label= StringMapper.fieldLabel(field_name);
		out.write( tabs + "<" + label + ">"
				+ ExportHelper.defaultString( resultSet.getString(field_name) )
				+ "</" + label + ">" + newLine );
	}

	//====================================================================================
	public static void formatFieldStr( String tabs, String field_name, String value) throws IOException, SQLException {
		String label= StringMapper.fieldLabel(field_name);
		out.write( tabs + "<" + label + ">"
				+  value
				+ "</" + label + ">" + newLine );	
	}
	//====================================================================================
	public static void formatOptionalFieldNum(ResultSet resultSet, String tabs, String fielName, int char_count ) throws IOException, SQLException {
		if ( !defaultString( resultSet.getString(fielName)).isEmpty() ){
			out.write( tabs + "<" +    StringMapper.fieldLabel(fielName) + ">"
							+ String.format("%0" + char_count + "d", resultSet.getLong(fielName) )
							+ "</" +   StringMapper.fieldLabel(fielName) + ">" + newLine );
		}		
	}
	
	//====================================================================================
	public static  void formatOptionalFieldDate(ResultSet resultSet, String tabs, String field_name) throws SQLException, IOException {
		String label= StringMapper.fieldLabel(field_name);
		Timestamp timestamp= resultSet.getTimestamp(field_name);
		if ( timestamp != null ){
			out.write( tabs + "<" + label + ">"
							+ formatDate( timestamp, date_format, true ) 
							+ "</" + label + ">" + newLine );
		}
		
	}

	//====================================================================================
	public static String formatSystemText(String textDataBlockStr) {
		textDataBlockStr= textDataBlockStr.replace("\r\n", "\n");
		textDataBlockStr= textDataBlockStr.replace("\r", "\n");
		textDataBlockStr= textDataBlockStr.replace("\n:", "}{");
		//textDataBlockStr= textDataBlockStr.replace("{:", "{");
		if ( !textDataBlockStr.endsWith("}") ){
			textDataBlockStr+= "}";
		}
		
		return textDataBlockStr;
	}
	

	//====================================================================================
	public static String printDecomposedSystemTextDataBlock(ResultSet systemTextFieldRS, int mesg_sumidl, int mesg_sumidh) throws SQLException {
		String text_data_block= "";
		while (systemTextFieldRS.next()){
			//make sure that the instance we are handling is for the message currently being printed
			int tefe_sumidl= systemTextFieldRS.getInt("TEXT_S_UMIDL");
			int tefe_sumidh= systemTextFieldRS.getInt("TEXT_S_UMIDH");
			if ( tefe_sumidl != mesg_sumidl || tefe_sumidh != mesg_sumidh ){
				//not the same message so we should get back to the previous instance so that we will handle this instance next time 
				systemTextFieldRS.previous();
				return text_data_block;
			}
			text_data_block+= "{" +  defaultString( systemTextFieldRS.getString("FIELD_CODE") ) + ":" + defaultString( systemTextFieldRS.getString("VALUE") ) + "}" ;
			
		}
		
		if ( text_data_block.isEmpty() ){
			EnLogger.logE(String.format("unable to load any instance for Message( %d , %d )", mesg_sumidl, mesg_sumidh ) );
		}
		return text_data_block;
	}
	

	//====================================================================================
	public static String printDecomposedTextDataBlock(ResultSet textFieldRS, int mesg_sumidl, int mesg_sumidh) throws SQLException {
		String text_data_block= "";
		while (textFieldRS.next()){
			//make sure that the instance we are handling is for the message currently being printed
			int tefe_sumidl= textFieldRS.getInt("TEXT_S_UMIDL");
			int tefe_sumidh= textFieldRS.getInt("TEXT_S_UMIDH");
			if ( tefe_sumidl != mesg_sumidl || tefe_sumidh != mesg_sumidh ){
				//not the same message so we should get back to the previous instance so that we will handle this instance next time 
				textFieldRS.previous();
				return text_data_block;
			}
			text_data_block+= appendTextDataBlocField( textFieldRS, mesg_sumidl, mesg_sumidh ) ;
			
		}
		
		if ( text_data_block.isEmpty() ){
			EnLogger.logE(String.format("unable to load any instance for Message( %d , %d )", mesg_sumidl, mesg_sumidh ) );
		}
		return text_data_block;
	}

	//====================================================================================
	private static String appendTextDataBlocField(ResultSet textFieldRS,int mesg_sumidl, int mesg_sumidh) throws SQLException {
		String field= "";
		String fieldCodeStr= textFieldRS.getString("FIELD_CODE");
		int fieldCode= textFieldRS.getInt("FIELD_CODE");
		String fieldOption= defaultString( textFieldRS.getString("FIELD_OPTION") )  ;
		String value= defaultString( textFieldRS.getString("VALUE") )  ;
		
		Clob valueMemo= textFieldRS.getClob("VALUE_MEMO");
		
		if ( fieldCodeStr == null )
		{
			EnLogger.logE(String.format("FIELD_CODE null for Message( %d , %d ) text", mesg_sumidl, mesg_sumidh ) );
			return "";
		}
		if( fieldCode == 0 )
		{
			String valueMemoStr= "";
			if ( valueMemo != null )
			{
				valueMemoStr= convertClob2String( valueMemo );
			}
			field+= "\r\n" +  defaultString( valueMemoStr ) ;
		}
		else if ( fieldCode > 0  && fieldCode < 100 )
		{
			String valueMemoStr= "";
			if ( valueMemo != null )
			{
				valueMemoStr= convertClob2String( valueMemo );
			}
			field+= "\r\n:" + fieldCode + fieldOption + ":" ;
			if ( !value.isEmpty() )
			{
				field+= value;
			}
			else
			{
				field+= valueMemoStr;
			}
		}
		else if ( fieldCode == 255 )
		{
			String valueMemoStr= "";
			if ( valueMemo != null )
			{
				valueMemoStr= convertClob2String( valueMemo );
			}
			field+= valueMemoStr ;
		}		
		
		return field;
	}


	//====================================================================================
	public static String getBlock3(ResultSet messageRS) throws SQLException {
		String block3= "";
		String val= messageRS.getString("MESG_COPY_SERVICE_ID");
		if ( val != null && val.trim().length() > 0  ){
			block3+= "{103:" + val.trim() + "}" ;
		}
		val= messageRS.getString("MESG_USER_REFERENCE_TEXT");
		if ( val != null && val.trim().length() > 0  ){
			block3+= "{108:" + val.trim() + "}" ;
		}
		val= messageRS.getString("MESG_USER_PRIORITY_CODE");
		if ( val != null && val.trim().length() > 0  ){
			block3+= "{113:" + val.trim() + "}" ;
		}
		val= messageRS.getString("MESG_RELEASE_INFO");
		if ( val != null && val.trim().length() > 0  ){
			block3+= "{115:" + val.trim() + "}" ;
		}
		val= messageRS.getString("MESG_MESG_USER_GROUP");
		if ( val != null && val.trim().length() > 0  ){
			block3+= "{119:" + val.trim() + "}" ;
		}
		return block3;
	}
	
	
	//====================================================================================
	public static String defaultString(String val) {
		if ( val == null ) return "";
		return ExportHelper.fixXml( val.trim() );
	}

	//====================================================================================
	public static String fixXml(String string) {
		if ( string == null  ){
			return "";
		}
		string= string.replace("\\r\\n", "\r\n" );
		string= string.replace("&", "&amp;" );
		string= string.replace("<", "&lt;" );
		string= string.replace(">", "&gt;" );
		string= string.replace("\"", "&quot;" );
		string= string.replace("'", "&apos;" );
		
		return string;
	}


	//====================================================================================
	static String convertClob2String(java.sql.Clob clobInData) {
		if (clobInData == null )
			return "";
		String stringClob = "";
		try {
			long i = 1;
			int clobLength = (int) clobInData.length();
			stringClob = clobInData.getSubString(i, clobLength);
		} catch (Exception e) {
			stringClob= "";
		}
		if ( stringClob == null ){
			stringClob= "";
		}
		return stringClob;
	}

	//====================================================================================
	public static String substring(String defaultString, int i) {
		try{
			return defaultString.substring(i);
		}catch(Exception e){}
		
		return defaultString;
	}
	
	//====================================================================================
	public static String substringR(String defaultString, int i) {
		try{
			return defaultString.substring(0, defaultString.length() - i );
		}catch(Exception e){}
		
		return defaultString;
	}

	//====================================================================================
	public static String formatDate(Timestamp timestamp, String format, boolean withTimeZone ) {		String timeZone= "";
		
		if ( maxDate == null ){
			GregorianCalendar gc = new GregorianCalendar(); //TimeZone.getTimeZone("GMT +0") );
			gc.set(Calendar.MILLISECOND, 0);
			gc.set( 2038, 0, 19, 3 , 9, 51 );
			gc.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			maxDate= gc.getTime();
		
			//GregorianCalendar gc = new GregorianCalendar(); //TimeZone.getTimeZone("GMT +0") );
			gc.set( 1970, 0, 1, 0 , 0, 0 );
			gc.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			minDate= gc.getTime();
		}
		
		
		if ( timestamp.after(maxDate) ){
			timestamp.setTime(maxDate.getTime());
		}

		if ( timestamp.before(minDate) ){
			timestamp.setTime(minDate.getTime());
		}
		
		if ( withTimeZone ){
			SimpleDateFormat sdf = new SimpleDateFormat( "Z" );
			timeZone= sdf.format(new Date( timestamp.getTime() ));
			timeZone= timeZone.substring(0, 3) + ":" + timeZone.substring(3) ;
		}
		SimpleDateFormat sdf = new SimpleDateFormat( format );
		return sdf.format(new Date( timestamp.getTime() )) + timeZone;
	}

	//====================================================================================
	public static void formatFieldBool(ResultSet resultSet, String tabs, String field_name) throws IOException, SQLException {
		String label= StringMapper.fieldLabel(field_name);
		out.write( tabs + "<" + label + ">"
				+  toBoolString( resultSet.getInt(field_name) )
				+ "</" + label + ">" + newLine );
	}
	
	//====================================================================================
	private static String toBoolString(int int1) {
		if ( int1 == 0 ) return "false";
		return "true";
	}

	//====================================================================================
	public static String getSUMID(int sumidl, int sumidh ) {
		String sumid= Integer.toHexString( sumidl );
		sumid+= Integer.toHexString( sumidh );
		
		return sumid.toUpperCase();
	}
}
