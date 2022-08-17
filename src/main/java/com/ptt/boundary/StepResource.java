package com.ptt.boundary;

import com.ptt.control.NextStepRepository;
import com.ptt.control.PlanRepository;
import com.ptt.control.StepRepository;
import com.ptt.entity.Step;
import com.ptt.entity.dto.NextStepDto;
import com.ptt.entity.dto.StepDto;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @DELETE
    @Transactional
    @Path("{stepId}")
    public Response deleteStepById(@PathParam("planId") long planId, @PathParam("stepId") long stepId) {
        Step step = stepRepository.find("plan.id = ?1 and id = ?2", planId, stepId).singleResult();
        if(step == null) {
            return Response.status(404).build();
        }
        if(step.plan.start == step) {
            step.plan.start = null;
            planRepository.persist(step.plan);
        }
        stepRepository.delete(step);
        return Response.ok(StepDto.from(step)).build();
    }
}
