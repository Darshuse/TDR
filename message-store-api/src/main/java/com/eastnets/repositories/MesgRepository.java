package com.eastnets.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.Mesg;
import com.eastnets.entities.PKMesg;

/**
 * This is the Data Access layer. Simple huh? PLease note that no need for any
 * dao implementation. This is an interface!
 */
@Repository
public interface MesgRepository extends JpaRepository<Mesg, PKMesg> {

	public Optional<Mesg> findMesgByMsgId(PKMesg pKMesg);

	// public List<Mesg> findAll();

}
