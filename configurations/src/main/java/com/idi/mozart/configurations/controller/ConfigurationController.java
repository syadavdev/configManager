package com.idi.mozart.configurations.controller;


import com.idi.mozart.configurations.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@CrossOrigin
@RestController
public class ConfigurationController {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

	@Autowired
	private ImportService importService;

	private String readResourceFile(String resourceName) throws IOException {
		Path resourceDirectory = Paths.get("src", "main", "resources");
		Path filePath = resourceDirectory.resolve(resourceName);
		try (
				InputStream is = Files.newInputStream(filePath);
				InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader reader = new BufferedReader(streamReader)
		) {
			StringBuilder buf = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				buf.append(line).append(System.lineSeparator());
			}
			return buf.toString();
		}
	}

	private void saveConfigurationFile(MultipartFile file, String resourcePath) throws IOException {
		Path resourceDirectory = Paths.get("src", "main", "resources");
		Path filePath = resourceDirectory.resolve(resourcePath);
		logger.info("Saving file to: {}", filePath.toAbsolutePath());

			// Ensure the directory exists
			Files.createDirectories(filePath.getParent());

			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
	}



	@RequestMapping(value = "api/tenant/getconfiguration", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public Object getTenantYamlConfiguration(HttpServletResponse response) throws Exception {
		return ResponseEntity.ok(readResourceFile("tenant.yaml"));
	}
	
	@RequestMapping(value = "api/productfamily/getconfiguration", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public Object getProductFamilyYamlConfiguration(HttpServletResponse response) throws Exception {
		return ResponseEntity.ok(readResourceFile("productfamily.yaml"));
	}
	
	@RequestMapping(value = "api/product/getconfiguration", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public Object getProductYamlConfiguration(HttpServletResponse response) throws Exception {
		return ResponseEntity.ok(readResourceFile("product.yaml"));
	}
	
	@RequestMapping(value = "api/api/getconfiguration", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public Object getAPIJsonConfiguration(HttpServletResponse response) throws Exception {
		return ResponseEntity.ok(readResourceFile("api.json"));
	}
	
	@RequestMapping(value = "api/api/saveconfiguration", method = RequestMethod.POST, produces = MediaType.ALL_VALUE)
	public Object saveAPIConfiguration(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		saveConfigurationFile(file, "api.json");
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/";
	}
	
	@RequestMapping(value = "api/product/saveconfiguration", method = RequestMethod.POST, produces = MediaType.ALL_VALUE)
	public Object saveProductConfiguration(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		saveConfigurationFile(file, "product.yaml");
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/";
	}
	
	@RequestMapping(value = "api/productfamily/saveconfiguration", method = RequestMethod.POST, produces = MediaType.ALL_VALUE)
	public Object saveProductFamilyConfiguration(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		saveConfigurationFile(file, "productfamily.yaml");
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/";
	}
	
	@RequestMapping(value = "api/tenant/saveconfiguration", method = RequestMethod.POST, produces = MediaType.ALL_VALUE)
	public Object saveTenantConfiguration(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		saveConfigurationFile(file, "tenant.yaml");
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/";
	}

	@PostMapping(value = "api/import")
	public ResponseEntity<String> importConfigs(@RequestParam("file") MultipartFile zipFile) throws IOException {
		if (zipFile.isEmpty()) {
			logger.info("zip file is empty");
			return ResponseEntity.badRequest().body("empty zip file");
		}

		return ResponseEntity.status(HttpStatus.OK).body(importService.importConfigs(zipFile));
	}
}
