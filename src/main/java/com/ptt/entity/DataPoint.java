package com.ptt.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class DataPoint {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private final long planId;
    private final long stepId;
    private final long startTime;
    private final long duration;

    public DataPoint(@JsonProperty("planId") long planId, @JsonProperty("stepId") long stepId, @JsonProperty("startTime") long startTime, @JsonProperty("duration") long duration) {
        this.planId = planId;
        this.stepId = stepId;
        this.startTime = startTime;
        this.duration = duration;
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

    @Override
    public String toString() {
        return "DataPoint [duration=" + duration + ", planId=" + planId + ", startTime=" + startTime + ", stepId="
                + stepId + "]";
    }

    
}
