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

        HttpStep httpStep = new HttpStep();
        httpStep.name = httpStepDto.getName();
        httpStep.description = httpStepDto.getDescription();
        httpStep.plan = plan;
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
        HttpStep httpStep = httpStepRepository
            .find("id=?1", stepId).firstResult();
        if(httpStep == null) {
            return Response.status(400).build();
        }

        httpStep.name = httpStepDto.getName();
        httpStep.description = httpStepDto.getDescription();
        httpStep.body = httpStepDto.getBody();
        httpStep.method = httpStepDto.getMethod();
        httpStep.url = httpStepDto.getUrl();
        httpStepRepository.persist(httpStep);

        return Response.ok(HttpStepDto.from(httpStep)).build();
    }

    @GET
    @Path("http")
    public List<HttpStepDto> getAllStepsForPlan(@PathParam("planId") long planId) {
        return httpStepRepository.find("plan.id", planId).project(HttpStepDto.class).list();
    }
}
