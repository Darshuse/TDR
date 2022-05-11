package com.eastnets.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.Intervention;
import com.eastnets.entities.IntvPK;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, IntvPK> {

	public Optional<Intervention> findInterventionByIntvId(IntvPK intvPK);

	@Query("select max(i.intvId.intvSeqNo) from Intervention i where i.intvId.aid = :aid and i.intvId.intvSUmidl = :intvSUmidl and i.intvId.intvSUmidh = :intvSUmidh and i.intvId.intvInstNum = :intvInstNum and i.intvId.intvDateTime = :intvDateTime")
	public Integer maxSeqNo(@Param("aid") Long aid, @Param("intvSUmidl") Long intvSUmidl,
			@Param("intvSUmidh") Long intvSUmidh, @Param("intvInstNum") Integer intvInstNum,
			@Param("intvDateTime") LocalDateTime intvDateTime);

}
