
package com.eastnets.textbreak.entities;

import java.io.Serializable;

/**
 * The primary key class for the RTEXT database table.
 * 
 */
// @Embeddable
public class TextFieldLoopPK implements Serializable {

	private static final long serialVersionUID = -4875394837997762738L;
	// @Column(name = "AID")
	private long aid;
	// @Column(name = "TEXT_S_UMIDL")
	private long textSUmidl;
	// @Column(name = "TEXT_S_UMIDH")
	private long textSUmidh;
	// @Column(name = "SEQUENCE_ID")
	private String sequenceId;
	// @Column(name = "GROUP_IDX")
	private long groupIdx;

	public long getGroupIdx() {
		return groupIdx;
	}

	public void setGroupIdx(long groupIdx) {
		this.groupIdx = groupIdx;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getTextSUmidl() {
		return textSUmidl;
	}

	public void setTextSUmidl(long textSUmidl) {
		this.textSUmidl = textSUmidl;
	}

	public long getTextSUmidh() {
		return textSUmidh;
	}

	public void setTextSUmidh(long textSUmidh) {
		this.textSUmidh = textSUmidh;
	}

	public String getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}
}
