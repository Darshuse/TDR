package com.eastnets.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.GrantedRoles;
import com.eastnets.entities.GrantedRolesId;

@Repository
public interface GrantedRolesRepository extends JpaRepository<GrantedRoles, GrantedRolesId> {
	
	public Optional<GrantedRoles> findGrantedRolesById(GrantedRolesId sGrantedRolesId);

}
