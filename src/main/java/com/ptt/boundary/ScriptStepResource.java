package com.ptt.boundary;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ptt.control.ScriptStepRepository;
import com.ptt.control.PlanRepository;
import com.ptt.control.StepRepository;
import com.ptt.entity.ScriptStep;
import com.ptt.entity.Plan;
import com.ptt.entity.dto.ScriptStepDto;


@Path("plan/{planId}/step")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ScriptStepResource {
    @Inject
    StepRepository stepRepository;

    @Inject
    PlanRepository planRepository;

    @Inject
    ScriptStepRepository scriptStepRepository;

    @POST
    @Path("script")
    @Transactional
    public Response createScriptStepForPlan(
            @PathParam("planId") long planId,
            ScriptStepDto scriptStepDto) {
        Plan plan = planRepository.findById(planId);
        if(plan == null) {
            return Response.status(400).build();
        }

        ScriptStep scriptStep = new ScriptStep();
        scriptStep.name = scriptStepDto.getName();
        scriptStep.description = scriptStepDto.getDescription();
        scriptStep.plan = plan;
        scriptStep.script = scriptStepDto.getScript();
        scriptStepRepository.persist(scriptStep);

        return Response.ok(ScriptStepDto.from(scriptStep)).build();
    }

    @POST
    @Path("{stepId}/script")
    @Transactional
    public Response updateScriptStepForPlan(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            ScriptStepDto scriptStepDto) {
        Plan plan = planRepository.findById(planId);
        if(plan == null) {
            return Response.status(400).build();
        }
        ScriptStep scriptStep = scriptStepRepository
            .find("id=?1", stepId).firstResult();
        if(scriptStep == null) {
            return Response.status(400).build();
        }

        scriptStep.name = scriptStepDto.getName();
        scriptStep.description = scriptStepDto.getDescription();
        scriptStep.script = scriptStepDto.getScript();
        scriptStepRepository.persist(scriptStep);

        return Response.ok(ScriptStepDto.from(scriptStep)).build();
    }

    @GET
    @Path("{stepId}/script")
    @Transactional
    public Response getScriptStepForPlan(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId) {
        Plan plan = planRepository.findById(planId);
        if(plan == null) {
            return Response.status(400).build();
        }
        ScriptStep scriptStep = scriptStepRepository
            .find("id=?1", stepId).firstResult();
        if(scriptStep == null) {
            return Response.status(400).build();
        }

        return Response.ok(ScriptStepDto.from(scriptStep)).build();
    }

    @GET
    @Path("script")
    public List<ScriptStepDto> getAllStepsForPlan(@PathParam("planId") long planId) {
        return scriptStepRepository.find("plan.id", planId).project(ScriptStepDto.class).list();
    }
}
