package com.ptt.control.plan;

import com.ptt.entity.plan.Plan;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlanRepository implements PanacheRepository<Plan> {
}
