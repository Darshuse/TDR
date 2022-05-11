package com.eastnets.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.entities.GrantedRoles;
import com.eastnets.entities.GrantedRolesId;
import com.eastnets.repositories.GrantedRolesRepository;

@Service
public class GrantedRolesServiceImpl implements GrantedRolesService {

	@Autowired
	private GrantedRolesRepository grantedRolesRepository;

	@Override
	public Optional<GrantedRoles> findGrantedRolesById(GrantedRolesId grantedRolesId) {
		return grantedRolesRepository.findGrantedRolesById(grantedRolesId);
	}

}
