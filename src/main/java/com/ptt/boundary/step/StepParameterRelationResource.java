package com.ptt.boundary.step;

import com.ptt.control.argument.InputArgumentRepository;
import com.ptt.control.argument.OutputArgumentRepository;
import com.ptt.control.step.StepParameterRelationRepository;
import com.ptt.entity.argument.InputArgument;
import com.ptt.entity.argument.OutputArgument;
import com.ptt.entity.step.StepParameterRelation;
import com.ptt.entity.dto.StepParameterRelationDto;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("plan/{planId}/step/{stepId}/parameterRelation")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class StepParameterRelationResource {

    @Inject
    StepParameterRelationRepository relationRepository;
    @Inject
    InputArgumentRepository inputArgumentRepository;
    @Inject
    JsonWebToken jwt;
    @Inject
    OutputArgumentRepository outputArgumentRepository;

    @POST
    @Transactional
    public Response createParameterRelation(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            StepParameterRelationDto relationDto) {
        OutputArgument outputArgument = outputArgumentRepository
                .find("id=?1 and step.id=?2 and step.plan.id=?3 and step.plan.ownerId=?4",
                        relationDto.getFromId(), stepId, planId, jwt.getSubject())
                .firstResult();
        InputArgument inputArgument = inputArgumentRepository
                .find("id=?1 and step.id=?2 and step.plan.id=?3 and step.plan.ownerId=?4",
                        relationDto.getToId(), stepId, planId, jwt.getSubject())
                .firstResult();
        if(outputArgument == null || inputArgument == null) {
                return Response.status(400).build();
        }
        StepParameterRelation stepParameterRelation = new StepParameterRelation();
        stepParameterRelation.fromArg = outputArgument;
        stepParameterRelation.toArg = inputArgument;
        relationRepository.persist(stepParameterRelation);
        return Response.ok(StepParameterRelationDto.from(stepParameterRelation)).build();
    }

    @GET
    @Path("from")
    public List<StepParameterRelationDto> getAllParameterRelationForStepFrom(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId) {
        return relationRepository
                .find("fromArg.step.id=?1 and fromArg.step.plan.id=?2 and fromArg.step.ownerId=?3",
                        stepId,planId,jwt.getSubject()).project(StepParameterRelationDto.class).list();
    }
    @GET
    @Path("to")
    public List<StepParameterRelationDto> getAllParameterRelationForStepTo(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId) {
        return relationRepository
                .find("toArg.step.id=?1 and toArg.step.plan.id=?2 and toArg.step.ownerId=?3",
                        stepId,planId, jwt.getSubject()).project(StepParameterRelationDto.class).list();
    }
}
