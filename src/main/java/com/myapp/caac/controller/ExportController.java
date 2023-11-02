package com.myapp.caac.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.caac.model.ApplicationMetaData;
import com.myapp.caac.model.RootMetadata;
import com.myapp.caac.service.ExportService;
import com.myapp.caac.util.Constants;
import com.myapp.caac.util.ZipFileCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@CrossOrigin
@RestController
@Slf4j
public class ExportController {

    @Autowired
    private ExportService exportService;
    @Autowired
    private ZipFileCreator zipFileCreator;

    /**
     *
     * @param ids - Array of string containing the name of the files to be zipped in bundle
     * @return Creates the bundle from the respected configuration files and returns the same
     * @throws IOException
     */

    @GetMapping("api/export")
    public ResponseEntity<FileSystemResource> convertPropertiesToJson(@RequestParam("ids") String[] ids) throws IOException {

        //set the map with fileName and metadata fileName
        Map<String, String> filesToZip = exportService.setMap(ids);

        //name of the zip file
        String zipFileName = Constants.ZIP_FILE_NAME; // Name of the output ZIP file

        //set the root metadata object from respective configuration files
        RootMetadata rootMetadata = exportService.setRootMetadata(filesToZip);

        // Create an ObjectMapper instance from Jackson
        ObjectMapper objectMapper = new ObjectMapper();


        for (Map.Entry<String, String> file : filesToZip.entrySet()) {
            ApplicationMetaData metadata = exportService.setRootMetadata();
            metadata.setConfigurationFileName(file.getKey());
            objectMapper.writeValue(new File(file.getValue()), metadata);
        }

        // Serialize the POJO to a JSON file
        objectMapper.writeValue(new File("root_metadata.json"), rootMetadata);

        //create zip file (bundle)
        Path zipFile = zipFileCreator.createZipFile(filesToZip, zipFileName);

        //added to the headers that the file is sent as an attachment
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bundle.zip");

        long contentLength = Files.size(zipFile);

        //returning the bundled zip with the configuration file and the metadata file
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(contentLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(zipFile.toFile()));

    }
}
