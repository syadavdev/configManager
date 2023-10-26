package com.myapp.caac.service;

import com.myapp.caac.entity.ExportConfigurations;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ExportConfigurationsForm {
    private List<ExportConfigurations> exportConfigurationsList;

    public void setExportConfigurationsList(List<ExportConfigurations> exportConfigurationsList) {
        this.exportConfigurationsList = exportConfigurationsList;
    }
}