package com.ptt.entity.dto;

import com.ptt.entity.argument.InputArgument;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class InputArgumentDto {
    private long id;
    private String name;
    private long stepId;

    public InputArgumentDto(long id, @ProjectedFieldName("step.id") long stepId, String name) {
        this.id = id;
        this.stepId = stepId;
        this.name = name;
    }

    public static InputArgumentDto from(InputArgument inputArgument) {
        return new InputArgumentDto(inputArgument.id, inputArgument.step.id, inputArgument.name);
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

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    
}
