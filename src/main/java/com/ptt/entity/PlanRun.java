package com.ptt.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity

public class PlanRun extends PanacheEntityBase {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne
    public Plan plan;
    public long startTime;
    public long duration;
    public String name;
    public boolean runOnce;
    @OneToMany(mappedBy = "planRun")
    public List<PlanRunInstruction> planRunInstructions = new ArrayList<>();
}
