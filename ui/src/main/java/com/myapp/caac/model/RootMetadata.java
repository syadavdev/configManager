package com.myapp.caac.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class RootMetadata {

    private String noOfApplications;

    private List<Application> applications;

}
