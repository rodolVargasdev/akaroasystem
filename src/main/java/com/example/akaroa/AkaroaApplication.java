package com.example.akaroa;

import com.example.akaroa.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.akaroa.repository")
@EntityScan(basePackages = "com.example.akaroa.model")
public class AkaroaApplication {

	public static void main(String[] args) {

		SpringApplication.run(AkaroaApplication.class, args);
	}

}
