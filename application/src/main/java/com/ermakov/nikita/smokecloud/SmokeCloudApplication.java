package com.ermakov.nikita.smokecloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "file:/etc/secret/smoke-cloud-secret", ignoreResourceNotFound = true)
@SpringBootApplication(scanBasePackages = "com.ermakov.nikita")
@EntityScan("com.ermakov.nikita.model")
public class SmokeCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmokeCloudApplication.class, args);
	}
}