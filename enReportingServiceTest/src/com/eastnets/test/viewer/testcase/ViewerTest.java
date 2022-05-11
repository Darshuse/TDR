/**
\ * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.test.viewer.testcase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.springframework.util.Assert;

import com.eastnets.domain.AdvancedDate;
import com.eastnets.domain.viewer.AppendixExtDetails;
import com.eastnets.domain.viewer.FieldSearchInfo;
import com.eastnets.domain.viewer.InstanceDetails;
import com.eastnets.domain.viewer.InstanceExtDetails;
import com.eastnets.domain.viewer.InterventionExtDetails;
import com.eastnets.domain.viewer.IntvAppe;
import com.eastnets.domain.viewer.SearchLookups;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.domain.viewer.ViewerSearchParam;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.test.BaseTest;

public class ViewerTest extends BaseTest {
	
	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -7787670849033247330L;
//
//	class MesgHdr{
//		Integer aid;
//		Integer umidl;
//		Integer umidh;
//		public java.sql.Date mesgCreaDateTime;
//	}
//	
//	MesgHdr getMesgHdr(){
//		ViewerSearchParam params = new ViewerSearchParam();
//		Calendar c = Calendar.getInstance();
//		c.set(2000, 1, 1);
//		
//		params.getCreationDate().setDate( c.getTime() );
//		params.getCreationDate().setSecondDate( new Date() );
//		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
//		params.setUmidFormat("Swift");
//		params.setContentSender("Any");
//		params.setContentReceiver("Any");
//		params.setContentNature("all");
//		params.setInterventionsNetworkName("Any");
//		params.setInterventionsFromToNetwork("Any");
//		params.setInstanceStatus("Any");
//		//test the search 
//		List<SearchResultEntity> result;
//		MesgHdr hdr = new MesgHdr();
//		try {
//			result = this.getServiceLocater().getViewerService().search(getLoggedInUser(), params, "", 1, 0 , null, true, true,1,true,true);
//		
//		Assert.notEmpty(result);
//		
//		
//		hdr.aid= result.get(0).getAid();
//		hdr.umidl= result.get(0).getMesgUmidl();
//		hdr.umidh= result.get(0).getMesgUmidh();
//		hdr.mesgCreaDateTime= result.get(0).getMesgCreaDateTimeOnDB();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return hdr;
//	}
//	
//	@Test
//	public void setDateFormat() {
//		this.getServiceLocater().getViewerService().setDateTimeFormats(getLoggedInUser(),
//				"yyyy/MM/dd HH:mm:ss",
//				"yyyy/MM/dd",
//				"HH:mm:ss");
//	}
//
//	/* */ 
//	
//	@Test
//	public void testGetViewerSearchLookups() {
//		SearchLookups searchLookups = this.getServiceLocater().getViewerService().getViewerSearchLookups(getLoggedInUser());
//		Assert.notNull(searchLookups);
//		Assert.notEmpty(searchLookups.getContentNature());
//		Assert.notEmpty(searchLookups.getSourceSearchFile());
//		Assert.notEmpty(searchLookups.getSourceAvailableSAA());
//		Assert.notEmpty(searchLookups.getUmidFormat());
//		Assert.notEmpty(searchLookups.getContentNature());
//		Assert.notEmpty(searchLookups.getQueuesAvilable());
//		Assert.notEmpty(searchLookups.getUnitsAvailable());		
//	}	
//	
//	@Test
//	public void testSearch(){
//		ViewerSearchParam params = new ViewerSearchParam();
//		Calendar c = Calendar.getInstance();
//		c.set(2010, 1, 1);
//		params.getCreationDate().setDate( c.getTime() );
//		params.getCreationDate().setSecondDate( new Date() );
//		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
//		params.setUmidFormat("Swift");
//		params.setContentSender("Any");
//		params.setContentReceiver("Any");
//		params.setContentNature("all");
//		params.setInterventionsNetworkName("Any");
//		params.setInterventionsFromToNetwork("Any");
//		params.setInstanceStatus("Any");
//		//test the search 
//		List<SearchResultEntity> result;
//		try {
//			result = this.getServiceLocater().getViewerService().search(getLoggedInUser(), params, "", 20, 0 , null, true, true,1,true,true);
//		
//		Assert.notEmpty(result);
//		
//		//now advanced search
//		List<FieldSearchInfo> fieldSearch = new ArrayList<FieldSearchInfo>();
//		fieldSearch.add( new FieldSearchInfo("32A", FieldSearchInfo.Condition.CONTAINS , "USD") );
//		result = this.getServiceLocater().getViewerService().search(getLoggedInUser(), params,"", 20, 0 , fieldSearch,true, true,1,true,true);
//		Assert.notEmpty(result);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	
//	@Test
//	public void testCount(){
//		ViewerSearchParam params = new ViewerSearchParam();
//		Calendar c = Calendar.getInstance();
//		c.set(2010, 1, 1);
//		params.getCreationDate().setDate( c.getTime() );
//		params.getCreationDate().setSecondDate( new Date() );
//		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
//		params.setUmidFormat("Swift");
//		params.setContentSender("Any");
//		params.setContentReceiver("Any");
//		params.setContentNature("all");
//		params.setInterventionsNetworkName("Any");
//		params.setInterventionsFromToNetwork("Any");
//		params.setInstanceStatus("Any");
//		
//		//test the casual count
//		Integer result = this.getServiceLocater().getViewerService().getMessagesCount(getLoggedInUser(), params, 0 , null, false, true,1,true,true);		
//		Assert.notNull(result);
//		Assert.isTrue(result != 0);
//		
//		//now advanced count
//		List<FieldSearchInfo> fieldSearch = new ArrayList<FieldSearchInfo>();
//		fieldSearch.add( new FieldSearchInfo("32A", FieldSearchInfo.Condition.DOES_NOT_CONTAIN , "USD") );
//		result = this.getServiceLocater().getViewerService().getMessagesCount(getLoggedInUser(), params, 0 , fieldSearch, false, true,1,true,true);
//		Assert.notNull(result);
//		Assert.isTrue( result != 0 );
//	}
//
//	@Test
//	public void testSearchHL(){
//		//get some message using the search
//		MesgHdr hdr= getMesgHdr();
//				
//		//now do the test		
//		List<SearchResultEntity> result = null;
//		try {
//			result = this.getServiceLocater().getViewerService().searchHL(getLoggedInUser(),hdr.umidl, hdr.umidh, 0 , getLoggedGroupId() );
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//		Assert.notEmpty(result);	
//	}
//	
//
//	@Test
//	public void testFormatNetworkStatus(){
//		 String result = this.getServiceLocater().getViewerService().formatNetworkStatus(getLoggedInUser(),"DLV_ACKED" );
//		 Assert.hasText(result);
//		 result = this.getServiceLocater().getViewerService().formatNetworkStatus(getLoggedInUser(),"DLV_REJECTED_LOCALLY" );
//		 Assert.hasText(result);
//		 Assert.isTrue( "Rejected Locally".equalsIgnoreCase( result ) );		
//	}
//	@Test
//	public void testMessageDetails() throws Exception
//	{
//		MesgHdr hdr= getMesgHdr();
//		
//		//now do the test
//		MessageDetails messageDetails= this.getServiceLocater().getViewerService().getMessageDetails(getLoggedInUser(),hdr.aid, hdr.umidl, hdr.umidh, hdr.mesgCreaDateTime, 0, true,false);
//		Assert.notNull(messageDetails);
//		Assert.hasText( messageDetails.getMesgType() );
//	}
//	@Test
//	public void testForceMessageUpdate()
//	{
//		MesgHdr hdr= getMesgHdr();
//		
//		this.getServiceLocater().getViewerService().forceMessageUpdate(getLoggedInUser(), hdr.aid, hdr.umidl,  hdr.umidh, hdr.mesgCreaDateTime );
//		Assert.isTrue(true);//just make sure the query executed without problems, it has no result to return
//	}
//	
//	@Test
//	public void testGetInstanceDetails() throws Exception
//	{
//		MesgHdr hdr= getMesgHdr();//hdr.aid, hdr.umidl, hdr.umidh
//	
//		MessageDetails messageDetails= this.getServiceLocater().getViewerService().getMessageDetails(getLoggedInUser(),hdr.aid, hdr.umidl, hdr.umidh, hdr.mesgCreaDateTime, 0, true,false);
//		Assert.notNull(messageDetails);
//	
//		//now do the test	
//		InstanceExtDetails instDetails= this.getServiceLocater().getViewerService().getInstanceDetails(getLoggedInUser(),hdr.aid, hdr.umidl, hdr.umidh, hdr.mesgCreaDateTime, messageDetails.getMesgInstances().get(0).getInstNum(), 0 );
//		Assert.notNull(instDetails);
//		Assert.notEmpty( instDetails.getInstIntvList() );
//	}
//	
//	@Test
//	public void testGetAppendixDetails() throws Exception 
//	{
//		//get some message using the search
//		ViewerSearchParam params = new ViewerSearchParam();
//		Calendar c = Calendar.getInstance();
//		c.set(2010, 1, 1);
//		params.getCreationDate().setDate( c.getTime() );
//		params.getCreationDate().setSecondDate( new Date() );
//		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
//		params.setUmidFormat("Swift");
//		params.setContentSender("Any");
//		params.setContentReceiver("Any");
//		params.setContentNature("all");
//		params.setInterventionsNetworkName("Any");
//		params.setInterventionsFromToNetwork("Any");
//		params.setInstanceStatus("Any");
//		List<SearchResultEntity> result = this.getServiceLocater().getViewerService().search(getLoggedInUser(), params, "", 50, 0 , null, true, true,1,true,true);
//		Assert.notEmpty(result);
//	
//		
//		for(SearchResultEntity r :  result ){
//			MessageDetails messageDetails= this.getServiceLocater().getViewerService().getMessageDetails(getLoggedInUser(),r.getAid(), r.getMesgUmidl(), r.getMesgUmidh(), r.getMesgCreaDateTimeOnDB(), 0,true,false);
//			Assert.notNull(messageDetails);
//			
//			
//			InstanceExtDetails instDetails= this.getServiceLocater().getViewerService().getInstanceDetails(getLoggedInUser(), result.get(0).getAid(),  result.get(0).getMesgUmidl(),  result.get(0).getMesgUmidh(),  result.get(0).getMesgCreaDateTimeOnDB(), messageDetails.getMesgInstances().get(0).getInstNum(), 0 );
//			Assert.notNull(instDetails);
//			Assert.notEmpty( instDetails.getInstIntvList() );
//			
//			for( IntvAppe appe : instDetails.getInstIntvList() ){
//				if( !appe.isIntervention() ){
//					AppendixExtDetails appeDetails = this.getServiceLocater().getViewerService().getAppendixDetails( getLoggedInUser(), 
//							appe.getAid(), appe.getUmidl(), appe.getUmidh(), messageDetails.getMesgCreaDateTimeOnDB(), appe.getInstNum(),
//							appe.getSeqNbr(), appe.getDate(), 0 );
//					
//
//					Assert.notNull(appeDetails);
//					Assert.hasText( appeDetails.getAppeType() );
//					return;
//				}
//			}
//			
//		}
//
//		Assert.isTrue(false, "no appendices in all messages??");	
//	}
//
//	@Test
//	public void testGetInterventionDetails() throws Exception 
//	{
//		//get some message using the search
//		ViewerSearchParam params = new ViewerSearchParam();
//		Calendar c = Calendar.getInstance();
//		c.set(2010, 1, 1);
//		params.getCreationDate().setDate( c.getTime() );
//		params.getCreationDate().setSecondDate( new Date() );
//		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
//		params.setUmidFormat("Swift");
//		params.setContentSender("Any");
//		params.setContentReceiver("Any");
//		params.setContentNature("all");
//		params.setInterventionsNetworkName("Any");
//		params.setInterventionsFromToNetwork("Any");
//		params.setInstanceStatus("Any");
//		List<SearchResultEntity> result = this.getServiceLocater().getViewerService().search(getLoggedInUser(), params, "", 50, 0 , null, true, true,1,true,true);
//		Assert.notEmpty(result);
//
//
//		for(SearchResultEntity r :  result ){
//			MessageDetails messageDetails= this.getServiceLocater().getViewerService().getMessageDetails(getLoggedInUser(),r.getAid(), r.getMesgUmidl(), r.getMesgUmidh(), r.getMesgCreaDateTimeOnDB(), 0, true,false);
//			Assert.notNull(messageDetails);
//
//
//			InstanceExtDetails instDetails= this.getServiceLocater().getViewerService().getInstanceDetails(getLoggedInUser(), result.get(0).getAid(),  result.get(0).getMesgUmidl(),  result.get(0).getMesgUmidh(), result.get(0).getMesgCreaDateTimeOnDB(), messageDetails.getMesgInstances().get(0).getInstNum(), 0 );
//			Assert.notNull(instDetails);
//			Assert.notEmpty( instDetails.getInstIntvList() );
//
//			for( IntvAppe appe : instDetails.getInstIntvList() ){
//				if( appe.isIntervention() ){
//					InterventionExtDetails intvDetails = this.getServiceLocater().getViewerService().getInterventionDetails( getLoggedInUser(), 
//							appe.getAid(), appe.getUmidl(), appe.getUmidh(), messageDetails.getMesgCreaDateTimeOnDB(), appe.getInstNum(),
//							appe.getSeqNbr(), appe.getDate(), 0 );
//
//
//					Assert.notNull(intvDetails);
//					Assert.hasText( intvDetails.getIntvIntyName() );
//					return;
//				}
//			}
//
//		}
//
//		Assert.isTrue(false, "no interventions in all messages??");	
//	}
//	
//	@Test
//	public void testFormatMPFNStatus() throws Exception{
//		MesgHdr hdr= getMesgHdr();//hdr.aid, hdr.umidl, hdr.umidh
//		MessageDetails messageDetails= this.getServiceLocater().getViewerService().getMessageDetails(getLoggedInUser(),hdr.aid, hdr.umidl, hdr.umidh, hdr.mesgCreaDateTime, 0, true,false);
//		Assert.notNull(messageDetails);
//		Assert.notEmpty( messageDetails.getMesgInstances() );
//		InstanceDetails instDetails= messageDetails.getMesgInstances().get(0);
//		
//		String format= this.getServiceLocater().getViewerService().formatMPFNStatus(getLoggedInUser(), hdr.aid, hdr.umidl, hdr.umidh, hdr.mesgCreaDateTime, instDetails.getInstNum() , instDetails.getInstStatus());
//		Assert.hasText(format);
//	}
//	
//	@Test
//	public void testFormatAuthResult(){		
//		
//		String result= this.getServiceLocater().getViewerService().formatAuthResult(getLoggedInUser(), "AUTH_SUCCESS");
//		Assert.isTrue( result.equalsIgnoreCase("Success") );
//		
//		result= this.getServiceLocater().getViewerService().formatAuthResult(getLoggedInUser(), "AUTH_SUCCESS_OLD_KEY");
//		Assert.isTrue( result.equalsIgnoreCase("Old Key") );		
//	}
//	
//
//	@Test
//	public void testFormatRmaCheckResult(){		
//		
//		String result= this.getServiceLocater().getViewerService().formatRmaCheckResult(getLoggedInUser(), "RMA_CHECK_SUCCESS");
//		Assert.isTrue( result.equalsIgnoreCase("Success") );
//		
//		result= this.getServiceLocater().getViewerService().formatRmaCheckResult(getLoggedInUser(), "RMA_CHECK_NOT_IN_VALID_PERIOD");
//		Assert.isTrue( result.equalsIgnoreCase("Invalid Period") );		
//	}
//	
//
//	@Test
//	public void testFormatAckSts(){		
//		String result= this.getServiceLocater().getViewerService().formatAckSts(getLoggedInUser(), "DLV_WAITING_ACK", "Network");
//		Assert.isTrue( result.equalsIgnoreCase("Waiting Network Ack") );
//		
//		result= this.getServiceLocater().getViewerService().formatAckSts(getLoggedInUser(), "DLV_REJECTED_LOCALLY", "Network");
//		Assert.isTrue( result.equalsIgnoreCase("Rejected Locally") );		
//	}
//	@Test
//	public void testMailMessages(){
//		ViewerSearchParam params = new ViewerSearchParam();
//		Calendar c = Calendar.getInstance();
//		c.set(2010, 1, 1);
//		params.getCreationDate().setDate( c.getTime() );
//		params.getCreationDate().setSecondDate( new Date() );
//		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
//		params.setUmidFormat("Swift");
//		params.setContentSender("Any");
//		params.setContentReceiver("Any");
//		params.setContentNature("all");
//		params.setInterventionsNetworkName("Any");
//		params.setInterventionsFromToNetwork("Any");
//		params.setInstanceStatus("Any");
//		//test the search 
//		List<SearchResultEntity> result;
//		try {
//			result = this.getServiceLocater().getViewerService().search(getLoggedInUser(), params, "", 5, 0 , null,true, true,1,true,true);
//
//			Assert.notEmpty(result);
//
//			this.getServiceLocater().getViewerService().mailMessages(getLoggedInUser(), "j-unit test", "salababneh@eastnets.com", result, true, true, 0 );} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	}
//	
//	@Test
//	public void testGetMessageExpandedText() throws Exception{
//		MesgHdr hdr= getMesgHdr();//hdr.aid, hdr.umidl, hdr.umidh
//		MessageDetails messageDetails= this.getServiceLocater().getViewerService().getMessageDetails(getLoggedInUser(),hdr.aid, hdr.umidl, hdr.umidh, hdr.mesgCreaDateTime, 0, true,false);
//		Assert.notNull(messageDetails);
//		
//		String result= "";
//		try {
//			result = this.getServiceLocater().getViewerService().getMessageExpandedText(getLoggedInUser(),  messageDetails,null,null);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		Assert.hasText( result );
//	}
//
//	@Test
//	public void testPrintMessages(){
//		ViewerSearchParam params = new ViewerSearchParam();
//		Calendar c = Calendar.getInstance();
//		c.set(2010, 1, 1);
//		params.getCreationDate().setDate( c.getTime() );
//		params.getCreationDate().setSecondDate( new Date() );
//		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
//		params.setUmidFormat("Swift");
//		params.setContentSender("Any");
//		params.setContentReceiver("Any");
//		params.setContentNature("all");
//		params.setInterventionsNetworkName("Any");
//		params.setInterventionsFromToNetwork("Any");
//		params.setInstanceStatus("Any");
//		//test the search 
//		List<SearchResultEntity> result;
//		try {
//			result = this.getServiceLocater().getViewerService().search(getLoggedInUser(), params, "", 5, 0 , null, true, true,1,true,true);
//		
//		Assert.notEmpty(result);
//		
//		String printVal= this.getServiceLocater().getViewerService().printMessages( getLoggedInUser(),  result, true, true, 0 );
//		Assert.hasText( printVal );
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//
//	@Test
//	public void testExportJRE(){
//		ViewerSearchParam params = new ViewerSearchParam();
//		Calendar c = Calendar.getInstance();
//		c.set(2010, 1, 1);
//		params.getCreationDate().setDate( c.getTime() );
//		params.getCreationDate().setSecondDate( new Date() );
//		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
//		params.setUmidFormat("Swift");
//		params.setContentSender("Any");
//		params.setContentReceiver("Any");
//		params.setContentNature("all");
//		params.setInterventionsNetworkName("Any");
//		params.setInterventionsFromToNetwork("Any");
//		params.setInstanceStatus("Any");
//		//test the search 
//		List<SearchResultEntity> result;
//		try {
//			result = this.getServiceLocater().getViewerService().search(getLoggedInUser(), params, "", 5, 0 , null, true, true,1,true,true);
//		
//		Assert.notEmpty(result);
//		
//		String printVal = null;
//		try {
//			printVal = this.getServiceLocater().getViewerService().exportRJE( getLoggedInUser(),  result, true, 0 );
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Assert.hasText( printVal );
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//	}
//	
//
//	@Test
//	public void testExportCSV(){
//		ViewerSearchParam params = new ViewerSearchParam();
//		Calendar c = Calendar.getInstance();
//		c.set(2010, 1, 1);
//		params.getCreationDate().setDate( c.getTime() );
//		params.getCreationDate().setSecondDate( new Date() );
//		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
//		params.setUmidFormat("Swift");
//		params.setContentSender("Any");
//		params.setContentReceiver("Any");
//		params.setContentNature("all");
//		params.setInterventionsNetworkName("Any");
//		params.setInterventionsFromToNetwork("Any");
//		params.setInstanceStatus("Any");
//		//test the search 
//		List<SearchResultEntity> result;
//		try {
//			result = this.getServiceLocater().getViewerService().search(getLoggedInUser(), params, "", 5, 0 , null, true, true,1,true,true);
//		
//		Assert.notEmpty(result);
//		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
//		String printVal = null;
//		try {
//			printVal = this.getServiceLocater().getViewerService().exportCSV( getLoggedInUser(),  result  ,  null,"\",");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Assert.hasText( printVal );
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	}
//	
//
//	@Test
//	public void testSettings(){
//		Random r = new Random();
//		String val= String.format("ViewerTest:%d", r.nextInt());//random that we can guarantee that we are reading and writing the same value each test 
//		this.getServiceLocater().getViewerService().setSetting(getLoggedInUser(), getLoggedInUserId(), "ViewerTest", val);		
//		String returnVal = this.getServiceLocater().getViewerService().getSetting(getLoggedInUser(), getLoggedInUserId(), "ViewerTest");
//		Assert.isTrue(val.equalsIgnoreCase(returnVal));
//		
//	}
//	/* */
//	
//	@Test
//	public void testIsFieldTagValid(){		
//		boolean returnVal = this.getServiceLocater().getViewerService().isFieldTagValid(getLoggedInUser(), "32A");
//		Assert.isTrue(returnVal);
//		returnVal = this.getServiceLocater().getViewerService().isFieldTagValid(getLoggedInUser(), "3EE");
//		Assert.isTrue(!returnVal);
//
//		Assert.isTrue(! this.getServiceLocater().getViewerService().isFieldTagValid( getLoggedInUser(), "ABCD") );	
//		
//	}
//	@Test
//	public void testGetMessageTypes(){
//		String stxVesrion=this.getServiceLocater().getViewerService().getLatestInstalledSyntaxVer("");
//		
//		Assert.isTrue(!stxVesrion.trim().isEmpty());
//		List<String> mesgTypes=this.getServiceLocater().getViewerService().getMessageTypes("",stxVesrion);
//		Assert.isTrue(!(mesgTypes.size()==0));
//		for(String str:mesgTypes){
//			System.out.println(str);
//		}
//	}
//	
//	/* * /
//	@Test 
//	public void testExpandMessages(){
//		ViewerSearchParam params = new ViewerSearchParam();
//		Calendar c = Calendar.getInstance();
//		c.set(2000, 1, 1);
//		//c.set(2013, 1, 1);
//		params.getCreationDate().setDate( c.getTime() );
//		params.getCreationDate().setSecondDate( new Date() );
//		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
//		c.set(2013, 3, 3, 18, 0, 0);
//		params.setUmidFormat("Swift");
//		params.setContentSender("Any");
//		params.setContentReceiver("Any");
//		params.setContentNature("all");
//		params.setInterventionsNetworkName("Any");
//		params.setInterventionsFromToNetwork("Any");
//		params.setInstanceStatus("Any");
//		//test the search 
//		List<SearchResultEntity> result = this.getServiceLocater().getViewerService().search(getLoggedInUser(), params, "", 1000, 0 , null);
//		Assert.notEmpty(result);
//
//		int index= 0 ;
//		try {
//			BufferedWriter errrrr = new BufferedWriter(new FileWriter(new File("d:\\test_expand\\errr3.txt"), false));
//			
//			for (SearchResultEntity s : result ){
//				index++;
//				String messageDetailsStr= "Message" + index + ": " + s.getMesgUmidl() +"" + s.getMesgUmidh() ;
//				
//				try{
//					MessageDetails messageDetails= this.getServiceLocater().getViewerService().getMessageDetails(getLoggedInUser(),s.getAid(), s.getMesgUmidl(), s.getMesgUmidh(), 0);
//					
//					String tst = messageDetails.getMesgType() + "-" + messageDetails.getMesgTrnRef()  + "-" + s.getMesgUmidl() +"" + s.getMesgUmidh();
//					
//					messageDetailsStr = "Message" + index + ": " +  tst + " ^^ " +  messageDetails.getMesgCreaDateTime().toString() +  " -------- ";
//					
//					tst= tst.replace('/', '_');
//					tst= tst.replace('\\', '_');
//					tst= tst.replace('*', '_');
//					tst= tst.replace(':', '_');
//					
//					BufferedWriter bw = new BufferedWriter(new FileWriter(new File("d:\\test_expand\\1000_8\\new_" + tst +".txt"), false));
//					messageDetailsStr += "1";
//					bw.write( messageDetails.getMesgType() + " / " + s.getMesgUmidl() +":" + s.getMesgUmidh()  );
//					bw.newLine();
//					bw.write(  messageDetails.getMesgUnExpandedText() );
//					bw.newLine();
//					bw.write(  " =============================================================================================" );
//					bw.newLine();
//					bw.flush();
//					messageDetailsStr += "2";
//					String txt= this.getServiceLocater().getViewerService().getMessageExpandedText(getLoggedInUser(),  messageDetails);
//					messageDetailsStr += "3";
//					
//					if (txt == null || txt.isEmpty() || txt.startsWith("Message Expand Failed")) {
//						errrrr.write( messageDetailsStr  );
//						errrrr.newLine();
//						errrrr.write( "\t\t:" + txt );
//						errrrr.newLine();
//						errrrr.flush();
//					}
//					bw.write( txt );
//					
//					bw.close();
//				}
//				catch(Exception e)
//				{
//					errrrr.write( messageDetailsStr  );
//					errrrr.newLine();
//					errrrr.flush();
//				}
//	
//			}
//			errrrr.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		
//	}/*	* /
//	
//	@Test 
//	public void testExpandMessage(){
//		ViewerSearchParam params = new ViewerSearchParam();
//		Calendar c = Calendar.getInstance();
//		c.set(2010, 2, 1, 0, 0, 0);
//		params.getCreationDate().setDate( c.getTime() );
//		params.getCreationDate().setSecondDate( new Date() );
//		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
//		c.set(2013, 2, 28, 0, 0, 0);
//		params.setUmidFormat("Swift");
//		params.setContentSender("Any");
//		params.setContentReceiver("Any");
//		params.setContentNature("all");
//		params.setInterventionsNetworkName("Any");
//		params.setInterventionsFromToNetwork("Any");
//		params.setInstanceStatus("Any");
//		params.setUmidType("321");
//		//test the search 
//		List<SearchResultEntity> result = this.getServiceLocater().getViewerService().search(getLoggedInUser(), params, "", 3, 0 , null);
//		Assert.notEmpty(result);
//
//		int index= 0 ;
//		for (SearchResultEntity s : result ){
//			index++;
//			try{
//				MessageDetails messageDetails= this.getServiceLocater().getViewerService().getMessageDetails(getLoggedInUser(),s.getAid(), s.getMesgUmidl(), s.getMesgUmidh(), 0);
//			
//				BufferedWriter bw = new BufferedWriter(new FileWriter(new File("d:\\test_expand\\new_" + messageDetails.getMesgType() + "-" + messageDetails.getMesgTrnRef()  + "-" + s.getMesgUmidl() +"" + s.getMesgUmidh() +".txt"), false));
//				
//				bw.write( messageDetails.getMesgType() + " / " + s.getMesgUmidl() +":" + s.getMesgUmidh()  );
//				bw.newLine();
//				bw.write(  ApplicationUtils.convertClob2String(messageDetails.getTextDataBlock()) );
//				bw.newLine();
//				bw.write(  " =============================================================================================" );
//				bw.newLine();
//				bw.flush();
//				String txt= this.getServiceLocater().getViewerService().getMessageExpandedText(getLoggedInUser(),  messageDetails);
//				bw.write( txt );
//				
//				bw.close();
//			}
//			catch(Exception e)
//			{
//				System.out.println("Message" + index + ": " + s.getMesgUmidl() +"" + s.getMesgUmidh() + " was not handled." );
//			}
//
//		}
//		
//	}/**/
}
