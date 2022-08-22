package com.ptt.entity.dto;

import com.ptt.entity.HttpStep;
import com.ptt.entity.RequestContentType;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RegisterForReflection
public class HttpStepDto {
    private Long id;
    private String name;
    private String description;
    private String method;
    private String url;
    private String body;
    private RequestContentType responseContentType;
    private RequestContentType contentType;
    private Collection<HttpStepHeaderDto> headers;

    // ong, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.ptt.entity.RequestContentType, com.ptt.entity.RequestContentType, java.util.Collection
    public HttpStepDto(Long id, String name, String description, String method, String url, String body, RequestContentType responseContentType, RequestContentType contentType, Collection<HttpStepHeaderDto> headers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.method = method;
        this.url = url;
        this.body = body;
        this.responseContentType = responseContentType;
        this.contentType = contentType;
        this.headers = headers;
    }


    public static HttpStepDto from(HttpStep httpStep) {
        return new HttpStepDto(httpStep.id,
                httpStep.name,
                httpStep.description,
                httpStep.method,
                httpStep.url,
                httpStep.body,
                httpStep.responseContentType,
                httpStep.contentType,
                httpStep.headers.stream().map(HttpStepHeaderDto::from)
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
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


    public RequestContentType getResponseContentType() {
        return responseContentType;
    }


    public void setResponseContentType(RequestContentType responseContentType) {
        this.responseContentType = responseContentType;
    }


    public RequestContentType getContentType() {
        return contentType;
    }


    public void setContentType(RequestContentType contentType) {
        this.contentType = contentType;
    }

    public Collection<HttpStepHeaderDto> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HttpStepHeaderDto> headers) {
        this.headers = headers;
    }
}
