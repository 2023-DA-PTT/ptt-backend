package com.ptt.entity.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class StepDto {
    public String name;
    public String description;
    public String method;
    public String url;
    public String body;

    public StepDto(String name, String description, String method, String url, String body) {
        this.name = name;
        this.description = description;
        this.method = method;
        this.url = url;
        this.body = body;
    }
}
