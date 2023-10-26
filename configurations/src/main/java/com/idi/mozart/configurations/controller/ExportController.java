package com.idi.mozart.configurations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idi.mozart.configurations.model.ApplicationMetaData;
import com.idi.mozart.configurations.model.RootMetadata;
import com.idi.mozart.configurations.service.ExportService;
import com.idi.mozart.configurations.util.ZipFileCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@Slf4j
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/convert-to-bundle")
    public ResponseEntity<FileSystemResource> convertPropertiesToJson(@RequestParam("ids") String[] ids) throws IOException{

        Map<String, String> filesToZip  = new HashMap<>();

        for(String id:ids){
            if(id.equals("api")){
                filesToZip.put("api.json" , "api_metadata.json");
            } else{
                filesToZip.put(id+".yaml" , id+"_metadata.json");
            }
        }

        String zipFileName = "bundle.zip"; // Name of the output ZIP file

        RootMetadata rootMetadata = exportService.setRootMetadata(filesToZip);

        // Create an ObjectMapper instance from Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        for (Map.Entry<String,String> file : filesToZip.entrySet()){
            ApplicationMetaData metadata = exportService.setRootMetadata();
            metadata.setConfigurationFileName(file.getKey());
            objectMapper.writeValue(new File(file.getValue()) , metadata);
        }

        // Serialize the POJO to a JSON file
        objectMapper.writeValue(new File("root_metadata.json"), rootMetadata);
        ZipFileCreator.createZipFile(filesToZip, zipFileName);

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
