package com.skillshare.instructor_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;

@SpringBootApplication
public class InstructorServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(InstructorServiceApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {

		return new ModelMapper();
	}


}
