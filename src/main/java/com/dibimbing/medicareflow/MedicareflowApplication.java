package com.dibimbing.medicareflow;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class MedicareflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicareflowApplication.class, args);
	}

}
