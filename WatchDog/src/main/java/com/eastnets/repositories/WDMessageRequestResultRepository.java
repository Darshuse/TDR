package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.WDMessageRequestResult;

@Repository
public interface WDMessageRequestResultRepository extends JpaRepository<WDMessageRequestResult, Long>, JpaSpecificationExecutor<WDMessageRequestResult> {

}
