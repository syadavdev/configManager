package com.myapp.caac.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.myapp.caac.entity.CustomApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ConfigExchangeController {


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/load")
    public String load(Model model) {
        model.addAttribute("currentPage", "load");
        return "save";
    }
    // Create a list of map entries
    private final String tenant = "tenant";
    private final  String productFamily = "productFamily";
    private final  String product = "product";
    private final  String api = "api";

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
    public String importPage() {
        return "import";
    }
}
