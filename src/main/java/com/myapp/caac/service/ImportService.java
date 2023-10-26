package com.myapp.caac.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.caac.model.Application;
import com.myapp.caac.model.ApplicationMetaData;
import com.myapp.caac.model.RootMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class ImportService {

    private final String ROOT_METADATA = "root_metadata.json";
    @Autowired
    private ObjectMapper objectMapper;

    public String importConfigs(MultipartFile zipFile) throws IOException {
        Boolean flag = true;
        Path resourceDirectory = Paths.get("src", "main", "resources", "config");
        log.info("Extracting file to: {}", resourceDirectory.toAbsolutePath());
        File targetDir = new File(resourceDirectory.toAbsolutePath().toString());

        //Reading zip and extracting to target directory
        if (extractingZipFile(zipFile, targetDir)) {

            log.info("Processing Root Metadata file");
            Path filePath = Paths.get(targetDir.getAbsolutePath(), ROOT_METADATA);
            RootMetadata rootMetadata = objectMapper.readValue(
                    new File(filePath.toAbsolutePath().toString()), RootMetadata.class);

            int noOfApplication = Integer.parseInt(rootMetadata.getNoOfApplications());
            final List<Application> applications = rootMetadata.getApplications();
            int sequence = 1;
            while (sequence <= noOfApplication) {
                int finalSequence = sequence;
                Optional<Application> applicationOptional = applications.stream()
                        .filter(e -> Objects.equals(e.getExecutionSeq(), finalSequence)).findFirst();

                if (applicationOptional.isPresent()) {
                    Application application = applicationOptional.get();

                    //Reading Application metadata file
                    final String applicationMetadataName = application.getApplicationMetadataName();
                    filePath = Paths.get(targetDir.getAbsolutePath(), applicationMetadataName);
                    ApplicationMetaData applicationMetaData = objectMapper.readValue(
                            new File(filePath.toAbsolutePath().toString()), ApplicationMetaData.class);

                    log.info("Processing application metadata file {}", filePath);
                    if (Objects.nonNull(applicationMetaData))
                        processApplicationMetaData(applicationMetaData, targetDir);
                    else
                        flag = false;
                } else {
                    flag = false;
                }
                sequence++;
            }
        } else {
            flag = false;
        }

        // Clean up: delete the temporary directory
        FileUtils.deleteDirectory(targetDir);
        return flag ? "Imported successfully" : "Import unsuccessfully";
    }

    private Boolean processApplicationMetaData(ApplicationMetaData applicationMetaData, File targetDir) {
        //TODO: Write more logic here depending on metadata file
        if ("FileUpload".equals(applicationMetaData.getConfigurationOperation())) {
            return uploadFile(applicationMetaData, targetDir);
        }
        return true;
    }

    private Boolean uploadFile(ApplicationMetaData applicationMetaData, File targetDir) {
        Path destinationPath = Path.of(applicationMetaData.getConfigurationApplyPath(), applicationMetaData.getConfigurationFileName());
        File destinationFile = destinationPath.toFile();
        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();
        }

        try {
            // Copy the source file to the destination
            Path filePath = Paths.get(targetDir.getAbsolutePath(), applicationMetaData.getConfigurationFileName());
            File sourceFile = new File(filePath.toAbsolutePath().toString());
            Files.move(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File copied successfully to : " + destinationPath.toAbsolutePath());
        } catch (IOException e) {
            log.error("Error while copying File", e);
            return false;
        }
        return true;
    }

    private Boolean extractingZipFile(MultipartFile zipFile, File tempDirectory) {
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
                    outputStream.write(zipInputStream.readAllBytes());
                }
            }
        } catch (IOException e) {
            log.error("Error while processing zip File", e);
            return false;
        }
        return true;
    }

}
