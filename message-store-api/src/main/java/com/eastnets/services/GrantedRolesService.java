package com.eastnets.services;

import java.util.Optional;

import com.eastnets.entities.GrantedRoles;
import com.eastnets.entities.GrantedRolesId;

public interface GrantedRolesService {

	public Optional<GrantedRoles> findGrantedRolesById(GrantedRolesId grantedRolesId);

}
