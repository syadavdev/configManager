package com.myapp.caac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ConfigExchangeController {

    private Map<String, String> uploadedFiles = new HashMap<>();

    @GetMapping("/")
    public String home() {
        return "redirect:/load";
    }

    @GetMapping("/load")
    public String load(Model model) {
        model.addAttribute("currentPage", "load");
        return "load";
    }

    @PostMapping("/upload")
    public String uploadFiles(@RequestParam("tenant") MultipartFile tenantFile,
                              @RequestParam("productFamily") MultipartFile productFamilyFile,
                              @RequestParam("product") MultipartFile productFile,
                              @RequestParam("api") MultipartFile apiFile) throws IOException {

        // Store the file content in a map
        uploadedFiles.put("tenant", new String(tenantFile.getBytes()));
        uploadedFiles.put("productFamily", new String(productFamilyFile.getBytes()));
        uploadedFiles.put("product", new String(productFile.getBytes()));
        uploadedFiles.put("api", new String(apiFile.getBytes()));

        // Redirect to the Export page
        return "redirect:/export";
    }

    @GetMapping("/export")
    public String export(Model model) {
        model.addAttribute("currentPage", "export");
        // Pass the uploaded file content to the Export page
        // Convert the map into a list of map entries (key-value pairs)
        List<Map.Entry<String, String>> entryList = new ArrayList<>(uploadedFiles.entrySet());

        // In your controller
        Map<String, String> labels = new HashMap<>();
        labels.put("tenant", "Tenant");
        labels.put("productFamily", "Product Family");
        labels.put("product", "Product");
        labels.put("api", "API");

        model.addAttribute("labels", labels);

        Map<String, String> languages = new HashMap<>();
        languages.put("tenant", "yaml");
        languages.put("productFamily", "json");
        languages.put("product", "plaintext");
        languages.put("api", "json");

        // Pass the languages map to the Export page
        model.addAttribute("languages", languages);


        // Log the name and content of each uploaded file for debugging
        entryList.forEach(entry -> {
            String fileName = entry.getKey();
            String fileContent = entry.getValue();
            System.out.println("File Name: " + fileName);
            System.out.println("File Content:\n" + fileContent);
        });


        // Pass the list of map entries to the Export page
        model.addAttribute("uploadedFiles", entryList);
        return "export";    }

    @GetMapping("/import")
    public String importPage() {
        return "import";
    }
}
