package com.eastnets.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class LdParseMsgTypePK {
	
	@Column(name = "MESG_TYPE")
	private String mesgType;
	
}
