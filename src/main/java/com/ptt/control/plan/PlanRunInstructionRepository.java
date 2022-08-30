package com.ptt.control.plan;

import com.ptt.entity.plan.PlanRunInstruction;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlanRunInstructionRepository implements PanacheRepository<PlanRunInstruction> {
}
