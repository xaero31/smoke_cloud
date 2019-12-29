package com.ermakov.nikita.smokecloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmokeCloudApplication {

	public static void main(String[] args) {
//		SpringApplication.run(SmokeCloudApplication.class, args);
		System.out.println("origin/dev".split("/")[1]);
	}
}