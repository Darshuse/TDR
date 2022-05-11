package com.eastnets.entities;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RTEXTFIELDLOOP")
@Cacheable(value = false)
public class TextFieldLoop {

	@EmbeddedId
	private TextFieldLoopPK id;
	@Column(name = "GROUP_ID")
	private long groupId=0;
	@Column(name = "GROUP_CNT")
	private long groupCnt;
	@Column(name = "PARENT_GROUP_IDX")
	private long parentGroupIGdx=0;

	public TextFieldLoopPK getId() {
		return id;
	}

	public void setId(TextFieldLoopPK id) {
		this.id = id;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getGroupCnt() {
		return groupCnt;
	}

	public void setGroupCnt(long groupCnt) {
		this.groupCnt = groupCnt;
	}

	public long getParentGroupIGdx() {
		return parentGroupIGdx;
	}

	public void setParentGroupIGdx(long parentGroupIGdx) {
		this.parentGroupIGdx = parentGroupIGdx;
	}

}
