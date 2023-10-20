package com.idi.mozart.configurations.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Application {

    String applicaitonName;
    String applicaitonMetadataName;
    String applicaitonMetadataPath;
    int executionSq;
}
