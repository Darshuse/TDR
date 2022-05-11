package com.eastnets.GPIParser.DAO;

import com.eastnets.dao.DAO;

public interface ParserDAO extends DAO {

	
	public String selectMessage(int aid);
}
