package com.idi.mozart.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConfigurationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigurationsApplication.class, args);
	}

	@Bean
	public ObjectMapper getObjectMapper(){
		return new ObjectMapper();
	}

}
