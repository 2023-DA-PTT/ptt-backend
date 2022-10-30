package com.ptt.boundary.argument;

import com.ptt.control.argument.InputArgumentRepository;
import com.ptt.control.step.StepRepository;
import com.ptt.entity.argument.InputArgument;
import com.ptt.entity.step.Step;
import com.ptt.entity.dto.InputArgumentDto;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("plan/{planId}/step/{stepId}/inputArgument")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class InputArgumentResource {
    @Inject
    InputArgumentRepository inputArgumentRepository;

    @Inject
    JsonWebToken jwt;

    @Inject
    StepRepository stepRepository;

    @POST
    @Transactional
    public Response createInputArgumentForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            InputArgumentDto inputArgumentDto) {
        Step step = stepRepository.findById(stepId);
        if(step == null) {
            return Response.status(400).build();
        }

        if(!step.plan.ownerId.equals(jwt.getSubject())) {
            return Response.status(403).build();
        }

        InputArgument inputArgument = new InputArgument();
        inputArgument.name = inputArgumentDto.getName();
        inputArgument.step = step;
        inputArgumentRepository.persist(inputArgument);
        return Response.ok(InputArgumentDto.from(inputArgument)).status(201).build();
    }

    @PUT
    @Transactional
    @Path("{inArgId}")
    public Response putInputArgumentForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            @PathParam("inArgId") long inArgId,
            InputArgumentDto inputArgumentDto) {
        Step step = stepRepository.findById(stepId);
        if(step == null) {
            return Response.status(400).build();
        }

        if(!step.plan.ownerId.equals(jwt.getSubject())) {
            return Response.status(403).build();
        }

        InputArgument arg = inputArgumentRepository.findById(inArgId);
        if(arg == null) {
            return Response.status(404).build();
        }
        arg.name = inputArgumentDto.getName();
        inputArgumentRepository.persist(arg);
        return Response.ok(InputArgumentDto.from(arg)).status(200).build();
    }

    @DELETE
    @Transactional
    @Path("{inArgId}")
    public Response deleteInputArgumentById(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            @PathParam("inArgId") long inArgId) {
        InputArgument inputArgument = inputArgumentRepository.find("id", inArgId).singleResult();
        if(inputArgument == null) {
            return Response.status(404).build();
        }
        if(inputArgument.step.plan.ownerId.equals(jwt.getSubject())) {
            return Response.status(403).build();
        }
        inputArgumentRepository.delete(inputArgument);
        return Response.ok(InputArgumentDto.from(inputArgument)).build();
    }

    @GET
    public List<InputArgumentDto> getAllInputArgumentForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId) {
        return inputArgumentRepository
                .find("step.id=?1 and step.plan.id=?2 and step.plan.ownerId=?3", stepId, planId, jwt.getSubject())
                .project(InputArgumentDto.class)
                .list();
    }

    @GET
    @Path("{inArgId}")
    public InputArgumentDto getInputArgumentByIdForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            @PathParam("inArgId") long inArgId) {
        return inputArgumentRepository
                .find("id=?1 and step.id=?2 and step.plan.id=?3 and step.plan.ownerId=?4", inArgId, stepId, planId, jwt.getSubject())
                .project(InputArgumentDto.class)
                .firstResult();
    }
}
