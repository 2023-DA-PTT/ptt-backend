package com.ptt.entity.dto;

import java.util.List;

import com.ptt.entity.PlanRun;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PlanRunDto {
    private long id;
    private long planId;
    private long startTime;
    private long duration;
    private List<PlanRunInstructionDto> planRunInstructions;

    public PlanRunDto(long id, @ProjectedFieldName("plan.id") long planId, long startTime, long duration) {
        this.id = id;
        this.planId = planId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public static PlanRunDto from(PlanRun planRun, List<PlanRunInstructionDto> planRunInstructions) {
        PlanRunDto planRunDto = new PlanRunDto(planRun.id, planRun.plan.id, planRun.startTime, planRun.duration);
        planRunDto.planRunInstructions = planRunInstructions;
        return planRunDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
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

    public List<PlanRunInstructionDto> getPlanRunInstructions() {
        return planRunInstructions;
    }

    public void setPlanRunInstructions(List<PlanRunInstructionDto> planRunInstructions) {
        this.planRunInstructions = planRunInstructions;
    }
}
