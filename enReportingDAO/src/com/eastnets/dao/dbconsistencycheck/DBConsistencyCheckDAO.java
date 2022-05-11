package com.eastnets.dao.dbconsistencycheck;

import java.util.List;
import com.eastnets.dao.DAO;

public interface DBConsistencyCheckDAO extends DAO  {

	public long getMessageSum (int iAid,int iSumidLow,int iSumidHigh);
	public void updateMessage (int iAid,int iSumidLow,int iSumidHigh);
	public List<String> getBICLicensed();
	public List<String> getMessageList(int iAid, String startCheck, String endCheck, boolean mxLicensed, boolean fileLicensed);
	public MessageDetails getMessageDetails(int iAid, String SUMID);
	public boolean checkTrafficLicense (String productID);
}
