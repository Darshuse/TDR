package com.eastnets.dao.csm;

public class ClientServerMonitorOracleDAOImp extends ClientServerMonitorDAOImp {

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1420033228822544136L;

	@Override
	String getClientServerConnectionsQuery() {
		return "Select SrvrName, SrvrPort, ConnDownAlrm, ClntName, ClntDownAlrm, HBPeriodSec, ThresholdPercent, AlarmIdleCycles, LastSrvr, LastClnt"
				+ ", EXTRACT(day FROM ( ( sysdate - lastSrvr)  * 24 * 60 * 60 ) ) as SecondsFromLastServerRequest"
				+ ", EXTRACT(day FROM ( ( sysdate - LastClnt)  * 24 * 60 * 60 ) ) as SecondsFromLastClientRequest, Status"
			     + " from CSM_CONNECTION ";
	}

	@Override
	public String getCSMStatusQuery(){
		return    "select t1.PROPVALUE as CSMTimeout "
				+ ", EXTRACT(day FROM ( ( sysdate - TO_DATE(t2.PROPVALUE, 'yyyy/mm/dd HH24:MI:SS'))  * 24 * 60 * 60 ) ) as CSMAliveSeconds"
				+ " from reportingproperties t1, reportingproperties t2   where t1.PROPNAME = 'CSMTimeout'  and t2.PROPNAME = 'CSMAlive'";
		
	}

}
