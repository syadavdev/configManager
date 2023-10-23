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
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class ImportService {

    public String importConfigs(MultipartFile zipFile) throws IOException {

        Path resourceDirectory = Paths.get("src", "main", "resources", "config");
        log.info("Extracting file to: {}", resourceDirectory.toAbsolutePath());
        File targetDir = new File(resourceDirectory.toAbsolutePath().toString());

        //Reading zip and extracting to target directory
        if(extractingZipFile(zipFile, targetDir)){

            //Reading root metadata file
            RootMetadata rootMetadata = readRootMetaData(targetDir);
            int noOfApplication = Integer.parseInt(rootMetadata.getNoofapplications());
            final List<Application> applications = rootMetadata.getApplications();
            int sequence = 1;
            while(sequence <= noOfApplication){
                int finalSequence = sequence;
                Optional<Application> applicationOptional = applications.stream().filter(e -> Objects.equals(e.getExecutionSeq(), finalSequence)).findFirst();

                if(applicationOptional.isPresent()) {
                    Application application = applicationOptional.get();
                    //Reading Application metadata file
                    final String applicationMetadataName = application.getApplicationMetadataName();
                    Path filePath = Paths.get(targetDir.getAbsolutePath(), applicationMetadataName);
                    File appMetaDataFile = new File(filePath.toAbsolutePath().toString());
                    ApplicationMetaData applicationMetaData = readAppMetaData(appMetaDataFile);

                    //processing application metadata file
                    assert applicationMetaData != null;
                    processApplicationMetaData(applicationMetaData, targetDir);
                }
                sequence++;
            }

        }

        // Clean up: delete the temporary directory
        FileUtils.deleteDirectory(targetDir);
        return "imported successfully";
    }

    private void processApplicationMetaData(ApplicationMetaData applicationMetaData, File targetDir){
        //TODO: Write more logic here depending on metadata file
        if ("FileUpload".equals(applicationMetaData.getConfigurationOperation())) {
            uploadFile(applicationMetaData, targetDir);
        }
    }

    private void uploadFile(ApplicationMetaData applicationMetaData, File targetDir) {
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
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                }
            }
        }catch (IOException e){
            log.info("Error while processing zip File");
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
