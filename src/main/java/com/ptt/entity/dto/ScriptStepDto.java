package com.ptt.entity.dto;

import com.ptt.entity.ScriptStep;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ScriptStepDto {
    private Long id;
    private String name;
    private String description;
    private String script;

    public ScriptStepDto(Long id, String name, String description, String script) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.script = script;
    }


    public static ScriptStepDto from(ScriptStep httpStep) {
        return new ScriptStepDto(httpStep.id, httpStep.name, httpStep.description, httpStep.script);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
