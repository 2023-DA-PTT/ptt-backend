package com.ptt.entity.dto;

import com.ptt.entity.OutputArgument;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OutputArgumentDto {
    private long stepId;
    private long id;
    private String name;
    private String parameterLocation;

    public OutputArgumentDto(long id, @ProjectedFieldName("step.id") long stepId, String name, String parameterLocation) {
        this.id = id;
        this.stepId = stepId;
        this.name = name;
        this.parameterLocation = parameterLocation;
    }

    public static OutputArgumentDto from(OutputArgument outputArgument) {
        return new OutputArgumentDto(outputArgument.id, outputArgument.step.id, outputArgument.name, outputArgument.parameterLocation);
    }

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
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

    public String getParameterLocation() {
        return parameterLocation;
    }

    public void setParameterLocation(String parameterLocation) {
        this.parameterLocation = parameterLocation;
    }
}
