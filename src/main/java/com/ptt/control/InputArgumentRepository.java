package com.ptt.control;

import com.ptt.entity.InputArgument;
import com.ptt.entity.OutputArgument;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InputArgumentRepository implements PanacheRepository<InputArgument> {
}
