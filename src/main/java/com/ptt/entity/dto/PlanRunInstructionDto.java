package com.ptt.entity.dto;

import java.util.ArrayList;
import java.util.List;

import com.ptt.entity.PlanRunInstruction;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PlanRunInstructionDto {
    private int numberOfClients;
    private String nodeName;

    public PlanRunInstructionDto(int numberOfClients, String nodeName) {
        this.numberOfClients = numberOfClients;
        this.nodeName = nodeName;
    }

    public static PlanRunInstructionDto from(PlanRunInstruction planRunInstruction) {
        return new PlanRunInstructionDto(planRunInstruction.getNumberOfClients(), planRunInstruction.getNodeName());
    }

    public static List<PlanRunInstructionDto> from(List<PlanRunInstruction> planRunInstructions) {
        List<PlanRunInstructionDto> result = new ArrayList<>();
        for(PlanRunInstruction planRunInstruction : planRunInstructions) {
            result.add(new PlanRunInstructionDto(planRunInstruction.getNumberOfClients(), planRunInstruction.getNodeName()));
        }
        return result;
    }
    
    public int getNumberOfClients() {
        return numberOfClients;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
