package com.ptt.entity.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class InputArgumentDto {
    public String name;

    public InputArgumentDto(String name) {
        this.name = name;
    }
}
