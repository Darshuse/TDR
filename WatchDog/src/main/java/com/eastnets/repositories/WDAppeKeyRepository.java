package com.eastnets.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.AppendixPK;
import com.eastnets.entities.WDAppeKey;
import com.eastnets.watchdog.resultbeans.WDAppeKeyBean;

@Repository
public interface WDAppeKeyRepository extends JpaRepository<WDAppeKey, AppendixPK> {

	@Query(value = "SELECT   NEW com.eastnets.watchdog.resultbeans.WDAppeKeyBean(w.appendixPK.aid,w.appendixPK.appeSUmidh,w.appendixPK.appeSUmidl " + " ,w.appendixPK.appeInstNum,w.appendixPK.appeDateTime,w.appendixPK.appeSeqNbr,w.lastUpdate) "
			+ " from WDAppeKey w   " + " where " + "  (w.processStatus = 0 or w.processStatus = null ) and ( :multiAlliance = false or w.appendixPK.aid=:aid ) ")
	public List<WDAppeKeyBean> getWDAppeKeys(Pageable pageable, @Param("multiAlliance") boolean multiAlliance, @Param("aid") Integer aid);

	@Query(value = "SELECT   NEW com.eastnets.watchdog.resultbeans.WDAppeKeyBean(w.appendixPK.aid,w.appendixPK.appeSUmidh,w.appendixPK.appeSUmidl "
			+ " ,w.appendixPK.appeInstNum,w.appendixPK.appeDateTime,w.appendixPK.appeSeqNbr,w.lastUpdate,w.mesgCreaDateTime) " + " from WDAppeKey w   " + " where "
			+ "  ( w.processStatus = 0 or w.processStatus = null )  and ( :multiAlliance = false or w.appendixPK.aid=:aid ) ")
	public List<WDAppeKeyBean> getWDAppeKeysPart(Pageable pageable, @Param("multiAlliance") boolean multiAlliance, @Param("aid") Integer aid);

	@Query("SELECT w from WDAppeKey W where w.processStatus = 1")
	List<WDAppeKey> restoreWDKeys();

}
