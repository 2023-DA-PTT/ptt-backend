package com.ptt.entity.dto;

import java.util.List;

public class PlanExportDto {
    private PlanDto plan;
    private List<HttpStepDto> httpSteps;
    private List<ScriptStepDto> scriptSteps;
    private List<InputArgumentDto> inputs;
    private List<OutputArgumentDto> outputs;
    private List<StepParameterRelationDto> relations;
    private List<SimpleNextStepDto> nextSteps;

    public PlanExportDto() {
    }
    public PlanDto getPlan() {
        return plan;
    }
    public List<SimpleNextStepDto> getNextSteps() {
        return nextSteps;
    }
    public void setNextSteps(List<SimpleNextStepDto> nextSteps) {
        this.nextSteps = nextSteps;
    }
    public void setPlan(PlanDto planDto) {
        this.plan = planDto;
    }
    public List<HttpStepDto> getHttpSteps() {
        return httpSteps;
    }
    public void setHttpSteps(List<HttpStepDto> httpSteps) {
        this.httpSteps = httpSteps;
    }
    public List<ScriptStepDto> getScriptSteps() {
        return scriptSteps;
    }
    public void setScriptSteps(List<ScriptStepDto> scriptSteps) {
        this.scriptSteps = scriptSteps;
    }
    public List<InputArgumentDto> getInputs() {
        return inputs;
    }
    public void setInputs(List<InputArgumentDto> inputs) {
        this.inputs = inputs;
    }
    public List<OutputArgumentDto> getOutputs() {
        return outputs;
    }
    public void setOutputs(List<OutputArgumentDto> outputs) {
        this.outputs = outputs;
    }
    public List<StepParameterRelationDto> getRelations() {
        return relations;
    }
    public void setRelations(List<StepParameterRelationDto> relations) {
        this.relations = relations;
    }


}
