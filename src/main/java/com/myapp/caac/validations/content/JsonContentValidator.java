package com.myapp.caac.validations.content;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonContentValidator implements ContentValidator {

    @Override
    public boolean isValid(String content) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
