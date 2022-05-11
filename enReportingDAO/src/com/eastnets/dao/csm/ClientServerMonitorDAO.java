package com.eastnets.dao.csm;

import java.sql.SQLException;
import java.util.List;

import com.eastnets.dao.DAO;
import com.eastnets.domain.Pair;
import com.eastnets.domain.csm.ClientServerConnection;

public interface ClientServerMonitorDAO extends DAO {

	/**
	 * return the client server connection identified by the server name and port
	 * @param serverName
	 * @param serverPort
	 * @return client server connection, or null if not found
	 * @throws SQLException
	 */
	public ClientServerConnection getClientServerConnection( String serverName, int serverPort ) throws SQLException;
	
	/**
	 * return list of all client server connections in the database
	 * @return list of all client server connections in the database
	 * @throws SQLException
	 */
	public List<ClientServerConnection> getClientServerConnections( ) throws SQLException;
	
	/**
	 * updates the connection status and LastServer/LastClient based on the isServer 
	 * @param connection
	 * @param isServer
	 * @throws Exception
	 */
	public void updateClientServerConnection( ClientServerConnection connection , boolean isServer ) throws Exception;
	
	/**
	 * updates the status of every passed connection 
	 * @param connections
	 * @throws Exception
	 */
	public void updateClientServerConnectionsStatus( List<ClientServerConnection> connections ) throws Exception;

	/**
	 * reset status ,LastServer and LastClient for all connections, this should be called only at startup
	 */
	public void resetFlags();

	/**
	 * update the last alive date on the database, so that others will be able to determine if the enCSM is running or not
	 */
	public void updateAlive();

	/**
	 * update the value of the timeout on the database 
	 * @param timeout
	 */
	public void updateTimeout(Integer timeout);
	
	/**
	 * insert new values for the timeout and the alive in the database 
	 * @param timeout
	 */
	public void insertAlive(Integer timeout);
	

	/**
	 * @return the value of the timeout stored in the database, if null was returned then the value does not exist
	 */
	public Integer getLastTimeout();

	/**
	 * @return pair of two integers, the first is the amount of seconds passed since the last alive message from the enCSM tool
	 *       , and the second is the timeout of the enCSM tool in seconds 
	 */
	public Pair<Integer, Integer> getCSMStatus();
}
