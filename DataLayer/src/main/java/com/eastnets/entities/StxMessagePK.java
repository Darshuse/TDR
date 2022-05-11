package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StxMessagePK  implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5593918132612111463L;
	@Column(name="IDX")
	private long idx;

	public long getIdx() {
		return idx;
	}

	public void setIdx(long idx) {
		this.idx = idx;
	}

}
