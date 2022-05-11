package com.eastnets.dao.relatedMsg;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat; 
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.domain.admin.User;
import com.eastnets.domain.relatedMessage.RelatedMessage;
import com.eastnets.domain.viewer.MessageNote; 

public class RelatedMessageSqlDAOImp extends RelatedMessageDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public List<List<RelatedMessage>>  getAllRealatedMsg(String user,Date fromDate, Date toDate, String references,int limit,String uter) {  

		List<RelatedMessage> msgList = null; 
		List<List<RelatedMessage>> group = new ArrayList<List<RelatedMessage>>(); 

		List<RelatedMessage> targetList=getAllTargetMessages(references,user,fromDate,toDate,uter);

		for(RelatedMessage list:targetList){

			msgList = null;//mesg_crea_date_time  TO_CHAR(, 'dd-MM-yyyy HH:MI:SS')
			String selectQuery = "WITH ReportingTree ( MESG_REL_TRN_REF,mesg_type ,mesg_s_umidh,mesg_s_umidl,aid,mesg_crea_date_time,x_fin_amount,x_fin_ccy,x_receiver_X1,mesg_sender_X1,mesg_sub_format,x_inst0_unit_name,x_fin_value_date, mesg_trn_ref, Lvl) AS ( SELECT  rmesg.MESG_REL_TRN_REF, rmesg.mesg_type , rmesg.mesg_s_umidh , rmesg.mesg_s_umidl , rmesg.aid , rmesg.mesg_crea_date_time , rmesg.x_fin_amount , rmesg.x_fin_ccy , rmesg.x_receiver_X1 , rmesg.mesg_sender_X1 , rmesg.mesg_sub_format , rmesg.x_inst0_unit_name , rmesg.x_fin_value_date , rmesg.mesg_trn_ref ,1 as Lvl FROM rmesg WHERE (aid= ? and mesg_s_umidh= ? and mesg_s_umidl= ?)  and (mesg_crea_date_time >= ? and mesg_crea_date_time <= ? )  UNION ALL SELECT rmesg.MESG_REL_TRN_REF, rmesg.mesg_type , rmesg.mesg_s_umidh , rmesg.mesg_s_umidl , rmesg.aid , rmesg.mesg_crea_date_time , rmesg.x_fin_amount , rmesg.x_fin_ccy , rmesg.x_receiver_X1 , rmesg.mesg_sender_X1 , rmesg.mesg_sub_format , rmesg.x_inst0_unit_name , rmesg.x_fin_value_date ,  rmesg.mesg_trn_ref, ReportingTree.Lvl+1 FROM rmesg  inner join SUSER on (UPPER(SUSER.USERNAME) = ? ) inner join  SBICUSERGROUP on (SUSER.GROUPID           = SBICUSERGROUP.GROUPID )  inner join SMSGUSERGROUP on (SUSER.GROUPID           = SMSGUSERGROUP.GROUPID)  inner join SUNITUSERGROUP  on( SUSER.GROUPID           = SUNITUSERGROUP.GROUPID)  INNER JOIN ReportingTree on rmesg.MESG_REL_TRN_REF = ReportingTree.mesg_trn_ref and (ReportingTree.mesg_crea_date_time >= ? and ReportingTree.mesg_crea_date_time <= ? ) where (X_OWN_LT               = BICCODE )  AND X_CATEGORY              = CATEGORY AND rmesg.X_INST0_UNIT_NAME       = UNIT  )  select * from ReportingTree";
			logQuery(user, selectQuery);

			msgList = jdbcTemplate.query(selectQuery,new Object[] {list.getAid(),list.getMesg_s_umidh(),list.getMesg_s_umidl(),fromDate,toDate,user.toUpperCase(),fromDate,toDate} , new RowMapper<RelatedMessage>(){
				@Override
				public RelatedMessage mapRow(ResultSet rs, int arg1) throws SQLException {
					RelatedMessage message=new RelatedMessage();
					message.setMsgType(rs.getString("mesg_type"));
					message.setMesg_s_umidh(rs.getInt("mesg_s_umidh"));
					message.setMesg_s_umidl(rs.getInt("mesg_s_umidl")); 
					message.setAid(rs.getInt("aid"));
					message.setCreationDate(rs.getTimestamp("mesg_crea_date_time"));
			
					message.setJustDate(rs.getDate("mesg_crea_date_time"));
					if(rs.getString("x_fin_ccy") == null  ){
						message.setApperCurancy(false); 

					}
					else{
						message.setApperCurancy(true); 
						message.setxFinCcy(rs.getString("x_fin_ccy"));

					}

					if(rs.getBigDecimal("x_fin_amount") != null){
						message.setAmountStr(new DecimalFormat("0.00##").format(rs.getBigDecimal("x_fin_amount")));	
						message.setApperAmount(true);
					}
					else{
						message.setApperAmount(false);

					}

					message.setSender(rs.getString("mesg_sender_X1"));
					message.setReceiver(rs.getString("x_receiver_X1"));
					if(rs.getString("MESG_REL_TRN_REF") == null){
						message.setShowRelatedRef(false);

					}else{
						message.setMESG_REL_TRN_REF(rs.getString("MESG_REL_TRN_REF"));	
						message.setShowRelatedRef(true);
					}
					message.setUsnitName(rs.getString("x_inst0_unit_name"));
					if(rs.getString("mesg_sub_format").equals("OUTPUT")){
						message.setSubFotmat(String.valueOf(rs.getString("mesg_sub_format").charAt(0))+String.valueOf(rs.getString("mesg_sub_format").charAt(1))+String.valueOf(rs.getString("mesg_sub_format").charAt(2)));

					}
					else if(rs.getString("mesg_sub_format").equals("INPUT")) {
						message.setSubFotmat(String.valueOf(rs.getString("mesg_sub_format").charAt(0))+String.valueOf(rs.getString("mesg_sub_format").charAt(1)));

					}
					message.setMesg_trn_ref(rs.getString("mesg_trn_ref"));
					message.setLEVEL(rs.getInt("Lvl"));
					if(rs.getTimestamp("x_fin_value_date") == null ){
						
						message.setShowValueDate(false);
					}
					else{
						message.setValueDate(rs.getDate("x_fin_value_date"));	
						message.setShowValueDate(true);
					}
					return message;	
				}

			});



			group.add(msgList);

		}
		SpecifyNumberOfRelatedMessage(group,limit); 
		return group;

	}


	@Override
	public List<MessageNote> getMessageNote(int aid, int umdh, int umdl) { 


		Object[] parameters = new Object[] { aid, umdl, umdh };

		String queryString = "SELECT * FROM RMESG_NOTES WHERE AID = ? AND MESG_S_UMIDL = ? AND MESG_S_UMIDH = ? ORDER BY CREATION_DATE";



		List<MessageNote> messageNotes = jdbcTemplate.query(queryString, parameters, new RowMapper<MessageNote>() {

			@Override
			public MessageNote mapRow(ResultSet rs, int rowNum) throws SQLException {
				MessageNote messageNote = new MessageNote();
				messageNote.setNoteId(rs.getLong("NOTE_ID"));
				messageNote.setAid(rs.getInt("AID"));
				messageNote.setMesgUmidl(rs.getInt("MESG_S_UMIDL"));
				messageNote.setMesgUmidh(rs.getInt("MESG_S_UMIDH"));
				messageNote.setCreationDate(rs.getTimestamp("CREATION_DATE"));
				messageNote.setNote(rs.getString("NOTE"));
				/*
				 * mock user object just to set the user id for later fetching the full user details
				 */
				User createdBy = new User();
				createdBy.setUserId(rs.getLong("CREATED_BY"));
				createdBy.setUserName(getUserNameById(rs.getLong("CREATED_BY")).get(0));
				messageNote.setCreatedBy(createdBy);
				return messageNote;
			}

		});




		return messageNotes;
	}




}
