package com.ptt.entity.dto;

import com.ptt.entity.Step;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class StepDto {
    private long id;
    private String name;
    private String description;
    private String method;
    private String url;
    private String body;

    public StepDto(long id, String name, String description, String method, String url, String body) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.method = method;
        this.url = url;
        this.body = body;
    }

    public static StepDto from(Step step) {
        return new StepDto(step.id, step.name, step.description, step.method, step.url, step.body);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    
}
