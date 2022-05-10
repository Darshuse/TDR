package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.WDSettings;
import com.eastnets.entities.WDSettingsPK;

@Repository
public interface WDSettingsRepository extends JpaRepository<WDSettings, WDSettingsPK> {

}
