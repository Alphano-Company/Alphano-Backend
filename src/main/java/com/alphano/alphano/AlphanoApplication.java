package com.alphano.alphano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AlphanoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlphanoApplication.class, args);
	}

}
