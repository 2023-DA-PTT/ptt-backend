package com.ptt.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OutputArgument extends PanacheEntityBase {
    @Id
    @GeneratedValue
    public Long id;
    @ManyToOne
    public Step step;
    public String name;
    public String jsonLocation;
}
