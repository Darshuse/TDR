package com.eastnets.resilience.toolmonitor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.eastnets.domain.csm.ClientServerConnection;

public class ToolMonitor {
	static ClientCommandLineArgumentsParser commandLineArgumentsParser;
	static ApplicationServiceInterface      applicationServiceInterface;
	
	static boolean applicationResetting = false;
	static Thread networkThread= null;
	static Thread hpThread  = null;
	static DatagramSocket serverSocket= null;

	public static void main(String[] args) {
		System.out.println(Globals.getDateString() + Globals.product_name + " " + Globals.product_version );
		
		commandLineArgumentsParser = new ClientCommandLineArgumentsParser();
		if (!commandLineArgumentsParser.parseAndValidate(args)) {
			// display the application usage
			commandLineArgumentsParser.displayUsage();
			return;
		}
		commandLineArgumentsParser.displayArguments();

		
		boolean firstLoop= true;
		
		
		//main application loop 		
		while (true) {
			applicationResetting = false;
			try {
				System.out.println(Globals.getDateString() + "Initializing...");
				applicationServiceInterface= new ApplicationServiceInterface(commandLineArgumentsParser);
				if ( !applicationServiceInterface.isLicensed() ){
					System.err.println(Globals.getDateString() + Globals.product_name + " is not licensed.");
					return;
				}
				if ( firstLoop ){
					System.out.println(Globals.getDateString() + "License checked successfully.");
					System.out.println();
				}				
				NotificationManager.getInstance().setServiceLocator(applicationServiceInterface.getServiceLocater());
				
				applicationServiceInterface.getServiceLocater().getClientServerMonitorService().resetFlags();
				applicationServiceInterface.getServiceLocater().getClientServerMonitorService().resetAlive( commandLineArgumentsParser.getTimeout() );
				
				networkThread = new Thread(new Runnable() {					
					@Override
					public void run() {
						while ( !applicationResetting ){

							try{
								if ( commandLineArgumentsParser.getListenAddress() != null && !commandLineArgumentsParser.getListenAddress().isEmpty()  ){
									try{
										SocketAddress address=new InetSocketAddress(commandLineArgumentsParser.getListenAddress(),commandLineArgumentsParser.getPort());
										serverSocket = new DatagramSocket(address);
										System.out.println(Globals.getDateString() + "Listening on '" + commandLineArgumentsParser.getListenAddress() + ":" + commandLineArgumentsParser.getPort() );
									}catch(Exception ex ){
										System.out.println(ex);
										System.out.println(Globals.getDateString() + "The ListenAddress '" + commandLineArgumentsParser.getListenAddress() + "' is not valid, listening on all addresses."  );
									}	
								}
							
								if ( serverSocket == null  ){
									serverSocket = new DatagramSocket(commandLineArgumentsParser.getPort());
									System.out.println(Globals.getDateString() + "Listening on port " + commandLineArgumentsParser.getPort() );
								}
							
								while(!applicationResetting)
								{
									byte[] receiveData = new byte[102400];
									DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
									serverSocket.receive(receivePacket);							
									String sentence = new String( receivePacket.getData());
									sentence= sentence.trim();
									
									DataProcessingThread thread= new DataProcessingThread( sentence, receivePacket.getAddress(), receivePacket.getPort() , applicationServiceInterface.getServiceLocater() );
									thread.start();
								}
							}catch(Exception ex){
								System.out.println(Globals.getDateString() + "Receiving data stopped"); 
								try{
									Thread.sleep( 100 );
								}catch( Exception ex2 ){}
							}
						}
					}
				});
				networkThread.start();
				
				hpThread = new Thread(new Runnable() {					
					@Override
					public void run() {
						
						
						try{
							//wait 0.7 of the timeout and then start the thread, this will minimize the database conjunction
							Thread.sleep( commandLineArgumentsParser.getTimeout() * 700 ); 
						}catch( Exception ex2 ){}
						
						while ( !applicationResetting ){
							try{
								//send the im alive signal
								applicationServiceInterface.getServiceLocater().getClientServerMonitorService().updateAlive();
								Thread.sleep( commandLineArgumentsParser.getTimeout() * 1000 );
							}catch( InterruptedException iex ){								
							}
							catch( Exception ex ){
								System.out.println(Globals.getDateString() + "sending heartbeat stopped.");
								try{
									Thread.sleep( 1000 );
								}catch( Exception ex2 ){}
							}
						}
					}
				});
				hpThread.start();
				
				
							
				while(true){					
					//read all configurations from database
					List<ClientServerConnection> configurations= applicationServiceInterface.getServiceLocater().getClientServerMonitorService().getClientServerConnections();
					List<ClientServerConnection> changedConfigurations = new ArrayList<ClientServerConnection>();
					for( ClientServerConnection config : configurations ){
						//validate the configuration
						validateConfiguration( config );
						
						if ( (config.getStatus() & ClientServerConnection.STATUS_DIRTY) != 0 ){
							config.setStatus( config.getStatus() &  ( ~ClientServerConnection.STATUS_DIRTY ) );//remove the dirty bit 
							config.setStatus( config.getStatus() &  ( ~ClientServerConnection.STATUS_UNKNOWN ) );//remove the unknown bit, if found
							NotificationManager.getInstance().triggerNotification( config );
						
							changedConfigurations.add(config);
						}
						
					}
					applicationServiceInterface.getServiceLocater().getClientServerMonitorService().updateClientServerConnectionsStatus(changedConfigurations);
					Thread.sleep( commandLineArgumentsParser.getTimeout() * 1000 );
					
				}
			}
			catch( Exception ex ){
				applicationResetting = true;
				System.out.println(Globals.getDateString() + "Error : " + ex.getMessage() );
				if ( networkThread != null  ){
					if ( serverSocket != null && !serverSocket.isClosed() ){
						serverSocket.close();
					}
					networkThread.interrupt();
				}
				
				if ( hpThread != null ){
					hpThread.interrupt();
				}
				
				//wait tell the threads are done
				if ( networkThread != null ){
					try {
						networkThread.join();
					} catch (InterruptedException e1) {}
				}
				
				if ( hpThread != null ){
					try {
						hpThread.join();
					} catch (InterruptedException e1) {}
				}
				
				
				//reset the data
				networkThread= null;
				hpThread= null;
				serverSocket= null;
				
				System.out.println(Globals.getDateString() + "Resetting in 5 seconds.... " );
				
				System.out.println();
				System.out.println();
				
				firstLoop= false;
				try {
					Thread.sleep(5 * 1000 );
				} catch (InterruptedException e) {}
				
				
			}
		}
	}

