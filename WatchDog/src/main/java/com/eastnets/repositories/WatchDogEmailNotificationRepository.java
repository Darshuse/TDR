package com.eastnets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.WDEmailNotification;

@Repository
public interface WatchDogEmailNotificationRepository extends JpaRepository<WDEmailNotification, Integer> {

}
