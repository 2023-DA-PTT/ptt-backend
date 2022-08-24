package com.ptt.entity.dto;

import java.util.List;

import com.ptt.entity.Step;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class StepWithNextsDto {
    private long id;
    private String name;
    private String description;
    private String type;
    private List<SimpleNextStepDto> nexts;

    public StepWithNextsDto(long id, String name, String description, String type, List<SimpleNextStepDto> nexts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.nexts = nexts;
    }

    public static StepWithNextsDto from(Step step) {
        return new StepWithNextsDto(step.id,
                step.name,
                step.description,
                step.type,
                step.nextSteps
                        .stream()
                        .map(nes -> SimpleNextStepDto.from(nes))
                        .toList());
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

    public List<SimpleNextStepDto> getNexts() {
        return nexts;
    }

    public void setNexts(List<SimpleNextStepDto> nexts) {
        this.nexts = nexts;
    }

}
