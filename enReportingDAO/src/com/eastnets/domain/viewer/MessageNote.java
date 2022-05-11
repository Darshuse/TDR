package com.eastnets.domain.viewer;

import java.util.Date;

import com.eastnets.domain.BaseEntity;
import com.eastnets.domain.admin.User;

public class MessageNote extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7854441148545150245L;
	private Integer aid;
	private Integer mesgUmidl;
	private Integer mesgUmidh;
	private Long noteId;
	private String note;
	private Date creationDate;
	private User createdBy;

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getMesgUmidl() {
		return mesgUmidl;
	}

	public void setMesgUmidl(Integer mesgUmidl) {
		this.mesgUmidl = mesgUmidl;
	}

	public Integer getMesgUmidh() {
		return mesgUmidh;
	}

	public void setMesgUmidh(Integer mesgUmidh) {
		this.mesgUmidh = mesgUmidh;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Long getNoteId() {
		return noteId;
	}

	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}

}
