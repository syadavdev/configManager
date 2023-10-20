package com.idi.mozart.configurations.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.util.List;
@NoArgsConstructor
@Data
public class RootMetadata {

    int noOfApplications;

    List<Application> applications;

}
