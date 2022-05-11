
package com.eastnets.textbreak.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LdParseMsgTypePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "MESG_TYPE")
	private String mesgType;

}
