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

import com.ptt.control.HttpStepRepository;
import com.ptt.control.PlanRepository;
import com.ptt.control.StepRepository;
import com.ptt.entity.HttpStep;
import com.ptt.entity.Plan;
import com.ptt.entity.Step;
import com.ptt.entity.StepId;
import com.ptt.entity.StepType;
import com.ptt.entity.dto.HttpStepDto;


@Path("plan/{planId}/step")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HttpStepResource {
    @Inject
    StepRepository stepRepository;

    @Inject
    PlanRepository planRepository;

    @Inject
    HttpStepRepository httpStepRepository;

    @POST
    @Path("http")
    @Transactional
    public Response createHttpStepForPlan(
            @PathParam("planId") long planId,
            HttpStepDto httpStepDto) {
        Plan plan = planRepository.findById(planId);
        if(plan == null) {
            return Response.status(400).build();
        }

        Step step = new Step();
        step.name = httpStepDto.getName();
        step.description = httpStepDto.getDescription();
        step.plan = plan;
        stepRepository.persist(step);

        HttpStep httpStep = new HttpStep();
        httpStep.id = new StepId(step, StepType.HTTP);
        httpStep.body = httpStepDto.getBody();
        httpStep.method = httpStepDto.getMethod();
        httpStep.url = httpStepDto.getUrl();
        httpStepRepository.persist(httpStep);

        return Response.ok(HttpStepDto.from(httpStep)).build();
    }

    @POST
    @Path("{stepId}/http")
    @Transactional
    public Response updateHttpStepForPlan(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            HttpStepDto httpStepDto) {
        Plan plan = planRepository.findById(planId);
        if(plan == null) {
            return Response.status(400).build();
        }

        Step step = stepRepository.findById(stepId);
        if(step == null) {
            return Response.status(400).build();
        }
        HttpStep httpStep = httpStepRepository
            .find("id.step.id=?1 and id.type.type=?2", stepId, StepType.HTTP).firstResult();
        if(httpStep == null) {
            return Response.status(400).build();
        }

        step.name = httpStepDto.getName();
        step.description = httpStepDto.getDescription();
        stepRepository.persist(step);

        httpStep.body = httpStepDto.getBody();
        httpStep.method = httpStepDto.getMethod();
        httpStep.url = httpStepDto.getUrl();
        httpStepRepository.persist(httpStep);

        return Response.ok(HttpStepDto.from(httpStep)).build();
    }

    @GET
    @Path("http")
    public List<HttpStepDto> getAllStepsForPlan(@PathParam("planId") long planId) {
        return httpStepRepository.find("id.type.type=?1", StepType.HTTP).project(HttpStepDto.class).list();
    }
}
