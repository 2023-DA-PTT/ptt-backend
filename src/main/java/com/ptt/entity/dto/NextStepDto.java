package com.ptt.entity.dto;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class NextStepDto {
    private Long id;
    private StepDto toStep;
    private int repeatAmount;

    public NextStepDto(Long id,
            @ProjectedFieldName("toStep.id") long toStepId,
            @ProjectedFieldName("toStep.name") String toStepName,
            @ProjectedFieldName("toStep.description") String toStepDescription,
            @ProjectedFieldName("toStep.type") String toStepType,
            int repeatAmount) {
        this.id = id;
        this.toStep = new StepDto(toStepId, toStepName, toStepDescription, toStepType);
        this.repeatAmount = repeatAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StepDto getToStep() {
        return toStep;
    }

    public void setToStep(StepDto toStep) {
        this.toStep = toStep;
    }

    public int getRepeatAmount() {
        return repeatAmount;
    }

    public void setRepeatAmount(int repeatAmount) {
        this.repeatAmount = repeatAmount;
    }
}
