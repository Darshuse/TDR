package com.eastnets.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.JrnlPK;
import com.eastnets.entities.JrnlSearchRequest;
import com.eastnets.entities.WDJrnlKey;

@Repository
public interface WDJrnlKeyRepository extends JpaRepository<WDJrnlKey, JrnlPK> {

	@Query("SELECT c from JrnlSearchRequest C where c.componentName = :jrnlCompName and c.eventNumber = :jrnlEventNumber and ( c.expirationDate IS NULL or c.expirationDate <= :creationDateTime)")
	List<JrnlSearchRequest> matchJrnlRequestsWithKeys(@Param("jrnlCompName") String jrnlCompName,
			@Param("jrnlEventNumber") Integer jrnlEventNumber, @Param("creationDateTime") Date creationDateTime);

	@Modifying
	@Query("UPDATE WDJrnlKey w SET w.status = 1 where w.jrnlPK.aid = :aid and w.jrnlPK.jrnlRevDateTime = :jrnlRevDateTime and w.jrnlPK.jrnlSeqNumber = :jrnlSeqNumber")
	void updateJrnlReaderStatus(@Param("aid") Integer aid, @Param("jrnlRevDateTime") Integer jrnlRevDateTime,
			@Param("jrnlSeqNumber") Integer jrnlSeqNumber);

	@Query("SELECT w from WDJrnlKey W where w.status = 1")
	List<WDJrnlKey> restoreJrnlKeys();

	@Query("SELECT w from WDJrnlKey W where w.status = null or w.status = 0")
	Page<WDJrnlKey> getWdJrnlKeys(PageRequest pageRequest);
}
