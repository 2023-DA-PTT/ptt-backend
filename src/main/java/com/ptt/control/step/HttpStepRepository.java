package com.ptt.control.step;

import javax.enterprise.context.ApplicationScoped;

import com.ptt.entity.step.HttpStep;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class HttpStepRepository implements PanacheRepository<HttpStep> {
    
}
