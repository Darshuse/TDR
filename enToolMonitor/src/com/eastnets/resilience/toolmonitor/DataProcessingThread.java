package com.eastnets.resilience.toolmonitor;

import java.net.InetAddress;

import com.eastnets.domain.csm.ClientServerConnection;
import com.eastnets.service.ServiceLocator;

public class DataProcessingThread extends Thread {
	private String 		message;	
	private InetAddress	clientAddress;
	private int 		clientPort;
	private ServiceLocator serviceLocator;
	
	
	public DataProcessingThread(String message, InetAddress clientAddress, int clientPort, ServiceLocator serviceLocator) {
		this.message= message;
		this.clientAddress= clientAddress;
		this.clientPort= clientPort;
		this.setServiceLocator(serviceLocator);
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public InetAddress getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(InetAddress clientAddress) {
		this.clientAddress = clientAddress;
	}

	public int getClientPort() {
		return clientPort;
	}

	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}

		
	
	@Override
	public void run() {
		String[] tokens= message.split(";");
		
		//Globals.printDate();
		//System.out.println("Message received : " + message + "\nFrom : "  + clientAddress.getHostAddress() + ":" + clientPort  );
		String customeError= "";
		if (tokens.length > 5 ){
			customeError= tokens[5].trim() ;
		}
		//make sure we got a valid message, and process it 
		if ( 	tokens.length < 5
			||	!ProcessClientMessage(tokens[0].trim(), tokens[1].trim(), tokens[2].trim(), tokens[3].trim(), tokens[4].trim(), customeError ) ){
			System.out.println(Globals.getDateString() + "Error : Invalid message recieved : " + message);
			return;
		}
	}

	/**
	 * Process a message from the client 
	 * @param clientType
	 * @param serverAddress
	 * @param serverPortStr
	 * @param remoteMachine
	 * @param customeError
	 * @return true of the message data was valid and processed successfully, false otherwise 
	 */
	private boolean ProcessClientMessage(String clientType, String serverName,
			String serverPortStr, String clientName, String connected, String customeError) {
		Integer serverPort = null ;
		try{
			serverPort= Integer.parseInt(serverPortStr);
		}catch( Exception e){}
		
		//make sure the all mandatory fields are provided 
		if( clientType == null || clientType.trim().isEmpty()
		 || serverName == null || serverName.trim().isEmpty() 
		 ||serverPort == null ){
			return false;
		}
		
		// make sure its a message that we understand 
		if (!clientType.equalsIgnoreCase("clnt") && !clientType.equalsIgnoreCase("srvr")){
			System.out.println(Globals.getDateString() + "Error : Invalid message client type : " + clientType );
		}
		
		try{
			//get the configuration from the database 
			ClientServerConnection configuration= serviceLocator.getClientServerMonitorService().getClientServerConnection( serverName, serverPort.intValue() );
			if( configuration != null ){
				//TODO we might wanna check the clientAddress, clientPort with the ones we got in the message, or with the ones in the database
				
				checkConnectionDown( configuration, clientName, connected ,  clientType );
				configuration.setStatus(configuration.getStatus() & ~ClientServerConnection.STATUS_UNKNOWN);
				serviceLocator.getClientServerMonitorService().updateClientServerConnection( configuration, clientType.equalsIgnoreCase("srvr") );
				
			}else{
				System.out.println(Globals.getDateString() + "Warning : server '" + serverName + "', port '" + serverPortStr + "' is not configured" );
			}
		}
		catch( Exception ex ){
			
		}
		return true;
		
	}

	private void checkConnectionDown(ClientServerConnection configuration, String clientName, String connected, String clientType) {
		if ( configuration == null || configuration.getClientDownAlarm() == null || configuration.getClientDownAlarm() ){
			return;
		}	
		if (connected == null || connected.isEmpty()  ){
			connected = "0";
		}
		int status = configuration.getStatus();
		
		status &= ~ClientServerConnection.STATUS_UNKNOWN ;//remove the unknown flag
			
		//get the connection status saved in the database
		boolean dbStatusConnected = ( status & ClientServerConnection.STATUS_CONNECTION_DOWN ) == 0 ;
		
		//connected but to the wrong peer 
		if ( clientType.equalsIgnoreCase("srvr") && !configuration.getClientName().equals(clientName) ){
			System.out.println(Globals.getDateString() + String.format("Non-configured Client(%s) connected to Server(%s)"
											, configuration.getClientName()
											, configuration.getServerName() 
						));
			
			//we will consider this as a disconnection 
			if ( dbStatusConnected ){
				status |= ClientServerConnection.STATUS_CONNECTION_DOWN;
				status |= ClientServerConnection.STATUS_DIRTY;				
			}
			return;
		}
			
		//just got reconnected
		if ( connected.equals("1") && !dbStatusConnected ){
			status &= ~ClientServerConnection.STATUS_CONNECTION_DOWN;//!dbStatusConnected guarantees that  STATUS_CONNECTION_DOWN is included in the status 
			status |= ClientServerConnection.STATUS_DIRTY;		
		}
		
		//just got disconnected
		if ( !connected.equals("1") && dbStatusConnected ){
			status |= ClientServerConnection.STATUS_CONNECTION_DOWN;
			status |= ClientServerConnection.STATUS_DIRTY;	
		}
	}

	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

}
