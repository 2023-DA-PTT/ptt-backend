package com.ptt.entity.dto;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PlanRunDto {
    public long id;
    public long planId;
    public long startTime;
    public long duration;

    public PlanRunDto(long id, @ProjectedFieldName("plan.id") long planId, long startTime, long duration) {
        this.id = id;
        this.planId = planId;
        this.startTime = startTime;
        this.duration = duration;
    }    
}
