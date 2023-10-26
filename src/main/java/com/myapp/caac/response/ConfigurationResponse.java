package com.myapp.caac.response;

import lombok.Data;

@Data
public class ConfigurationResponse {
    private String status;
    private String message;

    public ConfigurationResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
