package com.myapp.caac.validations.content;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;

public class YamlContentValidator implements ContentValidator {

    @Override
    public boolean isValid(String content) {
        try {
            Yaml yaml = new Yaml();
            Object parsedContent = yaml.load(content);
            return parsedContent != null;
        } catch (ParserException e) {
            return false;
        }
    }
}
