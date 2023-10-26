package com.myapp.caac.service.resource;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ConfigurationManagementService {
    Optional<String> getConfiguration();

    void saveConfiguration(MultipartFile file) throws IOException;

    boolean validateConfigurationContent(String file);
}
