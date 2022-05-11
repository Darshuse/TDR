package com.eastnets.dbconsistencycheck.app;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.eastnets.application.BaseApp;
import com.eastnets.dao.dbconsistencycheck.MessageDetails;
import com.eastnets.domain.viewer.SearchResultEntity;

public class ProcessDBCheckThread extends BaseApp implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DBConsistencyCheckApp app;	
	private DBConsistencyCheckConfig appConfigBean;
	private SimpleDateFormat dateFormat ;

	private ReportInfoBean infoBean;


	private MailReport mailReport = new MailReport();

	// bicList : list to store BIC(s) from SLICENSEDBIC table to check the BIC of Missing Message Licensed or not -- only on mode 1
	private List <String> bicList= new ArrayList<String>();

	// request & response buffer
	private Socket socket;

	private SocketAddress socketAddress ;

	public SocketAddress getSocketAddress() {
		return socketAddress;
	}

	public void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

	private DataOutputStream requestBuffer;

	private DataInputStream responseBuffer;

	private String request;
	private String response;

	// Request Argument to SAXS
	final static String  argument_mode1 = "0000200109"; // Data Length >> atoi(000020) = 20, Date Type >> atoi(0109) = 109,  Case >> MSG_CHECK_MESSAGE_REQUEST
	final static String  argument_mode2 = "0000200300"; // Data Length >> atoi(000020) = 20, Date Type >> atoi(0300) = 300,  Case >> MSG_CHECK_Exit_MESSAGE_REQUEST

	// detectList : list to store SUMID for ok messages
	private List <String> detectList = new ArrayList<String>();

	// missingList : list to store SUMID for missing messages 
	private List <String> missingList= new ArrayList<String>();

	// outofDateList : list to store SUMID for out of date (need to update) messages -- only on mode 1
	private List <String> outofDateList= new ArrayList<String>();

	// number of number to send report
	private int	numSendReport =1;

	// used to check if mx licensed or not
	private	boolean mxLicensed = false;
	// used to check if file licensed or not
	private boolean fileLicensed = false;



	public ReportInfoBean getInfoBean() {
		return infoBean;
	}

	public void setInfoBean(ReportInfoBean infoBean) {
		this.infoBean = infoBean;
	}

	public boolean isFileLicensed() {
		return fileLicensed;
	}

	public void setFileLicensed(boolean fileLicensed) {
		this.fileLicensed = fileLicensed;
	}

	public boolean isMxLicensed() {
		return mxLicensed;
	}

	public void setMxLicensed(boolean mxLicensed) {
		this.mxLicensed = mxLicensed;
	}

	private MessageDetails mesgDetail;

	// mesgList : list to store SUMID from rmesg table to check the messages exist in SAA or not -- only on mode 2
	private List <String> mesgList= new ArrayList<String>();

	Logger log = Logger.getLogger("DBConsistencyCheck");


	// HTML Constant
	private final static String title ="en.TDR - Database Consistency Check Report";
	private final static String imgSrc = "\"http://www.eastnets.com/img/logo-eastnets.jpg\"";
	private final static String bodyStyle = "body{font-family: \"Helvetica Neue]\", Helvetica, Arial, sans-serif;font-size: 14px;width: 99%;}";
	private final static String headerStyle = ".page-header{padding-bottom: 9px;margin: 20px 0 30px;border-bottom: 1px solid #004882;padding-top: 1%;padding-left: 10px;left: 1%;color:#004882;}";
	private final static String summaryStyle = "#Summary{padding-left: 10px; color:#004882;}"; 
	private final static String thStyle = "th{background-color: #004882;color:white;border-style: outset;font-size:12px;border-width: 1px;}";
	private final static String tableStyle = "table {max-width: 100%;background-color: transparent;border-collapse: collapse;border-spacing: 0;}";
	private final static String tdStyle = "#tdSpecific{border-width: 1px;border-style: inset;border-color: black;font-size:11px;white-space: nowrap;}";
	private final static String reportDateStyle = "#columnRight{float: right;width: 200px;font-size:12px}";
	private final static String footerStyle = "#footer{padding-right: 2px;padding-left: 10px; overflow-x:auto;}";
	private final static String textareaStyle = "textarea {overflow: auto;vertical-align: top;border-style: groove;border-top: 0;border-bottom: 0;background-color : #F3F3F3;width: 100%;-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;}";
	private final static String hrStyle = "hr {border: 0;border-top: 1px solid #eeeeee;border-bottom: 1px solid #ffffff;}";
	private final static String mesglistStyle = "#mesgList{text-align:center;}";

	public ProcessDBCheckThread(DBConsistencyCheckApp dbCheck, DBConsistencyCheckConfig appConfig, ReportInfoBean infoBean){
		this.app=dbCheck;
		this.appConfigBean = appConfig;
		this.infoBean = infoBean;
	}

	@Override
	public void run() {

		// to store result of each process
		boolean result = false;
		// flag to star cycle
		boolean startCycle = true;
		// to store start time & end time
		Time startTime, endTime;
		// to store difference between start time & end time
		long difference;
		try{

			if (appConfigBean.getMode() == 2){
				// check Traffic License or not { MX, File }
				log.info("check MX traffic licensed or not");
				mxLicensed = app.getServiceLocater().getDbCheckService().checkMXLicense();

				log.info("check File traffic licensed or not");
				fileLicensed = app.getServiceLocater().getDbCheckService().checkFILELicense();

			}else if (appConfigBean.getMode() == 1){

				// get Licensed BIC(s) from SLICENSEDBIC Table
				log.info("Get licensed BIC(s) ");
				bicList=app.getServiceLocater().getDbCheckService().getBIC();
			}

			// get active time to compare with current time 
			SimpleDateFormat timeformat = new SimpleDateFormat("kk:mm:ss");
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

			// init socket address
			socketAddress = new InetSocketAddress(appConfigBean.getSAAServer(), appConfigBean.getSAAPort());

			//Calendar currentTime, activeTime, sleepTime;
			Calendar currentTime, sleepTime;
			
			//activeTime = Calendar.getInstance();
			sleepTime = Calendar.getInstance();	
			sleepTime.set(Calendar.HOUR_OF_DAY,appConfigBean.getSleepTime().get(Calendar.HOUR_OF_DAY));
			sleepTime.set(Calendar.MINUTE,appConfigBean.getSleepTime().get(Calendar.MINUTE));
			sleepTime.set(Calendar.SECOND,appConfigBean.getSleepTime().get(Calendar.SECOND));
			
			//activeTime.getTime().setTime(timeformat.parse(timeformat.format( new Date())).getTime());
			//activeTime.set(Calendar.HOUR_OF_DAY,timeformat.parse(appConfigBean.getActiveTime()).getHours());
			//activeTime.set(Calendar.MINUTE,timeformat.parse(appConfigBean.getActiveTime()).getMinutes());
			//activeTime.set(Calendar.SECOND,timeformat.parse(appConfigBean.getActiveTime()).getSeconds());


			
			while(startCycle == true){

				currentTime = Calendar.getInstance();
				
				// add one day in case of  sleep time before current time 
				if (dateformat.parse(dateformat.format(sleepTime.getTime())).before( dateformat.parse(dateformat.format(currentTime.getTime())))) 
					sleepTime.add(Calendar.DATE, 1);

				// check current time before sleep time
				if ((currentTime.getTime().before(sleepTime.getTime())) || (currentTime.getTime().after(sleepTime.getTime()) && currentTime.getTime().getTime() > (sleepTime.getTime().getTime() +  appConfigBean.getFrequency() * 3600000)) ){
					// generate file name
					dateFormat =getDateFormat("yyyy_MM_dd_HH_mm_ss");
					appConfigBean.setReportFile("");
					appConfigBean.setReportFile(appConfigBean.getReportPath()+"_"+dateFormat.format(new Date())+ ".html");

					// cash start time process
					startTime = new Time(timeformat.parse(timeformat.format(new Date())).getTime());

					log.info("Start to check at [" + timeformat.format(startTime) +"]");

					// start process service
					result = processDBConsistencyCheck(appConfigBean);

					// cash end time process
					endTime = new Time(timeformat.parse(timeformat.format(new Date())).getTime());

					log.info("End of check at [" + timeformat.format(endTime) +"]");

					if (!result)
					{
						log.error("Processed Failed ");
						startCycle = false;

					}else{
						log.info("Processed Successfully ");

						currentTime.setTime(new Date());
						currentTime.getTime().setTime(timeformat.parse(timeformat.format(new Date())).getTime());

						// check current time before end time
						if ((currentTime.getTime().before(sleepTime.getTime())) || (currentTime.getTime().after(sleepTime.getTime()) && currentTime.getTime().getTime() > (sleepTime.getTime().getTime() +  appConfigBean.getFrequency() * 3600000)) ){

							// get difference time between end process and start process
							difference = endTime.getTime() - startTime.getTime();

							// check difference time < frequency time then 
							if (difference < appConfigBean.getFrequency() * 3600000){
								long sleepPeriod = (appConfigBean.getFrequency() * 3600000) - difference ;
								log.info("Current time is not exceed the frequency for the target process, start time :[" + timeformat.format(startTime) +"], end time :["+ timeformat.format(endTime) + "], sleep period :[" + Math.round(sleepPeriod * 0.0000166667)+"] minutes");
								log.info("Go to sleep ... ");
								Thread.sleep((appConfigBean.getFrequency()* 3600000) - difference);
							}
						}
					}
				}else{
					long exceedPeriod = currentTime.getTime().getTime() - sleepTime.getTime().getTime();
					long sleepPeriod = (appConfigBean.getSleepPeriod() * 3600000) - exceedPeriod ;
					log.info("Current time is exceed the sleep time for the target process, current time :[" +timeformat.format(currentTime.getTime()) +"], sleep time :[" +timeformat.format(sleepTime.getTime())+"], sleep period :[" + Math.round( sleepPeriod * 0.0000166667 )+"] minutes");
					log.info("Go to sleep ...");
					if (sleepPeriod > 0)
					Thread.sleep(sleepPeriod);
				}
			}

			System.out.println("Press Enter to exit ");
			System.in.read();
			System.exit(0);	


		}catch(Exception e){
			log.error(e.getMessage());
		}		
	}

	public void connect (DBConsistencyCheckConfig appConfigBean){

		try {

			log.info("Connect to SAA ["+appConfigBean.getSAAServer()+"] with SAXS Port ["+appConfigBean.getSAAPort()+"]" );
			socket = new Socket();

			socket.connect(socketAddress);	

		} catch (Exception e) {
			log.error("Connection failed, reason : "+ e.getMessage() );

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e1) {
				log.error(e1.getMessage() );
			}

			connect (appConfigBean);
		}
	}


	public boolean processDBConsistencyCheck( DBConsistencyCheckConfig  config){

		int result = 0;

		if (config.getMode() == 1)
			result = processMode1(config);
		else if (config.getMode() == 2)
			result = processMode2(config);

		if (result == 0)
			return false;

		// return 2 flag to send the report by Email
		if ((result == 2 && config.getReportFrequency() != 0 && numSendReport <= config.getReportFrequency())|| (result == 2 && config.getReportFrequency() == 0)){
			result = mailReport.sendReport(config, app , infoBean, false);

			if (result == 0)
				return false;	

			if (config.getReportFrequency() != 0)
				numSendReport++;
		}

		return true;
	}

	public int processMode1( DBConsistencyCheckConfig  config ) {

		// initial SUMID to start check cycle
		String checkSUMID = "FFFFFFFFFFFFFFFF";

		// initial status 
		int status =1;

		// to store SUMID which got from SAXS Response
		String sumid= "";

		// to store SAA token sum which got from SAXS Response
		long saa_Tokensum = 0;

		// to store DB token sum which got from DB
		long db_Tokensum = 0;

		// to store BIC-8 which gor from SAXS Response
		String biccode = "";

		int umidl,umidh;

		// to store number of messages checked
		infoBean.setCountMsg(0);

		infoBean.setCountOkMsg(0);

		// to store number of messages that status missing
		infoBean.setCountMissingMsg(0);

		// to store number of messages that status out of date
		infoBean.setCountOutOfDateMsg(0);

		boolean result =false;

		try 
		{
			// init request & response
			result =initSocket(socket);

			// connect to SAXS
			connect (appConfigBean);

			if(!result)
				//error init
				return 0;




			while(status == 1){

				log.info("Processing message <"+ sumid + ">");

				if (socket != null && requestBuffer != null && responseBuffer != null) {

					// send request 
					request=argument_mode1+checkSUMID;
					result = sendRequest(request); 
					if(!result)
						return 0;

				}else
					return 0;

				// receive response from SAXS
				response =recieveResponse();
				if(response == null)
					return 0;

				// to get status of SAXS response 
				status =  Integer.parseInt(response.substring(11, 12)) ;

				// check the status is equal 0 (end of message ) to exit loop
				if(status == 0)
					break;

				// increase number of message
				infoBean.setCountMsg(infoBean.getCountMsg() + 1);

				// to get SUMID from SAXS response 
				sumid = response.substring(12, 28);
				checkSUMID = sumid;

				// convert SUMID to UMIDl & UMIDH
				umidl = (int)Long.parseLong( StringUtils.substring( sumid, 0, 8 ), 16 );
				umidh = (int)Long.parseLong( StringUtils.substring( sumid, 8 ), 16 );

				// to get SAA Token Sum from SAXS response 
				saa_Tokensum = Integer.parseInt(response.substring(43,44));

				// to get BIC-8 from SAXS response
				biccode= response.substring(44,52);

				log.info("get token sum from database ");

				// get DB Token Sum from Database
				db_Tokensum = app.getServiceLocater().getDbCheckService().getTokenSum(config.getAid(), umidl, umidh);

				// check SAA Token Sum with DB Token Sum is equal the message status is ok if not the message status is missing or out of date 
				if (db_Tokensum == saa_Tokensum){

					detectList.add(sumid);
					log.info("SAA token sum : [" + saa_Tokensum + "],\t database token sum :["+db_Tokensum+"],\t the message is exist");
					infoBean.setCountOkMsg(infoBean.getCountOkMsg() + 1);


				}else{
					// check BIC License
					if (biccode.trim().isEmpty() || bicList.contains(biccode)){
						// if db_Tokensum is equal 0 the message is missing else the message is out of date
						if (db_Tokensum ==0){
							missingList.add(sumid);
							log.info("SAA token sum : [" + saa_Tokensum + "],\t database token sum : ["+db_Tokensum+"],\t the message is missing");				
							infoBean.setCountMissingMsg(infoBean.getCountMissingMsg() + 1);

						}else{
							outofDateList.add(sumid);
							log.info("SAA token sum : [" + saa_Tokensum + "], \t database token sum : ["+db_Tokensum+"], \t the message is out of date");
							infoBean.setCountOutOfDateMsg(infoBean.getCountOutOfDateMsg() + 1);
						}

						// check args parameter if exist -forceUpdate to insert record in ldRequestUpdate table to get update for the message 
						if(config.isForceUpdate() == true){
							log.info("get force update for the message");
							app.getServiceLocater().getDbCheckService().updateMessage(config.getAid(), umidl, umidh);
						}
					}
				}
			}

			// Close Request & Response Buffer
			result =closedSockets();
			if(!result)
				return 0;


			if ( infoBean.getCountOutOfDateMsg() > 0 || infoBean.getCountMissingMsg() > 0 ){
				// generate  report
				result = this.generateReport(config.getReportFile(),1 ,config.getAid());
				if(!result)
					return 0;

				return 2;
			}

			return 1;
		}catch(Exception e) 
		{ 
			log.error(e.getMessage());
			return 0;
		}
	}

	public int processMode2(DBConsistencyCheckConfig  config) {

		// to store number of messages checked
		infoBean.setCountMsg(0);

		// to store number of messages that status ok
		infoBean.setCountOkMsg(0);

		// to store number of messages that status missing
		infoBean.setCountMissingMsg(0);

		// to store number of messages that status out of date
		infoBean.setCountOutOfDateMsg(0);

		// to store process result
		boolean result =false;

		try 
		{
			Calendar today = Calendar.getInstance();
			today.add(Calendar.DAY_OF_MONTH, - config.getDayNum());	
			dateFormat =getDateFormat("dd-MMM-yy 00:00:00");
			infoBean.setStartCheck(dateFormat.format(new Date (today.getTimeInMillis())));

			today = Calendar.getInstance();
			dateFormat =getDateFormat("dd-MMM-yy 23:59:59");
			infoBean.setEndCheck(dateFormat.format(new Date (today.getTimeInMillis())));

			log.info("get list of messages from database ");
			// get list of messages from rmesg table
			mesgList = app.getServiceLocater().getDbCheckService().getMessages(config.getAid(), infoBean.getStartCheck(), infoBean.getEndCheck(), mxLicensed, fileLicensed);

			Iterator<String> itrSUMID = mesgList.iterator();

			// connect to SAXS
			connect (appConfigBean);

			// init request & response
			result =initSocket(socket);
			if(!result)
				return 0;
			

			while(itrSUMID.hasNext()){

				String checkSUMID = (String) itrSUMID.next();
				log.info("Processing Message < "+ checkSUMID + " > ");

				// increase number of message
				infoBean.setCountMsg(infoBean.getCountMsg() + 1);

				if (socket != null && requestBuffer != null && responseBuffer != null) {

					// send request 
					request=argument_mode2+checkSUMID;
					result = sendRequest(request); 
					if(!result)
						return 0;

				}else
					return 0;


				// receive response from SAXS
				response =recieveResponse();
				if(response == null)
					return 0;


				// check if the response is equal Exist then the message is exist in SAA and the message status is ok else the message is missing
				if(response.trim().equalsIgnoreCase("Exist")){
					detectList.add(checkSUMID);			
					log.info("Message is Exist");
					infoBean.setCountOkMsg(infoBean.getCountOkMsg() + 1);
				}else{
					missingList.add(checkSUMID);
					log.info("Message is Missing");
					infoBean.setCountMissingMsg(infoBean.getCountMissingMsg() + 1);
				}	
			}

			// closed request & response buffer
			result =closedSockets();
			if(!result)
				return 0;

			// generate  report
			result =this.generateReport(config.getReportFile(),2,config.getAid());
			if(!result)
				return 0;

			if (infoBean.getCountMissingMsg()  > 0)
				return 2;

			return 1;
		}
		catch(Exception e){
			log.error(e.getMessage());
			return 0;
		}
	}

	public boolean closedSockets() {
		try {
			log.debug("close socket, request and response buffers");

			requestBuffer.close();

			responseBuffer.close();

			socket.close();

			return true;
		} catch (IOException e) {
			log.error("Failed to close socket, request and response buffers, reason : "+ e.getMessage());
			return false;
		}
	}

	public String recieveResponse() {
		byte []responseLine = new byte[100] ;
		try {
			log.debug("Recieve Response From SAXS");
			responseBuffer.read(responseLine);
			return new String(responseLine, "UTF-8");

		} catch (IOException e) {
			log.error("Failed to Recieve Response From SAXS, reason : "+ e.getMessage());
			return null;
		}
	}

	public boolean sendRequest(String buffer) {
		try {
			log.debug("Send Request to SAXS");
			requestBuffer.writeBytes(buffer);
			return true;

		} catch (IOException e) {
			log.error("Failed to send Request to SAXS, reason : "+e.getMessage());
			return false;
		}   

	}


	public boolean initSocket(Socket socket) {

		try{
			log.debug("Initialize request and response buffers");

			requestBuffer = new DataOutputStream(socket.getOutputStream());

			responseBuffer = new DataInputStream(socket.getInputStream());

			return true;

		}catch(Exception e){
			log.error("Failed to Initialize request and response buffers, reason : "+ e.getMessage());
			return false;
		}


	}



	public boolean generateReport(String fileName, int mode, int aid) {
		String bgColor = "";
		try{

			log.info("Generate report ");

			if(infoBean.getCountMissingMsg() > 0){
				infoBean.setReportStatus("Inconsistent");
				bgColor = "\"ff0000\"";
			}else if(infoBean.getCountOutOfDateMsg() > 0){
				infoBean.setReportStatus("Out of Date Messages");
				bgColor = "\"ff0000\"";
			}else if(infoBean.getCountOkMsg() >0){
				infoBean.setReportStatus("Consistent");
				bgColor = "\"00ff00\"";
			}else{
				infoBean.setReportStatus("Nothing to Check");
				bgColor = "\"ffffff\"";
			}

			// generate html file
			File fileGenerated = new File(fileName);
			// generate HTML Content
			BufferedWriter htmlContent = new BufferedWriter(new FileWriter(fileGenerated));

			htmlContent.write("<html>");
			htmlContent.write("<head>");
			htmlContent.write("<style>");
			generateCSS(htmlContent);
			htmlContent.write("</style>");
			htmlContent.write("</head>");
			htmlContent.write("<body>");

			htmlContent.write("<div class=\"page-header\">");
			htmlContent.write("<h1>"+title);
			htmlContent.write("<img src="+imgSrc+"align=\"right\"/>");
			htmlContent.write("</h1>");
			htmlContent.write("</div>");

			htmlContent.write("<hr/>");

			dateFormat =getDateFormat("dd-MMM-yyyy HH:mm:ss");
			
			infoBean.setReportDate(dateFormat.format(new java.util.Date()));
			
			htmlContent.write("<div id=\"columnRight\">"+"Report date : "+infoBean.getReportDate()+"</div>");

			htmlContent.write("<div id=\"Summary\">");
			htmlContent.write("<h3>Summary</h3>");

			htmlContent.write("<table>");

			if (mode == 2){
				// generate Check Period
				htmlContent.write("<tr>");
				htmlContent.write("<td style=\"font-size:13px\"> DB check period </td>");
				htmlContent.write("<td>:</td>");
				htmlContent.write("<td style=\"font-size:12px\">"+" Start : "+infoBean.getStartCheck()+"</td>");
				htmlContent.write("<td width= 10%></td>");
				htmlContent.write("<td style=\"font-size:12px\">"+" End : "+ infoBean.getEndCheck()+"</td>");
				htmlContent.write("</tr>");
			}

			// generate Report Status Row
			htmlContent.write("<tr>");
			htmlContent.write("<td style=\"font-size:13px\">Status</td>");
			htmlContent.write("<td>:</td>");
			htmlContent.write("<td bgColor="+bgColor+" style=\"font-size:12px\">"+infoBean.getReportStatus()+"</td>");
			htmlContent.write("</tr>");

			if (mode == 2){

				// generate Number Of Message Row
				generateGlobalDetails(htmlContent, "Number of messages in Alliance",String.valueOf(infoBean.getCountOkMsg()));

				// generate Number Of OK Message Row
				generateGlobalDetails(htmlContent, "Number of messages in en.TDR ",String.valueOf(infoBean.getCountMsg()));

			}else if (mode == 1){

				// generate Number Of Message Row
				generateGlobalDetails(htmlContent, "Number of messages in Alliance",String.valueOf(infoBean.getCountMsg()));

				// generate Number Of OK Message Row
				generateGlobalDetails(htmlContent, "Number of messages in en.TDR ", String.valueOf(infoBean.getCountOkMsg()));

			}

			// generate Number Of Missing Message Row
			generateGlobalDetails(htmlContent, "Number of missing messages ",String.valueOf(infoBean.getCountMissingMsg()));

			if (mode == 1){
				// generate Number Of Out of Date Message Row
				generateGlobalDetails(htmlContent, "Number of out of date messages ", String.valueOf(infoBean.getCountOutOfDateMsg()));	
			}

			htmlContent.write("</table>");
			htmlContent.write("</div>");


			htmlContent.write("<hr/>");
			htmlContent.write("<div id=\"footer\">");

			if ( infoBean.getCountOkMsg() > 0 && infoBean.getCountMissingMsg() == 0 && infoBean.getCountOutOfDateMsg() == 0)
				htmlContent.write("<h4><FONT COLOR=\"#004882\">"+"No inconsistency found"+"</FONT></h4>");

			/*
			// generate OK Message List
			if (numberofOKMessage > 0 ){

				generateTable(htmlContent,"OK Message List :");

				htmlContent.write("<table style="+tableStyle+">");
				generateHeader(htmlContent, mode);

				for (int i =0; i<numberofOKMessage; i++){
					htmlContent.write("<tr>");
					htmlContent.write("<td style="+tdStyle+">"+detectList.get(i)+"</td>");
					if (mode == 2){
						mesgDetail = dbconsistencycheckdao.getMessageDetails(aid, detectList.get(i));
						generateDoc(htmlContent);	
					}

					htmlContent.write("</tr>");
				}
				htmlContent.write("</table>");
				htmlContent.write("<hr>");
			}
			 */

			// generate Missing Message List
			if (infoBean.getCountMissingMsg() > 0){

				if (mode == 2)
					htmlContent.write("<h4><FONT COLOR=\"#004882\">"+"Missing messages list (present in en.TDR but not in Alliance Access)"+"</FONT></h4>");
				else
					htmlContent.write("<h4><FONT COLOR=\"#004882\">"+"Missing messages list (present in Alliance Access but not in en.TDR)"+"</FONT></h4>");

				htmlContent.write("<table id=\"mesgList\">");
				generateHeader(htmlContent,mode);

				for (int i =0; i< infoBean.getCountMissingMsg(); i++){
					htmlContent.write("<tr>");
					htmlContent.write("<td id=\"tdSpecific\" style=\"width:120px;\">"+missingList.get(i)+"</td>");
					if (mode == 2){
						mesgDetail = app.getServiceLocater().getDbCheckService().getMessageDetails(aid, missingList.get(i));
						generateDoc(htmlContent);
					}
					htmlContent.write("</tr>");

					if (mode == 2 && mesgDetail.getMesg_frmt_name().equalsIgnoreCase("Swift")){

						int umidl,umidh;
						// convert SUMID to UMIDl & UMIDH
						
						umidl = (int)Long.parseLong( StringUtils.substring( missingList.get(i), 0, 8 ), 16 );
						umidh = (int)Long.parseLong( StringUtils.substring( missingList.get(i), 8 ), 16 );

						SimpleDateFormat datetimeformat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
						java.util.Date date = datetimeformat.parse(mesgDetail.getMesg_crea_date_time());
						java.sql.Date mesg_crea_date_time = new java.sql.Date(date.getTime());

						String text_block =  generateRJE(aid, umidl, umidh, mesg_crea_date_time);

						if (text_block != null && !text_block.isEmpty()){
							htmlContent.write("<tr>");
							htmlContent.write("<td colspan= 12>"+ "<textarea readonly rows=\"5\">"+text_block +"</textarea></td>");
							htmlContent.write("</tr>");
						}						
					}
				} 
				htmlContent.write("</table>");
				htmlContent.write("</div>");
				htmlContent.write("<hr/>");
			}

			// generate Out of Date Message List
			if ( infoBean.getCountOutOfDateMsg() > 0 && mode == 1){
				htmlContent.write("<h4><FONT COLOR=\"#004882\">"+"Out of date messages list :"+"</FONT></h4>");

				htmlContent.write("<table id=\"mesgList\">");
				generateHeader(htmlContent,mode);

				for (int i =0; i< infoBean.getCountOutOfDateMsg() ; i++){
					htmlContent.write("<tr>");
					htmlContent.write("<td id=\"tdSpecific\" style=\"width:120px;\">"+outofDateList.get(i)+"</td>");
					htmlContent.write("</tr>");
				}
				htmlContent.write("</table>");
				htmlContent.write("</div>");

				htmlContent.write("<hr/>");
			}

			// end of HTML Content
			htmlContent.write("</body>");
			htmlContent.write("</html>");

			htmlContent.close();
			return true;
		}
		catch(Exception e){
			log.error("Failed to generate the report, reason : "+ e.getMessage());
			return false;
		}

	}


	public String generateRJE(int aid, int umidl, int umidh, Date mesg_crea_date_time) throws InterruptedException{

		SearchResultEntity resultEntity = new SearchResultEntity() ;
		List < SearchResultEntity > resultEntityList =new ArrayList <SearchResultEntity>();

		resultEntity.setAid(aid);
		resultEntity.setMesgUmidh(umidh);
		resultEntity.setMesgUmidl(umidl);
		resultEntity.setMesgCreaDateTime((java.sql.Date) mesg_crea_date_time);
		resultEntityList.add(resultEntity);

		return app.getServiceLocater().getViewerService().exportRJE("",resultEntityList,true,0,true);
	}



	public void generateDoc(BufferedWriter htmlContent){
		try {
			htmlContent.write("<td id=\"tdSpecific\" style=\"width:120px;\">"+mesgDetail.getMesg_crea_date_time()+"</td>"); // crea_date_time
			htmlContent.write("<td id=\"tdSpecific\" style=\"width:100px;\">"+mesgDetail.getMesg_type()+"</td>"); // type
			htmlContent.write("<td id=\"tdSpecific\" style=\"width:90px;\">"+mesgDetail.getMesg_frmt_name()+"</td>"); // frmt_name
			htmlContent.write("<td id=\"tdSpecific\" style=\"width:70px;\">"+mesgDetail.getMesg_sub_format()+"</td>"); // dir
			htmlContent.write("<td id=\"tdSpecific\" style=\"width:100px;\" >"+mesgDetail.getMesg_sender()+"</td>"); // sender
			htmlContent.write("<td id=\"tdSpecific\" style=\"width:100px;\">"+mesgDetail.getMesg_receiver()+"</td>"); // receiver
			htmlContent.write("<td id=\"tdSpecific\" style=\"width:120px;\">"+mesgDetail.getTrn_ref()+"</td>"); // trn_ref
			htmlContent.write("<td id=\"tdSpecific\" style=\"width:170px;\">"+mesgDetail.getMesg_amount()+"</td>"); // amount
			htmlContent.write("<td id=\"tdSpecific\" style=\"width:70px;\">"+mesgDetail.getMesg_currency()+"</td>"); // currency
			htmlContent.write("<td id=\"tdSpecific\" style=\"width:90px;\">"+mesgDetail.getValueDate()+"</td>"); // val_date
			htmlContent.write("<td id=\"tdSpecific\" style=\"width:150px;\">"+mesgDetail.getMesg_createdby()+"</td>"); // create_by

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public void generateCSS(BufferedWriter htmlContent){
		try {			
			htmlContent.write(bodyStyle);
			htmlContent.write(headerStyle);
			htmlContent.write(summaryStyle);
			htmlContent.write(thStyle);
			htmlContent.write(tableStyle);
			htmlContent.write(tdStyle);
			htmlContent.write(reportDateStyle);
			htmlContent.write(footerStyle);
			htmlContent.write(textareaStyle);
			htmlContent.write(hrStyle);
			htmlContent.write(mesglistStyle);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}


	public void generateGlobalDetails(BufferedWriter htmlContent , String fieldName, String fieldValue){
		try {
			htmlContent.write("<tr>");
			htmlContent.write("<td style=\"font-size:13px\">"+fieldName+"</td>");
			htmlContent.write("<td>:</td>");
			htmlContent.write("<td style=\"font-size:12px\">"+fieldValue+"</td>");
			htmlContent.write("</tr>");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void generateHeader(BufferedWriter htmlContent, int mode){
		try {

			htmlContent.write("<tr>");
			htmlContent.write("<th>SUMID</th>");
			if(mode == 2){
				htmlContent.write("<th>Creation Date Time</th>");
				htmlContent.write("<th>Type</th>");
				htmlContent.write("<th>Format Name</th>");
				htmlContent.write("<th>Direction</th>");
				htmlContent.write("<th>Sender</th>");
				htmlContent.write("<th>Receiver</th>");
				htmlContent.write("<th>Reference</th>");
				htmlContent.write("<th>Amount</th>");
				htmlContent.write("<th>Currency</th>");
				htmlContent.write("<th>Value Date</th>");
				htmlContent.write("<th>Created By</th>");
			}
			htmlContent.write("</tr>");			

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}










	// Getter & Setter

	public DataOutputStream getRequestBuffer() {
		return requestBuffer;
	}

	public void setRequestBuffer(DataOutputStream requestBuffer) {
		this.requestBuffer = requestBuffer;
	}


	public DataInputStream getResponseBuffer() {
		return responseBuffer;
	}

	public void setResponseBuffer(DataInputStream responseBuffer) {
		this.responseBuffer = responseBuffer;
	}

	public List<String> getBicList() {
		return bicList;
	}

	public void setBicList(List<String> bicList) {
		this.bicList = bicList;
	}

	public DBConsistencyCheckApp getApp() {
		return app;
	}

	public void setApp(DBConsistencyCheckApp app) {
		this.app = app;
	}

	public DBConsistencyCheckConfig getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(DBConsistencyCheckConfig appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

	public SimpleDateFormat getDateFormat(String format) {
		return dateFormat = new SimpleDateFormat(format);
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public static String getArgumentMode1() {
		return argument_mode1;
	}

	public static String getArgumentMode2() {
		return argument_mode2;
	}

	public List<String> getDetectList() {
		return detectList;
	}

	public void setDetectList(List<String> detectList) {
		this.detectList = detectList;
	}

	public List<String> getMissingList() {
		return missingList;
	}

	public void setMissingList(List<String> missingList) {
		this.missingList = missingList;
	}

	public List<String> getOutofDateList() {
		return outofDateList;
	}

	public void setOutofDateList(List<String> outofDateList) {
		this.outofDateList = outofDateList;
	}

	public MessageDetails getMesgDetail() {
		return mesgDetail;
	}

	public void setMesgDetail(MessageDetails mesgDetail) {
		this.mesgDetail = mesgDetail;
	}

	public static String getTitle() {
		return title;
	}

	public static String getImgsrc() {
		return imgSrc;
	}

	public static String getBodystyle() {
		return bodyStyle;
	}

	public static String getHeaderstyle() {
		return headerStyle;
	}

	public static String getSummarystyle() {
		return summaryStyle;
	}

	public static String getThstyle() {
		return thStyle;
	}

	public static String getTablestyle() {
		return tableStyle;
	}

	public static String getTdstyle() {
		return tdStyle;
	}

	public static String getReportdatestyle() {
		return reportDateStyle;
	}

	public static String getFooterstyle() {
		return footerStyle;
	}

	public static String getTextareastyle() {
		return textareaStyle;
	}

	public static String getHrstyle() {
		return hrStyle;
	}

	public static String getMesgliststyle() {
		return mesglistStyle;
	}

	public List<String> getMesgList() {
		return mesgList;
	}

	public void setMesgList(List<String> mesgList) {
		this.mesgList = mesgList;
	}

	public MailReport getMailReport() {
		return mailReport;
	}

	public void setMailReport(MailReport mailReport) {
		this.mailReport = mailReport;
	}

	public int getNumSendReport() {
		return numSendReport;
	}

	public void setNumSendReport(int numSendReport) {
		this.numSendReport = numSendReport;
	}

}
