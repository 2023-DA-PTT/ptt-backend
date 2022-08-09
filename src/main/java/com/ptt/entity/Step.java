package com.ptt.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.List;

@Entity
public class Step extends PanacheEntityBase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne
    public Plan plan;
    @ManyToMany
    public List<Step> nextSteps;
    public String name;
    public String description;
    @OneToMany
    public List<InputArgument> inputArguments;
    @OneToMany
    public List<OutputArgument> outputArguments;
}
