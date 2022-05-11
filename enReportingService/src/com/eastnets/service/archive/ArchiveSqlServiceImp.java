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

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.archive.ArchiveSettings;
import com.eastnets.domain.archive.JrnlArchiveOptions;
import com.eastnets.domain.archive.JrnlArchiveSettings;
import com.eastnets.domain.archive.JrnlRestoreOptions;
import com.eastnets.domain.archive.MessageArchiveOptions;
import com.eastnets.domain.archive.MessageArchiveSettings;
import com.eastnets.domain.archive.MessageRestoreOptions;
import com.eastnets.utils.ApplicationUtils;

/**
 * Archive SQL Service Implementation
 * 
 * @author EastNets
 * @since September 19, 2012
 */
public class ArchiveSqlServiceImp extends ArchiveServiceImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8673710231352630538L;

	@Override
	protected String[] getArgs(MessageArchiveOptions messageArchiveOptions) {

		String retValu[] = null;
		List<MessageArchiveSettings> messageArchiveSettingsList = messageArchiveOptions.getMessageArchiveSettings();

		Map<String, String> args = new ConcurrentHashMap<String, String>();
		int count = 1;

		if (messageArchiveSettingsList != null && messageArchiveSettingsList.size() > 0) {

			MessageArchiveSettings messageArchiveSettings = messageArchiveSettingsList.get(0);

			args.put("-a", messageArchiveSettings.getArchivePath());
			args.put("-x", messageArchiveOptions.getTransactionSize().toString());

			if (messageArchiveOptions.isTrustedConnection()) {
				count++;
			} else {
				args.put("-U", messageArchiveOptions.getUserName());
				args.put("-P", messageArchiveOptions.getPassword());

			}
			if (messageArchiveOptions.getInstanceName() == null || messageArchiveOptions.getInstanceName().isEmpty()) // if the default instance, add port to the server ip
				args.put("-S", messageArchiveOptions.getServerName() + "," + messageArchiveOptions.getDatabasePortNumber());
			else
				args.put("-S", messageArchiveOptions.getServerName());

			Date dateTo = messageArchiveOptions.getDateTo();
			String dateToString = ApplicationUtils.formatDate(dateTo, Constants.ARCHIVE_DATE_PATTERN);

			if (messageArchiveOptions.isDateRange()) {
				Date dateFrom = messageArchiveOptions.getDateFrom();
				String dateFromString = ApplicationUtils.formatDate(dateFrom, Constants.ARCHIVE_DATE_PATTERN);
				args.put("-from", dateFromString);
				args.put("-to", dateToString);
			} else {
				args.put("-d", dateToString);
			}

			if (messageArchiveSettingsList.size() == 1) {
				// only one alliance
				args.put("-i", messageArchiveSettings.getAllianceId().toString());

			}
			if (messageArchiveOptions.isIncludeIncomplete()) {
				args.put("-INCLUDEINCOMPLETE", "");
			}

		}

		if (messageArchiveOptions.isDelete() == false) {
			count++;
		}

		retValu = new String[(args.size() * 2) + count];

		int current = 0;
		if (messageArchiveOptions.getExecPath().endsWith(File.separator)) {
			retValu[current++] = String.format("%s%s", messageArchiveOptions.getExecPath(), Constants.ARCHIVE_BINRAY_NAME);

		} else {
			retValu[current++] = String.format("%s%s%s", messageArchiveOptions.getExecPath(), File.separator, Constants.ARCHIVE_BINRAY_NAME);

		}
		if (messageArchiveOptions.isDelete() == false) {
			retValu[current++] = "-NODELETE";
		}
		if (messageArchiveOptions.isTrustedConnection()) {
			retValu[current++] = "-E";
		}

		for (Entry<String, String> archiveOptionsArgs : args.entrySet()) {

			retValu[current++] = archiveOptionsArgs.getKey();
			retValu[current++] = archiveOptionsArgs.getValue();
		}

		return retValu;
	}

	@Override
	protected String[] getArgs(MessageRestoreOptions messageRestoreOptions) {

		String retValu[] = null;

		List<ArchiveSettings> restoreSettingsList = messageRestoreOptions.getRestoreSettings();

		Map<String, String> args = new ConcurrentHashMap<String, String>();

		if (restoreSettingsList == null || restoreSettingsList.size() <= 0) {
			return null;
		}

		int count = 1;

		MessageArchiveSettings restoreSettings = (MessageArchiveSettings) restoreSettingsList.get(0);

		if (messageRestoreOptions.isTrustedConnection()) {
			count++;
		} else {
			args.put("-U", messageRestoreOptions.getUserName());
			args.put("-P", messageRestoreOptions.getPassword());

		}

		if (messageRestoreOptions.getInstanceName() == null || messageRestoreOptions.getInstanceName().isEmpty()) // if the default instance, add port to the server ip
			args.put("-S", messageRestoreOptions.getServerName() + "," + messageRestoreOptions.getDatabasePortNumber());
		else
			args.put("-S", messageRestoreOptions.getServerName());

		args.put("-a", restoreSettings.getArchivePath());

		List<String> selectedArchives = restoreSettings.getSelectedArchives();

		args.put("-i", restoreSettings.getAllianceId().toString());

		args.put("-t", "SIDE");

		int current = 0;

		int size = args.size() + selectedArchives.size();
		if (messageRestoreOptions.isShowRestored()) {
			count++;
		}
		if (messageRestoreOptions.isForce()) {
			count++;
		}

		retValu = new String[(size * 2) + count];

		if (messageRestoreOptions.getExecPath().endsWith(File.separator)) {
			retValu[current++] = String.format("%s%s", messageRestoreOptions.getExecPath(), Constants.RESTORE_BINRAY_NAME);

		} else {
			retValu[current++] = String.format("%s%s%s", messageRestoreOptions.getExecPath(), File.separator, Constants.RESTORE_BINRAY_NAME);

		}

		if (messageRestoreOptions.isTrustedConnection()) {
			retValu[current++] = "-E";
		}

		for (Entry<String, String> archiveOptionsArgs : args.entrySet()) {

			retValu[current++] = archiveOptionsArgs.getKey();
			retValu[current++] = archiveOptionsArgs.getValue();
		}

		for (String selectedArchive : selectedArchives) {
			retValu[current++] = "-f";
			retValu[current++] = selectedArchive;
		}

		if (messageRestoreOptions.isShowRestored()) {
			retValu[current++] = "-o";
		}
		if (messageRestoreOptions.isForce()) {
			retValu[current++] = "-force";
		}

		return retValu;

	}

	@Override
	protected String[] getSwiftArgs(MessageRestoreOptions messageRestoreOptions) {

		String retValu[] = null;

		List<ArchiveSettings> restoreSettingsList = messageRestoreOptions.getRestoreSettings();

		Map<String, String> args = new ConcurrentHashMap<String, String>();

		if (restoreSettingsList == null || restoreSettingsList.size() <= 0) {
			return null;
		}

		int count = 1;

		MessageArchiveSettings restoreSettings = (MessageArchiveSettings) restoreSettingsList.get(0);

		if (messageRestoreOptions.isTrustedConnection()) {
			count++;
		} else {
			args.put("-U", messageRestoreOptions.getUserName());
			args.put("-P", messageRestoreOptions.getPassword());

		}

		if (messageRestoreOptions.getInstanceName() == null || messageRestoreOptions.getInstanceName().isEmpty()) // if the default instance, add port to the server ip
			args.put("-S", messageRestoreOptions.getServerName() + "," + messageRestoreOptions.getDatabasePortNumber());
		else
			args.put("-S", messageRestoreOptions.getServerName());
		args.put("-a", restoreSettings.getSwiftArchivePath());

		List<String> selectedArchives = restoreSettings.getSelectedArchives();

		args.put("-i", restoreSettings.getAllianceId().toString());

		args.put("-t", "SWIFT");
		args.put("-M", messageRestoreOptions.getSwiftVersion());

		int current = 0;

		int size = args.size() + selectedArchives.size();
		if (messageRestoreOptions.isShowRestored()) {
			count++;
		}
		if (messageRestoreOptions.isForce()) {
			count++;
		}

		retValu = new String[(size * 2) + count];

		if (messageRestoreOptions.getExecPath().endsWith(File.separator)) {
			retValu[current++] = String.format("%s%s", messageRestoreOptions.getExecPath(), Constants.RESTORE_BINRAY_NAME);

		} else {
			retValu[current++] = String.format("%s%s%s", messageRestoreOptions.getExecPath(), File.separator, Constants.RESTORE_BINRAY_NAME);

		}

		if (messageRestoreOptions.isTrustedConnection()) {
			retValu[current++] = "-E";
		}

		for (Entry<String, String> archiveOptionsArgs : args.entrySet()) {

			retValu[current++] = archiveOptionsArgs.getKey();
			retValu[current++] = archiveOptionsArgs.getValue();
		}

		for (String selectedArchive : selectedArchives) {
			retValu[current++] = "-f";
			retValu[current++] = selectedArchive;
		}

		if (messageRestoreOptions.isShowRestored()) {
			retValu[current++] = "-o";
		}
		if (messageRestoreOptions.isForce()) {
			retValu[current++] = "-force";
		}

		return retValu;

	}

	@Override
	protected String[] getArgs(JrnlArchiveOptions jrnlArchiveOptions) {
		String retValu[] = null;
		List<JrnlArchiveSettings> jrnlArchiveSettingsList = jrnlArchiveOptions.getJrnlArchiveSettings();

		Map<String, String> args = new ConcurrentHashMap<String, String>();

		int count = 2;

		if (jrnlArchiveSettingsList != null && jrnlArchiveSettingsList.size() > 0) {

			JrnlArchiveSettings jrnlArchiveSettings = jrnlArchiveSettingsList.get(0);
			if (jrnlArchiveSettingsList.size() > 1) {
				for (JrnlArchiveSettings jrnlArchiveSetting : jrnlArchiveSettingsList) {
					if (jrnlArchiveSetting.getAllianceId() == 0L) {
						jrnlArchiveSettings = jrnlArchiveSetting;
						break;
					}
				}
			}

			if (jrnlArchiveOptions.isTrustedConnection()) {
				count++;
			} else {
				args.put("-U", jrnlArchiveOptions.getUserName());
				args.put("-P", jrnlArchiveOptions.getPassword());

			}

			if (jrnlArchiveOptions.getInstanceName() == null || jrnlArchiveOptions.getInstanceName().isEmpty()) // if the default instance, add port to the server ip
				args.put("-S", jrnlArchiveOptions.getServerName() + "," + jrnlArchiveOptions.getDatabasePortNumber());
			else
				args.put("-S", jrnlArchiveOptions.getServerName());

			args.put("-a", jrnlArchiveSettings.getJrnlArchivePath());
			args.put("-x", jrnlArchiveOptions.getTransactionSize().toString());
			Date dateTo = jrnlArchiveOptions.getDateTo();

			String format = ApplicationUtils.formatDate(dateTo, Constants.ARCHIVE_DATE_PATTERN);

			args.put("-d", format);

			if (jrnlArchiveSettingsList.size() == 1) {
				// only one alliance
				args.put("-I", jrnlArchiveSettings.getAllianceId().toString());

			}

		}

		retValu = new String[(args.size() * 2) + count];

		int current = 0;

		if (jrnlArchiveOptions.getExecPath().endsWith(File.separator)) {
			retValu[current++] = String.format("%s%s", jrnlArchiveOptions.getExecPath(), Constants.ARCHIVE_JRNL_BINRAY_NAME);

		} else {
			retValu[current++] = String.format("%s%s%s", jrnlArchiveOptions.getExecPath(), File.separator, Constants.ARCHIVE_JRNL_BINRAY_NAME);

		}

		retValu[current++] = "-archive";

		if (jrnlArchiveOptions.isTrustedConnection()) {
			retValu[current++] = "-E";
		}

		for (Entry<String, String> archiveOptionsArgs : args.entrySet()) {

			retValu[current++] = archiveOptionsArgs.getKey();
			retValu[current++] = archiveOptionsArgs.getValue();
		}

		return retValu;
	}

	@Override
	protected Map<String[], Integer> getArgs(JrnlRestoreOptions jrnlRestoreOptions, int countOfArch) {

		Map<String[], Integer> retv = new ConcurrentHashMap<String[], Integer>();
		String retValu[] = null;

		List<ArchiveSettings> restoreSettingsList = jrnlRestoreOptions.getRestoreSettings();
		Map<String, String> args = new ConcurrentHashMap<String, String>();
		JrnlArchiveSettings jrnlArchiveSettings = null;
		List<String> selectedArchives = null;

		if (restoreSettingsList != null && restoreSettingsList.size() > 0) {

			jrnlArchiveSettings = (JrnlArchiveSettings) restoreSettingsList.get(0);

			// -restore
			args.put("-restore", "");
			// args.put("-v", "");

			if (jrnlRestoreOptions.isTrustedConnection()) {
				// -E for trust connection no need to username & pass
				args.put("-E", "");
			} else {
				// -U Username database connection
				args.put("-U", jrnlRestoreOptions.getUserName());
				// -P Password database connection
				args.put("-P", jrnlRestoreOptions.getPassword());

			}

			if (jrnlRestoreOptions.getInstanceName() == null || jrnlRestoreOptions.getInstanceName().isEmpty()) // if the default instance, add port to the server ip
				// -S serverName + port
				args.put("-S", jrnlRestoreOptions.getServerName() + "," + jrnlRestoreOptions.getDatabasePortNumber());
			else
				// -S serverName
				args.put("-S", jrnlRestoreOptions.getServerName());

			// -a archive path
			args.put("-a", jrnlArchiveSettings.getJrnlArchivePath());

			// -x transaction size
			args.put("-x", jrnlRestoreOptions.getTransactionSize().toString());

			// -i alliance id
			args.put("-I", jrnlArchiveSettings.getAllianceId().toString());

			selectedArchives = jrnlArchiveSettings.getSelectedArchives();

			if (selectedArchives != null && selectedArchives.size() > 0)
				// -f archive name
				args.put("-f", selectedArchives.get(countOfArch));
			else
				// -f empty archive name
				args.put("-f", "");

		}

		int size = args.size();
		retValu = new String[(size * 2) + 1];
		int current = 0;

		// executable binary path
		if (jrnlRestoreOptions.getExecPath().endsWith(File.separator)) {

			retValu[current++] = String.format("%s%s", jrnlRestoreOptions.getExecPath(), Constants.ARCHIVE_JRNL_BINRAY_NAME);
		} else {
			retValu[current++] = String.format("%s%s%s", jrnlRestoreOptions.getExecPath(), File.separator, Constants.ARCHIVE_JRNL_BINRAY_NAME);
		}

		for (Entry<String, String> archiveOptionsArgs : args.entrySet()) {

			retValu[current++] = archiveOptionsArgs.getKey();
			retValu[current++] = archiveOptionsArgs.getValue();
		}

		retv.put(retValu, selectedArchives.size());

		return retv;
	}

	@Override
	protected Map<String[], Integer> getSwiftArgs(JrnlRestoreOptions jrnlRestoreOptions, int countOfArch) {

		Map<String[], Integer> retv = new ConcurrentHashMap<String[], Integer>();
		String retValu[] = null;

		List<ArchiveSettings> restoreSettingsList = jrnlRestoreOptions.getRestoreSettings();

		Map<String, String> args = new ConcurrentHashMap<String, String>();

		JrnlArchiveSettings jrnlArchiveSettings = null;
		List<String> selectedArchives = null;

		if (restoreSettingsList != null && restoreSettingsList.size() > 0) {

			jrnlArchiveSettings = (JrnlArchiveSettings) restoreSettingsList.get(0);

			// -restore
			args.put("-restore", "");

			if (jrnlRestoreOptions.isTrustedConnection()) {
				// -E for trust connection no need to username & pass
				args.put("-E", "");
			} else {
				// -U Username database connection
				args.put("-U", jrnlRestoreOptions.getUserName());
				// -P Password database connection
				args.put("-P", jrnlRestoreOptions.getPassword());

			}

			if (jrnlRestoreOptions.getInstanceName() == null || jrnlRestoreOptions.getInstanceName().isEmpty()) // if the default instance, add port to the server ip
				// -S serverName + port
				args.put("-S", jrnlRestoreOptions.getServerName() + "," + jrnlRestoreOptions.getDatabasePortNumber());
			else
				// -S serverName
				args.put("-S", jrnlRestoreOptions.getServerName());

			// -a archive path
			args.put("-a", jrnlArchiveSettings.getSwiftJrnlArchivePath());

			// -x transaction size
			args.put("-x", jrnlRestoreOptions.getTransactionSize().toString());

			// -i alliance id
			args.put("-I", jrnlArchiveSettings.getAllianceId().toString());

			// -m saa_version
			args.put("-M", jrnlRestoreOptions.getSwiftVersion());

			selectedArchives = jrnlArchiveSettings.getSelectedArchives();

			if (selectedArchives != null && selectedArchives.size() > 0)
				// -f archive name
				args.put("-f", selectedArchives.get(countOfArch));
			else
				// -f empty archive name
				args.put("-f", "");
		}

		int size = args.size();
		retValu = new String[(size * 2) + 1];
		int current = 0;

		// executable binary path
		if (jrnlRestoreOptions.getExecPath().endsWith(File.separator))
			retValu[current++] = String.format("%s%s", jrnlRestoreOptions.getExecPath(), Constants.ARCHIVE_JRNL_BINRAY_NAME);
		else
			retValu[current++] = String.format("%s%s%s", jrnlRestoreOptions.getExecPath(), File.separator, Constants.ARCHIVE_JRNL_BINRAY_NAME);

		for (Entry<String, String> archiveOptionsArgs : args.entrySet()) {

			retValu[current++] = archiveOptionsArgs.getKey();
			retValu[current++] = archiveOptionsArgs.getValue();
		}

		retv.put(retValu, selectedArchives.size());

		return retv;
	}

}
