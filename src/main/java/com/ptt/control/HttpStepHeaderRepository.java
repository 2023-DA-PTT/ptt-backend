package com.ptt.control;

import com.ptt.entity.HttpStepHeader;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HttpStepHeaderRepository implements PanacheRepository<HttpStepHeader> {
}
