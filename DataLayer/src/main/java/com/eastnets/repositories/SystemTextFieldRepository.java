package com.eastnets.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.SystemTextField;
import com.eastnets.entities.TextField;

/**
 * This is the Data Access layer. Simple huh? PLease note that no need for any
 * dao implementation. This is an interface!
 */
@Repository
public interface SystemTextFieldRepository extends CrudRepository<SystemTextField, Long> {

}
