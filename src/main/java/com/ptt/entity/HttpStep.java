package com.ptt.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("http")
public class HttpStep extends Step {
    public String method;
    public String url;
    public String body;
    public RequestContentType responseContentType;
    public RequestContentType contentType; 
}
