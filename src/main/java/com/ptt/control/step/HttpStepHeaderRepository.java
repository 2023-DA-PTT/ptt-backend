package com.ptt.control.step;

import com.ptt.entity.step.HttpStepHeader;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HttpStepHeaderRepository implements PanacheRepository<HttpStepHeader> {
}
