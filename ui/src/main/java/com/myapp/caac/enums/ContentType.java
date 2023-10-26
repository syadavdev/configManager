package com.myapp.caac.enums;

public enum ContentType {
    YAML("yaml", "yml"),
    JSON("json");

    private final String[] extensions;

    ContentType(String... extensions) {
        this.extensions = extensions;
    }

    public static ContentType fromString(String extension) {
        for (ContentType type : ContentType.values()) {
            for (String ext : type.extensions) {
                if (ext.equalsIgnoreCase(extension)) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException("Invalid content type: " + extension);
    }
}