package com.eastnets.dao.relatedMsg;


import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;


import org.springframework.jdbc.core.RowMapper; 
import com.eastnets.dao.DAOBaseImp;
import com.eastnets.domain.relatedMessage.RelatedMessage;
import com.eastnets.domain.viewer.MessageNote;

public class RelatedMessageDAOImp extends DAOBaseImp implements RelatedMessageDAO {

	/*protected String getStrDate(Date date) {
		String DATE_FORMAT = "dd-MM-yy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String dateStr = sdf.format(date);
		String[] strArr = dateStr.split("-");
		dateStr = strArr[0] + "-" + getMonth(Integer.parseInt(strArr[1])).toUpperCase() + "-" + strArr[2];
		return dateStr;
	}*/



	/*public  String getMonth(int month){
	    String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	    return monthNames[month-1];
	}*/

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<List<RelatedMessage>>  getAllRealatedMsg(String user,Date fromDate, Date toDate, String references,int limit,String uter ) {
		// TODO Auto-generated method stub
		return null;
	}

	/*public static void main(String[] args) {
		System.out.println(new RelatedMessageDAOImp().getMonth(04));
	}*/

	@Override
	public void logQuery(String loggedInUser, String query) {
		//query = query.replace("'", "''");
		do {
			String queryFragment = query;
			if (query.length() > 3998) {
				query = query.substring(3998);
				queryFragment = queryFragment.substring(0, 3998);

				/*if (queryFragment.charAt(0) == '\''
						&& queryFragment.charAt(1) != '\'')
					queryFragment = "'" + queryFragment;
				if (queryFragment.charAt(queryFragment.length() - 1) == '\''
						&& queryFragment.charAt(queryFragment.length() - 2) != '\'')
					queryFragment = queryFragment.substring(0,
							queryFragment.length() - 1);*/

			} else {
				query = "";
				/*if (queryFragment.charAt(0) == '\''
						&& queryFragment.charAt(1) != '\'')
					queryFragment = "'" + queryFragment;*/
			}

			List<Object> parameters = new ArrayList<Object>();
			parameters.add(loggedInUser);
			parameters.add(queryFragment);

			jdbcTemplate.update("INSERT INTO QueryExecuted VALUES ("
					+ getDbPortabilityHandler().getCurrentDateFunction() 
					+ ", ?, ? )"
					, parameters.toArray() );

		} while (!query.isEmpty());

	}

	@Override
	public List<String> getAlltransactionReference(String wildCardRef,String user,Date fromDate, Date toDate) {
		String queryString = "Select mesg_trn_ref from rmesg inner join SUSER on (UPPER(SUSER.USERNAME) = ? ) inner join  SBICUSERGROUP on (SUSER.GROUPID           = SBICUSERGROUP.GROUPID ) inner join SMSGUSERGROUP on (SUSER.GROUPID           = SMSGUSERGROUP.GROUPID) inner join SUNITUSERGROUP  on( SUSER.GROUPID           = SUNITUSERGROUP.GROUPID)   where (mesg_crea_date_time >= ? and  mesg_crea_date_time <= ? ) and (X_OWN_LT               = BICCODE )  AND X_CATEGORY              = CATEGORY AND X_INST0_UNIT_NAME       = UNIT and lower(mesg_trn_ref) LIKE ?";
		//System.out.println(queryString);
		List<String> listOfTranRef = jdbcTemplate.query(queryString, new Object[] { user.toUpperCase(), fromDate,toDate,wildCardRef }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String traRef;
				traRef = rs.getString("mesg_trn_ref");
				return traRef;
			}
		});
		return listOfTranRef;
	}

	@Override
	public List<MessageNote> getMessageNote(int aid, int umdh, int umdl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getUserNameById(Long id) {
		String queryString = "Select UserName from sUser  where UserID ="+id;
		List<String> userName = jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String userName;
				userName = rs.getString("UserName");
				return userName;
			}
		});
		return userName;
	}

	@Override
	public List<RelatedMessage> getAllTargetMessages(String wildCardRef, String user, Date fromDate, Date toDate,String uter) {
		String queryString = "Select aid,mesg_s_umidh,mesg_s_umidl,x_inst0_unit_name,mesg_trn_ref from rmesg inner join SUSER on (UPPER(SUSER.USERNAME) = ? ) inner join  SBICUSERGROUP on (SUSER.GROUPID           = SBICUSERGROUP.GROUPID ) inner join SMSGUSERGROUP on (SUSER.GROUPID           = SMSGUSERGROUP.GROUPID) inner join SUNITUSERGROUP  on( SUSER.GROUPID           = SUNITUSERGROUP.GROUPID)   where (mesg_crea_date_time >= ? and  mesg_crea_date_time <= ? ) and (X_OWN_LT               = BICCODE )  AND X_CATEGORY              = CATEGORY AND X_INST0_UNIT_NAME       = UNIT and lower(mesg_trn_ref) LIKE ? And mesg_e2e_transaction_reference like ?";

		List<RelatedMessage> listOfTranRef = jdbcTemplate.query(queryString, new Object[] { user.toUpperCase(), fromDate,toDate,wildCardRef,uter }, new RowMapper<RelatedMessage>() {
			public RelatedMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
				RelatedMessage message=new RelatedMessage();
				//message.setPrimaryKey(String.valueOf(rs.getInt("aid"))+","+String.valueOf(rs.getInt("mesg_s_umidh"))+","+String.valueOf(rs.getInt("mesg_s_umidl")));
				//message.setUmdhAndRef(rs.getString("mesg_trn_ref") +" ( "+rs.getInt("aid")+" , "+String.valueOf(rs.getInt("mesg_s_umidh"))+" , "+String.valueOf(rs.getInt("mesg_s_umidl"))+" )");
				message.setAid(rs.getInt("aid"));
				message.setMesg_s_umidh(rs.getInt("mesg_s_umidh"));
				message.setMesg_s_umidl(rs.getInt("mesg_s_umidl"));
				message.setMesg_trn_ref(rs.getString("mesg_trn_ref"));
				message.setUsnitName(rs.getString("x_inst0_unit_name"));
				return message;

			}
		});
		return listOfTranRef;
	}


	@Override
	public void SpecifyNumberOfRelatedMessage(List<List<RelatedMessage>> relMsgList ,int limit) { 
		if(limit == 0){
			return;
		}
		int count=0;
		List<RelatedMessage> dummy = new ArrayList<RelatedMessage>(); 
		for(List<RelatedMessage> list:relMsgList){
			count=0;
			dummy = new ArrayList<RelatedMessage>();
			for(RelatedMessage message:list){
				if(count < limit){
					dummy.add(message);
					//list.remove(count);
				} 
				count++;
			}
			list.clear();
			list.addAll(dummy);		
		}

	}

}