	/**
	 * validates the server and client running state, and reflecting it on the configuration status 
	 * @param config
	 * @return true if the state was changed, so that it will be reflected on the database, false otherwise 
	 */
	private static boolean validateConfiguration( ClientServerConnection config) {
		int status = config.getStatus();
		boolean serverDownDB = (status & ClientServerConnection.STATUS_SERVER_DOWN)  != 0 ;
		boolean clientDownDB = (status & ClientServerConnection.STATUS_CLIENT_DOWN)  != 0 ;
		
		int maxTimeout =  (int) (( config.getHPPeriodSeconds() + (config.getHPPeriodSeconds() * ( config.getThresholdPercent() / 100.0  ) ) ) * config.getAlarmIdleCycles() );
		boolean serverDown = config.getSecondsFromLastServerRequest() >=  maxTimeout;
		boolean clientDown = config.getSecondsFromLastClientRequest() >=  maxTimeout;
		
		boolean statusChanged = false; 
		//when the server state is changed, we will mark the status as dirty and reflect the change in server state on the status
		if (config.getLastServer() != null && serverDownDB != serverDown ) {
			if ( serverDownDB ) {
				status &= ~ClientServerConnection.STATUS_SERVER_DOWN;//remove the SERVER_DOWN flag
			}
			else{
				status |= ClientServerConnection.STATUS_SERVER_DOWN;// add the SERVER_DOWN flag
			}
			status |= ClientServerConnection.STATUS_DIRTY;
			statusChanged= true;
		}
		
		//when the client state is changed, we will mark the status as dirty and reflect the change in client state on the status
		if (config.getLastClient() != null && clientDownDB != clientDown ) {
			if ( clientDownDB ) {
				status &= ~ClientServerConnection.STATUS_CLIENT_DOWN;//remove the CLIENT_DOWN flag
			}
			else{
				status |= ClientServerConnection.STATUS_CLIENT_DOWN;// add the CLIENT_DOWN flag
			}
			status |= ClientServerConnection.STATUS_DIRTY;
			statusChanged= true;
		}
		config.setStatus( status );
		return statusChanged;
	}
}
