package com.idi.mozart.configurations.controller;


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
	public ResponseEntity importConfigs(@RequestParam("file") MultipartFile zipFile) throws IOException {
		if (zipFile.isEmpty()) {
			logger.info("zip file is empty");
			return ResponseEntity.badRequest().body("empty zip file");
		}

		Path resourceDirectory = Paths.get("src", "main", "resources");
		Path filePath = resourceDirectory.resolve("config/");
		logger.info("Extracting file to: {}", filePath.toAbsolutePath());
		File targetDir = new File(filePath.toAbsolutePath().toString());
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}

		// Create a ZipInputStream to read the contents of the uploaded zip file
		try (ZipInputStream zipInputStream = new ZipInputStream(zipFile.getInputStream())) {
			ZipEntry entry;
			while ((entry = zipInputStream.getNextEntry()) != null) {
				String entryName = entry.getName();
				File entryFile = new File(targetDir.getAbsolutePath(), entryName);

				// Ensure the parent directory of the entry file exists
				if (!entryFile.getParentFile().exists()) {
					entryFile.getParentFile().mkdirs();
				}

				// Write the entry data to the entry file
				try (OutputStream outputStream = new FileOutputStream(entryFile)) {
					byte[] buffer = new byte[1024];
					int len;
					while ((len = zipInputStream.read(buffer)) > 0) {
						outputStream.write(buffer, 0, len);
					}
				}
			}
		}

		//generate random response;
		int min = 1;  // Minimum value of the range (inclusive)
		int max = 10;  // Maximum value of the range (inclusive)

		Random random = new Random();
		int randomNumber = random.nextInt((max - min) + 1) + min;

		if(randomNumber > 5)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
		else
			return ResponseEntity.status(HttpStatus.OK).body("Successfully exported");



	}
}
