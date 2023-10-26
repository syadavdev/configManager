package com.myapp.caac.service.resource;

import com.myapp.caac.enums.ProductName;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ConfigurationManagementFactory {

    private ProductConfigurationService productConfigurationService;
    private TenantConfigurationService tenantConfigurationService;
    private ProductFamilyConfigurationService productFamilyConfigurationService;
    private ApiConfigurationService apiConfigurationService;

    public ConfigurationManagementService getConfigurationManagement(ProductName productEnum) {
        return switch (productEnum) {
            case PRODUCT -> productConfigurationService;
            case TENANT -> tenantConfigurationService;
            case PRODUCTFAMILY -> productFamilyConfigurationService;
            case API -> apiConfigurationService;
        };
    }
}
