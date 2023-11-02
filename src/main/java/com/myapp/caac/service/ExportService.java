package com.myapp.caac.service;

import com.myapp.caac.model.Application;
import com.myapp.caac.model.ApplicationMetaData;
import com.myapp.caac.model.RootMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExportService {


    private final Path resourceDirectory;

    public ExportService(@Value("${resource.basepath}") String basePath,
                         @Value("${resource.directory}") String resourceDirectoryPath,
                         ArchivingService archivingService) {
        if ("home".equalsIgnoreCase(basePath)) {
            String homeDirectory = System.getProperty("user.home");
            this.resourceDirectory = Paths.get(homeDirectory, resourceDirectoryPath);
        } else if ("project".equalsIgnoreCase(basePath)) {
            this.resourceDirectory = Paths.get(resourceDirectoryPath);
        } else {
            throw new IllegalArgumentException("Invalid value for resource.basepath");
        }
    }

    /**
     *
     * @param fileNames - file names to be zipped
     * @return returns the rootMetadata to be created from the files
     */

    public RootMetadata setRootMetadata(Map<String, String> fileNames) {
        RootMetadata rootMetadata = new RootMetadata();
        List<Application> applications = new ArrayList<>();

        log.info("Files: {}", fileNames.size());
        rootMetadata.setNoOfApplications(String.valueOf(fileNames.size()));
        int count = 0;

        for (Map.Entry<String, String> file : fileNames.entrySet()) {
            count++;
            log.info("File: {}", file.getKey());
            Application application = new Application();
            application.setApplicationName(file.getKey());
            application.setExecutionSeq(count);
            application.setApplicationMetadataName(file.getValue());
            application.setApplicationMetadataPath(resourceDirectory.toAbsolutePath().toString());
            applications.add(application);
        }
        rootMetadata.setApplications(applications);
        return rootMetadata;
    }

    /**
     * sets the application metadata files
     */

    public ApplicationMetaData setRootMetadata() {
        ApplicationMetaData metadata = new ApplicationMetaData();
        metadata.setConfigurationFilepath(resourceDirectory.toAbsolutePath().toString());
        metadata.setDescription("test desc");
        metadata.setConfigurationType("json");
        metadata.setConfigurationApplyPath(resourceDirectory.toAbsolutePath().toString());
        metadata.setConfigurationOperation("FileUpload");
        return metadata;
    }

    /**
     * @param ids - array of String containing the ids of configuration to be added
     * @return Map with configuration file name and Metadata file name
     */

    public Map<String , String> setMap(String[] ids){
        Map<String , String> filesToZip = new HashMap<>();

        for(String id : ids){
            if(id.equals("api")){
                filesToZip.put(id+".json" , id+"_metadata.json");
            } else{
                filesToZip.put(id+".yaml" , id+"_metadata.json");
            }
        }

        return filesToZip;
    }

}
