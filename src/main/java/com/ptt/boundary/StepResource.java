package com.ptt.boundary;

import com.ptt.control.PlanRepository;
import com.ptt.control.StepRepository;
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
    public List<StepDto> getAllNextsByIdForPlan(@PathParam("planId") long planId, @PathParam("stepId") long stepId) {
        return stepRepository.
        getEntityManager()
        .createQuery("select NEW com.ptt.entity.dto.StepDto(s2.id, s2.name, s2.description, s2.type) from Step s1 join s1.nextSteps s2 where s1.plan.id=?1 and s1.id = ?2", StepDto.class)
        .setParameter(1, planId)
        .setParameter(2, stepId)
        .getResultList();
    }
}
