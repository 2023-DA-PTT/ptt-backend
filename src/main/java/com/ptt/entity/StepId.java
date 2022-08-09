package com.ptt.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

@Embeddable
public class StepId implements Serializable {
    @OneToOne
    private Step step;
    @Enumerated(EnumType.STRING)
    private StepType type;
    
    public StepId(Step step, StepType type) {
        this.step = step;
        this.type = type;
    }

    public StepId() {
    }

    public Step getStep() {
        return step;
    }
    public void setStep(Step step) {
        this.step = step;
    }
    public StepType getType() {
        return type;
    }
    public void setType(StepType type) {
        this.type = type;
    }
}