package com.ptt.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("script")
public class ScriptStep extends Step {
    @Column(length = 65000)
    public String script;
}
