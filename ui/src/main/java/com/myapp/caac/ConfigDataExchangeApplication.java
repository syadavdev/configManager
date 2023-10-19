package com.myapp.caac;

import com.myapp.caac.fetcher.PropertiesFetcher;
import com.myapp.caac.fetcher.PropertiesFileFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
@Slf4j
public class ConfigDataExchangeApplication implements CommandLineRunner {


    private final ResourceLoader resourceLoader;

    public ConfigDataExchangeApplication(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigDataExchangeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("ConfigDataExchangeApplication started successfully");

//        String classpathResource = "classpath:application.properties";
//        String filePath = "file:/Users/gaurav/Library/Mobile Documents/com~apple~CloudDocs/Documents/Code/projects/java/ConfigDataExchange/src/main/resources/my-application.properties";
//
//        // Fetch properties from different sources
//        Map<String, Properties> propertiesMap = new HashMap<>();
//        propertiesMap.put(classpathResource, fetchProperties(classpathResource));
//        propertiesMap.put(filePath, fetchProperties(filePath));
//
//        // Inside your run method or wherever you are processing the properties
//        PropertiesToJsonConverter converter = new PropertiesToJsonConverterImpl();
//        String json = converter.convertAllToJson(propertiesMap);
//        System.out.println(json);
    }


    private Properties fetchProperties(String resourceLocation) throws IOException {
        PropertiesFetcher propertiesFetcher = new PropertiesFileFetcher(resourceLocation, resourceLoader);
        return propertiesFetcher.fetchProperties();
    }
}
