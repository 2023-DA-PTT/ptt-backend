package com.ptt.control;

import javax.enterprise.context.ApplicationScoped;

import com.ptt.entity.HttpStep;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class HttpStepRepository implements PanacheRepository<HttpStep> {
    
}
