package com.ptt.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.List;

@Entity

public class Plan extends PanacheEntityBase {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @OneToOne
    public Step start;
    @ManyToOne
    public User user;
    @OneToMany
    public List<Step> steps;
    public String name;
    public String description;
}
