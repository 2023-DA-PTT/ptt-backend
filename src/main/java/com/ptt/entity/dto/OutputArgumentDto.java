package com.ptt.entity.dto;

import com.ptt.entity.OutputArgument;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OutputArgumentDto {
    private long stepId;
    private long id;
    private String name;
    private String jsonLocation;

    public OutputArgumentDto(long id, @ProjectedFieldName("step.id") long stepId, String name, String jsonLocation) {
        this.id = id;
        this.stepId = stepId;
        this.name = name;
        this.jsonLocation = jsonLocation;
    }

    public static OutputArgumentDto from(OutputArgument outputArgument) {
        return new OutputArgumentDto(outputArgument.id, outputArgument.step.id, outputArgument.name, outputArgument.jsonLocation);
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

    public String getJsonLocation() {
        return jsonLocation;
    }

    public void setJsonLocation(String jsonLocation) {
        this.jsonLocation = jsonLocation;
    }

    
}
