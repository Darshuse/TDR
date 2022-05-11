package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StxVersionPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4246643075183516095L;


	@Column(name="IDX")
	private long idx;

	public long getIdx() {
		return idx;
	}

	public void setIdx(long idx) {
		this.idx = idx;
	}

}
