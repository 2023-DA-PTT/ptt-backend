package com.ptt.boundary;

import com.ptt.control.NextStepRepository;
import com.ptt.control.PlanRepository;
import com.ptt.control.StepRepository;
import com.ptt.entity.dto.NextStepDto;
import com.ptt.entity.dto.StepDto;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.List;

@Path("plan/{planId}/step")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StepResource {

    @Inject
    StepRepository stepRepository;

    @Inject
    PlanRepository planRepository;

    @Inject
    NextStepRepository nextStepRepository;

    @GET
    public List<StepDto> getAllStepsForPlan(@PathParam("planId") long planId) {
        return stepRepository.find("plan.id", planId).project(StepDto.class).list();
    }

    @GET
    @Path("{stepId}")
    public StepDto getAllStepByIdForPlan(@PathParam("planId") long planId, @PathParam("stepId") long stepId) {
        return stepRepository.find("plan.id = ?1 and id = ?2", planId, stepId).project(StepDto.class).singleResult();
    }

    @GET
    @Path("{stepId}/nexts")
    public List<NextStepDto> getAllNextsByIdForPlan(@PathParam("planId") long planId, @PathParam("stepId") long stepId) {
        return nextStepRepository
        .find("fromStep.plan.id = ?1 and fromStep.id = ?2", planId, stepId)
        .project(NextStepDto.class)
        .list();
    }
}
