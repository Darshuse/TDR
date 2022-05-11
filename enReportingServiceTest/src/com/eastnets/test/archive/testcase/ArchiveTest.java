/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.test.archive.testcase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.util.Assert;

import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.archive.ArchiveOptions;
import com.eastnets.domain.archive.JrnlArchiveOptions;
import com.eastnets.domain.archive.MessageArchiveOptions;
import com.eastnets.domain.archive.JrnlArchiveSettings;
import com.eastnets.domain.archive.MessageArchiveSettings;
import com.eastnets.domain.archive.RestoreOptions;
import com.eastnets.domain.archive.RestoreSet;
import com.eastnets.service.admin.AdminService;
import com.eastnets.service.archive.ArchiveService;
import com.eastnets.test.BaseTest;

public class ArchiveTest extends BaseTest {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1577201943976281553L;
	@Test
	public void testArchiveGetMessageArchiveSettings(){
	
		String loggedInUser = this.getLoggedInUser();
		Long userId = this.getLoggedInUserId();
		
		String userName = loggedInUser;
		ArchiveService archiveService = this.getServiceLocater().getArchiveService();
		AdminService adminService = this.getServiceLocater().getAdminService();
		
		List<LoaderConnection> loaderConnections = adminService.getLoaderConnections(userName);
		
		List<MessageArchiveSettings>messageArchiveOptionsList = new ArrayList<MessageArchiveSettings>();
		for (LoaderConnection loaderConnection : loaderConnections) {
			MessageArchiveSettings messageArchiveSettings = archiveService.getMessageArchiveSettings(userName, userId,loaderConnection.getAid());
			
			Assert.notNull(messageArchiveSettings);
			messageArchiveOptionsList.add(messageArchiveSettings);
			
			
		}
		
		for (MessageArchiveSettings messageArchiveSettings : messageArchiveOptionsList) {
			System.out.println(String.format("Alliance ID: %d, Archive Path: %s, Swift Archive Path: %s", 
					messageArchiveSettings.getAllianceId(),
					messageArchiveSettings.getArchivePath(),
					messageArchiveSettings.getSwiftArchivePath()));
			
		}
		
	}
	
	@Test
	public void testArchiveGetJrnlArchiveSettings(){
		
	
		String loggedInUser = this.getLoggedInUser();
		Long userId = this.getLoggedInUserId();
		ArchiveService archiveService = this.getServiceLocater().getArchiveService();
		AdminService adminService = this.getServiceLocater().getAdminService();
		
		List<LoaderConnection> loaderConnections = adminService.getLoaderConnections(loggedInUser);
		
		List<JrnlArchiveSettings>jrnlArchiveOptionsList = new ArrayList<JrnlArchiveSettings>();
		
		for (LoaderConnection loaderConnection : loaderConnections) {
			JrnlArchiveSettings jrnlArchiveSettings = archiveService.getJrnlArchiveSettings(loggedInUser,userId,loaderConnection.getAid());
			
			Assert.notNull(jrnlArchiveSettings);
			
			jrnlArchiveOptionsList.add(jrnlArchiveSettings);
		
		}
		
		for (JrnlArchiveSettings jrnlArchiveSettings : jrnlArchiveOptionsList) {
			
			System.out.println(String.format("Alliance ID: %d, Jrnl Archive Path: %s, Swift Jrnl Archive Path: %s", 
			jrnlArchiveSettings.getAllianceId(),
			jrnlArchiveSettings.getJrnlArchivePath(),
			jrnlArchiveSettings.getSwiftJrnlArchivePath()));
		}
		
	}
	
	@Test
	public void testArchiveGetMessageArchiveOptions(){		
		String loggedInUser = this.getLoggedInUser();
		Long userId = this.getLoggedInUserId();
		ArchiveService archiveService = this.getServiceLocater().getArchiveService();
		AdminService adminService = this.getServiceLocater().getAdminService();
		
		ArchiveOptions archiveOptions = archiveService.getMessageArchiveOptions(loggedInUser,userId);
		Assert.notNull(archiveOptions);
		
		
		List<LoaderConnection> loaderConnections = adminService.getLoaderConnections(loggedInUser);
		
		List<MessageArchiveSettings>messageArchiveOptionsList = new ArrayList<MessageArchiveSettings>();
		for (LoaderConnection loaderConnection : loaderConnections) {
			MessageArchiveSettings messageArchiveSettings = archiveService.getMessageArchiveSettings(loggedInUser,userId,loaderConnection.getAid());
			
			Assert.notNull(messageArchiveSettings);
			messageArchiveOptionsList.add(messageArchiveSettings);
			
			
		}
		
		MessageArchiveOptions messageArchiveOptions = (MessageArchiveOptions)archiveOptions;
		messageArchiveOptions.setMessageArchiveSettings(messageArchiveOptionsList);		
	}
	
	@Test
	public void testArchiveGetJrnlArchiveOptions(){		
		String loggedInUser = this.getLoggedInUser();
		Long userId = this.getLoggedInUserId();
		ArchiveService archiveService = this.getServiceLocater().getArchiveService();
		AdminService adminService = this.getServiceLocater().getAdminService();
		
		ArchiveOptions archiveOptions = archiveService.getJrnlArchiveOptions(loggedInUser, userId);
		Assert.notNull(archiveOptions);
		
		
		List<LoaderConnection> loaderConnections = adminService.getLoaderConnections(loggedInUser);
		
		List<JrnlArchiveSettings>jrnlArchiveOptionsList = new ArrayList<JrnlArchiveSettings>();
		
		for (LoaderConnection loaderConnection : loaderConnections) {
			JrnlArchiveSettings jrnlArchiveSettings = archiveService.getJrnlArchiveSettings(loggedInUser,userId, loaderConnection.getAid());
			
			Assert.notNull(jrnlArchiveSettings);
			
			jrnlArchiveOptionsList.add(jrnlArchiveSettings);
		}

		Assert.notEmpty(jrnlArchiveOptionsList);
		
		((JrnlArchiveOptions)archiveOptions).setJrnlArchiveSettings(jrnlArchiveOptionsList);
	}
	
