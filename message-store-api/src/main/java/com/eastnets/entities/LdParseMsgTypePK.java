package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LdParseMsgTypePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8694060074727110778L;
	@Column(name = "MESG_TYPE")
	private String mesgType;

}
