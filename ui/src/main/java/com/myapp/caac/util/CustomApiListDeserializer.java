package com.myapp.caac.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.myapp.caac.entity.CustomApi;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.myapp.caac.repository.ApiRepository.getCustomApis;

@Slf4j
public class CustomApiListDeserializer extends StdDeserializer<List<CustomApi>> {

    private static final List<CustomApi> predefinedApiList = getCustomApiList();

    public CustomApiListDeserializer() {
        this(null);
    }

    public CustomApiListDeserializer(Class<?> vc) {
        super(vc);
    }

    private static List<CustomApi> getCustomApiList() {
        return getCustomApis();
    }

    @Override
    public List<CustomApi> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        log.info("deserialize");
        List<CustomApi> apiList = new ArrayList<>();
        JsonNode node = jp.getCodec().readTree(jp);
        log.info("node:{}", node);
        if (node.isArray()) {
            log.info("node.isArray()");
            for (JsonNode elementNode : node) {
                log.info("elementNode:{}", elementNode);
                log.info("elementNode: {}", elementNode.getClass());
                String label = elementNode.asText();
                log.info("label:{}", label);
                // Find the matching CustomApi object from the predefined list
                CustomApi matchingApi = findMatchingApi(label);
                if (matchingApi != null) {
                    apiList.add(matchingApi);
                }
            }
        }
        log.info("apiList:{}", apiList);
        return apiList;
    }

    private CustomApi findMatchingApi(String label) {
        return predefinedApiList.stream()
                .filter(api -> label.equalsIgnoreCase(api.getLabel()))
                .findFirst()
                .orElse(null);
    }
}
