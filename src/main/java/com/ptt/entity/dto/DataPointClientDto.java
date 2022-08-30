package com.ptt.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataPointClientDto implements Serializable {
    private long planRunId;
    private long stepId;
    private long startTime;
    private long duration;

    @JsonCreator
    public DataPointClientDto(@JsonProperty("planRunId") long planRunId, @JsonProperty("stepId") long stepId, @JsonProperty("startTime") long startTime, @JsonProperty("duration") long duration) {
        this.planRunId = planRunId;
        this.stepId = stepId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public DataPointClientDto() {
    }

    public long getPlanRunId() {
        return planRunId;
    }

    public void setPlanRunId(long planRunId) {
        this.planRunId = planRunId;
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
        return "DataPointClientDto [duration=" + duration + ", planRunId=" + planRunId + ", startTime=" + startTime
                + ", stepId=" + stepId + "]";
    }
}
