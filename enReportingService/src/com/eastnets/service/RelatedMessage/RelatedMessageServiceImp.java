package com.eastnets.service.RelatedMessage;

import java.util.List;
import java.util.Date;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.relatedMsg.RelatedMessageDAO;
import com.eastnets.domain.Config;
import com.eastnets.domain.relatedMessage.RelatedMessage;
import com.eastnets.domain.viewer.MessageNote;
import com.eastnets.service.ServiceBaseImp;

public class RelatedMessageServiceImp  extends ServiceBaseImp implements RelatedMessageService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RelatedMessageDAO relatedMessageDAO;
	private CommonDAO commonDAO;
	private Config config;

	@Override
	public List<List<RelatedMessage>> getAllRelatedMessage(String user,Date fromDate,Date toDate,String references,int limit,String uter) throws Exception {
		// TODO Auto-generated method stub
		return relatedMessageDAO.getAllRealatedMsg(user,fromDate,toDate,references,limit,uter);
	}

	@Override
	public List<MessageNote> getMessageNote(int aid,int umdh,int umdl) { 
		return relatedMessageDAO.getMessageNote(aid,umdh,umdl);
	}

	public RelatedMessageDAO getRelatedMessageDAO() {
		return relatedMessageDAO;
	}



	public void setRelatedMessageDAO(RelatedMessageDAO relatedMessageDAO) {
		this.relatedMessageDAO = relatedMessageDAO;
	}



	public CommonDAO getCommonDAO() {
		return commonDAO;
	}



	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}



	public Config getConfig() {
		return config;
	}



	public void setConfig(Config config) {
		this.config = config;
	}



}
