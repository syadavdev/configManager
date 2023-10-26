package com.myapp.caac.validations.api;

import com.myapp.caac.enums.ContentType;
import com.myapp.caac.validations.content.ContentValidator;
import com.myapp.caac.validations.content.ContentValidatorFactory;
import org.springframework.stereotype.Service;

public class TenantConfigurationValidator implements ConfigurationValidator {
    @Override
    public boolean isValid(String content) {
        ContentValidatorFactory factory = new ContentValidatorFactory();
        ContentValidator validator = factory.getValidator(ContentType.YAML);
        return validator.isValid(content);

    }
}
