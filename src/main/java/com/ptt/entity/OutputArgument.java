package com.ptt.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import java.util.List;

import javax.persistence.*;

@Entity
public class OutputArgument extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne
    public Step step;
    public String name;
    public String parameterLocation;
    @Enumerated(EnumType.ORDINAL)
    public OutputType outputType;
    @OneToMany(mappedBy = "fromArg", cascade = {CascadeType.REMOVE})
    public List<StepParameterRelation> parameterRelations;
}
