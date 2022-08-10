package com.ptt.control;

import javax.enterprise.context.ApplicationScoped;

import com.ptt.entity.ScriptStep;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ScriptStepRepository implements PanacheRepository<ScriptStep> {
    
}
