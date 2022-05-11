package com.eastnets.cluster;

import org.apache.log4j.Logger;
import org.jgroups.JChannel;
import org.jgroups.View;
import org.jgroups.blocks.atomic.Counter;
import org.jgroups.blocks.atomic.CounterService;

import com.eastnets.enReportingLoader.config.AppConfigBean;

public class ClusterManager {

	private static final Logger LOGGER = Logger.getLogger(ClusterManager.class);
	
	private JChannel channel;
	private CounterService counterService;
	private boolean connected;
	
	private AppConfigBean appConfigBean;
	
	public void startCluster() throws Exception {
		if(appConfigBean.isClusteringEnabled() && connected == false){
			channel.setReceiver(new ClusterListener());
			channel.connect(appConfigBean.getClustername()); 
			connected = true;
			LOGGER.info("Cluster Session Started");
		}
	}
	
	public void stopClusterIfConnected() {
		if (appConfigBean.isClusteringEnabled() && connected ){
			channel.close();
			LOGGER.info("Cluster Session Stopped");
		}
	}
	
	public void createClusterCounterIfNull(String counterName,long initialValue) {
		if(connected==true){
			long cVal = counterService.getOrCreateCounter(counterName, initialValue).get();
			LOGGER.info("Cluster Counter " + counterName +" initialized with value= " + cVal);
		}
	}

	public long getClusterCounterNextValue(String counterName,long nextValue) {
		if(connected==true){
			Counter counter=counterService.getOrCreateCounter(counterName, -1);
			long cVal = counter.decrementAndGet();
			LOGGER.debug("Cluster Counter " + counterName +" accessed and decremented to value= " + cVal);
			return cVal;
		}
		return nextValue;
		
	}

	public boolean isFirstMember() {
		View clusterView = channel.getView();
		return clusterView != null && clusterView.getMembers() != null ? clusterView.getMembers().size() == 1:false;
	}

	public JChannel getChannel() {
		return channel;
	}

	public void setChannel(JChannel channel) {
		this.channel = channel;
	}
	
	public CounterService getCounterService() {
		return counterService;
	}

	public void setCounterService(CounterService counterService) {
		this.counterService = counterService;
	}
	
	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}
}