package com.ermakov.nikita.smokecloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@PropertySource(value = "file:/etc/secret/smoke-cloud-secret", ignoreResourceNotFound = true)
@SpringBootApplication(scanBasePackages = "com.ermakov.nikita")
@EnableJpaRepositories(basePackages = "com.ermakov.nikita.repository")
@EntityScan("com.ermakov.nikita.entity")
public class SmokeCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmokeCloudApplication.class, args);
	}
}