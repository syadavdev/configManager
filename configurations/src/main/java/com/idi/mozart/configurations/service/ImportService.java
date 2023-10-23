package com.idi.mozart.configurations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idi.mozart.configurations.model.Application;
import com.idi.mozart.configurations.model.ApplicationMetaData;
import com.idi.mozart.configurations.model.RootMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class ImportService {

    public String importConfigs(MultipartFile zipFile) throws IOException {

        Path resourceDirectory = Paths.get("src", "main", "resources", "config");
        //Path filePath = resourceDirectory.resolve("config");
        log.info("Extracting file to: {}", resourceDirectory.toAbsolutePath());
        File targetDir = new File(resourceDirectory.toAbsolutePath().toString());

        /*// Create a temporary directory to extract the files
        File tempDirectory = Files.createTempDirectory("temp-extract").toFile();
        */

        //Reading zip and extracting to target directory
        extractingFiles(zipFile, targetDir);

        //Reading root metadata file
        RootMetadata rootMetadata = readRootMetaData(targetDir);
        int noOfApplication = Integer.parseInt(rootMetadata.getNoofapplications());
        final List<Application> applications = rootMetadata.getApplications();
        int sequence = 1;
        while(sequence <= noOfApplication){
            int finalSequence = sequence;
            final Application application = applications.stream().filter(e -> Objects.equals(e.getExecutionSeq(), finalSequence)).findFirst().get();

            //Reading Application metadata file
            final String applicationMetadataName = application.getApplicationMetadataName();
            Path filePath = Paths.get(targetDir.getAbsolutePath(), applicationMetadataName);
            File appMetaDataFile = new File(filePath.toAbsolutePath().toString());
            ApplicationMetaData applicationMetaData = readAppMetaData(appMetaDataFile);

            //processing metadata file
            processApplicationMetaData(applicationMetaData, targetDir);
            sequence++;
        }


        // Clean up: delete the temporary directory
        FileUtils.deleteDirectory(targetDir);
        return "success";
    }

    private void processApplicationMetaData(ApplicationMetaData applicationMetaData, File targetDir){
        if ("FileUpload".equals(applicationMetaData.getConfigurationOperation())) {
            uploadFile(applicationMetaData, targetDir);
        }
    }

    private void uploadFile(ApplicationMetaData applicationMetaData, File targetDir) {
        // Destination file path
        Path destinationPath = Path.of(applicationMetaData.getConfigurationApplyPath(), applicationMetaData.getConfigurationFileName());

        try {
            // Copy the source file to the destination
            Path filePath = Paths.get(targetDir.getAbsolutePath(), applicationMetaData.getConfigurationFileName());
            File sourceFile = new File(filePath.toAbsolutePath().toString());
            Files.copy(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ApplicationMetaData readAppMetaData(File targetDir){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(targetDir.getAbsolutePath()), ApplicationMetaData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private RootMetadata readRootMetaData(File targetDir){
        try {
            Path filePath = Paths.get(targetDir.getAbsolutePath(), "root_metadata.json");
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(filePath.toAbsolutePath().toString()), RootMetadata.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Boolean extractingFiles(MultipartFile zipFile, File tempDirectory) throws IOException {
        // Create a ZipInputStream to read the contents of the uploaded zip file
        try (ZipInputStream zipInputStream = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                File entryFile = new File(tempDirectory, entryName);

                // Ensure the parent directory of the entry file exists
                if (!entryFile.getParentFile().exists()) {
                    entryFile.getParentFile().mkdirs();
                }

                // Write the entry data to the entry file
                try (OutputStream outputStream = new FileOutputStream(entryFile)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                }
            }
        }
        return true;
    }

}
