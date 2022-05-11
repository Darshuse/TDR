package com.eastnets.notifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.eastnets.notifier.service.EventsObserver;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class EnNotifierServiceApplication implements CommandLineRunner {

	@Autowired
	EventsObserver eventsObserver;

	public static void main(String[] args) {
		SpringApplication.run(EnNotifierServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		eventsObserver.startService();

	}

}
