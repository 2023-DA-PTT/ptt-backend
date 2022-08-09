package com.ptt.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Entity
public class HttpStep extends Step {
    public String method;
    public String url;
    public String body;
    @ElementCollection
    public Map<OutputArgument, String> parameterMap = new HashMap<>();
    //maps from the paramter name to the json Location
}
