package com.idi.mozart.configurations.util;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


@Data
public class ZipFileCreator {
    public static void createZipFile(MultipartFile[] files, String zipFileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (MultipartFile file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getOriginalFilename());
                zos.putNextEntry(zipEntry);

                // Get the InputStream from the MultipartFile
                try (InputStream is = file.getInputStream();
                     ReadableByteChannel channel = Channels.newChannel(is)) {
                    byte[] data = new byte[1024];
                    int read;
                    while ((read = channel.read(ByteBuffer.wrap(data))) != -1) {
                        zos.write(data, 0, read);
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

        }
    }

    // Rest of the code remains the same
}