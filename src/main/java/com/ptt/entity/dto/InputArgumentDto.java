package com.ptt.entity.dto;

import com.ptt.entity.InputArgument;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class InputArgumentDto {
    public long id;
    public String name;
    public long stepId;

    public InputArgumentDto(long id, @ProjectedFieldName("step.id") long stepId, String name) {
        this.id = id;
        this.stepId = stepId;
        this.name = name;
    }

    public static InputArgumentDto from(InputArgument inputArgument) {
        return new InputArgumentDto(inputArgument.id, inputArgument.step.id, inputArgument.name);
    }
}
