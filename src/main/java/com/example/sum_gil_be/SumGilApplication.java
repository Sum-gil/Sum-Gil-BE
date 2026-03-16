package com.example.sum_gil_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SumGilApplication {

	public static void main(String[] args) {
		SpringApplication.run(SumGilApplication.class, args);
	}

}
