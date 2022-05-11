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

import com.eastnets.entities.AppendixPK;
import com.eastnets.entities.WDAppeKey;

@Repository
public interface WDAppeKeyRepository extends JpaRepository<WDAppeKey, AppendixPK> {

	@Modifying
	@Query("UPDATE WDAppeKey w SET w.status = 1, w.lastUpdate = :currentTime where w.appendixPK.aid = :aid and w.appendixPK.appeSUmidh = :appeSUmidh and w.appendixPK.appeSUmidl = :appeSUmidl and w.appendixPK.appeInstNum = :appeInstNum and w.appendixPK.appeDateTime = :appeDataTime and w.appendixPK.appeSeqNbr =:appeSeqNbr  ")
	void updateAppeReaderStatus(@Param("aid") long aid, @Param("appeSUmidh") long appeSUmidh,
			@Param("appeSUmidl") long appeSUmidl, @Param("appeInstNum") long appeInstNum,
			@Param("appeDataTime") Date appeDateTime, @Param("appeSeqNbr") long appeSeqNbr,
			@Param("currentTime") Date currentTime);

	@Query("SELECT w from WDAppeKey W where w.status = 1")
	List<WDAppeKey> restoreKeys();

	@Query("SELECT w from WDAppeKey W where w.status = null or w.status = 0")
	Page<WDAppeKey> getWdKeys(PageRequest pageRequest);


}
