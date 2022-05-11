package com.eastnets.cluster;

import java.io.InputStream;
import java.io.OutputStream;

import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

public class ClusterListener extends ReceiverAdapter {

	public void receive(Message msg) { 
		System.out.println("ClusterListener.receive called with message " + msg);
	}

	public void getState(OutputStream output) throws Exception {
		System.out.println("ClusterListener.getState called with bytes " + output);
	}

	public void setState(InputStream input) throws Exception {
		System.out.println("ClusterListener.setState called with bytes " + input);
	}

	public void viewAccepted(View view) {
		System.out.println("ClusterListener.viewAccepted called with view " + view.getMembers());
	}
	
}
