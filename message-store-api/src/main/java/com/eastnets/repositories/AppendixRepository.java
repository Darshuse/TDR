package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.Appendix;

@Repository
public interface AppendixRepository extends JpaRepository<Appendix, Long> {


}
