package com.ermakov.nikita.smokecloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@PropertySource(value = "file:/etc/secret/smoke-cloud-secret", ignoreResourceNotFound = true)
@ComponentScan(basePackages = "com.ermakov.nikita")
@SpringBootApplication
@EnableTransactionManagement
public class SmokeCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmokeCloudApplication.class, args);
	}
}