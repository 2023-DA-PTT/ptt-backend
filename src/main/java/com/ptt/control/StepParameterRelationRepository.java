package com.ptt.control;

import com.ptt.entity.StepParameterRelation;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StepParameterRelationRepository implements PanacheRepository<StepParameterRelation> {
}
