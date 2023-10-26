package com.myapp.caac.service;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class ArchivingService {

    private static final String ARCHIVE_SUBDIRECTORY = "archive";

    public Path archiveFile(Path originalPath) throws IOException {
        if (!Files.exists(originalPath)) {
            throw new FileNotFoundException("File to be archived not found: " + originalPath);
        }

        Path archiveDirectory = originalPath.getParent().resolve(ARCHIVE_SUBDIRECTORY);

        // Ensure the archive directory exists
        Files.createDirectories(archiveDirectory);

        // Construct a new name for the old file: original_timestamp.extension
        String oldFileName = originalPath.getFileName().toString();
        String extension = oldFileName.contains(".") ? oldFileName.substring(oldFileName.lastIndexOf('.')) : "";
        String baseName = oldFileName.contains(".") ? oldFileName.substring(0, oldFileName.lastIndexOf('.')) : oldFileName;
        String timestamp = "_" + System.currentTimeMillis();

        Path archivePath = archiveDirectory.resolve(baseName + timestamp + extension);

        // Move (rename) the file
        Files.move(originalPath, archivePath, StandardCopyOption.REPLACE_EXISTING);

        return archivePath;
    }
}