	@Test
	public void testArchiveGetMessageRestoreOptions(){
		String loggedInUser = this.getLoggedInUser();
		Long userId = this.getLoggedInUserId();
		ArchiveService archiveService = this.getServiceLocater().getArchiveService();
			
		RestoreOptions restoreOptions = archiveService.getMessageRestoreOptions(loggedInUser,userId);
		Assert.notNull(restoreOptions);
		
	}
	
	@Test
	public void testArchiveGetJrnlRestoreOptions(){
		String loggedInUser = this.getLoggedInUser();
		Long userId = this.getLoggedInUserId();
		ArchiveService archiveService = this.getServiceLocater().getArchiveService();
		
		RestoreOptions jrnlRestoreOptions = archiveService.getJrnlRestoreOptions(loggedInUser,userId);
		Assert.notNull(jrnlRestoreOptions);
		
	}

	@Test
	public void testArchiveExec(){
		
		String loggedInUser = this.getLoggedInUser();
		Long userId = this.getLoggedInUserId();
		ArchiveService archiveService = this.getServiceLocater().getArchiveService();
		AdminService adminService = this.getServiceLocater().getAdminService();

		Calendar instance = Calendar.getInstance();		
		instance.set(2011, Calendar.JANUARY, 1);
		Date dateFrom = instance.getTime();
		instance.set(2011, Calendar.DECEMBER, 30);
		Date dateTo = instance.getTime();		
		
		ArchiveOptions archiveOptions = archiveService.getMessageArchiveOptions(loggedInUser,userId);
		Assert.notNull(archiveOptions);
		
		
		List<LoaderConnection> loaderConnections = adminService.getLoaderConnections(loggedInUser);
		
		List<MessageArchiveSettings>messageArchiveSettingsList = new ArrayList<MessageArchiveSettings>();
		for (LoaderConnection loaderConnection : loaderConnections) {
			MessageArchiveSettings messageArchiveSettings = archiveService.getMessageArchiveSettings(loggedInUser, userId,loaderConnection.getAid());
			
			Assert.notNull(messageArchiveSettings);
			messageArchiveSettingsList.add(messageArchiveSettings);
			
			
		}
		
		MessageArchiveOptions messageArchiveOptions = (MessageArchiveOptions)archiveOptions;
		messageArchiveOptions.setMessageArchiveSettings(messageArchiveSettingsList);
		messageArchiveOptions.setUserName("side");
		messageArchiveOptions.setPassword("edis");
		messageArchiveOptions.setServerName("VM_26");
		
		messageArchiveOptions.setDateFrom(dateFrom);
		messageArchiveOptions.setDateTo(dateTo);
		
		
		
		Process process = archiveService.createMessageArchiveProcess(messageArchiveOptions);
		
		Assert.notNull(process);
		
		InputStream inputStream = process.getInputStream();
		
		int read= 0;
		
		try {
			while((read = inputStream.read())>0){
				
				System.out.print((char)read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		process.destroy();
		
	}
	
	@Test
	public void getRestoredSets(){
		
		ArchiveService archiveService = this.getServiceLocater().getArchiveService();
		Calendar instance = Calendar.getInstance();		
		instance.set(2012, Calendar.JANUARY, 1);
		Date dateFrom = instance.getTime();
		instance.set(2012, Calendar.DECEMBER, 30);
		Date dateTo = instance.getTime();
		
		List<RestoreSet> restoreSetList = archiveService.getRestoreSet("side", 0L, dateFrom, dateTo);
		
		Assert.notNull(restoreSetList);
		Assert.notEmpty(restoreSetList);
		
		for (RestoreSet restoreSet : restoreSetList) {
			
			Long size =-1L;
			
			String archivePath = "E:\\archived files\\Alliance_0\\";
			archivePath = (archivePath.endsWith(File.separator))? archivePath:archivePath+File.separator;
			
			String fullPath = String.format("%s%s_%d", archivePath, restoreSet.getName(),0);
			File file = new File(fullPath);
			
			if(file.isDirectory()){
				File[] listFiles = file.listFiles();
				for (File file2 : listFiles) {
					if(file2.isFile()){
						size+= file2.length();
					}
				}
			}
			
			if( size>-1L ){
				size+=1L;
				restoreSet.setSize(size.toString());
			}				
		}
		
		}
	@Test
	public void getArchiveVersion() {

		String fullPath = "E:\\archived files\\Alliance_0\\MEAR_20120118\\";
		Double retValue = -1.0;
		
		File file = new File(fullPath);
		
		if(file != null && file.isDirectory()){
			
			String name = file.getName();
			if(name.startsWith("MEAR")){
				String configName = String.format("%s%s%s.inf", fullPath, File.separator, name);
				File configFile = new File(configName);
				try {
					FileInputStream fileInputStream = new FileInputStream(configFile);
					InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
					
					int ch =0;
					StringBuffer buffer = new StringBuffer();
					while( (ch = inputStreamReader.read())>-1)
					{
						buffer.append((char) ch);
					}
					inputStreamReader.close();
					
					String string = buffer.toString();
					int indexOf = string.indexOf("Version=");
					if(indexOf > -1){
						indexOf+=8;
						retValue =  Double.parseDouble(string.substring(indexOf, indexOf+3));

					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				retValue =  -1.0; 
			}
		}
		else
		{
			retValue =  -1.0;
		}
		
		System.out.println("Return Value = " + retValue);
	}
}

