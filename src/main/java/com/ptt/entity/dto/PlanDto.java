package com.ptt.entity.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PlanDto {
    public long id;
    public String name;
    public String description;

    public PlanDto(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
