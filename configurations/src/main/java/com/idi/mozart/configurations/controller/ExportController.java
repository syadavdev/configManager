package com.idi.mozart.configurations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idi.mozart.configurations.model.Application;
import com.idi.mozart.configurations.model.RootMetadata;
import com.idi.mozart.configurations.service.ExportService;
import com.idi.mozart.configurations.util.ZipFileCreator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
@CrossOrigin
@RestController
@Slf4j
public class ExportController {


    private ZipFileCreator zipFileCreator;

    @Autowired
    private ExportService exportService;

    @GetMapping("/convert-to-bundle")
    public ResponseEntity<FileSystemResource> convertPropertiesToJson(@RequestParam("files") MultipartFile[] files ) throws IOException{

        if (files == null || files.length == 0) {
            throw new IOException("No files provided");
        }

        RootMetadata rootMetadata = exportService.setRootMetadata(files);

        // Create an ObjectMapper instance from Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        // Serialize the POJO to a JSON file
        objectMapper.writeValue(new File("rootMetadata.json"), rootMetadata);
        String zipFileName = "bundle.zip";
        zipFileCreator.createZipFile(files , zipFileName);

        File zipFile = new File(zipFileName);
        if (!zipFile.exists()) {
            // Handle the case where the file does not exist or is not accessible
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bundle.zip");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(zipFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(zipFile));

    }
}
