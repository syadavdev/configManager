package com.myapp.caac.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.myapp.caac.entity.ExportConfigurations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class ConfigurationService {
    public List<ExportConfigurations> loadExportConfigurations() throws Exception {
        // Build the path to the resource
        Path resourceDirectory = Paths.get("src", "main", "resources");
        Path filePath = resourceDirectory.resolve("exportConfigurations.json");

        // Check if the file exists and is readable
        if (Files.notExists(filePath) || !Files.isReadable(filePath)) {
            throw new IOException("File not found or not readable: " + filePath);
        }

        // Read the file into a String
        String jsonContent = Files.readString(filePath);

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON content into a list of ExportConfigurations objects

        List<ExportConfigurations> exportConfigurations = objectMapper.readValue(jsonContent, new TypeReference<List<ExportConfigurations>>() {
        });
        log.info("exportConfigurations:{}", exportConfigurations);
        return exportConfigurations;
    }


    public void updateAndSaveExportConfigurations(List<ExportConfigurations> exportConfigurationsList) throws Exception {
        // Build the path to the resource
        Path resourceDirectory = Paths.get("src", "main", "resources");
        Path filePath = resourceDirectory.resolve("exportConfigurations.json");
        log.info("File:{},{}", filePath, Files.exists(filePath));
        Files.delete(filePath);
        log.info("File:{},{}", filePath, Files.exists(filePath));
        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Enable pretty printing
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

        // Serialize the list into a JSON string with pretty printing
        String jsonContent = writer.writeValueAsString(exportConfigurationsList);

        // Write the JSON string to the file
        log.info("Saving file to: {}", filePath.toAbsolutePath());
        log.info("File Content to: {}", jsonContent);
        Files.writeString(filePath, jsonContent);
    }
}
