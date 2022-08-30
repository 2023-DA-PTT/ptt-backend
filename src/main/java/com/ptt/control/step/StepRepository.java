package com.ptt.control.step;

import com.ptt.entity.step.Step;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StepRepository implements PanacheRepository<Step> {
}
