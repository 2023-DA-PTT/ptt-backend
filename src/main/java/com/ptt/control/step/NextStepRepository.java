package com.ptt.control.step;

import com.ptt.entity.step.NextStep;
import com.ptt.entity.dto.NextStepWithParameterRelationDto;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class NextStepRepository implements PanacheRepository<NextStep> {

    @Inject
    StepParameterRelationRepository stepParameterRelationRepository;

    public List<NextStepWithParameterRelationDto> getAdvancedNextSteps(long planId, long stepId) {
        List<NextStepWithParameterRelationDto> dtos =
        find("fromStep.plan.id = ?1 and fromStep.id = ?2", planId, stepId)
        .project(NextStepWithParameterRelationDto.class)
        .list();

        for(NextStepWithParameterRelationDto dto: dtos) {
            dto.setStepParameterRelations(
                stepParameterRelationRepository
                    .getAdvancedStepParamterRelations(stepId, dto.getToStep().getId()));
        }
        return dtos;
    }
}
