package com.skill_share.post_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;

@SpringBootApplication
public class PostServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(PostServiceApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {

		return new ModelMapper();
	}

}
