package com.ptt.control;

import com.ptt.entity.PlanRun;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlanRunRepository implements PanacheRepository<PlanRun> {
}
