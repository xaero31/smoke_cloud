package com.ermakov.nikita.smokecloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "file:/etc/secret/smoke-cloud-secret", ignoreResourceNotFound = true)
@SpringBootApplication
public class SmokeCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmokeCloudApplication.class, args);
	}
}