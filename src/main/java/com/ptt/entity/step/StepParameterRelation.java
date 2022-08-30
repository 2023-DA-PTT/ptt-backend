package com.ptt.entity.step;

import com.ptt.entity.argument.InputArgument;
import com.ptt.entity.argument.OutputArgument;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
public class StepParameterRelation extends PanacheEntityBase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne
    public InputArgument toArg;
    @ManyToOne
    public OutputArgument fromArg;
}
