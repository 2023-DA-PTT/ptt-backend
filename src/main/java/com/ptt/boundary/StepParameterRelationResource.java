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
                .find("id=?1 and step.id=?2 and step.plan.id=?3", relationDto.fromId, stepId, planId)
                .singleResult();
        InputArgument inputArgument = inputArgumentRepository
                .find("id=?1 and step.id=?2 and step.plan.id=?3", relationDto.toId, stepId, planId)
                .singleResult();
        StepParameterRelation stepParameterRelation = new StepParameterRelation();
        stepParameterRelation.from = outputArgument;
        stepParameterRelation.to = inputArgument;
        relationRepository.persist(stepParameterRelation);
        return relationDto;
    }

    @GET
    @Path("from")
    public List<StepParameterRelationDto> getAllParameterRelationForStepFrom(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId) {
        return relationRepository
                .find("from.step.id=?1 and from.step.plan.id=?2",
                        stepId,planId).project(StepParameterRelationDto.class).list();
    }

    @GET
    @Path("to")
    public List<StepParameterRelationDto> getAllParameterRelationForStepTo(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId) {
        return relationRepository
                .find("to.step.id=?1 and to.step.plan.id=?2",
                        stepId,planId).project(StepParameterRelationDto.class).list();
    }
}
