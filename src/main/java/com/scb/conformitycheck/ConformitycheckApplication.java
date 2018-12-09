package com.scb.conformitycheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@EnableMongoRepositories(basePackageClasses = ConformityCheckRequestController.class)
public class ConformitycheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConformitycheckApplication.class, args);
	}
}
