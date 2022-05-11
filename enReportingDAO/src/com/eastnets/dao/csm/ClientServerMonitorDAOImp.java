package com.eastnets.dao.csm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.domain.Pair;
import com.eastnets.domain.csm.ClientServerConnection;

public abstract class ClientServerMonitorDAOImp extends DAOBaseImp implements ClientServerMonitorDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1668789137550479201L;
	
	//abstract members 
	abstract String getClientServerConnectionsQuery();
	abstract String getCSMStatusQuery();

	@Override
	public ClientServerConnection getClientServerConnection(final String serverName, final int serverPort ) throws SQLException{
		String query = "Select SrvrName, SrvrPort, ConnDownAlrm, ClntName, ClntDownAlrm, HBPeriodSec, ThresholdPercent, AlarmIdleCycles, LastSrvr, LastClnt, Status"
					     + " from CSM_CONNECTION where SrvrName = ? and SrvrPort = ?";
		
		return getJdbcTemplate().query(query, new Object[]{serverName, serverPort}, new ResultSetExtractor<ClientServerConnection>(){

			@Override
			public ClientServerConnection extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ClientServerConnection clientServerConnection = new ClientServerConnection();
					clientServerConnection.setServerName( serverName );
					clientServerConnection.setServerPort( serverPort );
					clientServerConnection.setConnectionDownAlarm( getNullableValue( rs, rs.getBoolean("ConnDownAlrm")) );
					clientServerConnection.setClientName( rs.getString("ClntName") );
					clientServerConnection.setClientDownAlarm ( getNullableValue( rs, rs.getBoolean("ClntDownAlrm")) );
					clientServerConnection.setHPPeriodSeconds( getNullableValue( rs, rs.getInt("HBPeriodSec")) );
					clientServerConnection.setThresholdPercent( getNullableValue( rs, rs.getInt("ThresholdPercent")) );
					clientServerConnection.setAlarmIdleCycles( getNullableValue( rs, rs.getInt("AlarmIdleCycles")) );
					clientServerConnection.setLastServer( rs.getTimestamp("LastSrvr") );
					clientServerConnection.setLastClient( rs.getTimestamp("LastClnt") );
					clientServerConnection.setStatus( getNullableValue( rs, rs.getInt("Status")) );

					return clientServerConnection;
				}
				
				return null;
			}
			
		} );		
	}
	
	@Override
	public List<ClientServerConnection> getClientServerConnections( ) throws SQLException{
		String query = getClientServerConnectionsQuery() ;
		
		return getJdbcTemplate().query(query, new ResultSetExtractor<List<ClientServerConnection>>(){
			
			@Override
			public List<ClientServerConnection> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<ClientServerConnection> connections= new ArrayList<ClientServerConnection>();
				while (rs.next()) {
					ClientServerConnection clientServerConnection = new ClientServerConnection();
					clientServerConnection.setServerPort( getNullableValue( rs, rs.getInt("SrvrPort"))  );
					clientServerConnection.setServerName( rs.getString("SrvrName"));
					clientServerConnection.setConnectionDownAlarm( getNullableValue( rs, rs.getBoolean("ConnDownAlrm")) );
					clientServerConnection.setClientName( rs.getString("ClntName") );
					clientServerConnection.setClientDownAlarm ( getNullableValue( rs, rs.getBoolean("ClntDownAlrm")) );
					clientServerConnection.setHPPeriodSeconds( getNullableValue( rs, rs.getInt("HBPeriodSec")) );
					clientServerConnection.setThresholdPercent( getNullableValue( rs, rs.getInt("ThresholdPercent")) );
					clientServerConnection.setAlarmIdleCycles( getNullableValue( rs, rs.getInt("AlarmIdleCycles")) );
					clientServerConnection.setLastServer( rs.getTimestamp("LastSrvr") );
					clientServerConnection.setLastClient( rs.getTimestamp("LastClnt") );
					
					//calculated by database because we set the LastSrvr and  LastClnt using the database date 
					clientServerConnection.setSecondsFromLastServerRequest( getNullableValue( rs, rs.getInt("SecondsFromLastServerRequest")) );
					clientServerConnection.setSecondsFromLastClientRequest( getNullableValue( rs, rs.getInt("SecondsFromLastClientRequest")) );
					
					
					clientServerConnection.setStatus( getNullableValue( rs, rs.getInt("Status")) );		

					connections.add(clientServerConnection);
				}				
				return connections;
			}			
		});		
	}

	@Override
	public void updateClientServerConnection(final ClientServerConnection connection , final boolean isServer ) throws Exception {
		String query= "";
		if ( isServer ){
			query = "update CSM_CONNECTION set LastSrvr= " + ( getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE ? "SYSDATE" : "GETDATE()" )  + " , Status = ? "
				     + " where SrvrName = ? and SrvrPort = ?";
		}
		else{
			query = "update CSM_CONNECTION set LastClnt= " + ( getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE ? "SYSDATE" : "GETDATE()" )  + " , Status = ? "
				     + " where SrvrName = ? and SrvrPort = ?";
		}
		
		getJdbcTemplate().update(query, new Object[]{ connection.getStatus(), connection.getServerName(), connection.getServerPort() }) ;		
	}
	
	@Override
	public void updateClientServerConnectionsStatus( List<ClientServerConnection> configurations ) throws Exception {
		if ( configurations == null || configurations.isEmpty() ){
			return;
		}
		String query = "update CSM_CONNECTION set Status = ? "
				     + " where SrvrName = ? and SrvrPort = ?";
				
		for ( final ClientServerConnection configuration : configurations ) {
			getJdbcTemplate().update(query, new Object[]{ configuration.getStatus(), configuration.getServerName(), configuration.getServerPort() }) ;
		}
	}	
	

	@Override
	public void resetFlags() {
		String query = "update CSM_CONNECTION set Status = ? " 
				+ " , LastSrvr= " + ( getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE ? "SYSDATE" : "GETDATE()" ) 
				+ " , LastClnt= " + ( getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE ? "SYSDATE" : "GETDATE()" ) ;

		getJdbcTemplate().update(query, new Object[]{ClientServerConnection.STATUS_UNKNOWN}) ;		
	}

	@Override
	public void updateAlive() {	
		String query = "update reportingproperties set PROPVALUE = " + ( getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE ? "TO_CHAR(sysdate, 'yyyy/mm/dd HH24:MI:SS')" : "CONVERT(VARCHAR(24),GETDATE(),120)" )  + " where PROPNAME = 'CSMAlive' ";

		getJdbcTemplate().update(query, new Object[]{}) ;	
		
	}

	

	@Override
	public Integer getLastTimeout(){
		String query = "select t1.PROPVALUE as CSMTimeout , t2.PROPVALUE as CSMAlive  from reportingproperties t1, reportingproperties t2   where t1.PROPNAME = 'CSMTimeout'  and t2.PROPNAME = 'CSMAlive'";

		return getJdbcTemplate().query(query, new ResultSetExtractor<Integer>(){
			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next() ) {
					return getNullableValue( rs, rs.getInt("CSMTimeout"));
				}
				return null;
			}
		});
	}
	
	
	@Override
	public void updateTimeout(Integer timeout){
		String query = "update reportingproperties set PROPVALUE = ? where PROPNAME = 'CSMTimeout' ";
		getJdbcTemplate().update(query, new Object[]{timeout}) ;	
		
	}
	

	@Override
	public void insertAlive(Integer timeout){
		String query = "insert into reportingproperties( PROPNAME, PROPVALUE )   values( 'CSMTimeout', ? )" ;
		getJdbcTemplate().update(query, new Object[]{timeout});
		
		query = "insert into reportingproperties( PROPNAME, PROPVALUE )   values( 'CSMAlive', " + ( getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE ? "TO_CHAR(sysdate, 'yyyy/mm/dd HH24:MI:SS')" : "CONVERT(VARCHAR(24),GETDATE(),120)" )  + " )" ;
		getJdbcTemplate().update(query, new Object[]{}) ;	
	}
	
	@Override
	public Pair<Integer, Integer> getCSMStatus(){
		String query = getCSMStatusQuery();
		
		return getJdbcTemplate().query(query, new ResultSetExtractor<Pair<Integer, Integer>>(){
			@Override
			public Pair<Integer, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next() ) {
					return new Pair<Integer, Integer>(
							getNullableValue( rs, rs.getInt("CSMAliveSeconds")),
							getNullableValue( rs, rs.getInt("CSMTimeout")));
				}
				return null;
			}
		});
	}
	
	
}
