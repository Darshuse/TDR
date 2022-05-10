package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.Jrnl;
import com.eastnets.entities.JrnlPK;


@Repository
public interface JrnlRepository extends JpaRepository<Jrnl, JrnlPK> {

}
