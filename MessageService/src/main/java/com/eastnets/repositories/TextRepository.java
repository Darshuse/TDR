package com.eastnets.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.eastnets.entities.Text;

public interface TextRepository  extends CrudRepository<Text, Long> {


	@Modifying
	@Query("update Text t set t.textDataBlock = null , t.parsingStatus = '2' where t.id.aid= :aid and t.id.textSUmidh= :umidh and t.id.textSUmidl=:umidl")
	void updateText(@Param("aid") int aid,@Param("umidh") Long umidh,@Param("umidl") Long umidl);



	@Modifying
	@Query("update Text t set t.parsingStatus = :status where t.id.aid= :aid and t.id.textSUmidh= :umidh and t.id.textSUmidl=:umidl")
	void updateTextStatus(@Param("status") String status,@Param("aid") int aid,@Param("umidh") Long umidh,@Param("umidl") Long umidl);

	
}
