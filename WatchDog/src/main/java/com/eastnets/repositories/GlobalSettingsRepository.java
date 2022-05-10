package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.GlobalSettings;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Long> {

}
