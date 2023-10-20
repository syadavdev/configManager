package com.idi.mozart.configurations.service;

import com.idi.mozart.configurations.model.Application;
import com.idi.mozart.configurations.model.RootMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExportService {

    public RootMetadata setRootMetadata(MultipartFile[] files){
        RootMetadata rootMetadata = new RootMetadata();
        rootMetadata.setNoOfApplications(files.length);
        List<Application> applications = new ArrayList<>();


        log.info("Files: {}", files.length);
        int count =0;
        //files
        for (MultipartFile file : files) {
            count++;
            log.info("File: {}", file.getOriginalFilename());
            Application application =new Application();
            application.setApplicaitonName(file.getOriginalFilename());
            application.setExecutionSq(count);
            String baseName = file.getOriginalFilename().substring(0 , file.getOriginalFilename().lastIndexOf('.'));
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            application.setApplicaitonMetadataName(baseName + "-metadata" + extension);
            applications.add(application);
        }
        rootMetadata.setApplications(applications);
        return rootMetadata;
    }

}
