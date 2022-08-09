package com.ptt.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class HttpStep extends PanacheEntityBase {

    @EmbeddedId
    public StepId id;

    public String method;
    public String url;
    public String body;
    @ElementCollection
    public Map<OutputArgument, String> parameterMap = new HashMap<>();
    //maps from the paramter name to the json Location
}
