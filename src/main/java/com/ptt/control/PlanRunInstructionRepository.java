package com.ptt.control;

import com.ptt.entity.PlanRunInstruction;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlanRunInstructionRepository implements PanacheRepository<PlanRunInstruction> {
}
