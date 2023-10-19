package com.pnc.mozart.configurations.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
public class ConfigurationController {

	@RequestMapping(value = "api/tenant/getconfiguration", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public Object getTenantYamlConfiguration(HttpServletResponse response) throws Exception {
		response.setStatus(HttpStatus.OK.value());
		StringBuffer buf = new StringBuffer();
		InputStream is = getClass().getClassLoader().getResourceAsStream("tenant.yaml");
		try (InputStreamReader streamReader =
	                new InputStreamReader(is, StandardCharsets.UTF_8);
	         BufferedReader reader = new BufferedReader(streamReader)) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            buf.append(line);
	        }
	    } catch (Exception e) {
	        throw e;
	    }
		
		String yaml = buf.toString();
		return yaml;
	}
	
	@RequestMapping(value = "api/productfamily/getconfiguration", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public Object getProductFamilyYamlConfiguration(HttpServletResponse response) throws Exception {
		response.setStatus(HttpStatus.OK.value());
		StringBuffer buf = new StringBuffer();
		InputStream is = getClass().getClassLoader().getResourceAsStream("productfamily.yaml");
		try (InputStreamReader streamReader =
	                new InputStreamReader(is, StandardCharsets.UTF_8);
	         BufferedReader reader = new BufferedReader(streamReader)) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            buf.append(line);
	        }
	    } catch (Exception e) {
	        throw e;
	    }
		
		String yaml = buf.toString();
		return yaml;
	}
	
	@RequestMapping(value = "api/product/getconfiguration", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public Object getProductYamlConfiguration(HttpServletResponse response) throws Exception {
		response.setStatus(HttpStatus.OK.value());
		StringBuffer buf = new StringBuffer();
		InputStream is = getClass().getClassLoader().getResourceAsStream("product.yaml");
		try (InputStreamReader streamReader =
	                new InputStreamReader(is, StandardCharsets.UTF_8);
	         BufferedReader reader = new BufferedReader(streamReader)) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            buf.append(line);
	        }
	    } catch (Exception e) {
	        throw e;
	    }
		
		String yaml = buf.toString();
		return yaml;
	}
	
	@RequestMapping(value = "api/api/getconfiguration", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public Object getAPIJsonConfiguration(HttpServletResponse response) throws Exception {
		response.setStatus(HttpStatus.OK.value());
		StringBuffer buf = new StringBuffer();
		InputStream is = getClass().getClassLoader().getResourceAsStream("api.json");
		try (InputStreamReader streamReader =
	                new InputStreamReader(is, StandardCharsets.UTF_8);
	         BufferedReader reader = new BufferedReader(streamReader)) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            buf.append(line);
	        }
	    } catch (Exception e) {
	        throw e;
	    }
		
		String yaml = buf.toString();
		return yaml;
	}
	
	@RequestMapping(value = "api/api/saveconfiguration", method = RequestMethod.POST, produces = MediaType.ALL_VALUE)
	public Object saveAPIConfiguration(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		System.out.println(file);
		ClassLoader classLoader = getClass().getClassLoader();
		
		File dfile = new File(classLoader.getResource(".").getFile() + "../../src/main/resources/api.json");
		File d1file = new File(classLoader.getResource(".").getFile() + "/api.json");
		
		File nfile = new File(classLoader.getResource(".").getFile() + "../../src/main/resources/api.json");
		File n1file = new File(classLoader.getResource(".").getFile() + "/api.json");
		System.out.println(nfile.getAbsolutePath());
		
		
		// Delete File
		dfile.delete();
		d1file.delete();
		
		// Create files
		if (nfile.createNewFile()) {
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}
		
		if (n1file.createNewFile()) {
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}
		
		Path destinationFile = Paths.get(nfile.getAbsolutePath());
		Path destinationFile1 = Paths.get(n1file.getAbsolutePath());
		
		// Write to the files
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, destinationFile,
				StandardCopyOption.REPLACE_EXISTING);
		}
		
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, destinationFile1,
					StandardCopyOption.REPLACE_EXISTING);
		}
		
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}
	
	@RequestMapping(value = "api/product/saveconfiguration", method = RequestMethod.POST, produces = MediaType.ALL_VALUE)
	public Object saveProductConfiguration(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		System.out.println(file);
		ClassLoader classLoader = getClass().getClassLoader();
		
		File dfile = new File(classLoader.getResource(".").getFile() + "../../src/main/resources/product.yaml");
		File d1file = new File(classLoader.getResource(".").getFile() + "/product.yaml");
		
		File nfile = new File(classLoader.getResource(".").getFile() + "../../src/main/resources/product.yaml");
		File n1file = new File(classLoader.getResource(".").getFile() + "/product.yaml");
		System.out.println(nfile.getAbsolutePath());
		
		
		// Delete File
		dfile.delete();
		d1file.delete();
		
		// Create files
		if (nfile.createNewFile()) {
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}
		
		if (n1file.createNewFile()) {
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}
		
		Path destinationFile = Paths.get(nfile.getAbsolutePath());
		Path destinationFile1 = Paths.get(n1file.getAbsolutePath());
		
		// Write to the files
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, destinationFile,
				StandardCopyOption.REPLACE_EXISTING);
		}
		
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, destinationFile1,
					StandardCopyOption.REPLACE_EXISTING);
		}
		
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}
	
	@RequestMapping(value = "api/productfamily/saveconfiguration", method = RequestMethod.POST, produces = MediaType.ALL_VALUE)
	public Object saveProductFamilyConfiguration(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		System.out.println(file);
		ClassLoader classLoader = getClass().getClassLoader();
		
		File dfile = new File(classLoader.getResource(".").getFile() + "../../src/main/resources/productfamily.yaml");
		File d1file = new File(classLoader.getResource(".").getFile() + "/productfamily.yaml");
		
		File nfile = new File(classLoader.getResource(".").getFile() + "../../src/main/resources/productfamily.yaml");
		File n1file = new File(classLoader.getResource(".").getFile() + "/productfamily.yaml");
		System.out.println(nfile.getAbsolutePath());
		
		
		// Delete File
		dfile.delete();
		d1file.delete();
		
		// Create files
		if (nfile.createNewFile()) {
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}
		
		if (n1file.createNewFile()) {
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}
		
		Path destinationFile = Paths.get(nfile.getAbsolutePath());
		Path destinationFile1 = Paths.get(n1file.getAbsolutePath());
		
		// Write to the files
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, destinationFile,
				StandardCopyOption.REPLACE_EXISTING);
		}
		
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, destinationFile1,
					StandardCopyOption.REPLACE_EXISTING);
		}
		
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}
	
	@RequestMapping(value = "api/tenant/saveconfiguration", method = RequestMethod.POST, produces = MediaType.ALL_VALUE)
	public Object saveTenantConfiguration(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		System.out.println(file);
		ClassLoader classLoader = getClass().getClassLoader();
		
		File dfile = new File(classLoader.getResource(".").getFile() + "../../src/main/resources/tenant.yaml");
		File d1file = new File(classLoader.getResource(".").getFile() + "/tenant.yaml");
		
		File nfile = new File(classLoader.getResource(".").getFile() + "../../src/main/resources/tenant.yaml");
		File n1file = new File(classLoader.getResource(".").getFile() + "/tenant.yaml");
		System.out.println(nfile.getAbsolutePath());
		
		
		// Delete File
		dfile.delete();
		d1file.delete();
		
		// Create files
		if (nfile.createNewFile()) {
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}
		
		if (n1file.createNewFile()) {
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}
		
		Path destinationFile = Paths.get(nfile.getAbsolutePath());
		Path destinationFile1 = Paths.get(n1file.getAbsolutePath());
		
		// Write to the files
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, destinationFile,
				StandardCopyOption.REPLACE_EXISTING);
		}
		
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, destinationFile1,
					StandardCopyOption.REPLACE_EXISTING);
		}
		
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}
}
