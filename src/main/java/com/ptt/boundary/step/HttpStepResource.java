package com.ptt.boundary.step;

import java.util.List;
import java.util.stream.Collectors;

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

import com.ptt.control.step.HttpStepHeaderRepository;
import com.ptt.control.step.HttpStepRepository;
import com.ptt.control.plan.PlanRepository;
import com.ptt.control.step.StepRepository;
import com.ptt.entity.step.HttpStep;
import com.ptt.entity.step.HttpStepHeader;
import com.ptt.entity.plan.Plan;
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
    @Inject
    HttpStepHeaderRepository httpStepHeaderRepository;

    @POST
    @Path("http")
    @Transactional
    public Response createHttpStepForPlan(
            @PathParam("planId") long planId,
            HttpStepDto httpStepDto) {
        Plan plan = planRepository.findById(planId);
        if (plan == null) {
            return Response.status(400).build();
        }

        HttpStep httpStep = new HttpStep();
        httpStep.name = httpStepDto.getName();
        httpStep.description = httpStepDto.getDescription();
        httpStep.plan = plan;
        httpStep.body = httpStepDto.getBody();
        httpStep.method = httpStepDto.getMethod();
        httpStep.url = httpStepDto.getUrl();
        httpStep.responseContentType = httpStepDto.getResponseContentType();
        httpStep.contentType = httpStepDto.getContentType();
        httpStepRepository.persist(httpStep);

        httpStepDto.getHeaders().stream().map(headerDto -> {
            var header = new HttpStepHeader();
            header.value = headerDto.getValue();
            header.name = headerDto.getName();
            header.step = httpStep;
            return header;
        }).forEach(header -> httpStepHeaderRepository.persist(header));

        return Response.ok(HttpStepDto.from(httpStep)).build();
    }

    @PUT
    @Path("{stepId}/http")
    @Transactional
    public Response updateHttpStepForPlan(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId,
            HttpStepDto httpStepDto) {
        Plan plan = planRepository.findById(planId);
        if (plan == null) {
            return Response.status(400).build();
        }
        HttpStep httpStep = httpStepRepository
                .find("id=?1", stepId).firstResult();
        if (httpStep == null) {
            return Response.status(400).build();
        }

        httpStep.name = httpStepDto.getName();
        httpStep.description = httpStepDto.getDescription();
        httpStep.body = httpStepDto.getBody();
        httpStep.method = httpStepDto.getMethod();
        httpStep.url = httpStepDto.getUrl();
        httpStep.responseContentType = httpStepDto.getResponseContentType();
        httpStep.contentType = httpStepDto.getContentType();
        httpStepRepository.persist(httpStep);

        for(var headerDto : httpStepDto.getHeaders()) {
            HttpStepHeader header;

            if(headerDto.getId() != null && headerDto.getId() > 0) {
                header = httpStepHeaderRepository.findById(headerDto.getId());

                if(header == null)
                    return Response.status(400).build();
            }
            else {
                header = new HttpStepHeader();
            }

            header.value = headerDto.getValue();
            header.name = headerDto.getName();
            header.step = httpStep;

            httpStepHeaderRepository.persist(header);
        }

        return Response.ok(HttpStepDto.from(httpStep)).build();
    }

    @GET
    @Path("{stepId}/http")
    @Transactional
    public Response getHttpStepForPlan(
            @PathParam("planId") long planId,
            @PathParam("stepId") long stepId) {
        Plan plan = planRepository.findById(planId);
        if (plan == null) {
            return Response.status(400).build();
        }
        HttpStep httpStep = httpStepRepository
                .find("id=?1", stepId).firstResult();
        if (httpStep == null) {
            return Response.status(400).build();
        }

        return Response.ok(HttpStepDto.from(httpStep)).build();
    }


    @GET
    @Path("http")
    public List<HttpStepDto> getAllHttpStepsForPlan(@PathParam("planId") long planId) {
        return httpStepRepository.find("plan.id", planId).list().stream().map(HttpStepDto::from).collect(Collectors.toList());
    }
}
