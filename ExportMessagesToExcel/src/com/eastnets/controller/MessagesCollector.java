package com.eastnets.controller;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.eastnets.beans.MessageField;
import com.eastnets.beans.MessageKey;
import com.eastnets.beans.SearchCriteria;
import com.eastnets.dao.Connecter;
import com.eastnets.dao.Queries;


public class MessagesCollector {
	
	
	public void connectToDB() throws Exception {
		Connecter.connect();
	}
	
	
	public void disConnectToDB() throws Exception {
		Connecter.disconnect();
	}
	
	
	public Map<MessageKey, List<MessageField>> getMessages(SearchCriteria searchCriteriaObj) throws Exception {
		
		Map<MessageKey, List<MessageField>> messages = new HashMap<MessageKey, List<MessageField>>();
		Object[] queryParameters = new Object[] { searchCriteriaObj.getFromDate() + " 00:00:00", searchCriteriaObj.getToDate() + " 23:00:00",
				searchCriteriaObj.getxOwnLt()};

		//Connect to Database
		connectToDB();
		
		ResultSet rs = Connecter.exceuteSlctQuery(Queries.GET_MESSAGES_ORACLE_QUERY, queryParameters);
		
		if (rs != null) {			
			messages =  setResult(rs);			
		}//if
		
		disConnectToDB();		
		return messages;
	}
	
	
	public Map<MessageKey, List<MessageField>> setResult(ResultSet rs) throws Exception {
		
		Map<MessageKey, List<MessageField>> messages = new HashMap<MessageKey, List<MessageField>>();
		MessageField messageField;
		MessageKey messageKey;
		List<MessageField> messageFields;
		
		while (rs.next()) {
			
			messageKey = new MessageKey();
			messageField = new MessageField();
			
			//set message key
			setMessageKey(rs, messageKey);
			
			//set message field
			setMessageField(rs, messageField);
			
			messageFields = messages.get(messageKey);
			
			if(messageFields == null) {
				messageFields = new ArrayList<MessageField>();
			} 

			messageFields.add(messageField);
			messages.put(messageKey, messageFields);
			
		}//while
		
		return messages;
	}


	private void setMessageKey(ResultSet rs, MessageKey messageKey) throws Exception {
				
		messageKey.setAid(rs.getString("AID"));
		messageKey.setMesgUmidh(rs.getString("MESG_S_UMIDH"));
		messageKey.setMesgUmidl(rs.getString("MESG_S_UMIDL"));
	}


	private void setMessageField(ResultSet rs, MessageField messageField) throws Exception {
				
		messageField.setAid(rs.getString("AID"));
		messageField.setMesgUmidh(rs.getString("MESG_S_UMIDH"));
		messageField.setMesgUmidl(rs.getString("MESG_S_UMIDL"));
		messageField.setAppeSeqNumber(rs.getString("APPE_SEQ_NBR"));
		messageField.setCreationDate(rs.getString("MESG_CREA_DATE_TIME"));
		messageField.setFieldCode(rs.getString("FIELD_CODE"));
		messageField.setFieldOption(rs.getString("FIELD_OPTION"));
		messageField.setReceiverX1(rs.getString("X_RECEIVER_X1"));
		messageField.setValue(rs.getString("VALUE"));
	}

	
}
