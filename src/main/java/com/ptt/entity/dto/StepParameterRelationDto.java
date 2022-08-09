package com.ptt.entity.dto;

import com.ptt.entity.StepParameterRelation;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class StepParameterRelationDto {
    private Long id;
    private Long fromId;
    private Long toId;

    public StepParameterRelationDto(@ProjectedFieldName("id") Long id,
                                    @ProjectedFieldName("fromArg.id") Long fromId,
                                    @ProjectedFieldName("toArg.id") Long toId) {
        this.fromId = fromId;
        this.toId = toId;
    }

    public static StepParameterRelationDto from(StepParameterRelation stepParameterRelation) {
        return new StepParameterRelationDto(stepParameterRelation.id, stepParameterRelation.fromArg.id, stepParameterRelation.toArg.id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    
}
