package com.eastnets.resilience;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageSearchHandler {
	DatabaseQueries queryProvider;
	BufferedWriter out;//the XML file we are writing on
	Connection connection;//the database connection
	EnXmlDumpConfig config;
	String newLine= "\r\n";
	String tab= "\t";
	String date_format= "yyyy-MM-dd'T'HH:mm:ss.SSS";

	//database query recordSets 
	ResultSet mesgRS;//recordSets for the messages
	ResultSet instRS;//recordSets for the instances
	ResultSet intvRS;//recordSets for the interventions
	ResultSet appeRS;//recordSets for the appendices
	ResultSet textRS;//recordSets for the texts
	ResultSet tefeRS;//recordSets for the text fields
	ResultSet tefsRS;//recordSets for the system text fields

	//don't want to concatenate every time
	String tab2;
	String tab3;
	String tab4;
	String tab5;
	

	//====================================================================================
	public MessageSearchHandler(BufferedWriter out, Connection connection, DatabaseQueries queryProvider, EnXmlDumpConfig config, String newLine, String tab) {
		this.out= out;
		ExportHelper.out= out;
		this.connection= connection;
		this.config= config;
		this.newLine= newLine;
		this.queryProvider= queryProvider;
		ExportHelper.newLine= newLine;
		ExportHelper.date_format= date_format;
		this.tab= tab;
		tab2= tab + tab ;
		tab3= tab2 + tab ;
		tab4= tab3 + tab;
		tab5= tab4 + tab;

	}

	//====================================================================================
	public void processMessages() throws Exception {	
		if ( this.out == null || this.connection == null ){
			throw new Exception("MessageSearchHandler not initialized correctly");
		}
		ApplicationMonitor.processedMessages= 0;
		ApplicationMonitor.processedInstances= 0;
		ApplicationMonitor.processedInterventions= 0;
		ApplicationMonitor.processedAppendices= 0;
		
		try {
			
			//create the temp table, and fill it with the messages we want to print
			queryProvider.initTempTable(config);
			
			mesgRS = queryProvider.getMesgResultSet();
			instRS = queryProvider.getInstResultSet();
			intvRS = queryProvider.getIntvResultSet();
			appeRS = queryProvider.getAppeResultSet();
			textRS = queryProvider.getTextResultSet();
			tefeRS = queryProvider.getTextFieldResultSet();
			tefsRS = queryProvider.getSystemTextFieldResultSet();
			
/*			System.out.println( DatabaseQueries.temp_table_create );
			System.out.println( select_statement );
			System.out.println( DatabaseQueries.mesg_select );
			System.out.println( DatabaseQueries.inst_select );
			System.out.println( DatabaseQueries.intv_select );
			System.out.println( DatabaseQueries.appe_select );
			
			System.out.println(  );*/
			
			while (mesgRS.next()) {
				printMessage();//print next message
				ApplicationMonitor.processedMessages++;
			}
		} catch (SQLException e ) {
			EnLogger.logE("SQL Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	

	//====================================================================================
	private void printMessage() throws IOException, SQLException {
		int mesg_sumidl= mesgRS.getInt("MESG_S_UMIDL");
		int mesg_sumidh= mesgRS.getInt("MESG_S_UMIDH");
		EnLogger.print( String.format("Processing Message ( %d, %d )", mesg_sumidl, mesg_sumidh ) );
		
		out.write( tab + "<Message>" + newLine );		
		out.write( tab2 + "<Identifier>" + newLine );
		ExportHelper.formatFieldStr( tab3, "MESG_S_UMID", ExportHelper.getSUMID( mesg_sumidl, mesg_sumidh ) );		
		out.write( tab3 + "<PassiveLockToken></PassiveLockToken>" + newLine );
		out.write( tab3 + "<TableId>" + ExportHelper.formatDate( mesgRS.getTimestamp( "MESG_CREA_DATE_TIME" ), "yyyyMMdd", false )+  "</TableId>" + newLine );	
		out.write( tab2 + "</Identifier>" + newLine );
		
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_VALIDATION_REQUESTED",  4 );
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_VALIDATION_PASSED",  4 );
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_CLASS",  5 );
		ExportHelper.formatFieldBool( mesgRS, tab2, "MESG_IS_TEXT_READONLY" );
		ExportHelper.formatFieldBool( mesgRS, tab2, "MESG_IS_DELETE_INHIBITED");
		ExportHelper.formatFieldBool( mesgRS, tab2, "MESG_IS_TEXT_MODIFIED" );
		ExportHelper.formatFieldBool( mesgRS, tab2, "MESG_IS_PARTIAL");
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_STATUS");
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_CREA_APPL_SERV_NAME");
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_CREA_MPFN_NAME");
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_CREA_RP_NAME");
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_CREA_OPER_NICKNAME");
		ExportHelper.formatFieldDate( mesgRS, tab2, "MESG_CREA_DATE_TIME");
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_MOD_OPER_NICKNAME");
		ExportHelper.formatFieldDate( mesgRS, tab2, "MESG_MOD_DATE_TIME");
		ExportHelper.formatOptionalFieldStr ( mesgRS, tab2, "MESG_CAS_TARGET_RP_NAME");

 
		//the field MESG_RECOVERY_ACCEPT_INFO has special values ( 0, 1 , true, false, null )
		String recoveryAcceptInfo= ExportHelper.defaultString( mesgRS.getString("MESG_RECOVERY_ACCEPT_INFO") ).toLowerCase();
		if ( !recoveryAcceptInfo.isEmpty() ){
			if( recoveryAcceptInfo.equalsIgnoreCase("1") ){
				recoveryAcceptInfo= "true";
			}
			if( recoveryAcceptInfo.equalsIgnoreCase("0") ){
				recoveryAcceptInfo= "false";
			}
		}
		ExportHelper.formatOptionalFieldStr( tab2, "MESG_RECOVERY_ACCEPT_INFO", recoveryAcceptInfo);
		ExportHelper.formatFieldStr( mesgRS, tab2, "MESG_UUMID");
		ExportHelper.formatFieldStr( mesgRS, tab2, "MESG_UUMID_SUFFIX");
		ExportHelper.formatFieldStr( mesgRS, tab2, "MESG_SENDER_CORR_TYPE");
		ExportHelper.formatFieldStr( mesgRS, tab2, "MESG_SENDER_X1");
		ExportHelper.formatFieldStr( mesgRS, tab2, "MESG_FRMT_NAME");
		ExportHelper.formatFieldStr( mesgRS, tab2, "MESG_SUB_FORMAT");
		ExportHelper.formatFieldStr( mesgRS, tab2, "MESG_SYNTAX_TABLE_VER");
		ExportHelper.formatFieldStr( tab2, "MESG_NATURE", ExportHelper.substringR( ExportHelper.defaultString( mesgRS.getString("MESG_NATURE") ), 4 ) );
		ExportHelper.formatFieldStr( mesgRS, tab2, "MESG_NETWORK_APPL_IND");
		ExportHelper.formatFieldStr( mesgRS, tab2, "MESG_TYPE");
		ExportHelper.formatFieldBool( mesgRS, tab2, "MESG_IS_LIVE");
		ExportHelper.formatOptionalFieldStr( mesgRS, tab2, "MESG_NETWORK_PRIORITY", 4);
		ExportHelper.formatFieldBool( mesgRS, tab2, "MESG_DELV_OVERDUE_WARN_REQ");
		ExportHelper.formatFieldBool ( mesgRS, tab2, "MESG_NETWORK_DELV_NOTIF_REQ");
		ExportHelper.formatOptionalFieldStr( mesgRS, tab2, "MESG_USER_REFERENCE_TEXT");
		ExportHelper.formatOptionalFieldStr( mesgRS, tab2, "MESG_FIN_VALUE_DATE");
		ExportHelper.formatOptionalFieldStr( mesgRS, tab2, "MESG_FIN_CCY_AMOUNT");
		ExportHelper.formatOptionalFieldStr( mesgRS, tab2, "MESG_TRN_REF");
		ExportHelper.formatOptionalFieldStr( mesgRS, tab2, "MESG_REL_TRN_REF");
		ExportHelper.formatOptionalFieldStr( mesgRS, tab2, "MESG_MESG_USER_GROUP");
		ExportHelper.formatFieldBool( mesgRS, tab2, "MESG_ZZ41_IS_POSSIBLE_DUP");
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_IDENTIFIER");
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_SENDER_SWIFT_ADDRESS");
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_RECEIVER_SWIFT_ADDRESS");
		ExportHelper.formatFieldStr ( mesgRS, tab2, "MESG_SERVICE");
		
		printInstances( mesg_sumidl, mesg_sumidh );
		
		printText( mesg_sumidl, mesg_sumidh );
		out.write( tab + "</Message>" + newLine );		
	}

	//====================================================================================
	private void printText(int mesg_sumidl, int mesg_sumidh) throws IOException, SQLException {
		boolean messageTextFound= false;
		boolean messageTextFieldFound= false;
		boolean messageSystemTextFieldFound= false;
		if (textRS.next()){
			//make sure that the text we are handling is for the message currently being printed
			int text_sumidl= textRS.getInt("TEXT_S_UMIDL");
			int text_sumidh= textRS.getInt("TEXT_S_UMIDH");
			if ( text_sumidl != mesg_sumidl || text_sumidh != mesg_sumidh ){
				//not the same message so we should get back to the previous text so that we will handle this instance next time 
				textRS.previous();
			}else{
				messageTextFound= true;
			}
		}
		if ( tefeRS.next()){
			//make sure that the text we are handling is for the message currently being printed
			int text_sumidl= tefeRS.getInt("TEXT_S_UMIDL");
			int text_sumidh= tefeRS.getInt("TEXT_S_UMIDH");
			if ( text_sumidl == mesg_sumidl && text_sumidh == mesg_sumidh ){
				messageTextFieldFound= true;
			}
			tefeRS.previous();
		}
		if ( !messageTextFieldFound && tefsRS.next()){
			int text_sumidl= tefsRS.getInt("TEXT_S_UMIDL");
			int text_sumidh= tefsRS.getInt("TEXT_S_UMIDH");
			if ( text_sumidl == mesg_sumidl && text_sumidh == mesg_sumidh ){
				messageSystemTextFieldFound= true;
			}
			tefsRS.previous();
		}
		
		if ( !messageTextFound ){
			EnLogger.logE(String.format("unable to load text for Message( %d , %d )", mesg_sumidl, mesg_sumidh ) );
			return;
		}
			
		out.write( tab2 + "<Text>" + newLine );		
		out.write( tab3 + "<Identifier>" + newLine );
		ExportHelper.formatFieldStr(tab4, "TEXT_S_UMID", ExportHelper.getSUMID( mesg_sumidl, mesg_sumidh ));
		out.write( tab4 + "<PassiveLockToken></PassiveLockToken>" + newLine );
		out.write( tab3 + "</Identifier>" + newLine );
		
		String textDataBlockStr= ExportHelper.convertClob2String( textRS.getClob("TEXT_DATA_BLOCK") );
		int mesgType= 999;
		String mesgtypeStr = ExportHelper.defaultString( mesgRS.getString("MESG_TYPE") );
		try{
			mesgType= Integer.parseInt(mesgtypeStr);
		}catch(Exception e){
			mesgType= 999;
		}
		
		if ( !textDataBlockStr.isEmpty()  ){
			if ( mesgType >= 100 ){
				ExportHelper.formatFieldStr( tab3, "TEXT_DATA_BLOCK", textDataBlockStr );
			}else{
				ExportHelper.formatFieldStr( tab3, "TEXT_DATA_BLOCK", ExportHelper.formatSystemText(textDataBlockStr) );
			}
		}else if ( messageTextFieldFound ){
			ExportHelper.formatFieldStr( tab3, "TEXT_DATA_BLOCK", ExportHelper.printDecomposedTextDataBlock( tefeRS, mesg_sumidl, mesg_sumidh ));
		}else if ( messageSystemTextFieldFound ){
			ExportHelper.formatFieldStr( tab3, "TEXT_DATA_BLOCK", ExportHelper.printDecomposedSystemTextDataBlock( tefsRS, mesg_sumidl, mesg_sumidh ));
		}
		else{
			if ( mesgType != 5){
				EnLogger.logE(String.format("unable to find valid text for Message( %d , %d )", mesg_sumidl, mesg_sumidh ) );
			}
			ExportHelper.formatFieldStr( tab3, "TEXT_DATA_BLOCK", "");
		}
		
		String block3= ExportHelper.getBlock3(mesgRS);
		if ( !block3.isEmpty() ){
			ExportHelper.formatFieldStr( tab3, "TEXT_SWIFT_BLOCK_3", block3 );
		}
		ExportHelper.formatFieldStr( textRS, tab3, "TEXT_SWIFT_BLOCK_5");

		out.write( tab2 + "</Text>" + newLine );	
	}

	//====================================================================================
	private void printInstances(int mesg_sumidl, int mesg_sumidh) throws SQLException, IOException {
		boolean instancesFound= false;
		while (instRS.next()){
			//make sure that the instance we are handling is for the message currently being printed
			int inst_sumidl= instRS.getInt("INST_S_UMIDL");
			int inst_sumidh= instRS.getInt("INST_S_UMIDH");
			
			EnLogger.print( String.format("    instance stack_top ( %d, %d, %s )", inst_sumidl, inst_sumidh, ExportHelper.defaultString( instRS.getString("INST_NUM" ) ) ) );
			if ( inst_sumidl != mesg_sumidl || inst_sumidh != mesg_sumidh ){
				//not the same message so we should get back to the previous instance so that we will handle this instance next time 
				instRS.previous();
				return;
			}
			instancesFound= true;
			EnLogger.print( String.format("Processing Instance ( %d, %d, %s )", mesg_sumidl, mesg_sumidh, ExportHelper.defaultString( instRS.getString("INST_NUM" ) ) ) );
			printInstance( inst_sumidl, inst_sumidh );

			ApplicationMonitor.processedInstances++;
			
		}
		
		if (! instancesFound ){
			EnLogger.logE(String.format("unable to load any instance for Message( %s , %d , %d )", mesgRS.getInt("MESG_TYPE") , mesg_sumidl, mesg_sumidh ) );
		}
		
	}

	//====================================================================================
	private void printInstance(int inst_sumidl, int inst_sumidh) throws IOException, SQLException {
		long inst_num=  instRS.getLong("INST_NUM");
		out.write( tab2 + "<Instance>" + newLine );		
		out.write( tab3 + "<Identifier>" + newLine );
		ExportHelper.formatFieldStr( tab4, "INST_S_UMID", ExportHelper.getSUMID( inst_sumidl, inst_sumidh ) );
		ExportHelper.formatFieldStr( tab4, "INST_NUM", "" + inst_num );		
		out.write( tab4 + "<PassiveLockToken></PassiveLockToken>" + newLine );
		out.write( tab3 + "</Identifier>" + newLine );
		
		ExportHelper.formatFieldStr( instRS, tab3, "INST_TYPE", 10);
		String val = MessageFormatter.format( instRS, "INST_NOTIFICATION_TYPE") ;
		
		ExportHelper.formatFieldStr( tab3, "INST_NOTIFICATION_TYPE", val);
		
		ExportHelper.formatFieldStr( instRS, tab3, "INST_STATUS");
		ExportHelper.formatFieldStr( instRS, tab3, "INST_RELATED_NBR");
		ExportHelper.formatOptionalFieldDate( instRS, tab3, "INST_APPE_DATE_TIME");
		
		String appeSeqNum= ExportHelper.defaultString( instRS.getString("INST_APPE_SEQ_NBR") );
		try{ 
			long appeSeqNumInt= Long.parseLong(appeSeqNum);
			if ( appeSeqNumInt > 2147483647 || appeSeqNumInt < 0  ){
				appeSeqNum= "0";
			}
		}catch( Exception e){}
		
		ExportHelper.formatFieldStr( tab3, "INST_APPE_SEQ_NBR", appeSeqNum );
		
		ExportHelper.formatFieldStr( instRS, tab3, "INST_UNIT_NAME");
		ExportHelper.formatOptionalFieldStr( instRS, tab3, "INST_RP_NAME");
		ExportHelper.formatFieldStr( instRS, tab3, "INST_MPFN_NAME"); 
		ExportHelper.formatOptionalFieldStr( instRS, tab3, "INST_MPFN_HANDLE");
		ExportHelper.formatFieldStr( instRS, tab3, "INST_PROCESS_STATE"); 
		ExportHelper.formatFieldStr( instRS, tab3, "INST_LAST_MPFN_RESULT", 2); 
		ExportHelper.formatFieldStr( instRS, tab3, "INST_RELATIVE_REF"); 
		ExportHelper.formatFieldStr( instRS, tab3, "INST_SM2000_PRIORITY"); 
		ExportHelper.formatFieldDate( instRS, tab3, "INST_DEFERRED_TIME"); 
		ExportHelper.formatFieldStr( instRS, tab3, "INST_CREA_APPL_SERV_NAME"); 
		ExportHelper.formatFieldStr( instRS, tab3, "INST_CREA_MPFN_NAME"); 
		ExportHelper.formatFieldStr( instRS, tab3, "INST_CREA_RP_NAME"); 
		ExportHelper.formatFieldDate( instRS, tab3, "INST_CREA_DATE_TIME"); 
		ExportHelper.formatFieldStr( instRS, tab3, "INITIAL_TARGET_RP_NAME");
		ExportHelper.formatOptionalFieldStr( instRS, tab3, "INST_AUTH_OPER_NICKNAME");
		ExportHelper.formatFieldStr( instRS, tab3, "INST_LAST_OPER_NICKNAME");
		ExportHelper.formatFieldStr( tab3, "INST_CREA_DATE_TIMEInQueueSince", ExportHelper.formatDate( instRS.getTimestamp("INST_CREA_DATE_TIME"), date_format, true ) );
		ExportHelper.formatFieldStr( instRS, tab3, "INST_RECEIVER_CORR_TYPE");
		ExportHelper.formatFieldStr( instRS, tab3, "INST_RECEIVER_X1");
		ExportHelper.formatOptionalFieldStr( instRS, tab3, "INST_RECEIVER_INSTITUTION_NAME");
		ExportHelper.formatOptionalFieldStr( instRS, tab3, "INST_RECEIVER_BRANCH_INFO");
		ExportHelper.formatOptionalFieldStr( instRS, tab3, "INST_RECEIVER_CITY_NAME");
		ExportHelper.formatOptionalFieldStr( instRS, tab3, "INST_RECEIVER_CTRY_CODE");
		ExportHelper.formatOptionalFieldStr( instRS, tab3, "INST_RECEIVER_NETWORK_IAPP_NAM");
		
		String receiverDelStatus= MessageFormatter.format( instRS, "INST_RCV_DELIVERY_STATUS" );
		ExportHelper.formatOptionalFieldStr( tab3, "INST_RCV_DELIVERY_STATUS", receiverDelStatus );
		
		ExportHelper.formatOptionalFieldDate( instRS, tab3, "INST_INTV_DATE_TIME");
		ExportHelper.formatOptionalFieldStr( instRS, tab3, "INST_INTV_SEQ_NBR");
	 
		
		printInterventions( inst_sumidl, inst_sumidh, inst_num );
		printAppendices( inst_sumidl, inst_sumidh, inst_num );
		
		out.write( tab2 + "</Instance>" + newLine );	
	}

	//====================================================================================
	private void printInterventions(int inst_sumidl, int inst_sumidh, long inst_num) throws SQLException, IOException {
		boolean intvFound= false;
		while (intvRS.next()){
			//make sure that the intervention we are handling is for the message instance currently being printed
			int intv_sumidl= intvRS.getInt("INTV_S_UMIDL");
			int intv_sumidh= intvRS.getInt("INTV_S_UMIDH");
			long intv_inst_num= intvRS.getLong("INTV_INST_NUM");

			EnLogger.print( String.format("   intervention stacktop ( %d, %d, %d, %s )", 
					inst_sumidl, inst_sumidh, intv_inst_num, ExportHelper.defaultString( intvRS.getString("INTV_SEQ_NBR") )));
			
			if ( intv_sumidl != inst_sumidl || intv_sumidh != inst_sumidh || intv_inst_num != inst_num ){
				//not the same message so we should get back to the previous intervention so that we will handle this intervention next time 
				intvRS.previous();
				return;
			}

			EnLogger.print( String.format("Processing Intervention (  %d, %d, %d , %s )", 
					inst_sumidl, inst_sumidh, intv_inst_num, ExportHelper.defaultString( intvRS.getString("INTV_SEQ_NBR") )));
			intvFound= true;
			printIntervention( inst_sumidl, inst_sumidh, intv_inst_num );
			ApplicationMonitor.processedInterventions++;
		}
		
		if (! intvFound ){
			EnLogger.logE(String.format("unable to load any intervention for Message( %d , %d ), instance ( %d )", inst_sumidl, inst_sumidh, inst_num ) );
		}
		
	}

	//====================================================================================
	private void printIntervention(int inst_sumidl, int inst_sumidh, long intv_inst_num) throws IOException, SQLException {
		
		out.write( tab3 + "<Intervention>" + newLine );		
		out.write( tab4 + "<Identifier>" + newLine );
		ExportHelper.formatFieldStr(tab5, "INTV_S_UMID", ExportHelper.getSUMID( inst_sumidl, inst_sumidh ));
		ExportHelper.formatFieldStr(tab5, "INTV_INST_NUM", "" + intv_inst_num );
		ExportHelper.formatFieldDate(intvRS, tab5, "INTV_DATE_TIME" );
		
		String intvSeqNum= ExportHelper.defaultString( intvRS.getString("INTV_SEQ_NBR") );
		try{ 
			long intvSeqNumInt= Long.parseLong(intvSeqNum);
			if ( intvSeqNumInt > 2147483647 || intvSeqNumInt < 0  ){
				intvSeqNum= "0";
			}
		}catch( Exception e){}
		
		ExportHelper.formatFieldStr( tab5, "INTV_SEQ_NBR", intvSeqNum );
		
		out.write( tab5 + "<PassiveLockToken></PassiveLockToken>" + newLine );
		out.write( tab4 + "</Identifier>" + newLine );

		ExportHelper.formatFieldStr(intvRS, tab4, "INTV_INTY_NUM" );
		ExportHelper.formatFieldStr(intvRS, tab4, "INTV_INTY_NAME" );
		ExportHelper.formatFieldStr(intvRS, tab4, "INTV_INTY_CATEGORY", 5 );
		ExportHelper.formatFieldStr(intvRS, tab4, "INTV_OPER_NICKNAME" );
		ExportHelper.formatOptionalFieldStr(intvRS, tab4, "INTV_APPL_SERV_NAME" );
		ExportHelper.formatFieldStr(intvRS, tab4, "INTV_MPFN_NAME" );
		

		String appeSeqNum= ExportHelper.defaultString( intvRS.getString("INTV_APPE_SEQ_NBR") );
		try{ 
			long appeSeqNumInt= Long.parseLong(appeSeqNum);
			if ( appeSeqNumInt > 2147483647 || appeSeqNumInt < 0  ){
				appeSeqNum= "0";
			}
		}catch( Exception e){}
		ExportHelper.formatFieldStr( tab4, "INTV_APPE_SEQ_NBR", appeSeqNum );
		
		ExportHelper.formatFieldStr(intvRS, tab4, "INTV_LENGTH" );
		ExportHelper.formatFieldStr(intvRS, tab4, "INTV_MERGED_TEXT" );

		out.write( tab3 + "</Intervention>" + newLine );	
	}

	//====================================================================================
	private void printAppendices(int inst_sumidl, int inst_sumidh, long inst_num) throws SQLException, IOException {
		while (appeRS.next()){
			//make sure that the appendix we are handling is for the message instance currently being printed
			int appe_sumidl= appeRS.getInt("APPE_S_UMIDL");
			int appe_sumidh= appeRS.getInt("APPE_S_UMIDH");
			long appe_inst_num= appeRS.getLong("APPE_INST_NUM");

			EnLogger.print( String.format("   appendix stacktop ( %d, %d, %d, %s )", 
					inst_sumidl, inst_sumidh, inst_num, ExportHelper.defaultString( appeRS.getString("APPE_SEQ_NBR") ) ));
			
			if ( appe_sumidl != inst_sumidl || appe_sumidh != inst_sumidh || appe_inst_num != inst_num ){
				//not the same message so we should get back to the previous intervention so that we will handle this intervention next time 
				appeRS.previous();
				return;
			}
			EnLogger.print( String.format("Processing Appendix( %d, %d, %d, %s )", 
					inst_sumidl, inst_sumidh, inst_num, ExportHelper.defaultString( appeRS.getString("APPE_SEQ_NBR") ) ));
			printAppendix( inst_sumidl, inst_sumidh, appe_inst_num );
			ApplicationMonitor.processedAppendices++;
		}
	}

	//====================================================================================
	private void printAppendix(int inst_sumidl, int inst_sumidh, long appe_inst_num) throws IOException, SQLException {
		out.write( tab3 + "<Appendix>" + newLine );		
		out.write( tab4 + "<Identifier>" + newLine );
		ExportHelper.formatFieldStr(tab5, "APPE_S_UMID",  ExportHelper.getSUMID( inst_sumidl, inst_sumidh ) );
		ExportHelper.formatFieldStr(tab5, "APPE_INST_NUM", "" + appe_inst_num );
		ExportHelper.formatFieldDate(appeRS, tab5, "APPE_DATE_TIME"); 
		
		String appeSeqNum= ExportHelper.defaultString( appeRS.getString("APPE_SEQ_NBR") );
		try{ 
			long appeSeqNumInt= Long.parseLong(appeSeqNum);
			if ( appeSeqNumInt > 2147483647 || appeSeqNumInt < 0  ){
				appeSeqNum= "0";
			}
		}catch( Exception e){}
		ExportHelper.formatFieldStr( tab5, "APPE_SEQ_NBR", appeSeqNum );
		
		out.write( tab5 + "<PassiveLockToken></PassiveLockToken>" + newLine );
		out.write( tab4 + "</Identifier>" + newLine );
		
		ExportHelper.formatFieldStr( appeRS, tab4, "APPE_IAPP_NAME" );
		ExportHelper.formatFieldStr( appeRS, tab4, "APPE_TYPE", 5 );
		ExportHelper.formatFieldStr( appeRS, tab4, "APPE_SESSION_HOLDER" );
		ExportHelper.formatFieldNum( appeRS, tab4, "APPE_SESSION_NBR",  4 );
		ExportHelper.formatFieldNum( appeRS, tab4, "APPE_SEQUENCE_NBR", 6 );
		ExportHelper.formatFieldStr( appeRS, tab4, "APPE_TRANSMISSION_NBR" );
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_CREA_APPL_SERV_NAME" );
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_CREA_MPFN_NAME" );
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_CREA_RP_NAME" );
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_CHECKSUM_RESULT", 5 );
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_CHECKSUM_VALUE" );
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_CONN_RESPONSE_CODE", 9 );
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_CONN_RESPONSE_TEXT" );
		
		String receiverDelStatus= MessageFormatter.format( appeRS, "APPE_RCV_DELIVERY_STATUS" );
		ExportHelper.formatOptionalFieldStr( tab4, "APPE_RCV_DELIVERY_STATUS", receiverDelStatus );
		
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_NETWORK_DELIVERY_STATUS", 4 );
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_ACK_NACK_TEXT" );
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_NAK_REASON" );
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_REMOTE_INPUT_REFERENCE" );
		ExportHelper.formatOptionalFieldDate( appeRS, tab4, "APPE_REMOTE_INPUT_TIME" );
		ExportHelper.formatOptionalFieldDate( appeRS, tab4, "APPE_LOCAL_OUTPUT_TIME" );
				
		if ( appeRS.getString("APPE_PKI_AUTH_VALUE" ) != null ){
			ExportHelper.formatFieldStr( appeRS, tab4, "APPE_PKI_AUTH_VALUE");
		}
		
		String PKIAuthorisationRes= MessageFormatter.format(appeRS, "APPE_PKI_AUTHORISATION_RES");
		
		if ( !ExportHelper.defaultString( appeRS.getString("APPE_PKI_AUTHORISATION_RES") ).isEmpty() ){
			ExportHelper.formatFieldStr( tab4, "PKI_APPE_COMBINED_AUTH_RES", PKIAuthorisationRes );
		}

		String PKIPac2Result= MessageFormatter.format(appeRS, "APPE_PKI_PAC2_RESULT");
		ExportHelper.formatOptionalFieldStr( tab4, "APPE_PKI_PAC2_RESULT", PKIPac2Result );
		ExportHelper.formatOptionalFieldStr( tab4, "APPE_PKI_AUTHORISATION_RES", PKIAuthorisationRes );
		
		String PKIAuthenticationRes= MessageFormatter.format(appeRS, "APPE_PKI_AUTHENTICATION_RES");
		ExportHelper.formatOptionalFieldStr( tab4, "APPE_PKI_AUTHENTICATION_RES", PKIAuthenticationRes);
		
		String combinedAuthRes= MessageFormatter.format(appeRS, "APPE_COMBINED_AUTH_RES");
		ExportHelper.formatOptionalFieldStr(  tab4, "APPE_COMBINED_AUTH_RES", combinedAuthRes );
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_COMBINED_PAC_RES", 5 );
		

		String RMACheckResult= MessageFormatter.format(appeRS, "APPE_RMA_CHECK_RESULT");
		ExportHelper.formatOptionalFieldStr( tab4, "APPE_RMA_CHECK_RESULT", RMACheckResult);
		
		ExportHelper.formatOptionalFieldStr( appeRS, tab4, "APPE_SENDER_SWIFT_ADDRESS" );
		out.write( tab3 + "</Appendix>" + newLine );
	}



	
}
