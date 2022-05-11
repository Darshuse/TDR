package com.eastnets.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.eastnets.entities.PKText;
import com.eastnets.entities.Text;

public interface TextRepository extends CrudRepository<Text, PKText> {

	public Optional<Text> findTextByTextId(PKText pkText);
}
