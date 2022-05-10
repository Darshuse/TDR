package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.WDNackResult;

@Repository
public interface WDNackResultRepository extends JpaRepository<WDNackResult, Long> {

}
