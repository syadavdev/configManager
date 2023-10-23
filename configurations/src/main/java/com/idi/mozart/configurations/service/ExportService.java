package com.idi.mozart.configurations.service;

import com.idi.mozart.configurations.model.Application;
import com.idi.mozart.configurations.model.ApplicationMetaData;
import com.idi.mozart.configurations.model.RootMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExportService {

    public RootMetadata setRootMetadata(Map<String, String> fileNames) {
        RootMetadata rootMetadata = new RootMetadata();
        List<Application> applications = new ArrayList<>();

        log.info("Files: {}", fileNames.size());
        rootMetadata.setNoofapplications(String.valueOf(fileNames.size()));
        int count = 0;

        for (Map.Entry<String, String> file : fileNames.entrySet()) {
            count++;
            log.info("File: {}", file.getKey());
            Application application = new Application();
            application.setApplicationName(file.getKey());
            application.setExecutionSeq(count);
            application.setApplicationMetadataName(file.getValue());
            applications.add(application);
        }
        rootMetadata.setApplications(applications);
        return rootMetadata;
    }

    public ApplicationMetaData setRootMetadata() {

        ApplicationMetaData metadata = new ApplicationMetaData();
//        metadata.setConfigurationFileName(file.getValue());
        metadata.setConfigurationFilepath("src/main/resources/");
        metadata.setDescription("test desc");
        metadata.setConfigurationType("json");
        metadata.setConfigurationApplyPath("src/main/resources/output/");
        metadata.setConfigurationOperation("FileUpload");
        return metadata;
    }

}
