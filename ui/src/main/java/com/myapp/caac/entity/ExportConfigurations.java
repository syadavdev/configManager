package com.myapp.caac.entity;

import lombok.Data;

import java.util.List;

@Data
public class ExportConfigurations {

    private String name;
    private List<CustomApi> apiList;
}
