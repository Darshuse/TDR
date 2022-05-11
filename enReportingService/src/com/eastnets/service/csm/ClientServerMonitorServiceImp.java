package com.eastnets.service.csm;

import java.sql.SQLException;
import java.util.List;

import com.eastnets.dao.csm.ClientServerMonitorDAO;
import com.eastnets.domain.Pair;
import com.eastnets.domain.csm.ClientServerConnection;

public class ClientServerMonitorServiceImp implements ClientServerMonitorService {
	private ClientServerMonitorDAO clientServerMonitorDAO;
	
	public ClientServerMonitorDAO getClientServerMonitorDAO() {
		return clientServerMonitorDAO;
	}

	public void setClientServerMonitorDAO(ClientServerMonitorDAO clientServerMonitorDAO) {
		this.clientServerMonitorDAO = clientServerMonitorDAO;
	}	

	@Override
	public ClientServerConnection getClientServerConnection( String serverName, int serverPort ) throws SQLException {
		return clientServerMonitorDAO.getClientServerConnection(serverName, serverPort);
	}

	@Override
	public List<ClientServerConnection> getClientServerConnections() throws SQLException {
		return clientServerMonitorDAO.getClientServerConnections();
	}

	@Override
	public void updateClientServerConnection(ClientServerConnection connection, boolean isServer) throws Exception {
		clientServerMonitorDAO.updateClientServerConnection(connection, isServer);

	}

	@Override
	public void updateClientServerConnectionsStatus( List<ClientServerConnection> connections ) throws Exception {
		clientServerMonitorDAO.updateClientServerConnectionsStatus(connections);
	}

	@Override
	public void resetFlags() {
		clientServerMonitorDAO.resetFlags();
		
	}

	@Override
	public void updateAlive() {
		clientServerMonitorDAO.updateAlive();		
	}

	@Override
	public void resetAlive(Integer timeout) {
		Integer lastTimeout = clientServerMonitorDAO.getLastTimeout();
		if ( lastTimeout == null ){
			clientServerMonitorDAO.insertAlive( timeout );
		}else{
			clientServerMonitorDAO.updateTimeout( timeout );
		}
	}

	@Override
	public boolean isClientServerMonitorRunning() {
		Pair<Integer, Integer> aliveAndTimeoutSecondsPair = clientServerMonitorDAO.getCSMStatus();
		if ( aliveAndTimeoutSecondsPair == null || aliveAndTimeoutSecondsPair.getKey() == null || aliveAndTimeoutSecondsPair.getValue() == null ){
			return false;
		}
		
		if ( (aliveAndTimeoutSecondsPair.getKey()) < aliveAndTimeoutSecondsPair.getValue() + 30 ){
			return true;
		}		
		return false;
	}
}
