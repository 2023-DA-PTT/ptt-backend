package com.ptt.control;

import com.ptt.entity.NextStep;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NextStepRepository implements PanacheRepository<NextStep> {
}
