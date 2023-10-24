package com.myapp.caac;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
@Slf4j
public class ConfigDataExchangeApplication {


    public static void main(String[] args) {
        SpringApplication.run(ConfigDataExchangeApplication.class, args);
    }

}
