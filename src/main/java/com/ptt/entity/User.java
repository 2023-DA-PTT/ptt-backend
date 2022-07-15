package com.ptt.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "PTT_USER")
public class User extends PanacheEntityBase {
    @Id @GeneratedValue
    public Long id;
    public String username;
}
