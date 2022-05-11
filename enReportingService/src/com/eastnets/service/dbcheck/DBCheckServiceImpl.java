package com.eastnets.service.dbcheck;

import java.util.List;
import com.eastnets.dao.dbconsistencycheck.DBConsistencyCheckDAOImp;
import com.eastnets.dao.dbconsistencycheck.MessageDetails;
import com.eastnets.service.ServiceBaseImp;

public class DBCheckServiceImpl extends ServiceBaseImp implements DBCheckService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DBConsistencyCheckDAOImp dbconsistencycheckdao;



	@Override
	public boolean checkMXLicense() {
		return dbconsistencycheckdao.checkTrafficLicense("13");
	}

	@Override
	public boolean checkFILELicense() {
		return dbconsistencycheckdao.checkTrafficLicense("15");
	}

	@Override
	public List<String> getMessages(int aid, String startCheck, String endCheck, boolean mxLicensed, boolean fileLicensed) {
		return dbconsistencycheckdao.getMessageList(aid, startCheck, endCheck, mxLicensed, fileLicensed);

	}

	@Override
	public MessageDetails getMessageDetails(int aid, String missingMessage) {
		return dbconsistencycheckdao.getMessageDetails(aid, missingMessage);
	}
	
	@Override
	public List<String> getBIC() {
		return dbconsistencycheckdao.getBICLicensed();
	}
	
	@Override
	public long getTokenSum(int aid, int umidl, int umidh) {
		return dbconsistencycheckdao.getMessageSum(aid, umidl, umidh);
	}
	
	@Override
	public void updateMessage(int aid, int umidl, int umidh) {
		dbconsistencycheckdao.updateMessage(aid, umidl, umidh);
	}


	// getter & setter

	public DBConsistencyCheckDAOImp getDbconsistencycheckdao() {
		return dbconsistencycheckdao;
	}

	public void setDbconsistencycheckdao(DBConsistencyCheckDAOImp dbconsistencycheckdao) {
		this.dbconsistencycheckdao = dbconsistencycheckdao;
	}

	



	

	















}
