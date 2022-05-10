package com.eastnets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.WDJrnlSearchRequest;

@Repository
public interface WDJrnlSearchRequestRepository extends JpaRepository<WDJrnlSearchRequest, Integer> {

	@Query("SELECT w from WDJrnlSearchRequest W where  w.componentName = :jrnlCompName and w.eventNumber = :jrnlEventNumber")
	List<WDJrnlSearchRequest> getWdJrnlSearchRequest(@Param("jrnlCompName") String jrnlCompName, @Param("jrnlEventNumber") Integer jrnlEventNumber);

}
