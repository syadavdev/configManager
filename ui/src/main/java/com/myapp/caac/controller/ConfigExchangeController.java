package com.myapp.caac.controller;

import com.myapp.caac.entity.CustomApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class ConfigExchangeController {


    // Create a list of map entries
    private final String tenant = "tenant";
    private final String productFamily = "productFamily";
    private final String product = "product";
    private final String api = "api";

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


}
