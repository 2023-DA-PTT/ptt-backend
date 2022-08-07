package com.ptt.entity.dto;

import java.util.ArrayList;
import java.util.List;

import com.ptt.entity.PlanRunInstruction;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PlanRunInstructionDto {
    private int amount;
    private String nodeName;

    public PlanRunInstructionDto(int amount, String nodeName) {
        this.amount = amount;
        this.nodeName = nodeName;
    }

    public static PlanRunInstructionDto from(PlanRunInstruction planRunInstruction) {
        return new PlanRunInstructionDto(planRunInstruction.getAmount(), planRunInstruction.getNodeName());
    }

    public static List<PlanRunInstructionDto> from(List<PlanRunInstruction> planRunInstructions) {
        List<PlanRunInstructionDto> result = new ArrayList<>();
        for(PlanRunInstruction planRunInstruction : planRunInstructions) {
            result.add(new PlanRunInstructionDto(planRunInstruction.getAmount(), planRunInstruction.getNodeName()));
        }
        return result;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
