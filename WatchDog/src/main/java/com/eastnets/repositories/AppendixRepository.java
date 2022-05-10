package com.eastnets.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.Appendix;
import com.eastnets.entities.AppendixPK;

@Repository
public interface AppendixRepository extends JpaRepository<Appendix, AppendixPK>, JpaSpecificationExecutor<Appendix> {

	@Query("SELECT a FROM Appendix A where A.id.appeSUmidh = :appeSUmidh " + "and A.id.appeSUmidl = :appeSUmidl " + "and A.id.aid = :aid and A.id.appeInstNum = :appeInstNum " + "and A.id.appeDateTime = :appeDateTime "
			+ "and A.id.appeSeqNbr = :appeSeqNbr")
	public List<Appendix> getAppendixInfo(@Param("appeSUmidl") Long appeSUmidl, @Param("appeSUmidh") Long appeSUmidh, @Param("aid") Long aid, @Param("appeInstNum") Long appeInstNum, @Param("appeDateTime") Date appeDateTime,
			@Param("appeSeqNbr") Long appeSeqNbr);
}
