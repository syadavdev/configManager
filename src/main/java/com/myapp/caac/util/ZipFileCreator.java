package com.myapp.caac.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Data
@Slf4j
@Service
public class ZipFileCreator {

    private final Path apiProcessingDirectory;
    private final Path metadataDirectory;

    public ZipFileCreator(@Value("${resource.basepath}") String basePath,
                          @Value("${resource.directory}") String resourceDirectoryPath,
                          @Value("${resource.metadata}") String metadataDirectory) {
        if ("home".equalsIgnoreCase(basePath)) {
            String homeDirectory = System.getProperty("user.home");
            this.apiProcessingDirectory = Paths.get(homeDirectory, resourceDirectoryPath);
        } else if ("project".equalsIgnoreCase(basePath)) {
            this.apiProcessingDirectory = Paths.get(resourceDirectoryPath);
        } else {
            throw new IllegalArgumentException("Invalid value for resource.basepath");
        }
        this.metadataDirectory = Paths.get("src", "main", "resources", metadataDirectory);
    }

    public Path createZipFile(Map<String, String> fileNames, String zipFileName) {
        List<String> addedFiles = new ArrayList<>();
        Path zipFile = apiProcessingDirectory.resolve(zipFileName);
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            for (Map.Entry<String, String> fileName : fileNames.entrySet()) {
                addFileToZip(apiProcessingDirectory, fileName.getKey(), zos, addedFiles);
                addFileToZip(metadataDirectory, fileName.getValue(), zos, addedFiles);
            }

            addFileToZip(metadataDirectory, "root_metadata.json", zos, addedFiles);

            if (Files.exists(zipFile)) {
                log.info("ZIP file created successfully: " + zipFileName);
                log.info("Files added to ZIP: " + String.join(", ", addedFiles));
                return zipFile;
            } else {
                log.error("Error creating Zip file: " + zipFileName);
            }
        } catch (IOException e) {
            log.error("Error occurred while creating ZIP: ", e);
        }
        return zipFile;
    }

    private void addFileToZip(Path baseDir, String fileName, ZipOutputStream zos, List<String> addedFiles) throws IOException {
        Path filePath = baseDir.resolve(fileName);
        if (Files.exists(filePath)) {
            addedFiles.add(filePath.getFileName().toString());
            log.info("File added to ZIP: " + filePath.toAbsolutePath());
            ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
            zos.putNextEntry(zipEntry);
            zos.write(Files.readAllBytes(filePath));
            zos.closeEntry();
        } else {
            log.error("File not found: " + filePath.toAbsolutePath());
        }
    }
}
