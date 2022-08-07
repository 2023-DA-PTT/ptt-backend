package com.ptt.entity.dto;

import com.ptt.entity.StepParameterRelation;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class StepParameterRelationDto {
    public Long fromId;
    public Long toId;

    public StepParameterRelationDto(@ProjectedFieldName("fromArg.id") Long fromId,
                                    @ProjectedFieldName("toArg.id") Long toId) {
        this.fromId = fromId;
        this.toId = toId;
    }

    public static StepParameterRelationDto from(StepParameterRelation stepParameterRelation) {
        return new StepParameterRelationDto(stepParameterRelation.fromArg.id, stepParameterRelation.toArg.id);
    }
}
