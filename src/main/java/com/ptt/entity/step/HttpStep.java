package com.ptt.entity.step;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("http")
public class HttpStep extends Step {
    public String method;
    public String url;
    public String body;
    public RequestContentType responseContentType;
    public RequestContentType contentType;
    @OneToMany(mappedBy = "step", cascade = {CascadeType.REMOVE})
    public List<HttpStepHeader> headers = new ArrayList<>();
}
