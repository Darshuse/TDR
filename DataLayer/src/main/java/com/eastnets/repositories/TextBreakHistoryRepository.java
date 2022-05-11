package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.LdTextbreakHistory;

/**
 * This is the Data Access layer. Simple huh? PLease note that no need for any
 * dao implementation. This is an interface!
 */
@Repository
public interface TextBreakHistoryRepository extends JpaRepository<LdTextbreakHistory, Long> {

	
	@Modifying
	@Query(value = "insert into LDTEXTBREAKHISTORY  (aid, MESG_S_UMIDL, MESG_S_UMIDH) VALUES (?1,?2,?3)",nativeQuery=true)
	public void persist(@Param("aid") Integer aid, @Param("umidl") Integer umidl,@Param("umidh") Integer umidh);
	
	
}
