package com.eastnets.test.helper;
 
import com.eastnets.domain.AdvancedDate; 
import com.eastnets.domain.viewer.DaynamicMsgDetailsParam;
import com.eastnets.domain.viewer.DaynamicViewerParam;
import com.eastnets.domain.viewer.ViewerSearchParam; 
import java.util.Calendar;
import java.util.Date; 

public class ViewerUtility {


	public static  DaynamicViewerParam buildSearchParam(String frmtName,String trnRef, String loggedInUser ,Integer listMax  ){
		DaynamicViewerParam paramSearchMethod=new DaynamicViewerParam();
		paramSearchMethod.setLoggedInUser(loggedInUser);
		paramSearchMethod.setAddColums(true);
		paramSearchMethod.setParams(preperMsgSearchCriteriaParam(frmtName,trnRef));  
		paramSearchMethod.setListFilter("");
		paramSearchMethod.setListMax(listMax);
		paramSearchMethod.setTimeZoneOffset(0);
		paramSearchMethod.setFieldSearch(null);
		paramSearchMethod.setAddColums(true);
		paramSearchMethod.setShowInternalMessages(true);
		paramSearchMethod.setTextDecompostionType(1);
		paramSearchMethod.setIncludeSysMessages(true);
		paramSearchMethod.setEnableUnicodeSearch(false);
		paramSearchMethod.setEnableGpiSearch(false);
		paramSearchMethod.setDecimalAmountFormat(null);
		paramSearchMethod.setThousandAmountFormat(null);
		return paramSearchMethod;
	} 
	
 
	public static  DaynamicMsgDetailsParam buildLaodMsgParam( String loggedInUser ,int aid, int umidl,int umidh,java.sql.Date mesg_crea_date_str){ 
		Calendar c = Calendar.getInstance();
		c.set(2000, 1, 1);
		Date mesg_crea_date=c.getTime();  
		DaynamicMsgDetailsParam daynamicMsgDetailsParam=new DaynamicMsgDetailsParam();
		daynamicMsgDetailsParam.setLoggedInUser(loggedInUser);
		daynamicMsgDetailsParam.setAid(aid);
		daynamicMsgDetailsParam.setUmidh(umidh);
		daynamicMsgDetailsParam.setUmidl(umidl);
		daynamicMsgDetailsParam.setMesg_crea_date(mesg_crea_date);
		daynamicMsgDetailsParam.setTimeZoneOffset(0);
		daynamicMsgDetailsParam.setIncludeHistory(true);
		daynamicMsgDetailsParam.setIncludeMessageNotes(false);
		return daynamicMsgDetailsParam; 
	} 
	
	
	


	private static ViewerSearchParam  preperMsgSearchCriteriaParam(String frmtName,String trnRef){
		ViewerSearchParam params = new ViewerSearchParam(); 
		Calendar c = Calendar.getInstance();
		c.set(2000, 1, 1);
		params.getCreationDate().setDate( c.getTime() );
		params.getCreationDate().setSecondDate( new Date() );
		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
		params.setUmidFormat(frmtName); 
		params.setUmidReference(trnRef);
		params.setContentSender("Any");
		params.setContentReceiver("Any");
		params.setContentNature("all"); 
		params.setInterventionsNetworkName("Any");
		params.setInterventionsFromToNetwork("Any");
		params.setEmiNetworkDeliveryStatus("Any");
		params.setInstanceStatus("Any"); 
		return params;
	}


}
