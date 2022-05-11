package com.eastnets.resilience.toolmonitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.eastnets.domain.csm.ClientServerConnection;
import com.eastnets.service.ServiceLocator;


public class NotificationManager implements Serializable  {
	
	/**
	 * 
	 */ 
	private static final long serialVersionUID = -6556642804263390480L;
	
	private static NotificationManager instance = new NotificationManager();	
	private List<NotificationHandler>  handlers = new ArrayList<NotificationHandler>();	
	private List<String>  registeredHandlers = new ArrayList<String>();//for not registering the same handler more than once  
	
	private ServiceLocator serviceLocator;
	
	
	public static NotificationManager getInstance() {
		return instance;
	}
	
	/**
	 * this will register a handler that will be triggered once the state of a client-server pair has changed
	 * @param fullClassID : full class name including package
	 * 		, this class must implement the class com.eastnets.resilience.toolmonitor.NotificationHandler
	 * @param config 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void registerHandler( String fullClassID, Element config ){
		try{
			NotificationHandler handler= (NotificationHandler) Class.forName( fullClassID ).newInstance();
			if ( handler == null || registeredHandlers.contains(fullClassID)  ){
				return;
			}
			if ( handler.loadSettings( config ) ) {
				handlers.add(handler);
			}else{
				System.out.println(Globals.getDateString() + "Class \"" + fullClassID + "\" not loaded due to invalid settings" );
			}
		}catch(Exception e){
			System.out.println(Globals.getDateString() + "Invalid class \"" + fullClassID +"\"" );
		}
		
	}

	public void triggerNotification(ClientServerConnection config ) {
		for(NotificationHandler handler : handlers ){
			try{
			handler.handle( config , serviceLocator );
			}
			catch(Exception ex){
				//don't want to reset the application each time a handler crash, we'll just log and ignore it 
				System.out.println( Globals.getDateString() + "Error in handler : " + handler.getClass().getName() );
				System.out.println( Globals.getDateString() + "Error message    : " + ex );
				System.out.println();
			}
		}
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}
	
}
