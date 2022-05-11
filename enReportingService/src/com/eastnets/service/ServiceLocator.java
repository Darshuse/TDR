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

package com.eastnets.service;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.velocity.app.VelocityEngine;

import com.eastnets.domain.Config;
import com.eastnets.service.RelatedMessage.RelatedMessageService;
import com.eastnets.service.admin.AdminService;
import com.eastnets.service.archive.ArchiveService;
import com.eastnets.service.audit.AuditService;
import com.eastnets.service.bicloader.BICLoaderService;
import com.eastnets.service.common.CommonService;
import com.eastnets.service.common.MailService;
import com.eastnets.service.common.ReportService;
import com.eastnets.service.csm.ClientServerMonitorService;
import com.eastnets.service.dashboard.DashboardService;
import com.eastnets.service.dataAnalyzer.DataAnalyzerService;
import com.eastnets.service.dbcheck.DBCheckService;
import com.eastnets.service.filepayload.FilePayloadService;
import com.eastnets.service.gpi.GPIService;
import com.eastnets.service.jrnl.JrnlService;
import com.eastnets.service.ldap.LdapService;
import com.eastnets.service.license.LicenseService;
import com.eastnets.service.loader.monitoring.LoaderMonitoringService;
import com.eastnets.service.monitoring.MonitoringService;
import com.eastnets.service.reporting.ReportingService;
import com.eastnets.service.reports.repository.ReportsRepositoryService;
import com.eastnets.service.security.SecurityService;
import com.eastnets.service.security.data.SecurityDataService;
import com.eastnets.service.swing.SwingService;
import com.eastnets.service.viewer.ViewerService;
import com.eastnets.service.warning.notification.WarningNotifer;
import com.eastnets.service.watchdog.WatchDogService;

/**
 * Service Locator, encapsulates all services in service layer and used to lookup required service by presentation
 * 
 * @author EastNets
 * @since July 12, 2012
 */
public class ServiceLocator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1644898425097472273L;
	private SecurityService securityService;
	private WatchDogService watchDogService;
	private ViewerService viewerService;
	private CommonService commonService;
	private MailService mailService;
	private SwingService swingService;
	private JrnlService jrnlService;
	private AdminService adminService;
	private MonitoringService monitoringService;
	private ArchiveService archiveService;
	private RelatedMessageService relatedMessageService;
	private DataAnalyzerService dataAnalyzerService;

	private DashboardService dashboardService;
	private LicenseService licenseService;
	private ReportingService reportingService;
	private ReportService reportService;
	private VelocityEngine velocityEngine;
	private LdapService ldapService;
	private BICLoaderService bicLoaderService;
	private DBCheckService dbCheckService;
	private AuditService auditService;

	private FilePayloadService filePayloadService;
	private ClientServerMonitorService clientServerMonitorService;
	private ReportsRepositoryService reportsRepositoryService;

	private LoaderMonitoringService loaderNotificationService;
	private WarningNotifer warningNotifer;

	private SecurityDataService securityDataService;

	private GPIService gpiService;

	private Config config;

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	@Resource(name = "securityService")
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public BICLoaderService getBicLoaderService() {
		return bicLoaderService;
	}

	public void setBicLoaderService(BICLoaderService bicLoaderService) {
		this.bicLoaderService = bicLoaderService;
	}

	public DBCheckService getDbCheckService() {
		return dbCheckService;
	}

	public void setDbCheckService(DBCheckService dbCheckService) {
		this.dbCheckService = dbCheckService;
	}

	public WatchDogService getWatchDogService() {
		return watchDogService;
	}

	public void setWatchDogService(WatchDogService watchDogService) {
		this.watchDogService = watchDogService;
	}

	public ViewerService getViewerService() {
		return viewerService;
	}

	public void setViewerService(ViewerService viewerService) {
		this.viewerService = viewerService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public SwingService getSwingService() {
		return swingService;
	}

	public void setSwingService(SwingService swingService) {
		this.swingService = swingService;
	}

	public JrnlService getJrnlService() {
		return jrnlService;
	}

	public void setJrnlService(JrnlService jrnlService) {
		this.jrnlService = jrnlService;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public MonitoringService getMonitoringService() {
		return monitoringService;
	}

	public void setMonitoringService(MonitoringService monitoringService) {
		this.monitoringService = monitoringService;
	}

	public ArchiveService getArchiveService() {
		return archiveService;
	}

	public void setArchiveService(ArchiveService archiveService) {
		this.archiveService = archiveService;
	}

	public DashboardService getDashboardService() {
		return dashboardService;
	}

	public void setDashboardService(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public MailService getMailService() {
		return mailService;
	}

	/**
	 * @return the licenseService
	 */
	public LicenseService getLicenseService() {
		return licenseService;
	}

	/**
	 * @param licenseService
	 *            the licenseService to set
	 */
	public void setLicenseService(LicenseService licenseService) {
		this.licenseService = licenseService;
	}

	/**
	 * @param reportingService
	 *            the reportingService to set
	 */
	public ReportingService getReportingService() {
		return reportingService;
	}

	/**
	 * @param reportingService
	 *            the reportingService to set
	 */
	public void setReportingService(ReportingService reportingService) {
		this.reportingService = reportingService;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public FilePayloadService getFilePayloadService() {
		return filePayloadService;
	}

	public void setFilePayloadService(FilePayloadService filePayloadService) {
		this.filePayloadService = filePayloadService;
	}

	public ClientServerMonitorService getClientServerMonitorService() {
		return clientServerMonitorService;
	}

	public void setClientServerMonitorService(ClientServerMonitorService clientServerMonitorService) {
		this.clientServerMonitorService = clientServerMonitorService;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public LdapService getLdapService() {
		return ldapService;
	}

	public void setLdapService(LdapService ldapService) {
		this.ldapService = ldapService;
	}

	public ReportsRepositoryService getReportsRepositoryService() {
		return reportsRepositoryService;
	}

	public void setReportsRepositoryService(ReportsRepositoryService reportsRepositoryService) {
		this.reportsRepositoryService = reportsRepositoryService;
	}

	public RelatedMessageService getRelatedMessageService() {
		return relatedMessageService;
	}

	public void setRelatedMessageService(RelatedMessageService relatedMessageService) {
		this.relatedMessageService = relatedMessageService;
	}

	public AuditService getAuditService() {
		return auditService;
	}

	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}

	public GPIService getGpiService() {
		return gpiService;
	}

	public void setGpiService(GPIService gpiService) {
		this.gpiService = gpiService;
	}

	public DataAnalyzerService getDataAnalyzerService() {
		return dataAnalyzerService;
	}

	public void setDataAnalyzerService(DataAnalyzerService dataAnalyzerService) {
		this.dataAnalyzerService = dataAnalyzerService;
	}

	public LoaderMonitoringService getLoaderNotificationService() {
		return loaderNotificationService;
	}

	public void setLoaderMonitoringService(LoaderMonitoringService loaderNotificationService) {
		this.loaderNotificationService = loaderNotificationService;
	}

	public WarningNotifer getWarningNotifer() {
		return warningNotifer;
	}

	public void setWarningNotifer(WarningNotifer warningNotifer) {
		this.warningNotifer = warningNotifer;
	}

	public SecurityDataService getSecurityDataService() {
		return securityDataService;
	}

	public void setSecurityDataService(SecurityDataService securityDataService) {
		this.securityDataService = securityDataService;
	}

}
