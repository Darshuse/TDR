package com.eastnets.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.WDMessageSearchRequest;

@Repository
public interface WDMessageSearchRequestRepository extends JpaRepository<WDMessageSearchRequest, Integer>, JpaSpecificationExecutor<WDMessageSearchRequest> {

	@Query("SELECT w from WDMessageSearchRequest W where ( w.expirationDate IS NULL or w.expirationDate <= :creationDateTime)")
	Page<WDMessageSearchRequest> getMessagesNotificationRequests(Pageable pagable, @Param("creationDateTime") Date creationDateTime);

	@Modifying
	@Query("delete from  WDMessageSearchRequest w WHERE w.requestID = :requestID")
	void deleteMessageRequest(@Param("requestID") Integer requestID);

	@Query("SELECT w from WDMessageSearchRequest W where w.status = :networkDeliveryStatus")
	List<WDMessageSearchRequest> findAllRequestsWithMatchingStatus(@Param("networkDeliveryStatus") int networkDeliveryStatus);
}
