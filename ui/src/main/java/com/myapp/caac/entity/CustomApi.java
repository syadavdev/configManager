package com.myapp.caac.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomApi {
    private String id;
    private String label;
    private String language;

}