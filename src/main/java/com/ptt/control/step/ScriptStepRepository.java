package com.ptt.control.step;

import javax.enterprise.context.ApplicationScoped;

import com.ptt.entity.step.ScriptStep;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ScriptStepRepository implements PanacheRepository<ScriptStep> {
    
}
