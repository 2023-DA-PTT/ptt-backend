package com.ptt.entity.argument;

import com.ptt.entity.step.Step;
import com.ptt.entity.step.StepParameterRelation;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import java.util.List;

import javax.persistence.*;

@Entity
public class InputArgument extends PanacheEntityBase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne
    public Step step;
    public String name;
    @OneToMany(mappedBy = "toArg", cascade = {CascadeType.REMOVE})
    public List<StepParameterRelation> parameterRelations;
}
