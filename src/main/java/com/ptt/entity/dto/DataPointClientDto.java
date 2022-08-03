package com.ptt.entity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataPointClientDto {
    private long planId;
    private long stepId;
    private long startTime;
    private long duration;

    @JsonCreator
    public DataPointClientDto(@JsonProperty("planId") long planId, @JsonProperty("stepId") long stepId, @JsonProperty("startTime") long startTime, @JsonProperty("duration") long duration) {
        this.planId = planId;
        this.stepId = stepId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public DataPointClientDto() {
    }

    public long getPlanId() {
        return planId;
    }
    public long getStepId() {
        return stepId;
    }
    public long getStartTime() {
        return startTime;
    }
    public long getDuration() {
        return duration;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "DataPoint [duration=" + duration + ", planId=" + planId + ", startTime=" + startTime + ", stepId="
                + stepId + "]";
    }

    
}
