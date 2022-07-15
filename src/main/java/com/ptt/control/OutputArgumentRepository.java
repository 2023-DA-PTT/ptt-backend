package com.ptt.control;

import com.ptt.entity.OutputArgument;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OutputArgumentRepository implements PanacheRepository<OutputArgument> {
}
