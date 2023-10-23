package com.idi.mozart.configurations.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Application {

    private String applicationName;
    private String applicationMetadataName;
    private String applicationMetadataPath;
    private Integer executionSeq;
}
