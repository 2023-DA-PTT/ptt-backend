package com.ptt.entity.dto;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AdvancedStepParameterRelationDto {
    private Long id;
    private InputArgumentDto inputArg;
    private Long outputArgId;

    public AdvancedStepParameterRelationDto(
        Long id,
        @ProjectedFieldName("toArg.id") long toId,
        @ProjectedFieldName("toArg.step.id") long toStepId,
        @ProjectedFieldName("toArg.name") String toName,
        @ProjectedFieldName("fromArg.id") long fromId) {
        this.id = id;
        inputArg = new InputArgumentDto(toId, toStepId, toName);
        outputArgId = fromId;
    }

    public InputArgumentDto getInputArg() {
        return inputArg;
    }

    public void setInputArg(InputArgumentDto inputArg) {
        this.inputArg = inputArg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOutputArgId() {
        return outputArgId;
    }

    public void setOutputArgId(Long outputArgId) {
        this.outputArgId = outputArgId;
    }
}
