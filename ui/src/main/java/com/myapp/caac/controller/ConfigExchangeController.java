package com.myapp.caac.controller;

import com.myapp.caac.entity.CustomApi;
import com.myapp.caac.entity.ExportConfigurations;
import com.myapp.caac.service.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

import static com.myapp.caac.repository.ApiRepository.getCustomApis;

@Controller
@Slf4j
public class ConfigExchangeController {


    private final ConfigurationService configurationService;

    public ConfigExchangeController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/export")
    public String export(Model model) throws Exception {
        model.addAttribute("currentPage", "export");
        model.addAttribute("apiList", getCustomApis());
        String allApis = getCustomApis().stream().map(CustomApi::getId).collect(Collectors.joining(","));;
        model.addAttribute("allApis", allApis);
        List<ExportConfigurations> exportOptions = configurationService.loadExportConfigurations();
        model.addAttribute("exportOptions",exportOptions);
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
        model.addAttribute("exportConfigurationsList", configurationService.loadExportConfigurations());
        List<CustomApi> customApis = getCustomApis();
        model.addAttribute("apiList", customApis);
        return "export-configuration";
    }

    @PostMapping("/configuration")
    public ResponseEntity<Map<String, String>> saveExportConfiguration(
            @RequestBody Map<String, Object> payload,
            Model model) throws Exception {

        Map<String, String> responseMap = new HashMap<>();

        // Manual validation for 'name'
        if (!payload.containsKey("name") || payload.get("name") == null || payload.get("name").toString().isEmpty()) {
            responseMap.put("status", "failure");
            responseMap.put("message", "Name is mandatory");
            return ResponseEntity.badRequest().body(responseMap);
        }

        // Manual validation for 'apiList'
        Object apiObject = payload.get("apiList");
        if (!payload.containsKey("apiList") || !(apiObject instanceof List) || ((List<?>) apiObject).isEmpty()) {
            responseMap.put("status", "failure");
            responseMap.put("message", "apiList is mandatory and should be a non-empty list");
            return ResponseEntity.badRequest().body(responseMap);
        }

        String name = payload.get("name").toString();

        @SuppressWarnings("unchecked")  // Suppress the unchecked cast warning
        List<String> ids = (List<String>) apiObject;

        // You may need to convert apiListLabels to List<CustomApi> based on your requirements
        List<CustomApi> apiList = convertToCustomApiList(ids);

        ExportConfigurations exportConfigurations = new ExportConfigurations();
        exportConfigurations.setName(name);
        exportConfigurations.setApiList(apiList);
        List<ExportConfigurations> exportConfigurationsList = new ArrayList<>(configurationService.loadExportConfigurations());

        upsertConfiguration(exportConfigurationsList, exportConfigurations);

        configurationService.updateAndSaveExportConfigurations(exportConfigurationsList);

        responseMap.put("status", "success");
        responseMap.put("message", "Export configuration saved successfully!");
        return ResponseEntity.ok(responseMap);
    }

    private List<CustomApi> convertToCustomApiList(List<String> ids) {

        return ids.stream()
                .map(this::findMatchingApi)
                .flatMap(Optional::stream)
                .filter(Objects::nonNull)
                .toList();
    }

    private Optional<CustomApi> findMatchingApi(String id) {
        log.info("label: {}", id);
        List<CustomApi> customApis = getCustomApis();
        log.info("customApis: {}", customApis);
        Optional<CustomApi> first = customApis.stream()
                .filter(api -> id.equalsIgnoreCase(api.getId()))
                .findFirst();

        return first;
    }

    private void upsertConfiguration(List<ExportConfigurations> configurationsList,
                                     ExportConfigurations newConfig) {
        configurationsList.stream()
                .filter(config -> config.getName().equals(newConfig.getName()))
                .findFirst()
                .ifPresentOrElse(
                        existingConfig -> existingConfig.setApiList(newConfig.getApiList()),
                        () -> configurationsList.add(newConfig)
                );
    }

    private String getErrorMessages(BindingResult result) {
        return result.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }



    @GetMapping("/apis")
    public ResponseEntity<List<CustomApi>> getApis() {
        List<CustomApi> apiList =  getCustomApis();
        return ResponseEntity.ok(apiList);
    }

}
