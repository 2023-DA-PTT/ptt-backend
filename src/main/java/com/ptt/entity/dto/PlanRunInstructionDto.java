package com.ptt.entity.dto;

public class PlanRunInstructionDto {
    private int amount;
    private String nodeName;

    public PlanRunInstructionDto() {
    }

    public PlanRunInstructionDto(int amount, String nodeName) {
        this.amount = amount;
        this.nodeName = nodeName;
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
