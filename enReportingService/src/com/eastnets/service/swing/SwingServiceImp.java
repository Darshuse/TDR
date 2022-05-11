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

package com.eastnets.service.swing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.swing.SwingDAO;
import com.eastnets.domain.swing.SwingEntity;
import com.eastnets.service.ServiceBaseImp;

/**
 * Swing Service Implementation
 * @author EastNets
 * @since July 11, 2012
 */
public class SwingServiceImp extends ServiceBaseImp implements SwingService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2962081188854890784L;
	private SwingDAO swingDAO;
	private CommonDAO commonDAO;
	
	@Override
	public SwingEntity viewMessageDetails(String loggedInUser,String statusId, String uniqueIdent) {
		swingDAO.viewMessageDetails(statusId, uniqueIdent);
		return swingDAO.viewMessageDetails(statusId, uniqueIdent);
	}
	
	@Override
	public  ArrayList<SwingEntity> doSearch(String loggedInUser, SwingEntity swing) {
		if(swing.getUserFin().getFinMsgType() != null && swing.getUserFin().getFinMsgType().length()>0 ){
			swing.getUserStatus().setCategory(swing.getUserFin().getFinMsgType().substring(0,1));
		}
		else
		{
			swing.getUserStatus().setCategory(null);
		}
		if (swing.getIsn() != null && swing.getOsn() != null && swing.getIsn().length()>0 && swing.getOsn().length()>0) {
			swing.getUserFin().setFinIsnOsn(swing.getIsn() + "/" + swing.getOsn());
		}
		if(swing.getMesgFormat()!=null && swing.getMesgFormat().equalsIgnoreCase("swift")){
			swing.getUserStatus().setStandard("FIN");
		}else{
			swing.getUserStatus().setStandard(swing.getMesgFormat());
		}
		Calendar cal = Calendar.getInstance();




		
		String preSubSQLF = "e.id = f.ruserstatus_id";
		String subSQLF = "";
		if(swing.getUserFin().getFinSwiftIO() != null && swing.getUserFin().getFinSwiftIO().length() > 0 && !swing.getUserFin().getFinSwiftIO().equals("%")){
			subSQLF += " and f.fin_swift_IO = '" + swing.getUserFin().getFinSwiftIO() + "'";			
		}			
		if(swing.getUserFin().getFinCorr() != null && swing.getUserFin().getFinCorr().length() > 0){
			swing.getUserFin().setFinOwnDist(swing.getUserFin().getFinCorr());
			subSQLF +=" and ( f.fin_corr = '" + swing.getUserFin().getFinCorr() + 
			"' or f.fin_own_dest = '" + swing.getUserFin().getFinOwnDist() + "')";			
		}
		if(swing.getUserFin().getFinMsgType() != null && swing.getUserFin().getFinMsgType().length() > 0){
			subSQLF += " and f.fin_mesg_type = '" + swing.getUserFin().getFinMsgType() + "'";			
		}		
		if(swing.getUserFin().getFinTrnRef() != null && swing.getUserFin().getFinTrnRef().length() > 0){
			subSQLF += " and f.fin_trn_ref = '" + swing.getUserFin().getFinTrnRef() + "'";			
		}	
		if(swing.getFromFinAmount() != null && swing.getToFinAmount() != null){
			subSQLF += " and f.fin_amnt between " + swing.getFromFinAmount() + " and " + swing.getToFinAmount();			
		}	
		if(swing.getUserFin().getFinCurr() != null && swing.getUserFin().getFinCurr().length() > 0){
			subSQLF += " and f.fin_curr = '" + swing.getUserFin().getFinCurr() + "'";			
		}		
		if(swing.getFromFinValueDate() != null && swing.getToFinValueDate()  !=null){
			cal.setTime(swing.getFromFinValueDate());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			String fromFinValueDateStr = getFormatedDateStr(cal);
			
			cal.setTime(swing.getToFinValueDate());
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			String toFinValueDateStr = getFormatedDateStr(cal);					
			subSQLF += 
			" and f.fin_value_date between " +
			"TO_DATE('" + fromFinValueDateStr + "', 'YYYYMMDD HH24:MI:SS') and " + 
			"TO_DATE('" + toFinValueDateStr + "', 'YYYYMMDD HH24:MI:SS')" ;			
		}
		if(swing.getUserFin().getFinIsnOsn() != null && swing.getUserFin().getFinIsnOsn().length() > 0){
			subSQLF += " and f.fin_isn_osn = '" + swing.getUserFin().getFinIsnOsn() + "'";			
		}	
		
		if(subSQLF!=null && subSQLF.trim().length()>0){
			subSQLF = preSubSQLF + subSQLF;
		}
		
		Date fromEAITimeStamp = swing.getFromEAITimeStamp();
		Date toEAITimeStamp = swing.getToEAITimeStamp();
		
		String subSQLE = "";
		if(swing.getUserStatus().getEaiUniqueIdentifier() != null && swing.getUserStatus().getEaiUniqueIdentifier().length() >0){
			subSQLE = "e.eai_unique_identifier = '" + swing.getUserStatus().getEaiUniqueIdentifier() + "'";
		}
		
		if(fromEAITimeStamp != null && toEAITimeStamp != null){
			cal.setTime(swing.getFromEAITimeStamp());
			cal.set(Calendar.HOUR_OF_DAY, new Integer(swing.getFromFinValueTimeHours()));
			cal.set(Calendar.MINUTE, new Integer(swing.getFromFinValueTimeMinutes()));
			cal.set(Calendar.SECOND, new Integer(swing.getFromFinValueTimeSeconds()));
			String fromEAITimeStampStr = getFormatedDateStr(cal);
			
			cal.setTime(swing.getToEAITimeStamp());
			cal.set(Calendar.HOUR_OF_DAY, new Integer(swing.getToFinValueTimeHours()));
			cal.set(Calendar.MINUTE, new Integer(swing.getToFinValueTimeMinutes()));
			cal.set(Calendar.SECOND, new Integer(swing.getToFinValueTimeSeconds()));
			String toEAITimeStampStr = getFormatedDateStr(cal);
			
			subSQLE += " and e.eai_time_stamp between TO_DATE('" + fromEAITimeStampStr + "','YYYYMMDD HH24:MI:SS') and " +
			"TO_DATE('" + toEAITimeStampStr + "','YYYYMMDD HH24:MI:SS')" ;	
		}
		if(swing.getUserStatus().getEaiStatus() != null && swing.getUserStatus().getEaiStatus().length() >0){
			subSQLE += " and e.eai_status = '" + swing.getUserStatus().getEaiStatus() + "'";
		}	
		if(swing.getUserStatus().getEaiOperator() != null && swing.getUserStatus().getEaiOperator().length() >0){
			subSQLE += " and e.eai_operator = '" + swing.getUserStatus().getEaiOperator() + "'";
		}	
		if(swing.getUserStatus().getEaiErrorCode() != null && swing.getUserStatus().getEaiErrorCode().length() >0){
			subSQLE += " and e.eai_error_code = '" + swing.getUserStatus().getEaiErrorCode() + "'";
		}			
		if(swing.getUserStatus().getEaiComment() != null && swing.getUserStatus().getEaiComment().length() >0){
			subSQLE += " and e.eai_comment = '" + swing.getUserStatus().getEaiComment() + "'";
		}	
		if(swing.getUserStatus().getEaiMessageSource() != null && swing.getUserStatus().getEaiMessageSource().length() >0){
			subSQLE += " and e.eai_message_source = '" + swing.getUserStatus().getEaiMessageSource() + "'";
		}	
		if(swing.getUserStatus().getStandard() != null && swing.getUserStatus().getStandard().length() >0 && !swing.getUserStatus().getStandard().equalsIgnoreCase("Any")){
			subSQLE += " and e.standard = '" + swing.getUserStatus().getStandard() + "'";
		}
		if(swing.getUserStatus().getCategory() != null && swing.getUserStatus().getCategory().length() >0){
			subSQLE += " and e.category = '" + swing.getUserStatus().getCategory() + "'";
		}
		if(subSQLE.startsWith(" and")){
			subSQLE = subSQLE.substring(5);
		}
		System.out.println("subSQLF: " + subSQLF);
		System.out.println("subSQLE: " + subSQLE);
//		subSQLF = "";
//		subSQLE = "e.eai_time_stamp between TO_DATE('20001128 00:00:00','YYYYMMDD HH24:MI:SS') and TO_DATE('20111128 23:59:59','YYYYMMDD HH24:MI:SS') and e.eai_operator = 'eai_operator'";
		return swingDAO.doSearch(subSQLF,subSQLE);
	} 
	
	public SwingDAO getSwingDAO() {
		return swingDAO;
	}

	public void setSwingDAO(SwingDAO swingDAO) {
		this.swingDAO = swingDAO;
	}

	public CommonDAO getCommonDAO() {
		return commonDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}
	
	private String getFormatedDateStr(Calendar calendar){
		String year = calendar.get(Calendar.YEAR) + "";
		String month = (calendar.get(Calendar.MONTH) + 1) + "";
		String day = calendar.get(Calendar.DATE) + "";
		String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
		String minute = calendar.get(Calendar.MINUTE) + "";
		String second = calendar.get(Calendar.SECOND) + "";
		String result = (year.length()==1?"0"+year:year)+(month.length()==1?"0"+month:month)+(day.length()==1?"0"+day:day)+ " " + (hour.length()==1?"0"+hour:hour)+ ":" + (minute.length()==1?"0"+minute:minute)+ ":" + (second.length()==1?"0"+second:second);
		
		return result;
	}
	
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		System.out.println(cal.get(Calendar.DATE));
		System.out.println(cal.get(Calendar.MONTH));
		System.out.println(cal.get(Calendar.YEAR));
		System.out.println(cal.get(Calendar.HOUR));
		System.out.println(cal.get(Calendar.MINUTE));
		System.out.println(cal.get(Calendar.SECOND));
		
	}

}
