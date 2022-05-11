package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.TextField;

@Repository
public interface TextFieldRepository extends JpaRepository<TextField, Long> {

}
