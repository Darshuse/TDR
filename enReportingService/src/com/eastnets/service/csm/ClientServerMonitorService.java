package com.eastnets.service.csm;

import java.sql.SQLException;
import java.util.List;

import com.eastnets.domain.csm.ClientServerConnection;

public interface ClientServerMonitorService {
	
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
	 * checks if the alive parameters for the csm is on the database or not, if not it will insert new values 
	 * @param timeout
	 */
	public void resetAlive(Integer timeout);

	/**
	 * identifies weather the enCSM tool is running by its last alive message  
	 * @return 
	 */
	public boolean isClientServerMonitorRunning();
	
	

}
