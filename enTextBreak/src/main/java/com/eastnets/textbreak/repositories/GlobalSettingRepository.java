package com.eastnets.textbreak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.textbreak.entities.GlobalSettings;

@Repository
public interface GlobalSettingRepository extends JpaRepository<GlobalSettings, Long> {

}
