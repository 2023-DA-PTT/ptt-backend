package com.ptt.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DataPointDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private long planRunId;
    private long stepId;
    private long startTime;
    private long duration;

    public DataPointDto(Long id,
            @ProjectedFieldName("planRun.id") long planRunId,
            @ProjectedFieldName("step.id") long stepId,
            long startTime,
            long duration) {
        this.id = id;
        this.planRunId = planRunId;
        this.stepId = stepId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public DataPointDto(@ProjectedFieldName("planRun.id") long planRunId,
                        @ProjectedFieldName("step.id") long stepId,
                        long startTime,
                        long duration) {
        this(null, planRunId, stepId, startTime, duration);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
