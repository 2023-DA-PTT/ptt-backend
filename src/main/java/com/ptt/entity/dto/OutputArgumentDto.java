package com.ptt.entity.dto;

import com.ptt.entity.OutputArgument;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OutputArgumentDto {
    public long stepId;
    public long id;
    public String name;
    public String jsonLocation;

    public OutputArgumentDto(long id, @ProjectedFieldName("step.id") long stepId, String name, String jsonLocation) {
        this.id = id;
        this.stepId = stepId;
        this.name = name;
        this.jsonLocation = jsonLocation;
    }

    public static OutputArgumentDto from(OutputArgument outputArgument) {
        return new OutputArgumentDto(outputArgument.id, outputArgument.step.id, outputArgument.name, outputArgument.jsonLocation);
    }
}
