package com.ptt.entity.step;

import com.ptt.entity.argument.InputArgument;
import com.ptt.entity.argument.OutputArgument;
import com.ptt.entity.plan.Plan;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "step_type", discriminatorType = DiscriminatorType.STRING)
public class Step extends PanacheEntityBase {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "step_id")
    public Long id;
    @ManyToOne
    public Plan plan;
    @ManyToMany(mappedBy = "fromStep", cascade = {CascadeType.REMOVE})
    public List<NextStep> nextSteps = new ArrayList<>();
    public String name;
    public String description;
    @Column(name = "step_type", insertable = false, updatable = false)
    public String type;
    @OneToMany(mappedBy = "step", cascade = {CascadeType.REMOVE})
    public List<InputArgument> inputArguments = new ArrayList<>();
    @OneToMany(mappedBy = "step", cascade = {CascadeType.REMOVE})
    public List<OutputArgument> outputArguments = new ArrayList<>();
}
