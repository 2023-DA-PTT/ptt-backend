package com.ptt.entity.dto;

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
}
