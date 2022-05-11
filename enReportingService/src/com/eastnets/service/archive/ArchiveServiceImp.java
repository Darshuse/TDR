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

package com.eastnets.service.archive;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.eastnets.dao.archive.ArchiveDAO;
import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.common.Constants;
import com.eastnets.domain.ApplicationSetting;
import com.eastnets.domain.archive.ArchiveLog;
import com.eastnets.domain.archive.ArchiveOptions;
import com.eastnets.domain.archive.JrnlArchiveOptions;
import com.eastnets.domain.archive.JrnlArchiveSettings;
import com.eastnets.domain.archive.JrnlRestoreOptions;
import com.eastnets.domain.archive.MessageArchiveOptions;
import com.eastnets.domain.archive.MessageArchiveSettings;
import com.eastnets.domain.archive.MessageRestoreOptions;
import com.eastnets.domain.archive.RestoreOptions;
import com.eastnets.domain.archive.RestoreSet;
import com.eastnets.service.ServiceBaseImp;
import com.eastnets.utils.ApplicationUtils;

/**
 * Archive Service Implementation
 * 
 * @author EastNets
 * @since July 22, 2012
 */
public abstract class ArchiveServiceImp extends ServiceBaseImp implements ArchiveService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4041418294601616409L;
	private ArchiveDAO archiveDAO;
	private CommonDAO commonDAO;

	private List<String> saaVersionsList;

	protected abstract String[] getArgs(MessageArchiveOptions messageArchiveOptions);

	protected abstract String[] getArgs(MessageRestoreOptions messageRestoreOptions);

	protected abstract String[] getSwiftArgs(MessageRestoreOptions messageRestoreOptions);

	protected abstract String[] getArgs(JrnlArchiveOptions jrnlArchiveOptions);

	protected abstract Map<String[], Integer> getArgs(JrnlRestoreOptions jrnlArchiveOptions, int countOfArch);

	protected abstract Map<String[], Integer> getSwiftArgs(JrnlRestoreOptions jrnlArchiveOptions, int countOfArch);

	public ArchiveDAO getArchiveDAO() {
		return archiveDAO;
	}

	public void setArchiveDAO(ArchiveDAO archiveDAO) {
		this.archiveDAO = archiveDAO;
	}

	public CommonDAO getCommonDAO() {
		return commonDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}

	public List<String> getSaaVersionsList() {
		return saaVersionsList;
	}

	public void setSaaVersionsList(List<String> saaVersionsList) {
		this.saaVersionsList = saaVersionsList;
	}

	@Override
	public Process createMessageArchiveProcess(MessageArchiveOptions messageArchiveOptions) {
		Process process = null;
		Runtime runtime = Runtime.getRuntime();

		String cmdArray[] = getArgs(messageArchiveOptions);
		String[] environmentArgs = getEnvironmentArgs();

		try {
			process = runtime.exec(cmdArray, environmentArgs);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return process;
	}

	@Override
	public Process createMessageRestoreProcess(MessageRestoreOptions messageRestoreOptions) {
		Process process = null;
		Runtime runtime = Runtime.getRuntime();

		String cmdArray[] = getArgs(messageRestoreOptions);
		String[] environmentArgs = getEnvironmentArgs();
		try {
			process = runtime.exec(cmdArray, environmentArgs);
		} catch (IOException e) {

		}

		return process;
	}

	@Override
	public Process createSwiftMessageRestoreProcess(MessageRestoreOptions messageRestoreOptions) {
		Process process = null;
		Runtime runtime = Runtime.getRuntime();

		String cmdArray[] = getSwiftArgs(messageRestoreOptions);
		String[] environmentArgs = getEnvironmentArgs();
		try {
			process = runtime.exec(cmdArray, environmentArgs);
		} catch (IOException e) {

		}

		return process;
	}

	@Override
	public Process createJrnlArchiveProcess(JrnlArchiveOptions jrnlArchiveOptions) {
		Process process = null;
		Runtime runtime = Runtime.getRuntime();

		String cmdArray[] = getArgs(jrnlArchiveOptions);
		String[] environmentArgs = getEnvironmentArgs();

		try {
			process = runtime.exec(cmdArray, environmentArgs);
		} catch (IOException e) {

		}

		return process;
	}

	@Override
	public Map<Process, Integer> createJrnlRestoreProcess(JrnlRestoreOptions jrnlRestoreOptions, int countOfArch) {

		Process process = null;
		Map<Process, Integer> retvProcess = new ConcurrentHashMap<Process, Integer>();
		Map<String[], Integer> retvJrnlArgs = new ConcurrentHashMap<String[], Integer>();
		String[] cmdArray = null;
		String[] environmentArgs = null;
		int numOfArch = 0;

		Runtime runtime = Runtime.getRuntime();

		// get args JRNL archive tool
		retvJrnlArgs = getArgs(jrnlRestoreOptions, countOfArch);

		// only one entry
		for (Entry<String[], Integer> archiveOptionsArgs : retvJrnlArgs.entrySet()) {

			cmdArray = archiveOptionsArgs.getKey();
			numOfArch = archiveOptionsArgs.getValue();
		}

		environmentArgs = getEnvironmentArgs();

		try {

			process = runtime.exec(cmdArray, environmentArgs);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		retvProcess.put(process, numOfArch);
		return retvProcess;
	}

	@Override
	public Map<Process, Integer> createSwiftJrnlRestoreProcess(JrnlRestoreOptions jrnlRestoreOptions, int countOfArch) {

		Process process = null;
		Map<Process, Integer> retvProcess = new ConcurrentHashMap<Process, Integer>();
		Map<String[], Integer> retvSwiftArgs = new ConcurrentHashMap<String[], Integer>();
		String[] cmdArray = null;
		String[] environmentArgs = null;
		int numOfArch = 0;

		Runtime runtime = Runtime.getRuntime();
		// get args SWIFT JRNL archive tool
		retvSwiftArgs = getSwiftArgs(jrnlRestoreOptions, countOfArch);

		// only one entry
		for (Entry<String[], Integer> archiveOptionsArgs : retvSwiftArgs.entrySet()) {

			cmdArray = archiveOptionsArgs.getKey();
			numOfArch = archiveOptionsArgs.getValue();
		}

		environmentArgs = getEnvironmentArgs();

		try {
			process = runtime.exec(cmdArray, environmentArgs);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		retvProcess.put(process, numOfArch);
		return retvProcess;
	}

	@Override
	public ArchiveOptions getMessageArchiveOptions(String loggedInUser, Long userId) {

		long allianceId = 0L;

		List<ApplicationSetting> archicveSettingsList = getCommonDAO().getApplicationSettings(allianceId, userId, Constants.APPLICATION_ARCHICVE_OPTIONS);

		if (archicveSettingsList == null || archicveSettingsList.size() == 0) {
			return null;
		}

		MessageArchiveOptions messageArchiveOptions = new MessageArchiveOptions();
		for (ApplicationSetting applicationSetting : archicveSettingsList) {

			String fieldName = applicationSetting.getFieldName();
			String fieldValue = applicationSetting.getFieldValue();
			if (fieldName.equals("ExeArchivePath")) {
				fieldValue = (fieldValue == null) ? null : fieldValue.trim();
				messageArchiveOptions.setExecPath(fieldValue);
			} else if (fieldName.equals("MsgByTransaction")) {
				messageArchiveOptions.setTransactionSize(Long.parseLong(fieldValue));
			} else if (fieldName.equals("Delete")) {
				messageArchiveOptions.setDelete((fieldValue.equals("True")) ? true : false);
			}

		}

		return messageArchiveOptions;
	}

	@Override
	public ArchiveOptions getJrnlArchiveOptions(String loggedInUser, Long userId) {

		long allianceId = 0L;

		List<ApplicationSetting> archicveSettingsList = getCommonDAO().getApplicationSettings(allianceId, userId, Constants.APPLICATION_ARCHICVE_OPTIONS);

		if (archicveSettingsList == null || archicveSettingsList.size() == 0) {
			return null;
		}

		JrnlArchiveOptions jrnlArchiveOptions = new JrnlArchiveOptions();

		for (ApplicationSetting applicationSetting : archicveSettingsList) {

			String fieldName = applicationSetting.getFieldName();
			String fieldValue = applicationSetting.getFieldValue();
			if (fieldName.equals("ExeJournalPath")) {
				fieldValue = (fieldValue == null) ? null : fieldValue.trim();
				jrnlArchiveOptions.setExecPath(fieldValue);
			} else if (fieldName.equals("EventsByTransaction")) {
				jrnlArchiveOptions.setTransactionSize(Long.parseLong(fieldValue));
			}
		}

		return jrnlArchiveOptions;
	}

	@Override
	public RestoreOptions getMessageRestoreOptions(String loggedInUser, Long userId) {

		long allianceId = 0L;

		List<ApplicationSetting> archicveSettingsList = getCommonDAO().getApplicationSettings(allianceId, userId, Constants.APPLICATION_ARCHICVE_OPTIONS);

		if (archicveSettingsList == null || archicveSettingsList.size() == 0) {
			return null;
		}

		MessageRestoreOptions messageRestoreOptions = new MessageRestoreOptions();

		for (ApplicationSetting applicationSetting : archicveSettingsList) {

			String fieldName = applicationSetting.getFieldName();
			String fieldValue = applicationSetting.getFieldValue();
			if (fieldName.equals("ExeRestorePath")) {
				fieldValue = (fieldValue == null) ? null : fieldValue.trim();
				messageRestoreOptions.setExecPath(fieldValue);
			} else if (fieldName.equals("ShowRestored")) {
				messageRestoreOptions.setShowRestored((fieldValue.equalsIgnoreCase("TRUE") ? true : false));
			}
		}

		return messageRestoreOptions;
	}

	@Override
	public RestoreOptions getJrnlRestoreOptions(String loggedInUser, Long userId) {

		long allianceId = 0L;

		List<ApplicationSetting> archicveSettingsList = getCommonDAO().getApplicationSettings(allianceId, userId, Constants.APPLICATION_ARCHICVE_OPTIONS);

		if (archicveSettingsList == null || archicveSettingsList.size() == 0) {
			return null;
		}

		JrnlRestoreOptions jrnlRestoreOptions = new JrnlRestoreOptions();

		for (ApplicationSetting applicationSetting : archicveSettingsList) {

			String fieldName = applicationSetting.getFieldName();

			String fieldValue = applicationSetting.getFieldValue();
			if (fieldName.equals("ExeJournalPath")) {
				fieldValue = (fieldValue == null) ? null : fieldValue.trim();
				jrnlRestoreOptions.setExecPath(fieldValue);
			} else if (fieldName.equals("EventsByTransaction")) {
				jrnlRestoreOptions.setTransactionSize(Long.parseLong(fieldValue));
			}
		}

		return jrnlRestoreOptions;
	}

	@Override
	public void updateMessageArchiveOptions(String loggedInUser, Long userId, ArchiveOptions archiveOptions) {

		Long allainceId = 0L;

		ApplicationSetting applicationSetting = new ApplicationSetting();

		applicationSetting.setAllianceID(allainceId);
		applicationSetting.setId(Constants.APPLICATION_ARCHICVE_OPTIONS);
		applicationSetting.setUserID(userId);

		applicationSetting.setFieldName("ExeArchivePath");
		String execPath = archiveOptions.getExecPath();
		execPath = (execPath == null) ? null : execPath.trim();
		applicationSetting.setFieldValue(execPath);
		archiveOptions.setExecPath(execPath);
		this.getCommonDAO().updateApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("MsgByTransaction");
		applicationSetting.setFieldValue(archiveOptions.getTransactionSize().toString());
		this.getCommonDAO().updateApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("Delete");
		applicationSetting.setFieldValue((archiveOptions.isDelete()) ? "True" : "False");
		this.getCommonDAO().updateApplicationSetting(applicationSetting);

	}

	@Override
	public void updateMessageRestoreOptions(String loggedInUser, Long userId, RestoreOptions restoreOptions) {

		ApplicationSetting applicationSetting = new ApplicationSetting();

		applicationSetting.setAllianceID(0L);
		applicationSetting.setId(Constants.APPLICATION_ARCHICVE_OPTIONS);
		applicationSetting.setUserID(userId);

		applicationSetting.setFieldName("ExeRestorePath");
		String execPath = restoreOptions.getExecPath();
		execPath = (execPath == null) ? null : execPath.trim();
		applicationSetting.setFieldValue(execPath);
		restoreOptions.setExecPath(execPath);
		this.getCommonDAO().updateApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowRestored");
		applicationSetting.setFieldValue(restoreOptions.isShowRestored() ? "True" : "False");
		this.getCommonDAO().updateApplicationSetting(applicationSetting);

	}

	@Override
	public void updateJrnlArchiveOptions(String loggedInUser, Long userId, ArchiveOptions archiveOptions) {

		Long allianceId = 0L;

		ApplicationSetting applicationSetting = new ApplicationSetting();

		applicationSetting.setAllianceID(allianceId);
		applicationSetting.setId(Constants.APPLICATION_ARCHICVE_OPTIONS);
		applicationSetting.setUserID(userId);

		applicationSetting.setFieldName("ExeJournalPath");
		String execPath = archiveOptions.getExecPath();
		execPath = (execPath == null) ? null : execPath.trim();
		applicationSetting.setFieldValue(execPath);
		archiveOptions.setExecPath(execPath);
		this.getCommonDAO().updateApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("EventsByTransaction");
		applicationSetting.setFieldValue(archiveOptions.getTransactionSize().toString());
		this.getCommonDAO().updateApplicationSetting(applicationSetting);

	}

	@Override
	public void updateMessageArchiveSettings(String loggedInUser, Long userId, MessageArchiveSettings messageArchiveSettings) {

		ApplicationSetting applicationSetting = new ApplicationSetting();

		applicationSetting.setAllianceID(messageArchiveSettings.getAllianceId());
		applicationSetting.setId(Constants.APPLICATION_MESG_ARCHICVE_SETTING);
		applicationSetting.setUserID(userId);

		applicationSetting.setFieldName("SideArcPath");
		String archivePath = messageArchiveSettings.getArchivePath();
		archivePath = (archivePath == null) ? null : archivePath.trim();
		applicationSetting.setFieldValue(archivePath);
		messageArchiveSettings.setArchivePath(archivePath);
		getCommonDAO().updateApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("SwiftArcPath");
		archivePath = messageArchiveSettings.getSwiftArchivePath();
		archivePath = (archivePath == null) ? null : archivePath.trim();
		applicationSetting.setFieldValue(archivePath);
		messageArchiveSettings.setSwiftArchivePath(archivePath);
		getCommonDAO().updateApplicationSetting(applicationSetting);

	}

	@Override
	public MessageArchiveSettings getMessageArchiveSettings(String loggedInUser, Long userId, Long allianceId) {

		List<ApplicationSetting> messageArchiveSettingsList = getCommonDAO().getApplicationSettings(Constants.APPLICATION_MESG_ARCHICVE_SETTING, userId, allianceId);

		if (messageArchiveSettingsList == null || messageArchiveSettingsList.size() == 0) {
			return null;
		}

		MessageArchiveSettings messageArchiveSettings = new MessageArchiveSettings();
		messageArchiveSettings.setAllianceId(allianceId);

		for (ApplicationSetting applicationSetting : messageArchiveSettingsList) {

			String fieldValue = applicationSetting.getFieldValue();
			fieldValue = (fieldValue == null) ? null : fieldValue.trim();
			if (applicationSetting.getFieldName().equals("SideArcPath")) {
				messageArchiveSettings.setArchivePath(fieldValue);
			} else {
				messageArchiveSettings.setSwiftArchivePath(fieldValue);
			}

		}

		return messageArchiveSettings;
	}

	@Override
	public JrnlArchiveSettings getJrnlArchiveSettings(String loggedInUser, Long userId, Long allianceId) {

		List<ApplicationSetting> jrnlArchiveSettingsList = getCommonDAO().getApplicationSettings(Constants.APPLICATION_JRNL_ARCHICVE_SETTING, userId, allianceId);

		if (jrnlArchiveSettingsList == null || jrnlArchiveSettingsList.size() == 0) {
			return null;
		}

		JrnlArchiveSettings jrnlArchiveSettings = new JrnlArchiveSettings();
		jrnlArchiveSettings.setAllianceId(allianceId);

		for (ApplicationSetting applicationSetting : jrnlArchiveSettingsList) {
			String fieldValue = applicationSetting.getFieldValue();
			fieldValue = (fieldValue == null) ? null : fieldValue.trim();
			if (applicationSetting.getFieldName().equals("SideJrnlPath")) {
				jrnlArchiveSettings.setJrnlArchivePath(fieldValue);
			} else {
				jrnlArchiveSettings.setSwiftJrnlArchivePath(fieldValue);
			}

		}

		return jrnlArchiveSettings;
	}

	@Override
	public void updateJrnlArchiveSettings(String loggedInUser, Long userId, JrnlArchiveSettings jrnlArchiveSettings) {

		ApplicationSetting applicationSetting = new ApplicationSetting();

		applicationSetting.setAllianceID(jrnlArchiveSettings.getAllianceId());
		applicationSetting.setId(Constants.APPLICATION_JRNL_ARCHICVE_SETTING);
		applicationSetting.setUserID(userId);

		applicationSetting.setFieldName("SideJrnlPath");
		String path = jrnlArchiveSettings.getJrnlArchivePath();
		path = (path == null) ? null : path.trim();
		applicationSetting.setFieldValue(path);
		jrnlArchiveSettings.setJrnlArchivePath(path);
		getCommonDAO().updateApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("SwiftJrnlPath");
		path = jrnlArchiveSettings.getSwiftJrnlArchivePath();
		path = (path == null) ? null : path.trim();
		applicationSetting.setFieldValue(path);
		jrnlArchiveSettings.setSwiftJrnlArchivePath(path);
		getCommonDAO().updateApplicationSetting(applicationSetting);

	}

	public List<RestoreSet> getRestoreSet(String loggedInUser, Long aid, Date dateFrom, Date dateTo) {

		String DateFromString = ApplicationUtils.formatDateTime(dateFrom);
		String dateToString = ApplicationUtils.formatDateTime(dateTo);
		List<RestoreSet> restoreSet = getArchiveDAO().getRestoreSet(aid, DateFromString, dateToString);

		return restoreSet;
	}

	public List<ArchiveLog> getArchiveLogs(String loggedInUser) {
		List<ArchiveLog> archiveLogs = getArchiveDAO().getArchiveLogs();
		return archiveLogs;
	}

	public List<ArchiveLog> getArchiveLogs(String loggedInUser, Long aid) {
		List<ArchiveLog> archiveLogs = null;
		if (aid == -1) {
			archiveLogs = getArchiveDAO().getArchiveLogs();
		} else {
			archiveLogs = getArchiveDAO().getArchiveLogs(aid);
		}
		return archiveLogs;
	}

	public List<ArchiveLog> getArchiveLogs(String loggedInUser, Long aid, Long moduleId) {
		List<ArchiveLog> archiveLogs = null;
		if (aid == -1) {
			archiveLogs = getArchiveDAO().getArchiveLogsByModuleId(moduleId);
		} else {
			archiveLogs = getArchiveDAO().getArchiveLogs(aid, moduleId);
		}
		return archiveLogs;
	}

	public ArchiveLog getArchiveLog(String loggedInUser, Long id) {
		return getArchiveDAO().getArchiveLog(id);
	}

	public void addArchiveLog(String loggedInUser, ArchiveLog archiveLog) {

		getArchiveDAO().addArchiveLog(archiveLog);
	}

	public ArchiveLog getArchiveLog(String loggedInUser, Long moduleId, Date creationTime, Long aid) {
		return getArchiveDAO().getArchiveLog(moduleId, creationTime, aid);
	}

	public void updateArchiveLog(String loggedInUser, ArchiveLog archiveLog) {
		getArchiveDAO().updateArchiveLog(archiveLog);
	}

	public void addArchiveSettings(String loggedInUser, ApplicationSetting applicationSetting) {
		getCommonDAO().addApplicationSetting(applicationSetting);
	}

	@Override
	public void addDefaultArchiveOptions(String loggedInUser, Long userId) {
		ApplicationSetting applicationSetting = new ApplicationSetting();

		applicationSetting.setId(Constants.APPLICATION_ARCHICVE_OPTIONS);
		applicationSetting.setUserID(userId);
		applicationSetting.setAllianceID(0L);

		applicationSetting.setFieldName("Delete");
		applicationSetting.setFieldValue("False");
		getCommonDAO().addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("EventsByTransaction");
		applicationSetting.setFieldValue("1000");
		getCommonDAO().addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ExeArchivePath");
		applicationSetting.setFieldValue("");
		getCommonDAO().addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ExeRestorePath");
		applicationSetting.setFieldValue("");
		getCommonDAO().addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ExeJournalPath");
		applicationSetting.setFieldValue("");
		getCommonDAO().addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("MsgByTransaction");
		applicationSetting.setFieldValue("10000");
		getCommonDAO().addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowRestored");
		applicationSetting.setFieldValue("False");
		getCommonDAO().addApplicationSetting(applicationSetting);

	}

	@Override
	public void addDefaultMessageArchiveSettings(String loggedInUser, Long userId, Long aid) {
		ApplicationSetting applicationSetting = new ApplicationSetting();

		applicationSetting.setId(Constants.APPLICATION_MESG_ARCHICVE_SETTING);
		applicationSetting.setUserID(userId);
		applicationSetting.setAllianceID(aid);

		applicationSetting.setFieldValue("");

		applicationSetting.setFieldName("SideArcPath");
		commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("SwiftArcPath");
		commonDAO.addApplicationSetting(applicationSetting);

	}

	@Override
	public void addDefaultJrnlArchiveSettings(String loggedInUser, Long userId, Long aid) {
		ApplicationSetting applicationSetting = new ApplicationSetting();

		applicationSetting.setId(Constants.APPLICATION_JRNL_ARCHICVE_SETTING);
		applicationSetting.setUserID(userId);
		applicationSetting.setAllianceID(aid);

		applicationSetting.setFieldValue(" ");

		applicationSetting.setFieldName("SideJrnlPath");
		commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("SwiftJrnlPath");
		commonDAO.addApplicationSetting(applicationSetting);
	}

	public String[] getEnvironmentArgs() {

		String[] retValue = null;

		String osName = System.getProperty("os.name");

		if (osName.equals("SunOS")) {
			retValue = new String[1];
			Map<String, String> getenv = System.getenv();
			String string = getenv.get("LD_LIBRARY_PATH");
			retValue[0] = String.format("LD_LIBRARY_PATH=%s", string);
		} else if (osName.equals("AIX")) {
			retValue = new String[1];
			Map<String, String> getenv = System.getenv();
			String string = getenv.get("LIBPATH");
			retValue[0] = String.format("LIBPATH=%s", string);
		}

		return retValue;
	}
}
