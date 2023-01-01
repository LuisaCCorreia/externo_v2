package com.scb.externo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ExternoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExternoApplication.class, args);
	}

}
