package com.ptt.entity.plan;

import com.ptt.entity.step.Step;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.List;

@Entity

public class Plan extends PanacheEntityBase {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @OneToOne
    public Step start;
    public String ownerId;
    @OneToMany(mappedBy = "plan")
    public List<Step> steps;
    public String name;
    public String description;
}
