package com.idi.mozart.configurations.util;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


@Data
public class ZipFileCreator {
    public static void createZipFile(String[] fileNames, String zipFileName) throws IOException {
//        try (FileOutputStream fos = new FileOutputStream(zipFileName);
//             ZipOutputStream zos = new ZipOutputStream(fos)) {
//
//            for (MultipartFile file : files) {
//                ZipEntry zipEntry = new ZipEntry(file.getOriginalFilename());
//                zos.putNextEntry(zipEntry);
//
//                // Get the InputStream from the MultipartFile
//                try (InputStream is = file.getInputStream();
//                     ReadableByteChannel channel = Channels.newChannel(is)) {
//                    byte[] data = new byte[1024];
//                    int read;
//                    while ((read = channel.read(ByteBuffer.wrap(data))) != -1) {
//                        zos.write(data, 0, read);
//                    }
//                }
//                zos.closeEntry();
//            }
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String fileName : fileNames) {
                File file = new File("src/main/resources/"+fileName);
                if (!file.exists()) {
                    System.out.println("File not found: " + fileName);
                    continue; // Skip non-existent files
                }

                // Add a file to the ZIP archive
                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);

                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                }

                zos.closeEntry();
            }

            File newFile = new File("rootMetadata.json");
            ZipEntry newEntry = new ZipEntry(newFile.getName());
            zos.putNextEntry(newEntry);
            byte[] data = new byte[1024];
            int bytesRead;
            FileInputStream newFileInputStream = new FileInputStream(newFile);
            while ((bytesRead = newFileInputStream.read(data)) != -1) {
                zos.write(data, 0, bytesRead);
            }
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

