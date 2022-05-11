package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.JrnlSearchRequest;

@Repository
public interface JrnlSearchRequestRepository extends JpaRepository<JrnlSearchRequest, Integer> {

}
