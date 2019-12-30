package com.ermakov.nikita.smokecloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class SmokeCloudApplication {

	public static void main(String[] args) {
//		SpringApplication.run(SmokeCloudApplication.class, args);
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")));
	}
}