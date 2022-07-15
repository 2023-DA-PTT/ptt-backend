package com.ptt.entity.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OutputArgumentDto {
    public String name;
    public String jsonLocation;

    public OutputArgumentDto(String name, String jsonLocation) {
        this.name = name;
        this.jsonLocation = jsonLocation;
    }
}
