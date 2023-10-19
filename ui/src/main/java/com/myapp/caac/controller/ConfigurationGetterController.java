package com.myapp.caac.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/configuration")
public class ConfigurationGetterController {

    @Value("${config.read.directory}")
    private String configReadDirectory;

    @GetMapping("/getTenant")
    public String getTenant() throws IOException {
        return readFileFromDirectory(configReadDirectory, "tenant.yaml");
    }

    @GetMapping("/getProduct")
    public String getProduct() throws IOException {
        return readFileFromDirectory(configReadDirectory, "product.yaml");
    }

    @GetMapping("/getProductFamily")
    public String getProductFamily() throws IOException {
        return readFileFromDirectory(configReadDirectory, "productFamily.yaml");
    }

    @GetMapping("/getApi")
    public String getApi() throws IOException {
        return readFileFromDirectory(configReadDirectory, "api.json");
    }

    // Method to read a file from a directory and return its contents as a string
    private String readFileFromDirectory(String directory, String filename) throws IOException {
        String filePath = Paths.get(directory, filename).toString();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        return String.join("\n", lines);
    }
}
