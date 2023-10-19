package com.myapp.caac.controller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ConfigExchangeController {

    private final Map<String, String> uploadedFiles = new HashMap<>();
    private final RestTemplate restTemplate;

    public ConfigExchangeController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/load")
    public String load(Model model) {
        model.addAttribute("currentPage", "load");
        return "save";
    }

//    @PostMapping("/upload")
//    public String uploadFiles(@RequestParam("tenant") MultipartFile tenantFile,
//                              @RequestParam("productFamily") MultipartFile productFamilyFile,
//                              @RequestParam("product") MultipartFile productFile,
//                              @RequestParam("api") MultipartFile apiFile) throws IOException {
//
//        // Store the file content in a map
//        uploadedFiles.put("tenant", new String(tenantFile.getBytes()));
//        uploadedFiles.put("productFamily", new String(productFamilyFile.getBytes()));
//        uploadedFiles.put("product", new String(productFile.getBytes()));
//        uploadedFiles.put("api", new String(apiFile.getBytes()));
//
//        // Redirect to the Export page
//        return "redirect:/export";
//    }

    @GetMapping("/export")
    public String export(Model model) {
        model.addAttribute("currentPage", "export");

        // Define the base URL of your configuration service
        String baseUrl = "http://localhost:8080/configuration";  // Update with the actual URL

        // Make GET requests to the API endpoints
        String tenantContent = restTemplate.getForObject(baseUrl + "/getTenant", String.class);
        String productContent = restTemplate.getForObject(baseUrl + "/getProduct", String.class);
        String productFamilyContent = restTemplate.getForObject(baseUrl + "/getProductFamily", String.class);
        String apiContent = restTemplate.getForObject(baseUrl + "/getApi", String.class);

        // Create a list of map entries
        List<Map.Entry<String, String>> entryList = List.of(
                Map.entry("tenant", tenantContent),
                Map.entry("product", productContent),
                Map.entry("productFamily", productFamilyContent),
                Map.entry("api", apiContent)
        );

        // Pass the list of map entries to the Export page
        model.addAttribute("uploadedFiles", entryList);

        // Add labels and languages maps to the model
        Map<String, String> labels = Map.of(
                "tenant", "Tenant",
                "productFamily", "Product Family",
                "product", "Product",
                "api", "API"
        );
        model.addAttribute("labels", labels);

        Map<String, String> languages = Map.of(
                "tenant", "yaml",
                "productFamily", "json",
                "product", "plaintext",
                "api", "json"
        );
        model.addAttribute("languages", languages);

        return "export";
    }

    @GetMapping("/import")
    public String importPage() {
        return "import";
    }
}
