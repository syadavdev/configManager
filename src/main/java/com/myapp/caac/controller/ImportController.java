package com.myapp.caac.controller;

import com.myapp.caac.service.ImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@AllArgsConstructor
@RestController
@CrossOrigin
public class ImportController {

    private final ImportService importService;

    /**
     * @param zipFile - Multipart zip file
     * @return Successful or Unsuccessful response
     */
    @PostMapping(value = "api/import", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Import configuration operation, provide .zip In form of Multipart",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Imported successfully/Import unsuccessfully : response in form of string",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(
                                            name = "successExample",
                                            value = "{\"success\": \"Imported successfully\"}")
                            )
                    )
            }
    )
    public ResponseEntity<Map<String, String>> importConfigs(@NotNull @RequestParam(value = "file") MultipartFile zipFile)
            throws IOException {
        if (zipFile.isEmpty()) {
            log.info("zip file is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("status", "empty zip file"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("status", importService.importConfigs(zipFile)));
    }
}
