package com.eastnets.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.Mesg;
import com.eastnets.entities.Test;
import com.eastnets.resultbean.TextBreakResultBean;

/**
 * This is the Data Access layer. Simple huh? PLease note that no need for any
 * dao implementation. This is an interface!
 */
@Repository
public interface MesgRepository extends JpaRepository<Mesg, Long> {

	public List<Mesg> findMesgByMesgTrnRefAndMesgType(String mesgTrnRef, String mesgType);

	public List<Mesg> findMesgByMesgType(String mesgType);


	
    @Query(value = "SELECT   NEW com.eastnets.resultbean.TextBreakResultBean(m.mesgCreaDateTime,m.id.aid,m.id.umidl,m.id.umidh,m.mesgType,m.mesgSyntaxTableVer,m.text.textDataBlock) "
            + " from Mesg m "
            + " where "
            +"  m.mesgFrmtName = :mesgFrmtName and m.text.textDataBlock is not null   and m.text.parsingStatus ='0'"
            + " and m.id.aid = :aid")
	public List<TextBreakResultBean> findRestoreMessages(
			@Param("mesgFrmtName") String mesgFrmtName,@Param("aid") int aid);
	
	
    @Query(value = "SELECT   NEW com.eastnets.resultbean.TextBreakResultBean(m.mesgCreaDateTime,m.id.aid,m.id.umidl,m.id.umidh,m.mesgType,m.mesgSyntaxTableVer,m.text.textDataBlock) "
            + " from Mesg m "
            + " where "
            + " m.mesgCreaDateTime BETWEEN :fromDate and :toDate and"
            +"  m.mesgFrmtName = :mesgFrmtName and m.text.textDataBlock is not null and m.text.parsingStatus is  null"
            + " and m.id.aid = :aid")
	public List<TextBreakResultBean> getAllTextBreakMessage(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate,
			@Param("mesgFrmtName") String mesgFrmtName,@Param("aid") int aid ,Pageable pageable);



	@Query(value = "SELECT   NEW com.eastnets.resultbean.TextBreakResultBean(m.mesgCreaDateTime,m.id.aid,m.id.umidl,m.id.umidh,m.mesgType,m.mesgSyntaxTableVer,m.text.textDataBlock) "
			+ " from Mesg m  ,GlobalSettings e "
			+ " LEFT OUTER JOIN m.ldParseMsgType f "
			+ " where "
			+ " ( ( (e.parseTextblock = 2 ) and (f.id.mesgType = m.mesgType) ) or (e.parseTextblock = 1 ) ) and "
			+ " m.mesgCreaDateTime BETWEEN :fromDate and :toDate and "
			+"  m.mesgFrmtName = :mesgFrmtName and m.text.textDataBlock is not null and m.text.parsingStatus is  null"
			+ " and m.id.aid = :aid")
	public List<TextBreakResultBean> getTextBreakMessage(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate,
			@Param("mesgFrmtName") String mesgFrmtName,@Param("aid") int aid ,Pageable pageable);

}
