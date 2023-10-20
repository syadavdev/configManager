package com.myapp.caac;

import com.myapp.caac.convertor.PropertiesToJsonConverter;
import com.myapp.caac.exception.EmptyFileException;
import com.myapp.caac.exception.NullFileNameException;
import com.myapp.caac.fetcher.PropertiesFetcher;
import com.myapp.caac.fetcher.PropertiesFetcherFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/api/properties")
@Tag(name = "Properties to JSON Converter", description = "Converts properties apiList to JSON")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")  // Allow requests from your Angular app
public class PropertiesController {

    private final PropertiesFetcherFactory propertiesFetcherFactory;
    private final PropertiesToJsonConverter propertiesToJsonConverter;

    @Autowired
    public PropertiesController(
            PropertiesFetcherFactory propertiesFetcherFactory,
            PropertiesToJsonConverter propertiesToJsonConverter
    ) {
        this.propertiesFetcherFactory = propertiesFetcherFactory;
        this.propertiesToJsonConverter = propertiesToJsonConverter;
    }

    @PostMapping("/convert")
    @Operation(summary = "Converts a properties file to JSON", description = "Converts a properties file to JSON")
    public String convertPropertiesToJson(@Parameter(description = "The properties file to convert", required = true) @RequestParam("file") MultipartFile file)
            throws IOException, EmptyFileException, NullFileNameException {

        validateFile(file);

        PropertiesFetcher propertiesFetcher = propertiesFetcherFactory.getPropertiesFetcher(file);
        Properties properties = propertiesFetcher.fetchProperties();
        String json = propertiesToJsonConverter.convertToJson(file.getOriginalFilename(), properties);

        log.info("Successfully converted properties file {} to JSON", file.getOriginalFilename());

        return json;
    }

    @PostMapping("/convert-multiple")
    @Operation(summary = "Converts multiple properties apiList to JSON", description = "Converts multiple properties apiList to JSON")
    public String convertMultiplePropertiesToJson(
            @Parameter(description = "The properties apiList to convert", required = true) @RequestParam("files") MultipartFile[] files)
            throws IOException, EmptyFileException, NullFileNameException {

        checkFilesNotNullOrEmpty(files);

        Map<String, Properties> propertiesMap = new HashMap<>();
        for (MultipartFile file : files) {
            validateFile(file);
            PropertiesFetcher propertiesFetcher = propertiesFetcherFactory.getPropertiesFetcher(file);
            Properties properties = propertiesFetcher.fetchProperties();
            propertiesMap.put(file.getOriginalFilename(), properties);
        }

        log.info("Successfully converted {} properties apiList to JSON", files.length);

        return propertiesToJsonConverter.convertAllToJson(propertiesMap);
    }

    private void checkFilesNotNullOrEmpty(MultipartFile[] files) throws EmptyFileException {
        if (files == null || files.length == 0) {
            log.error("No apiList provided for conversion");
            throw new EmptyFileException("No apiList provided");
        }
    }

    private void validateFile(MultipartFile file) throws EmptyFileException, NullFileNameException {
        if (file == null) {
            log.error("File is null");
            throw new IllegalArgumentException("File is null");
        }

        if (file.isEmpty()) {
            log.error("Empty file: {}", file.getOriginalFilename());
            throw new EmptyFileException("Empty file: " + file.getOriginalFilename());
        }

        if (file.getOriginalFilename() == null) {
            log.error("File name is null for file {}", file);
            throw new NullFileNameException("File name is null");
        }

    }
}
