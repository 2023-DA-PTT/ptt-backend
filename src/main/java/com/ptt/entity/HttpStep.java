package com.ptt.entity;

import javax.persistence.Entity;

@Entity
public class HttpStep extends Step {
    public String method;
    public String url;
    public String body;
    public String responseContentType;
}
