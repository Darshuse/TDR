package com.eastnets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.LiveMessage;

@Repository
public interface LiveMessageRepository extends JpaRepository<LiveMessage, Integer> {

	@Query(value = "SELECT c FROM LiveMessage AS c")
	public List<LiveMessage> getLiveMessages();
}
