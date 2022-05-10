package com.eastnets.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.JrnlPK;
import com.eastnets.entities.WDJrnlKey;
import com.eastnets.watchdog.resultbeans.WDJrnlKeyBean;

@Repository
public interface WDJrnlKeyRepository extends JpaRepository<WDJrnlKey, JrnlPK> {

	@Query(value = "SELECT   NEW com.eastnets.watchdog.resultbeans.WDJrnlKeyBean(w.jrnlPK.aid,w.jrnlPK.jrnlRevDateTime,w.jrnlPK.jrnlSeqNumber " + " ,w.jrnlCompName,w.jrnlEventNumber,w.lastUpdate) " + " from WDJrnlKey w   " + " where "
			+ "  ( w.processStatus = 0 or w.processStatus = null  ) and ( :multiAlliance = false or w.jrnlPK.aid=:aid )")
	public List<WDJrnlKeyBean> getWDJrnlKeys(Pageable pageable, @Param("multiAlliance") boolean multiAlliance, @Param("aid") Integer aid);

	@Query(value = "SELECT   NEW com.eastnets.watchdog.resultbeans.WDJrnlKeyBean(w.jrnlPK.aid,w.jrnlPK.jrnlRevDateTime,w.jrnlPK.jrnlSeqNumber " + " ,w.jrnlCompName,w.jrnlEventNumber,w.lastUpdate,w.jrnlDateTime) " + " from WDJrnlKey w   " + " where "
			+ " ( w.processStatus = 0 or w.processStatus = null  ) and ( :multiAlliance = false or w.jrnlPK.aid=:aid )")
	public List<WDJrnlKeyBean> getWDJrnlKeysPart(Pageable pageable, @Param("multiAlliance") boolean multiAlliance, @Param("aid") Integer aid);

}
