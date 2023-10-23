package com.myapp.caac.controller;

import com.myapp.caac.entity.CustomApi;
import com.myapp.caac.entity.ExportConfigurations;
import com.myapp.caac.service.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ConfigExchangeController {


    @Autowired
    private ConfigurationService configurationService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/export")
    public String export(Model model) {

        model.addAttribute("currentPage", "export");

        CustomApi tenantApi = new CustomApi("tenant", "Tenant", "yaml");
        CustomApi productFamilyApi = new CustomApi("productfamily", "Product Family", "yaml");
        CustomApi productApi = new CustomApi("product", "Product", "yaml");
        CustomApi apiApi = new CustomApi("api", "API", "json");

        List<CustomApi> apiList = List.of(tenantApi, productFamilyApi, productApi, apiApi);

        model.addAttribute("apiList", apiList);

        return "export";
    }

    @GetMapping("/import")
    public String importPage(Model model) {

        model.addAttribute("currentPage", "import");

        return "import";
    }

    @GetMapping("/export-configuration")
    public String exportConfiguration(Model model) throws Exception {
        model.addAttribute("currentPage", "export-configuration");
        List<ExportConfigurations>  exportConfigurationsList = configurationService.loadExportConfigurations();
        model.addAttribute("exportConfigurationsList", exportConfigurationsList);

        CustomApi tenantApi = new CustomApi("tenant", "Tenant", "yaml");
        CustomApi productFamilyApi = new CustomApi("productfamily", "Product Family", "yaml");
        CustomApi productApi = new CustomApi("product", "Product", "yaml");
        CustomApi apiApi = new CustomApi("api", "API", "json");

        List<CustomApi> apiList = List.of(tenantApi, productFamilyApi, productApi, apiApi);

        model.addAttribute("apiList", apiList);
        return "export-configuration";
    }

    @PostMapping("/configuration")
    public ResponseEntity<Map<String, String>>  saveExportConfiguration(@ModelAttribute ExportConfigurations exportConfigurations, Model model) throws Exception {
        log.info("ExportConfigurationsForm: {}", exportConfigurations);
        // The ExportConfigurationsForm object should now contain the data from the form
        List<ExportConfigurations> exportConfigurationsList = new ArrayList<>(configurationService.loadExportConfigurations());

        for (ExportConfigurations configurations : exportConfigurationsList) {
            if ( configurations.getName().equals(exportConfigurations.getName())) {
                configurations.setApiList(exportConfigurations.getApiList());
            }
        }
        configurationService.updateAndSaveExportConfigurations(exportConfigurationsList);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("status", "success");
        responseMap.put("message", "Export configuration saved successfully!");

        return ResponseEntity.ok(responseMap);
    }

    @GetMapping("/apis")
    public ResponseEntity<List<CustomApi>> getApis(Model model) throws Exception {
        CustomApi tenantApi = new CustomApi("tenant", "Tenant", "yaml");
        CustomApi productFamilyApi = new CustomApi("productfamily", "Product Family", "yaml");
        CustomApi productApi = new CustomApi("product", "Product", "yaml");
        CustomApi apiApi = new CustomApi("api", "API", "json");

        List<CustomApi> apiList = List.of(tenantApi, productFamilyApi, productApi, apiApi);

        return ResponseEntity.ok(apiList);
    }

}
