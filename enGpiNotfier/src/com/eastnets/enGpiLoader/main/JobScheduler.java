package com.eastnets.enGpiLoader.main;

import java.io.IOException;
import java.sql.SQLException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

 
public class JobScheduler extends QuartzJobBean {

	private ServiceLauncher luncher;

	@Override
	public void executeInternal(JobExecutionContext context) throws JobExecutionException {
	 

	}

	public ServiceLauncher getLuncher() {
		return luncher;
	}

	public void setLuncher(ServiceLauncher luncher) {
		this.luncher = luncher;
	}

}
