package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sGrantedRoles")
public class GrantedRoles implements Serializable {

	private static final long serialVersionUID = 935049377566649828L;
	@EmbeddedId
	private GrantedRolesId id;

	public GrantedRolesId getId() {
		return id;
	}

	public void setId(GrantedRolesId id) {
		this.id = id;
	}

}
