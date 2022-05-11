
package com.eastnets.textbreak.entities;

import java.util.Date;

//@Entity
//@Table(name = "RTEXTFIELDLOOP")
//@Cacheable(value = false)
public class TextFieldLoop {

	// @EmbeddedId
	private TextFieldLoopPK id;
	// @Column(name = "GROUP_ID")
	private long groupId = 0;
	// @Column(name = "GROUP_CNT")
	private long groupCnt;
	// @Column(name = "PARENT_GROUP_IDX")
	private long parentGroupIGdx = 0;

	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "X_CREA_DATE_TIME_MESG", nullable = true)
	private Date mesgCreaDateTime;

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

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
