package com.eastnets.service.RelatedMessage;

import java.util.Date;
import java.util.List;

import com.eastnets.domain.relatedMessage.RelatedMessage;
import com.eastnets.domain.viewer.MessageNote;

import com.eastnets.service.Service;

public interface RelatedMessageService extends Service {


	public List<List<RelatedMessage>> getAllRelatedMessage(String user,Date fromDate,Date toDate,String references,int limit,String uter ) throws Exception;

	public List<MessageNote> getMessageNote(int aid,int umdh,int umdl); 

}
