package com.ptt.control.argument;

import com.ptt.entity.argument.InputArgument;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InputArgumentRepository implements PanacheRepository<InputArgument> {
}
