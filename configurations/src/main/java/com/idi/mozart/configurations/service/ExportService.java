package com.idi.mozart.configurations.service;

import com.idi.mozart.configurations.model.Application;
import com.idi.mozart.configurations.model.RootMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExportService {

    public RootMetadata setRootMetadata(String[] fileNames){
        RootMetadata rootMetadata = new RootMetadata();
        List<Application> applications = new ArrayList<>();

        log.info("Files: {}", fileNames.length);
        rootMetadata.setNoOfApplications(fileNames.length);
        int count =0;

        for (String file : fileNames) {
            count++;
            log.info("File: {}", file);
            Application application =new Application();
            application.setApplicaitonName(file);
            application.setExecutionSq(count);
            String baseName = file.substring(0 , file.lastIndexOf('.'));
            String extension = file.substring(file.lastIndexOf('.'));
            application.setApplicaitonMetadataName(baseName + "-metadata" + extension);
            applications.add(application);
        }
        rootMetadata.setApplications(applications);
        return rootMetadata;
    }

}
