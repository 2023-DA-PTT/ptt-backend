package com.ptt.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StepParameterRelation extends PanacheEntityBase {
    @Id @GeneratedValue
    public Long id;
    @ManyToOne
    public InputArgument to;
    @ManyToOne
    public OutputArgument from;
}
