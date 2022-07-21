package com.ptt.entity.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class StepDto {
    public long id;
    public String name;
    public String description;
    public String method;
    public String url;
    public String body;

    public StepDto(long id, String name, String description, String method, String url, String body) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.method = method;
        this.url = url;
        this.body = body;
    }
}
