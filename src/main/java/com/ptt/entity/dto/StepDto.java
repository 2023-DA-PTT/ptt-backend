package com.ptt.entity.dto;

import com.ptt.entity.step.Step;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class StepDto {
    private long id;
    private String name;
    private String description;
    private String type;

    public StepDto(long id, String name, String description, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public static StepDto from(Step step) {
        return new StepDto(step.id, step.name, step.description, step.type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

}
