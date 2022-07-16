package com.ptt.boundary;

import com.ptt.control.StepRepository;
import com.ptt.entity.Step;
import com.ptt.entity.dto.StepDto;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("plan/{planId}/step")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StepResource {

    @Inject
    StepRepository stepRepository;

    @GET
    public List<StepDto> getAllStepsForPlan(@PathParam("planId") long planId) {
        return stepRepository.find("plan.id", planId).project(StepDto.class).list();
    }

    @GET
    @Path("{stepId}")
    public StepDto getAllStepByIdForPlan(@PathParam("planId") long planId, @PathParam("stepId") long stepId) {
        return stepRepository.find("plan.id = ?1 and id = ?2", planId, stepId).project(StepDto.class).singleResult();
    }

    @POST
    @Transactional
    public StepDto createStepForPlan(
            @PathParam("planId") long planId,
            StepDto stepDto) {
        Step step = new Step();
        step.name = stepDto.name;
        step.body = stepDto.body;
        step.description = stepDto.description;
        step.method = stepDto.method;
        step.url = stepDto.url;
        stepRepository.persist(step);
        return stepDto;
    }
}
