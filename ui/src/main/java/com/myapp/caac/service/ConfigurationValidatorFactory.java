package com.myapp.caac.service;

import com.myapp.caac.enums.ProductName;
import com.myapp.caac.validations.api.*;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationValidatorFactory {

    private final ConfigurationValidator tenantValidator = new TenantConfigurationValidator();
    private final ConfigurationValidator productValidator = new ProductConfigurationValidator();
    private final ConfigurationValidator productFamilyValidator = new ProductFamilyConfigurationValidator();
    private final ConfigurationValidator apiValidator = new ApiConfigurationValidator();

    public ConfigurationValidator getValidator(ProductName productName) {
        return switch (productName) {
            case TENANT -> tenantValidator;
            case PRODUCT -> productValidator;
            case PRODUCTFAMILY -> productFamilyValidator;
            case API -> apiValidator;
        };
    }
}
