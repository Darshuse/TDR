package com.eastnets.dao.dbconsistencycheck;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.eastnets.dao.DAOBaseImp;

public abstract class DBConsistencyCheckDAOImp extends DAOBaseImp implements DBConsistencyCheckDAO {


	/**
	 * 
	 */

	public DBConsistencyCheckDAOImp(){

	}

	private static final long serialVersionUID = 1L;


	@Override
	public void updateMessage(int aid, int umidl, int umidh) {

		String query = "select count(*) as countRecords from ldrequestupdate where aid = ?  and mesg_s_umidl = ? and mesg_s_umidh = ? ";
		Long countOfRecord =  jdbcTemplate.queryForObject(query, Long.class, aid, umidl, umidh);

		if(countOfRecord > 0 )
			System.out.println("Record Already Exist");
		else{
			query = "INSERT INTO ldrequestupdate (aid,mesg_s_umidl,mesg_s_umidh) VALUES (?,?,?) ";
			jdbcTemplate.update(query, new Object[] {aid, umidl, umidh});
			System.out.println("Insert Record Successfully");
		}
	}


	public List <String> getBICLicensed(){
		String query = "SELECT BICCode FROM sLicensedBIC";
		return jdbcTemplate.query(query, new BICCodeMapping());
	}


	public MessageDetails getMessageDetails(int aid, String sumid){

		int umidl = (int)Long.parseLong( StringUtils.substring( sumid, 0, 8 ), 16 );
		int umidh = (int)Long.parseLong( StringUtils.substring( sumid, 8 ), 16 );

		String query = "select mesg_crea_date_time, mesg_identifier, mesg_frmt_name , MESG_SENDER_X1, X_RECEIVER_X1,  X_FIN_AMOUNT, X_FIN_CCY, MESG_CREA_OPER_NICKNAME, mesg_sub_format, MESG_TRN_REF, X_FIN_VALUE_DATE, MESG_USER_REFERENCE_TEXT from rmesg where aid = ?  and mesg_s_umidl = ?  and mesg_s_umidh = ? ";
		return jdbcTemplate.queryForObject(query, new MessageDetailsMapping(), aid, umidl, umidh);
	}

	@Override
	public boolean checkTrafficLicense(String productID) {

		String query = "SELECT count(*) as count FROM SLICENSEDPRODUCT WHERE ID = ? AND LICENSED = 1";
		Long licensed =  jdbcTemplate.queryForObject(query, Long.class, productID);
		
		if (licensed == 0)
			return false;

		return true;
	}

}
