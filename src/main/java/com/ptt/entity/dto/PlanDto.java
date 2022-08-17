package com.ptt.entity.dto;

import com.ptt.entity.Plan;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PlanDto {
    private long id;
    private Long startId;
    private String name;
    private String description;

    public PlanDto(long id, @ProjectedFieldName("start.id") Long startId, String name, String description) {
        this.id = id;
        this.startId = startId;
        this.name = name;
        this.description = description;
    }

    public static PlanDto from(Plan plan) {
        return new PlanDto(plan.id, plan.start == null ? null : plan.start.id, plan.name, plan.description);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getStartId() {
        return startId;
    }

    public void setStartId(Long startId) {
        this.startId = startId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}
