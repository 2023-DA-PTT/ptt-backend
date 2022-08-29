package com.ptt.control.argument;

import com.ptt.entity.argument.OutputArgument;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OutputArgumentRepository implements PanacheRepository<OutputArgument> {
}
