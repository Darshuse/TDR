package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.MessageSearchRequest;

@Repository
public interface MessageSearchRequestRepository extends JpaRepository<MessageSearchRequest, Integer> {

}
