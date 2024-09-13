package com.example.akaroa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class AkaroaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AkaroaApplication.class, args);
	}

}
