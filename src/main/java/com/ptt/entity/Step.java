package com.ptt.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Step extends PanacheEntityBase {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "step_id")
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
