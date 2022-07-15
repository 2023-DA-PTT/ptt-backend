package com.ptt.boundary;

import com.ptt.control.InputArgumentRepository;
import com.ptt.control.OutputArgumentRepository;
import com.ptt.control.StepParameterRelationRepository;
import com.ptt.entity.InputArgument;
import com.ptt.entity.OutputArgument;
import com.ptt.entity.StepParameterRelation;
import com.ptt.entity.dto.StepParameterRelationDto;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("plan/{planId}/step/{stepId}/parameterRelation")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StepParameterRelationResource {

    @Inject
    StepParameterRelationRepository relationRepository;
    @Inject
    InputArgumentRepository inputArgumentRepository;
    @Inject
    OutputArgumentRepository outputArgumentRepository;

    @POST
    @Transactional
    public StepParameterRelationDto createParameterRelation(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            StepParameterRelationDto relationDto) {
        OutputArgument outputArgument = outputArgumentRepository
                .find("id=? and step.id=? and step.plan.id=?", relationDto.fromId, stepId, planId)
                .singleResult();
        InputArgument inputArgument = inputArgumentRepository
                .find("id=? and step.id=? and step.plan.id=?", relationDto.toId, stepId, planId)
                .singleResult();
        StepParameterRelation stepParameterRelation = new StepParameterRelation();
        stepParameterRelation.from = outputArgument;
        stepParameterRelation.to = inputArgument;
        relationRepository.persist(stepParameterRelation);
        return relationDto;
    }

    @GET
    public List<StepParameterRelationDto> getAllParameterRelationForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId) {
        return relationRepository
                .find("from.step.id=? and from.step.plan.id=? or to.step.id=? and to.step.plan.id=?",
                stepId,planId,stepId,planId).project(StepParameterRelationDto.class).list();
    }
}
