package com.myapp.caac.model;

import lombok.Data;

@Data
public class Configuration {
    private String type;
    private String filename;

    public Configuration(String type, String filename) {
        this.type = type;
        this.filename = filename;
    }
}
