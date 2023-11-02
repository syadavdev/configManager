package com.myapp.caac.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.caac.model.Application;
import com.myapp.caac.model.ApplicationMetaData;
import com.myapp.caac.model.RootMetadata;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.myapp.caac.util.Constants.*;
import static java.lang.Boolean.FALSE;

@Service
@Slf4j
public class ImportService {

    private final ObjectMapper objectMapper;

    private final File tempExtractedPath;

    public ImportService() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.tempExtractedPath = Files.createTempDirectory("temp-directory").toFile();
    }


    /**
     * @param zipFile - Multipart zip file
     * @return Successful or Unsuccessful response
     */
    public String importConfigs(MultipartFile zipFile) throws IOException {
        //Reading zip and extracting to target directory
        if (!extractingZipFile(zipFile))
           return IMPORT_UNSUCCESSFULLY;

        boolean flag = processExtractedDirectory();

        // Clean up: delete the temporary directory
        FileUtils.deleteDirectory(tempExtractedPath);
        return flag ? IMPORT_SUCCESSFULLY : IMPORT_UNSUCCESSFULLY;
    }

    /**
     * @return return true and false based on reading of metadata successful or not
     */
    private boolean processExtractedDirectory() {
        log.info("Processing root metadata file");
        Path filePath = Paths.get(tempExtractedPath.getAbsolutePath(), ROOT_METADATA);
        RootMetadata rootMetadata = null;
        try {
            rootMetadata = objectMapper.readValue(
                    new File(filePath.toAbsolutePath().toString()), RootMetadata.class);
        } catch (IOException e) {
            log.error("Error in processing root metadata file");
            return false;
        }

        //Processing application meta data By Sequence
        List<Boolean> statusOfAppMetaDataList = rootMetadata.getApplications()
                .stream()
                .sorted(Comparator.comparing(Application::getExecutionSeq))
                .map(this::processApplicationBySequence).toList();

        return statusOfAppMetaDataList
                .parallelStream()
                .anyMatch(FALSE::equals);
    }


    /**
     * @return return true and false based on reading of application metadata successful or not
     */
    private boolean processApplicationBySequence(Application application) {
        final String applicationMetadataName = application.getApplicationMetadataName();
        Path filePath = Paths.get(tempExtractedPath.getAbsolutePath(), applicationMetadataName);
        ApplicationMetaData applicationMetaData;
        try {
            applicationMetaData = objectMapper.readValue(
                    new File(filePath.toAbsolutePath().toString()), ApplicationMetaData.class);
        } catch (IOException e) {
            log.error("Error in reading application metadata file {}", filePath);
            return false;
        }

        if (Objects.isNull(applicationMetaData))
            return false;
        processApplicationMetaData(applicationMetaData);
        return true;
    }

    private Boolean processApplicationMetaData(ApplicationMetaData applicationMetaData) {
        //TODO: Write more logic here depending on metadata file
        log.info("Processing application metadata file {}", applicationMetaData.getConfigurationFileName());
        if ("FileUpload".equals(applicationMetaData.getConfigurationOperation())) {
            return uploadFile(applicationMetaData);
        }
        return true;
    }

    private boolean uploadFile(ApplicationMetaData applicationMetaData) {
        Path destinationPath = Path.of(applicationMetaData.getConfigurationApplyPath(), applicationMetaData.getConfigurationFileName());
        File destinationFile = destinationPath.toFile();
        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();
        }

        try {
            // Copy the source file to the destination
            Path filePath = Paths.get(tempExtractedPath.getAbsolutePath(), applicationMetaData.getConfigurationFileName());
            File sourceFile = new File(filePath.toAbsolutePath().toString());
            Files.move(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File copied successfully to : " + destinationPath.toAbsolutePath());
        } catch (IOException e) {
            log.error("Error while copying File", e);
            return false;
        }
        return true;
    }

    private boolean extractingZipFile(MultipartFile zipFile) {
        // Create a ZipInputStream to read the contents of the uploaded zip file
        try (ZipInputStream zipInputStream = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                File entryFile = new File(tempExtractedPath, entryName);

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
