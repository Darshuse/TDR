package com.eastnets.repositories;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eastnets.entities.Appendix;
import com.eastnets.entities.AppendixPK;
import com.eastnets.response.dto.AppendixDTO;

@Repository
public interface AppendixRepository extends JpaRepository<Appendix, AppendixPK> {

	@Query("SELECT new com.eastnets.response.dto.AppendixDTO"
			+ "(A.id.aid,A.id.appeSUmidh, A.id.appeSUmidl,A.id.appeInstNum,A.id.appeDateTime,A.id.appeSeqNbr,A.appeType,A.appeNetworkDeliveryStatus,A.appeRcvDeliveryStatus)"
			+ " FROM Appendix A where A.id.appeSUmidh = :appeSUmidh " + "and A.id.appeSUmidl = :appeSUmidl "
			+ "and A.id.aid = :aid and A.id.appeInstNum = :appeInstNum " + "and A.id.appeDateTime = :appeDateTime "
			+ "and A.id.appeSeqNbr = :appeSeqNbr")
	public List<AppendixDTO> getAppendixInfo(@Param("appeSUmidl") Long appeSUmidl, @Param("appeSUmidh") Long appeSUmidh,
			@Param("aid") Long aid, @Param("appeInstNum") Long appeInstNum, @Param("appeDateTime") Date appeDateTime,
			@Param("appeSeqNbr") Long appeSeqNbr);
}
