package com.eastnets.service.loader;

import org.apache.log4j.Logger;

import com.eastnets.cluster.ClusterManager;

public class ClusterService implements Runnable{

	private static final Logger LOGGER = Logger.getLogger(ClusterService.class);
	
	private ClusterManager clusterManager;  
	
	@Override
	public void run() {
		
		try {
			clusterManager.startCluster();
			while (true) {
				try{
					Thread.sleep(5000);
				}catch (InterruptedException e) {
					LOGGER.error(e);
					e.printStackTrace();
				} catch (Exception e) {
					LOGGER.error(e);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			LOGGER.error(e);
			e.printStackTrace();
		} finally{
			clusterManager.stopClusterIfConnected();
		}

	}

	public ClusterManager getClusterManager() {
		return clusterManager;
	}

	public void setClusterManager(ClusterManager clusterManager) {
		this.clusterManager = clusterManager;
	}
}