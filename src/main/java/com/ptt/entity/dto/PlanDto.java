package com.ptt.entity.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PlanDto {
    public String name;
    public String description;

    public PlanDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
