package com.idi.mozart.configurations.controller;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@CrossOrigin
@RestController
public class ConfigurationController {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

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
}
