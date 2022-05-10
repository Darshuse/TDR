package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.WDPossibleDuplicateResult;


@Repository
public interface WDPossibleDuplicateResultRepository extends JpaRepository<WDPossibleDuplicateResult, Long> {

}
