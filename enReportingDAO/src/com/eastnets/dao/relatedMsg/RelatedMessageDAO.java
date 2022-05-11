package com.eastnets.dao.relatedMsg;

import java.util.Date;
import java.util.List;

import com.eastnets.domain.relatedMessage.RelatedMessage;
import com.eastnets.domain.viewer.MessageNote;

public interface RelatedMessageDAO {
	public List<List<RelatedMessage>>  getAllRealatedMsg(String user,Date fromDate,Date toDate,String references,int limit,String uter );
	public void logQuery(String loggedInUser, String query);
	public List<String> getAlltransactionReference(String wildCardRef,String user,Date fromDate, Date toDate);
	public List<MessageNote> getMessageNote(int aid,int umdh,int umdl);
	public List<String> getUserNameById(Long id);
 	public void SpecifyNumberOfRelatedMessage(List<List<RelatedMessage>> relMsgList,int limit);
	public List<RelatedMessage> getAllTargetMessages(String wildCardRef,String user,Date fromDate, Date toDate,String uter);



}
