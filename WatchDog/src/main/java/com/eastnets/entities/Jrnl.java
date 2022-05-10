package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RJRNL")
public class Jrnl {

	@EmbeddedId
	private JrnlPK jrnlPK;

	@Column(name = "JRNL_COMP_NAME")
	private String jrnlCompName;

	@Column(name = "JRNL_EVENT_IS_SECURITY")
	private Integer jrnlEventIsSecurity;

	@Column(name = "JRNL_EVENT_NUM")
	private Integer jrnlEventNum;

	@Column(name = "JRNL_EVENT_NAME")
	private String jrnlEventName;

	@Column(name = "JRNL_EVENT_CLASS")
	private String jrnlEventClass;

	@Column(name = "JRNL_EVENT_SEVERITY")
	private String jrnlEventServerity;

	@Column(name = "JRNL_EVENT_IS_ALARM")
	private Integer jrnlEventIsAlarm;

	@Column(name = "JRNL_APPL_SERV_NAME")
	private String jrnlApplServName;

	@Column(name = "JRNL_FUNC_NAME")
	private String jrnlFuncName;

	@Column(name = "JRNL_HOSTNAME")
	private String jrnlHostname;

	@Column(name = "JRNL_OPER_NICKNAME")
	private String jrnlOperNickName;

	@Column(name = "JRNL_DATE_TIME")
	private Date jrnlDateTime;

	@Column(name = "JRNL_ALARM_STATUS")
	private String jrnlAlarmStatus;

	@Column(name = "JRNL_ALARM_DIST_STATUS")
	private String jrnlAlarmDistStatus;

	@Column(name = "JRNL_ALARM_DATE_TIME")
	private Date jrnlAlarmDateTime;

	@Column(name = "JRNL_ALARM_OPER_NICKNAME")
	private String jrnlAlarmOperNickName;

	@Column(name = "JRNL_DISPLAY_TEXT")
	private String jrnlDisplayText;

	@Column(name = "JRNL_MERGED_TEXT")
	private String jrnlMergedText;

	@Column(name = "JRNL_LENGTH")
	private Integer jrnlLength;

	@Column(name = "JRNL_TOKEN")
	private Integer jrnlToken;

	@Column(name = "ARCHIVED")
	private Integer archiver;

	@Column(name = "RESTORED")
	private Integer restored;

	@Column(name = "LAST_UPDATE")
	private Date lastUpdate;

	@Column(name = "X_UMIDL")
	private Integer xUmidl;

	@Column(name = "X_UMIDH")
	private Integer xUmidh;

	@Column(name = "JRNL_IS_CONFIG_MGMT")
	private Integer jrnlIsConfigMgmt;

	@Column(name = "JRNL_BUSINESS_ENTITY_NAME")
	private String jrnlBusinessEntityName;

}
