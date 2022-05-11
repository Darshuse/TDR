package com.eastnets.dao.dbconsistencycheck;

import java.util.List;

public class DBConsistencyCheckOracleDAOImp extends DBConsistencyCheckDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public long getMessageSum(int aid, int umidl, int umidh) {

		String query = "select sum(mesg_token + text_token + inst_token + appe_token + intv_token) as sumtoken from" +
				" ( " +
				"  select nvl(mesg_token,0) as mesg_token, 0 as text_token, 0 as inst_token, 0 as appe_token, 0 as intv_token from rmesg where mesg_s_umidl = ? and mesg_s_umidh = ? and aid = ? "  +
				"  union " +
				"  select 0 as mesg_token, nvl(text_token,0) as text_token, 0 as inst_token, 0 as appe_token, 0 as intv_token from rtext where text_s_umidl = ?   and text_s_umidh = ? and aid = ? " +
				"  union " + 
				"  select 0 as mesg_token, 0 as text_token, nvl(sum(inst_token),0) as inst_token, 0 as appe_token, 0 as intv_token  from rinst where inst_s_umidl = ?  and inst_s_umidh = ?  and aid = ? "  +
				"  union " +
				"  select 0 as mesg_token, 0 as text_token, 0 as inst_token, nvl(sum(appe_token),0) as appe_token, 0 as intv_token  from rappe where appe_s_umidl = ?  and appe_s_umidh = ?  and aid = ? "  +
				"  union " +
				"  select 0 as mesg_token, 0 as text_token, 0 as inst_token, 0 as appe_token, nvl(sum(intv_token),0) as intv_token  from rintv where intv_s_umidl = ?  and intv_s_umidh = ?  and aid = ? " +
				" )";

		Long checksum =  jdbcTemplate.queryForObject(query, Long.class, umidl, umidh, aid);
		return checksum;
	}



	@Override
	public List<String> getMessageList(int aid, String startCheck, String endCheck, boolean mxLicensed, boolean fileLicensed) {

		String query = "";
		if (mxLicensed == true && fileLicensed == true )
			query = "select mesg_s_umidl,mesg_s_umidh from rmesg where aid = ? and mesg_crea_date_time >=  to_date(?,'DD-MON-YY HH24:MI:SS') and mesg_crea_date_time <=  to_date(?,'DD-MON-YY HH24:MI:SS')";
		else if (mxLicensed == true && fileLicensed == false )
			query = "select mesg_s_umidl,mesg_s_umidh from rmesg where aid = ? and mesg_frmt_name in ('Swift','MX') and mesg_crea_date_time >=  to_date(?,'DD-MON-YY HH24:MI:SS') and mesg_crea_date_time <=  to_date(?,'DD-MON-YY HH24:MI:SS')";
		else if (mxLicensed == false && fileLicensed == true )
			query = "select mesg_s_umidl,mesg_s_umidh from rmesg where aid = ? and mesg_frmt_name in ('Swift','File') and mesg_crea_date_time >=  to_date(?,'DD-MON-YY HH24:MI:SS') and mesg_crea_date_time <=  to_date(?,'DD-MON-YY HH24:MI:SS')";
		else
			query = "select mesg_s_umidl,mesg_s_umidh from rmesg where aid = ? and mesg_frmt_name = 'Swift' and mesg_crea_date_time >=  to_date(?,'DD-MON-YY HH24:MI:SS') and mesg_crea_date_time <=  to_date(?,'DD-MON-YY HH24:MI:SS')";

		return jdbcTemplate.query(query, new MessageIDMapping(), aid , startCheck, endCheck );

	}


}
