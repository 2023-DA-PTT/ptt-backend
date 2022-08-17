package com.ptt.boundary;

import com.ptt.control.OutputArgumentRepository;
import com.ptt.control.StepRepository;
import com.ptt.entity.OutputArgument;
import com.ptt.entity.Step;
import com.ptt.entity.dto.OutputArgumentDto;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("plan/{planId}/step/{stepId}/outputArgument")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OutputArgumentResource {

    @Inject
    OutputArgumentRepository outputArgumentRepository;
    @Inject
    StepRepository stepRepository;

    @POST
    @Transactional
    public Response createOutputArgumentForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            OutputArgumentDto outputArgumentDto) {
        Step step = stepRepository.findById(stepId);
        if(step == null) {
            return Response.status(400).build();
        }
        OutputArgument outputArgument = new OutputArgument();
        outputArgument.step = step;
        outputArgument.name = outputArgumentDto.getName();
        outputArgument.parameterLocation = outputArgumentDto.getParameterLocation();
        outputArgument.outputType = outputArgumentDto.getOutputType();
        outputArgumentRepository.persist(outputArgument);
        return Response.ok(OutputArgumentDto.from(outputArgument)).build();
    }

    @PUT
    @Transactional
    @Path("{outArgId}")
    public Response putOutputArgumentForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            @PathParam("outArgId") long outArgId,
            OutputArgumentDto outputArgumentDto) {
        Step step = stepRepository.findById(stepId);
        if(step == null) {
            return Response.status(400).build();
        }
        OutputArgument arg = outputArgumentRepository.findById(outArgId);
        if(arg == null) {
            return Response.status(400).build();
        }
        arg.name = outputArgumentDto.getName();
        arg.outputType = outputArgumentDto.getOutputType();
        arg.parameterLocation = outputArgumentDto.getParameterLocation();
        outputArgumentRepository.persist(arg);
        return Response.ok(OutputArgumentDto.from(arg)).status(200).build();
    }

    @DELETE
    @Transactional
    @Path("{outArgId}")
    public Response deleteInputArgumentById(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            @PathParam("outArgId") long outArgId) {
        OutputArgument outputArgument = outputArgumentRepository.find("id", outArgId).singleResult();
        if(outputArgument == null) {
            return Response.status(404).build();
        }
        outputArgumentRepository.delete(outputArgument);
        return Response.ok(OutputArgumentDto.from(outputArgument)).build();
    }

    @GET
    public List<OutputArgumentDto> getAllOutputArgumentForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId) {
        return outputArgumentRepository
                .find("step.id=?1 and step.plan.id=?2", stepId, planId)
                .project(OutputArgumentDto.class)
                .list();
    }

    @GET
    @Path("{outArgId}")
    public OutputArgumentDto getOutputArgumentByIdForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            @PathParam("outArgId") long outArgId) {
        return outputArgumentRepository
                .find("id=?1 and step.id=?2 and step.plan.id=?3", outArgId, stepId, planId)
                .project(OutputArgumentDto.class)
                .firstResult();
    }
}
