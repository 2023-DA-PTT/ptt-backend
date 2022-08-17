package com.ptt.boundary;

import com.ptt.control.InputArgumentRepository;
import com.ptt.control.StepRepository;
import com.ptt.entity.InputArgument;
import com.ptt.entity.Step;
import com.ptt.entity.dto.InputArgumentDto;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("plan/{planId}/step/{stepId}/inputArgument")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InputArgumentResource {
    @Inject
    InputArgumentRepository inputArgumentRepository;

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
        InputArgument inputArgument = new InputArgument();
        inputArgument.name = inputArgumentDto.getName();
        inputArgument.step = step;
        inputArgumentRepository.persist(inputArgument);
        return Response.ok(InputArgumentDto.from(inputArgument)).status(201).build();
    }

    @PUT
    @Transactional
    public Response putInputArgumentForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            InputArgumentDto inputArgumentDto) {
        Step step = stepRepository.findById(stepId);
        if(step == null) {
            return Response.status(400).build();
        }
        InputArgument arg = inputArgumentRepository.findById(inputArgumentDto.getId());
        if(arg == null) {
            return Response.status(400).build();
        }
        arg.name = inputArgumentDto.getName();
        inputArgumentRepository.persist(arg);
        return Response.ok(InputArgumentDto.from(arg)).status(200).build();
    }

    @GET
    public List<InputArgumentDto> getAllInputArgumentForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId) {
        return inputArgumentRepository
                .find("step.id=?1 and step.plan.id=?2", stepId, planId)
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
                .find("id=?1 and step.id=?2 and step.plan.id=?3", inArgId, stepId, planId)
                .project(InputArgumentDto.class)
                .firstResult();
    }
}
