package com.example.carwashapi;

import org.springframework.boot.SpringApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition
@SpringBootApplication
public class CarWashApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarWashApiApplication.class, args);
	}

}
