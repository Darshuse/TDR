package com.eastnets.GPIParser.DAO;

import com.eastnets.dao.DAOBaseImp;

public class ParserDAOImpl extends DAOBaseImp implements ParserDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String selectMessage(int aid) {
		String queryString = "select mesg_uumid from rmesg where aid = ? and mesg_s_umidl = 652629843 and mesg_s_umidh = -316";

		String uumid = (String) jdbcTemplate.queryForObject(queryString, new Object[] { aid }, String.class);

		return uumid;

	}

}
