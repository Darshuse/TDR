package com.eastnets.dao.csm;


public class ClientServerMonitorSqlDAOImp extends ClientServerMonitorDAOImp {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1943826121632624720L;

	@Override
	String getClientServerConnectionsQuery() {
		return "Select SrvrName, SrvrPort, SrvrName, ConnDownAlrm, ClntName, ClntDownAlrm, HBPeriodSec, ThresholdPercent, AlarmIdleCycles, LastSrvr, LastClnt"
				+ ", DATEDIFF( SECOND , LastSrvr, GETDATE() ) as SecondsFromLastServerRequest"
				+ ", DATEDIFF( SECOND , LastClnt, GETDATE() ) as SecondsFromLastClientRequest, Status"
			     + " from CSM_CONNECTION ";
	}	
	
	
	@Override
	public String  getCSMStatusQuery(){
		return "select t1.PROPVALUE as CSMTimeout , "
				+ "DATEDIFF( SECOND , CONVERT(DATETIME, t2.PROPVALUE, 120), GETDATE() ) as CSMAliveSeconds  from reportingproperties t1, reportingproperties t2   where t1.PROPNAME = 'CSMTimeout'  and t2.PROPNAME = 'CSMAlive'";
	}
}
