package com.idi.mozart.configurations.model;

import lombok.Data;

@Data
public class ApplicationMetaData {

    private String description;
    private String configurationType;
    private String configurationFileName;
    private String configurationOperation;
    private String configurationFilepath;
    private String configurationApplyPath;

}
