package com.ptt.control;

import javax.enterprise.context.ApplicationScoped;

import com.ptt.entity.HttpStep;
import com.ptt.entity.StepId;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class HttpStepRepository implements PanacheRepositoryBase<HttpStep, StepId> {
    
}
