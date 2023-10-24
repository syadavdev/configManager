package com.myapp.caac.repository;

import com.myapp.caac.entity.CustomApi;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiRepository {
    @NotNull
    public static List<CustomApi> getCustomApis() {
        CustomApi tenantApi = new CustomApi("tenant", "Tenant", "yaml");
        CustomApi productFamilyApi = new CustomApi("productfamily", "Product Family", "yaml");
        CustomApi productApi = new CustomApi("product", "Product", "yaml");
        CustomApi apiApi = new CustomApi("api", "API", "json");
        return List.of(tenantApi, productFamilyApi, productApi, apiApi);
    }
}
