package com.ptt.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("script")
public class ScriptStep extends Step {
    public String script;
}
