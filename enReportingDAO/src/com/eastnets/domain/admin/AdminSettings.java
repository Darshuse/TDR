package com.eastnets.domain.admin;

import java.io.Serializable;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.springframework.beans.BeanUtils;

import com.eastnets.encdec.AESEncryptDecrypt;

public class AdminSettings implements Serializable,Diffable<AdminSettings>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8721889912233495730L;

	private String archiveRestoreUser;
	private String archiveRestorePassword;

	public String getArchiveRestoreUser() {
		return archiveRestoreUser;
	}
	public void setArchiveRestoreUser(String archiveRestoreUser) {
		this.archiveRestoreUser = archiveRestoreUser;
	}
	public String getArchiveRestorePassword() {
		return archiveRestorePassword;
	}
	public void setArchiveRestorePassword(String archiveRestorePassword) {
		this.archiveRestorePassword = archiveRestorePassword;
	}
	public String getArchiveRestorePasswordEncrypted() {
		try {
			return AESEncryptDecrypt.encrypt(this.archiveRestorePassword);
		} catch (Exception e) {
			return this.archiveRestorePassword;
		}
	}
	public void setArchiveRestorePasswordEncrypted(String passwordEncrypted) {
		try {
			this.archiveRestorePassword = AESEncryptDecrypt.decrypt(passwordEncrypted);
		} catch (Exception e) {
			this.archiveRestorePassword = passwordEncrypted;
		}
	}

	// clone method
	public AdminSettings shallowClone() {
		AdminSettings adminSettingsOld = new AdminSettings();
		BeanUtils.copyProperties(this, adminSettingsOld);
		return adminSettingsOld;
	}


	@Override
	public DiffResult diff(AdminSettings oldAdminSettings) { 
		DiffBuilder compare = new DiffBuilder(this, oldAdminSettings, null,false);
		compare.append("Database user",this.getArchiveRestoreUser(),oldAdminSettings.getArchiveRestoreUser()); 
		compare.append("password",this.getArchiveRestorePassword(),oldAdminSettings.getArchiveRestorePassword()); 

		return compare.build();

	}

}
