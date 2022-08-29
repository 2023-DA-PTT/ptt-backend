package com.ptt.control.step;

import com.ptt.entity.step.StepParameterRelation;
import com.ptt.entity.dto.AdvancedStepParameterRelationDto;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StepParameterRelationRepository implements PanacheRepository<StepParameterRelation> {
    public List<AdvancedStepParameterRelationDto> getAdvancedStepParamterRelations(long from, long to) {
        return find("fromArg.step.id=?1 and toArg.step.id=?2", from,to).project(AdvancedStepParameterRelationDto.class).list();
    }
}
