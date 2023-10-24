package com.myapp.caac.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myapp.caac.util.CustomApiListDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
@ToString
public class ExportConfigurations {

    @JsonProperty("name")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @JsonProperty("apiList")
//    @JsonSerialize(using = CustomApiListSerializer.class)
//    @JsonDeserialize(using = CustomApiListDeserializer.class)
    private List<CustomApi> apiList;

    public boolean hasApi(String apiId) {
        boolean exists = apiList.stream().anyMatch(api -> api.getId().equals(apiId));
//        log.info("exists:{},apiId:{},exportConfigurations:{} ", exists,apiId,this);
        return exists;
    }
}
