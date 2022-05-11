/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.test.watchdog.testcase;

import org.junit.Test;
import org.springframework.util.Assert;

import com.eastnets.domain.watchdog.WDGeneralSettings;
import com.eastnets.domain.watchdog.WDNotificationSettings;
import com.eastnets.service.watchdog.WatchDogService;
import com.eastnets.test.BaseTest;

public class WatchDogTest extends BaseTest {
	
/**
	 * 
	 */
	private static final long serialVersionUID = -8608317043659351020L;
	/*
	@Test
	public void testInsertMessageRequest() throws InstantiationException, IllegalAccessException {
		MessageRequest request = (MessageRequest)MockFactory.getIntance(MessageRequest.class);
		System.out.println(this.getServiceLocater().getWatchDogService().insertMessageRequest(getLoggedInUser(),request));
//    	Assert.notNull(this.getServiceLocater().getWatchDogService().insertMessageRequest(profile,request));
	}	
    @Test
    public void testListMessageNotifications() throws InstantiationException, IllegalAccessException{
    	Profile profile = (Profile)MockFactory.getIntance(Profile.class);
    	UserSettings settings = this.getServiceLocater().getCommonService().getUserSettings(profile);
    	String str_wdNbDayHistory = settings.getWdNbDayHistory();
    	int wdNbDayHistory = 0;
    	if(str_wdNbDayHistory != null){
    		wdNbDayHistory = Integer.parseInt(str_wdNbDayHistory);
    	}
    	System.out.println("Msg.size:-" + this.getServiceLocater().getWatchDogService().listMessageNotifications(profile,wdNbDayHistory, false).size());
    	Assert.notNull(this.getServiceLocater().getWatchDogService().listMessageNotifications(profile,wdNbDayHistory, false));
    }
    

    @Test
    public void testListEventNotifications() throws InstantiationException, IllegalAccessException{
    	Profile profile = (Profile)MockFactory.getIntance(Profile.class);
    	System.out.println(this.getServiceLocater().getCommonService());
    	System.out.println("Evt.size:- "+ this.getServiceLocater().getWatchDogService().listEventNotifications(profile,99, false).size());
    	UserSettings settings = this.getServiceLocater().getCommonService().getUserSettings(profile);
    	String str_wdNbDayHistory = settings.getWdNbDayHistory();
    	int wdNbDayHistory = 0;
    	if(str_wdNbDayHistory != null){
    		wdNbDayHistory = Integer.parseInt(str_wdNbDayHistory);
    	}
    	Assert.notNull(this.getServiceLocater().getWatchDogService().listEventNotifications(profile,wdNbDayHistory, false));
    }    
    
    @Test
    public void testListMessageRequests() throws InstantiationException, IllegalAccessException{
    	Profile profile = (Profile)MockFactory.getIntance(Profile.class);
    	System.out.println("Msg Req.size:-" + this.getServiceLocater().getWatchDogService().listMessageRequests(profile, false).size());
    	ArrayList<MessageRequest> params = new ArrayList<MessageRequest>(); 
    	params.addAll(this.getServiceLocater().getWatchDogService().listMessageRequests(profile, false));
    	for(MessageRequest param:params){
    		System.out.println("*** " + param.getDescription());
    	}
    	Assert.notNull(this.getServiceLocater().getWatchDogService().listMessageRequests(profile, false));
    }
    
    @Test
    public void testListEventRequests() throws InstantiationException, IllegalAccessException{
    	Profile profile = (Profile)MockFactory.getIntance(Profile.class);
    	System.out.println("Evt Req.size:- "+ this.getServiceLocater().getWatchDogService().listEventRequests(profile, false).size());
    	Assert.notNull(this.getServiceLocater().getWatchDogService().listEventRequests(profile, false));
    }     
    
//    @Test
//    public void testDeleteNotification() throws InstantiationException, IllegalAccessException{
//    	Profile profile = (Profile)MockFactory.getIntance(Profile.class);
//    	List<>
//    	this.getServiceLocater().getWatchDogService().deleteMessageRequest(profile, 3);
//    }   
    
    @Test
    public void testDeleteMessageRequest() throws InstantiationException, IllegalAccessException{
    	Profile profile = (Profile)MockFactory.getIntance(Profile.class);
    	MessageRequest req = (MessageRequest)MockFactory.getIntance(MessageRequest.class);
    	List<MessageRequest> reqs = new ArrayList<MessageRequest>();
    	reqs.add(req);
    	this.getServiceLocater().getWatchDogService().deleteMessageRequest(profile, reqs);
    }

    @Test
    public void testListEventJrnlComponents() throws InstantiationException, IllegalAccessException{
    	System.out.println(this.getServiceLocater().getWatchDogService().listEventJrnlComponents(getLoggedInUser()));
    }     */   
 /*   
    @Test
    public void testPrintInstanceTransmission() throws InstantiationException, IllegalAccessException{
    	Profile profile = (Profile)MockFactory.getIntance(Profile.class);
    	Message msg = new Message();
    	msg.setAid(0);
    	msg.setUmidl(846236888);
    	msg.setUmidh(-228);
    	this.getServiceLocater().getWatchDogService().getMsgDetails(profile,msg);
    	Clob clb = msg.getMessageHeader().getText_data_block();
    	String stringClob = null;
    	try {
    	long i = 1;
    	int clobLength = (int) clb.length();
    	stringClob = clb.getSubString(i, clobLength);
    	}
    	catch (Exception e) {
    	System.out.println(e);
    	}
    	System.out.println("#$#$#$#: " + stringClob);
    	System.out.println(msg.getAppe_ack_nack_text());
    	System.out.println(msg.getAppe_iapp_name());
    	System.out.println(msg.getAppe_nak_reason());
    	System.out.println(msg.getAppe_network_delivery_status());
    	System.out.println(msg.getAppe_remote_input_reference());
    	System.out.println(msg.getAppe_session_holder());
    	System.out.println(msg.getAppe_type());
    	System.out.println(msg.getAppe_date_time());
    	System.out.println(msg.getAppe_local_output_time());
    	System.out.println(msg.getAppe_remote_input_time());
    	System.out.println(msg.getAppe_sequence_nbr());
    	System.out.println(msg.getAppe_session_nbr());
    	System.out.println(msg.getFirst_inst_type());
    	System.out.println(msg.getLast_inst_notification_type());
    	System.out.println(msg.getLast_inst_num());
    	System.out.println(msg.getLast_inst_type());
    	System.out.println(msg.getMesg_network_priority());
    	System.out.println(msg.getMesg_sub_format());
    	System.out.println(msg.getText_swift_block_5());
    	System.out.println("##" + msg.getMessageHeader().getMesg_crea_date_time() + "#");
    	
    	System.out.println(msg.getMessageHeader().getInst_receiver_X1());
    	System.out.println(msg.getMessageHeader().getMesg_class());
    	System.out.println(msg.getMessageHeader().getMesg_copy_service_id());
    	System.out.println(msg.getMessageHeader().getMesg_crea_appl_serv_name());
    	System.out.println(msg.getMessageHeader().getMesg_crea_date_time());
    	System.out.println(msg.getMessageHeader().getMesg_crea_mpfn_name());
    	System.out.println(msg.getMessageHeader().getMesg_frmt_name());
    	System.out.println(msg.getMessageHeader().getMesg_Identifier());
    	System.out.println(msg.getMessageHeader().getMesg_nature());
    	System.out.println(msg.getMessageHeader().getMesg_network_appl_ind());
    	System.out.println(msg.getMessageHeader().getMesg_network_priority());
    	System.out.println(msg.getMessageHeader().getMesg_possible_dup_creation());
    	System.out.println(msg.getMessageHeader().getMesg_receiver_alia_name());
    	System.out.println(msg.getMessageHeader().getMesg_rel_trn_ref());
    	System.out.println(msg.getMessageHeader().getMesg_requestor_dn());
    	System.out.println(msg.getMessageHeader().getMesg_sender_corr_type());
    	System.out.println(msg.getMessageHeader().getMesg_sender_swift_address());
    	System.out.println(msg.getMessageHeader().getMesg_sender_X1());
    	System.out.println(msg.getMessageHeader().getMesg_sender_x2());
    	System.out.println(msg.getMessageHeader().getMesg_sender_x3());
    	System.out.println(msg.getMessageHeader().getMesg_sender_x4());
    	System.out.println(msg.getMessageHeader().getMesg_service());
    	System.out.println(msg.getMessageHeader().getMesg_sub_format());
    	System.out.println(msg.getMessageHeader().getMesg_syntax_table_ver());
    	System.out.println(msg.getMessageHeader().getMesg_template_name());
    	System.out.println(msg.getMessageHeader().getMesg_trn_ref());
    	System.out.println(msg.getMessageHeader().getMesg_type());
    	System.out.println(msg.getMessageHeader().getMesg_user_group());
    	System.out.println(msg.getMessageHeader().getMesg_user_priority_code());
    	System.out.println(msg.getMessageHeader().getMesg_user_reference_text());
    	System.out.println(msg.getMessageHeader().getMesg_uumid_suffix());
    	System.out.println(msg.getMessageHeader().getMesg_validation_passed());
    	System.out.println(msg.getMessageHeader().getMesg_validation_requested());

    	
    	
    	
    }     
    @Test
    public void testInsertEventRequest() throws InstantiationException, IllegalAccessException {
    	EventRequest request = (EventRequest)MockFactory.getIntance(EventRequest.class);
    	Profile profile = (Profile)MockFactory.getIntance(Profile.class);
    	System.out.println("****** eventSearchParam: " + request.getDescription());
    	System.out.println(this.getServiceLocater().getWatchDogService().insertEventRequest(profile,request));
    	Assert.notNull(this.getServiceLocater().getWatchDogService().insertEventRequest(profile,request));
    }    
    @Test
    public void testPrintEventDetails() throws InstantiationException, IllegalAccessException {
    	EventNotification event = new EventNotification();
    	event = this.getServiceLocater().getWatchDogService().printEvent(0, 832962831, 2.147483584E9);
    	Assert.notNull(event);
//    	EventRequest request = (EventRequest)MockFactory.getIntance(EventRequest.class);
//    	Profile profile = (Profile)MockFactory.getIntance(Profile.class);
//    	System.out.println("****** eventSearchParam: " + request.getDescription());
//    	System.out.println(this.getServiceLocater().getWatchDogService().insertEventRequest(profile,request));
//    	Assert.notNull(this.getServiceLocater().getWatchDogService().insertEventRequest(profile,request));
    }    
 */
    /*
    @Test
    public void testAddDefaultGeneralSetting(){
    	
    	WatchDogService watchDogService = this.getServiceLocater().getWatchDogService();
    	String loggedInUser = this.getLoggedInUser();
    	
    	WDGeneralSettings generalSettings = watchDogService.getGeneralSettings(loggedInUser);
    	if(generalSettings == null){
    		watchDogService.addDefaultGeneralSettings(loggedInUser);
    	}
    }
    @Test
    public void testAddDefaultNotificationSetting(){
    	
    	WatchDogService watchDogService = this.getServiceLocater().getWatchDogService();
    	String loggedInUser = this.getLoggedInUser();
    	
    	WDNotificationSettings notificationSettings = watchDogService.getNotificationSettings(loggedInUser);
    	if(notificationSettings ==null){
    		watchDogService.addDefaultNotificationSettings(loggedInUser);
    	}
    }

    
	 
    @Test
    public void testUpdateGeneralSettings(){
    
    	WatchDogService watchDogService = this.getServiceLocater().getWatchDogService();
    	String loggedInUser = this.getLoggedInUser();
    	
    	WDGeneralSettings wdGeneralSettings = new WDGeneralSettings();
		wdGeneralSettings.setHideAnnotatedNotifications(false);
		wdGeneralSettings.setNotificationHistory(30L);
		wdGeneralSettings.setRefreshRate(10L);
		wdGeneralSettings.setVisibleNotification(false);
		
		wdGeneralSettings.setUserNotification("#195949");
		wdGeneralSettings.setAnnotatedSystemNotification("#195949");
		wdGeneralSettings.setNewSystemNotification("#195949");
  
		watchDogService.updateGeneralSettings(loggedInUser, wdGeneralSettings);
    }
    
    @Test
    public void testUpdateNotificationSettings(){
    	WatchDogService watchDogService = this.getServiceLocater().getWatchDogService();
    	String loggedInUser = this.getLoggedInUser();
    	
    	WDNotificationSettings wdNotificationSettings = new WDNotificationSettings();
    	
    	wdNotificationSettings.setShowCalculatedDuplicates(false);
		wdNotificationSettings.setShowEventNotifications(false);
		wdNotificationSettings.setShowIsnGaps(false);
		wdNotificationSettings.setShowMessageNotifications(false);
		wdNotificationSettings.setShowNakedMesages(false);
		wdNotificationSettings.setShowOsnGaps(false);
		wdNotificationSettings.setShowPossibleDuplicates(false);
		
		watchDogService.updateNotificationSettings(loggedInUser, wdNotificationSettings);
    }
    
*/
	@Test
	public void testGetGeneralSettings(){
		
		WatchDogService watchDogService = this.getServiceLocater().getWatchDogService();
		String loggedInUser = this.getLoggedInUser();
		Long userId = this.getLoggedInUserId();
		
		WDGeneralSettings wdGeneralSettings = watchDogService.getWDSettings(loggedInUser,userId);
		Assert.notNull(wdGeneralSettings);
				
		System.out.println(wdGeneralSettings.getMsgNotificationHistory());
		System.out.println(wdGeneralSettings.getMsgGapsNotificationHistory());
		System.out.println(wdGeneralSettings.getEventNotificationHistory());
		System.out.println(wdGeneralSettings.isHideAnnotatedNotifications());
		
	}
	
	@Test
	public void testGetNotificationSettings(){
		WatchDogService watchDogService = this.getServiceLocater().getWatchDogService();
		String loggedInUser = this.getLoggedInUser();
		Long userId = this.getLoggedInUserId();
		
		WDNotificationSettings wdNotificationSettings = watchDogService.getNotificationSettings(loggedInUser, userId);
		Assert.notNull(wdNotificationSettings);
		System.out.println(wdNotificationSettings.isShowCalculatedDuplicates());
		System.out.println(wdNotificationSettings.isShowEventNotifications());
		System.out.println(wdNotificationSettings.isShowMessageNotifications());
		System.out.println(wdNotificationSettings.isShowNakedMesages());
		System.out.println(wdNotificationSettings.isShowPossibleDuplicates());
		System.out.println(wdNotificationSettings.isShowIsnGaps());
		System.out.println(wdNotificationSettings.isShowOsnGaps());
		
	}
    @Test
    public void testBatee(){
    	Assert.isTrue(true);
    }
}
