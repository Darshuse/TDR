package com.eastnets.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.Mesg;
import com.eastnets.watchdog.resultbeans.PossibleDuplicateCustomMessageInfo;

/**
 * This is the Data Access layer. Simple huh? PLease note that no need for any dao implementation. This is an interface!
 */
@Repository
public interface MesgRepository extends JpaRepository<Mesg, Long>, JpaSpecificationExecutor<Mesg> {

	@Query("SELECT m FROM Mesg m where m.id.aid = :aid and m.id.umidl = :appeSUmidl and m.id.umidh = :appeSUmidh")
	public Mesg getMessageInformation(@Param("aid") long aid, @Param("appeSUmidl") long appeSUmidl, @Param("appeSUmidh") long appeSUmidh);

	@Query("SELECT m.archived FROM Mesg m where m.id.aid = :aid and m.id.umidl = :appeSUmidl and m.id.umidh = :appeSUmidh ")
	public int isMessageArchived(@Param("aid") long aid, @Param("appeSUmidl") long appeSUmidl, @Param("appeSUmidh") long appeSUmidh);

	@Query("SELECT m.archived FROM Mesg m where m.id.aid = :aid and m.id.umidl = :appeSUmidl and m.id.umidh = :appeSUmidh and m.mesgCreaDateTime = :xCreaDateTime ")
	public int isMessageArchivedPart(@Param("aid") long aid, @Param("appeSUmidl") long appeSUmidl, @Param("appeSUmidh") long appeSUmidh, @Param("xCreaDateTime") Date xCreaDateTime);

	@Query("SELECT new com.eastnets.watchdog.resultbeans.PossibleDuplicateCustomMessageInfo (m.mesgUserIssuedAsPde,m.mesgPossibleDupCreation) " + "FROM Mesg m where m.id.aid = :aid and m.id.umidl = :umidl and m.id.umidh = :umidh "
			+ "AND ((m.mesgSubFormat = 'INPUT' and m.mesgUserIssuedAsPde = 1) " + "OR (m.mesgSubFormat = 'OUTPUT' and m.mesgPossibleDupCreation is not null))")
	public List<PossibleDuplicateCustomMessageInfo> getMesgInfoForPossibleDuplicate(@Param("umidh") Long umidh, @Param("umidl") Long umidl, @Param("aid") Long aid);

}
