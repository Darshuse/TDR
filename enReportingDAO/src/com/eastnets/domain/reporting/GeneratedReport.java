package com.eastnets.domain.reporting;

import java.util.Arrays;
import java.util.Date;

import com.eastnets.domain.BaseEntity;

public class GeneratedReport  extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6316193020401457883L;

	private Integer id;

	private Long criteriaId;

	private Date startTime;

	private Date endTime;

	private Integer status;

	private String log;

	@Override
	public String toString() {
		return "GeneratedReport [id=" + id + ", criteriaId=" + criteriaId
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", status=" + status + ", log=" + log + ", format=" + format
				+ ", downloadCount=" + downloadCount + ", groupId=" + groupId
				+ ", blob=" + Arrays.toString(blob) + "]";
	}

	private String format;

	private Integer downloadCount;

	private Long groupId;
	
	private byte [] blob;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public byte [] getBlob() {
		return blob;
	}

	public void setBlob(byte [] blob) {
		this.blob = blob;
	}

	public Long getCriteriaId() {
		return criteriaId;
	}

	public void setCriteriaId(Long criteriaId) {
		this.criteriaId = criteriaId;
	}

}
