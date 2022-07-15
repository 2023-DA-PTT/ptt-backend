package com.ptt.control;

import com.ptt.entity.Step;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StepRepository implements PanacheRepository<Step> {
}
