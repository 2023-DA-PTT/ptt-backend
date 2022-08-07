package com.ptt.boundary;

import com.ptt.control.OutputArgumentRepository;
import com.ptt.entity.OutputArgument;
import com.ptt.entity.dto.OutputArgumentDto;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("plan/{planId}/step/{stepId}/outputArgument")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OutputArgumentResource {

    @Inject
    OutputArgumentRepository outputArgumentRepository;

    @POST
    @Transactional
    public OutputArgumentDto createOutputArgumentForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            OutputArgumentDto outputArgumentDto) {
        OutputArgument outputArgument = new OutputArgument();
        outputArgument.name = outputArgumentDto.getName();
        outputArgument.jsonLocation = outputArgumentDto.getJsonLocation();
        outputArgumentRepository.persist(outputArgument);
        return OutputArgumentDto.from(outputArgument);
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
                .singleResult();
    }
}
