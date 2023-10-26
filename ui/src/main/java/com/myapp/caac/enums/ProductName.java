package com.myapp.caac.enums;

import com.myapp.caac.exception.InvalidConfigurationTypeException;
import java.util.Arrays;

public enum ProductName {
    PRODUCT("product", "product", "Product", "yaml"),
    TENANT("tenant", "tenant", "Tenant", "yaml"),
    PRODUCTFAMILY("productfamily", "productfamily", "ProductFamily", "yaml"),
    API("api", "api", "API", "json");

    private final String id;
    private final String displayName;
    private final String label;
    private final String language;

    ProductName(String id, String displayName, String label, String language) {
        this.id = id;
        this.displayName = displayName;
        this.label = label;
        this.language = language;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLabel() {
        return label;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static ProductName fromString(String productName) {
        try {
            String upperCase = productName.toUpperCase();
            return ProductName.valueOf(upperCase);
        } catch (IllegalArgumentException e) {
            throw new InvalidConfigurationTypeException("Invalid configuration type: " + productName);
        }
    }

    public static ProductName fromDisplayName(String displayName) {
        return Arrays.stream(ProductName.values())
                .filter(productName -> productName.displayName.equals(displayName))
                .findFirst()
                .orElseThrow(() -> new InvalidConfigurationTypeException("Invalid configuration display name: " + displayName));
    }
}
