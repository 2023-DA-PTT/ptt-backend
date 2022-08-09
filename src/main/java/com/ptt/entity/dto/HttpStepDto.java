package com.ptt.entity.dto;

import com.ptt.entity.HttpStep;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class HttpStepDto {
    private String name;
    private String description;
    private String method;
    private String url;
    private String body;

    public HttpStepDto(String name, String description, String method, String url, String body) {
        this.name = name;
        this.description = description;
        this.method = method;
        this.url = url;
        this.body = body;
    }


    public static HttpStepDto from(HttpStep httpStep) {
        return new HttpStepDto(httpStep.id.getStep().name, httpStep.id.getStep().description, httpStep.method, httpStep.url, httpStep.body);
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

}
