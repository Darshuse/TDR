package com.eastnets.dao.xml;

import java.util.Date;
import java.util.List;

import com.eastnets.dao.DAO;
import com.eastnets.domain.viewer.XMLMessage;

public interface XMLReaderDAO extends DAO {

	public List<XMLMessage> getXMLMessage(int aid, int umidl, int umidh, Date mesg_crea_date, boolean mxMessage);

	public void fillLoggedInUserName(String loggedInUserName);

}
