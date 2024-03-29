package com.ptt.boundary.step;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ptt.control.step.ScriptStepRepository;
import com.ptt.control.plan.PlanRepository;
import com.ptt.control.step.StepRepository;
import com.ptt.entity.step.ScriptStep;
import com.ptt.entity.plan.Plan;
import com.ptt.entity.dto.ScriptStepDto;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;


@Path("plan/{planId}/step")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class ScriptStepResource {
    @Inject
    StepRepository stepRepository;

    @Inject
    PlanRepository planRepository;

    @Inject
    JsonWebToken jwt;

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

        if (!plan.ownerId.equals(jwt.getSubject())) {
            return Response.status(403).build();
        }

        ScriptStep scriptStep = new ScriptStep();
        scriptStep.name = scriptStepDto.getName();
        scriptStep.description = scriptStepDto.getDescription();
        scriptStep.plan = plan;
        scriptStep.script = scriptStepDto.getScript();
        scriptStepRepository.persist(scriptStep);

        return Response.ok(ScriptStepDto.from(scriptStep)).build();
    }

    @PUT
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
        if (!plan.ownerId.equals(jwt.getSubject())) {
            return Response.status(403).build();
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
        if (!plan.ownerId.equals(jwt.getSubject())) {
            return Response.status(403).build();
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
    public List<ScriptStepDto> getAllScriptStepsForPlan(@PathParam("planId") long planId) {
        return scriptStepRepository.find("plan.id=?1 and plan.ownerId=?2", planId, jwt.getSubject()).project(ScriptStepDto.class).list();
    }
}
