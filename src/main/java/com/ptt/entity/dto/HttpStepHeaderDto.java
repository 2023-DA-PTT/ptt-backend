package com.ptt.entity.dto;

import com.ptt.entity.HttpStepHeader;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class HttpStepHeaderDto {
    private Long id;
    private String name;
    private String value;

    public HttpStepHeaderDto(Long id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public static HttpStepHeaderDto from(HttpStepHeader header) {
        return new HttpStepHeaderDto(header.id, header.name, header.value);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
