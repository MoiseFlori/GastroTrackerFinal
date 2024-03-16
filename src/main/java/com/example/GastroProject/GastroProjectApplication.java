package com.example.GastroProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GastroProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(GastroProjectApplication.class, args);
	}

}
