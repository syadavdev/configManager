package com.myapp.caac.validations.content;

import com.myapp.caac.enums.ContentType;

public class ContentValidatorFactory {

    public ContentValidator getValidator(ContentType contentType) {
        return switch (contentType) {
            case YAML -> new YamlContentValidator();
            case JSON -> new JsonContentValidator();
        };
    }}
