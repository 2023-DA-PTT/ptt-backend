package com.ptt.control.plan;

import com.ptt.entity.plan.PlanRun;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlanRunRepository implements PanacheRepository<PlanRun> {
}
