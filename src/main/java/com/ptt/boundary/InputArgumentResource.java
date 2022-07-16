package com.ptt.boundary;

import com.ptt.control.InputArgumentRepository;
import com.ptt.control.OutputArgumentRepository;
import com.ptt.entity.InputArgument;
import com.ptt.entity.OutputArgument;
import com.ptt.entity.dto.InputArgumentDto;
import com.ptt.entity.dto.OutputArgumentDto;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("plan/{planId}/step/{stepId}/inputArgument")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InputArgumentResource {
    @Inject
    InputArgumentRepository inputArgumentRepository;

    @POST
    @Transactional
    public InputArgumentDto createInputArgumentForStep(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            InputArgumentDto inputArgumentDto) {
        InputArgument inputArgument = new InputArgument();
        inputArgument.name = inputArgumentDto.name;
        inputArgumentRepository.persist(inputArgument);
        return inputArgumentDto;
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
                .singleResult();
    }
}
