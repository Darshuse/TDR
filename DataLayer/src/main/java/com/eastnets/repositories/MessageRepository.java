package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.Mesg;
import com.eastnets.entities.MesgPK;

@Repository
public interface MessageRepository extends JpaRepository<Mesg, MesgPK> {

}
