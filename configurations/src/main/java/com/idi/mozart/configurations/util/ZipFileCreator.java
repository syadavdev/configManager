package com.idi.mozart.configurations.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


@Data
@Slf4j
public class ZipFileCreator {
    public static void createZipFile(Map<String ,String> fileNames, String zipFileName) throws IOException {

        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (Map.Entry<String,String> fileName : fileNames.entrySet()){
                Path resourceDirectory = Paths.get("src", "main", "resources");
                Path filePath = resourceDirectory.resolve(fileName.getKey());
                File file = filePath.toFile();
//                File file = new File(filePath);
                File metadataFile = new File(fileName.getValue());
                if (!file.exists() || !metadataFile.exists()) {
                    log.info("File not found: " + fileName.getKey());
                    continue; // Skip non-existent files
                }

                // Add a file to the ZIP archive
                ZipEntry zipEntry = new ZipEntry(fileName.getKey());
                zos.putNextEntry(zipEntry);

                try (FileInputStream fis = new FileInputStream(file)) {
                    zos.write(fis.readAllBytes());
                }

                ZipEntry zipEntry1 =  new ZipEntry(fileName.getValue());
                zos.putNextEntry(zipEntry1);

                try (FileInputStream fis = new FileInputStream(metadataFile)) {
                    zos.write(fis.readAllBytes());
                }

                zos.closeEntry();
            }

            File newFile = new File("root_metadata.json");
            ZipEntry newEntry = new ZipEntry(newFile.getName());
            zos.putNextEntry(newEntry);
            FileInputStream newFileInputStream = new FileInputStream(newFile);
            zos.write(newFileInputStream.readAllBytes());
            newFileInputStream.close();
            zos.closeEntry();
            // Close streams
            zos.close();

            // Replace the existing ZIP file with the updated one
            File existingZip = new File("bundle.zip");
            File updatedZip = new File("bundle.zip");
            if (updatedZip.renameTo(existingZip)) {
                System.out.println("File added to the existing ZIP file successfully.");
            } else {
                System.out.println("Failed to replace the existing ZIP file.");
            }

            System.out.println("ZIP file created successfully: " + zipFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

