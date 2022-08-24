package com.ptt.entity.dto;

import com.ptt.entity.NextStep;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class SimpleNextStepDto {
    private Long id;
    private Long fromStepId;
    private Long toStepId;
    private int repeatAmount;
    
    public SimpleNextStepDto(Long id,
            @ProjectedFieldName("fromStep.id") Long fromStepId,
            @ProjectedFieldName("toStep.id") long toStepId,
            int repeatAmount) {
        this.id = id;
        this.fromStepId = fromStepId;
        this.toStepId = toStepId;
        this.repeatAmount = repeatAmount;
    }

    public static SimpleNextStepDto from(NextStep nextStep) {
        return new SimpleNextStepDto(nextStep.id,nextStep.fromStep.id, nextStep.toStep.id, nextStep.repeatAmount);
    }

    public Long getToStepId() {
        return toStepId;
    }

    public void setToStepId(Long toStepId) {
        this.toStepId = toStepId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRepeatAmount() {
        return repeatAmount;
    }

    public void setRepeatAmount(int repeatAmount) {
        this.repeatAmount = repeatAmount;
    }

    public Long getFromStepId() {
        return fromStepId;
    }

    public void setFromStepId(Long fromStepId) {
        this.fromStepId = fromStepId;
    }
}
