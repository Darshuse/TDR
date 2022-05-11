package com.eastnets.domain;

import java.io.Serializable;

public class SortOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3676969153514878575L;
	
	public static final SortOrder ascending  = new SortOrder();
	public static final SortOrder descending = new SortOrder();
	public static final SortOrder unsorted   = new SortOrder();
	
	private SortOrder(){
	}

}
