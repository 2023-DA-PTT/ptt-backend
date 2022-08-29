package com.ptt.entity.dto;
import com.ptt.entity.argument.OutputArgument;
import com.ptt.entity.argument.OutputType;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OutputArgumentDto {
    private long stepId;
    private long id;
    private String name;
    private String parameterLocation;
    private OutputType outputType;

    public OutputArgumentDto(long id, @ProjectedFieldName("step.id") long stepId, String name, String parameterLocation, OutputType outputType) {
        this.id = id;
        this.stepId = stepId;
        this.name = name;
        this.parameterLocation = parameterLocation;
        this.outputType = outputType;
    }

    public static OutputArgumentDto from(OutputArgument outputArgument) {
        return new OutputArgumentDto(outputArgument.id, outputArgument.step.id, outputArgument.name, outputArgument.parameterLocation, outputArgument.outputType);
    }

    public OutputType getOutputType() {
        return outputType;
    }

    public void setOutputType(OutputType outputType) {
        this.outputType = outputType;
    }

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameterLocation() {
        return parameterLocation;
    }

    public void setParameterLocation(String parameterLocation) {
        this.parameterLocation = parameterLocation;
    }
}
