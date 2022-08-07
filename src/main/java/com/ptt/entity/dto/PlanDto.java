package com.ptt.entity.dto;

import com.ptt.entity.Plan;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PlanDto {
    public long id;
    public long startId;
    public String name;
    public String description;

    public PlanDto(long id, @ProjectedFieldName("start.id") long startId, String name, String description) {
        this.id = id;
        this.startId = startId;
        this.name = name;
        this.description = description;
    }

    public static PlanDto from(Plan plan) {
        return new PlanDto(plan.id, plan.start.id, plan.name, plan.description);
    }
}
