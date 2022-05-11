package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.LiveMessage;

@Repository
public interface LiveMessageRepository extends JpaRepository<LiveMessage, Integer> {


}
