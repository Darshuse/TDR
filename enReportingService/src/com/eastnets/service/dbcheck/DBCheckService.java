package com.eastnets.service.dbcheck;


import java.util.List;
import com.eastnets.dao.dbconsistencycheck.MessageDetails;
import com.eastnets.service.Service;

public interface DBCheckService extends Service {
	
	public boolean checkMXLicense();
	public boolean checkFILELicense();
	public List <String> getMessages(int aid, String startCheck, String endCheck, boolean mxLicensed, boolean fileLicensed);
	public MessageDetails getMessageDetails (int aid, String missingMessage);
	public List <String> getBIC();
	public long getTokenSum(int aid, int umidl, int umidh);
	public void updateMessage(int aid, int umidl, int umidh);
	
}
