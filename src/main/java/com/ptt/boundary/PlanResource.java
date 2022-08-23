package com.ptt.boundary;

import com.ptt.control.HttpStepRepository;
import com.ptt.control.InputArgumentRepository;
import com.ptt.control.OutputArgumentRepository;
import com.ptt.control.PlanRepository;
import com.ptt.control.ScriptStepRepository;
import com.ptt.control.StepParameterRelationRepository;
import com.ptt.control.UserRepository;
import com.ptt.entity.Plan;
import com.ptt.entity.User;
import com.ptt.entity.dto.HttpStepDto;
import com.ptt.entity.dto.InputArgumentDto;
import com.ptt.entity.dto.OutputArgumentDto;
import com.ptt.entity.dto.PlanDto;
import com.ptt.entity.dto.ScriptStepDto;
import com.ptt.entity.dto.StepParameterRelationDto;
import com.ptt.entity.dto.export.PlanExportDto;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("plan")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlanResource {
    @ApplicationPath("/api")
    public static class ApplicationConfiguration extends Application {

    }

    @Inject
    PlanRepository planRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    HttpStepRepository httpStepRepository;
    @Inject
    ScriptStepRepository scriptStepRepository;
    @Inject
    InputArgumentRepository inputArgumentRepository;
    @Inject
    OutputArgumentRepository outputArgumentRepository;
    @Inject
    StepParameterRelationRepository relationRepository;

    @GET
    public List<PlanDto> getAllPlans() {
       return planRepository.findAll().project(PlanDto.class).list();
    }

    @GET
    @Path("user/{userId}")
    public List<PlanDto> getAllPlansForUser(@PathParam("userId") long userId) {
        return planRepository.find("plan.user.id", userId).project(PlanDto.class).list();
    }

    @GET
    @Path("{id}")
    public PlanDto getPlanById(@PathParam("id") long planId) {
        return planRepository.find("id", planId).project(PlanDto.class).singleResult();
    }

    @POST
    @Path("{userId}")
    @Transactional
    public Response createPlanForUser(@PathParam("userId") long userId, PlanDto planDto) {
        User user = userRepository.findById(userId);
        if(user == null) {
            return Response.status(400).build();
        }
        Plan plan = new Plan();
        plan.name = planDto.getName();
        plan.description = planDto.getDescription();
        plan.user = user;
        planRepository.persist(plan);
        return Response.ok(PlanDto.from(plan)).status(201).build();
    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response export(@PathParam("id") long planId) {
        PlanExportDto exportDto = new PlanExportDto();
        Plan plan = planRepository.find("id", planId).firstResult();
        if(plan == null) {
            return Response.status(404).build();
        }
        List<HttpStepDto> httpSteps = httpStepRepository.find("plan.id", planId).project(HttpStepDto.class).list();
        List<ScriptStepDto> scriptSteps = scriptStepRepository.find("plan.id", planId).project(ScriptStepDto.class).list();
        List<InputArgumentDto> inArgs = inputArgumentRepository.find("step.plan.id", planId).project(InputArgumentDto.class).list();
        List<OutputArgumentDto> outArgs = outputArgumentRepository.find("step.plan.id", planId).project(OutputArgumentDto.class).list();
        List<StepParameterRelationDto> relations = relationRepository.find("toArg.step.plan.id = ?1 and fromArg.step.plan.id=?1", planId).project(StepParameterRelationDto.class).list();

        exportDto.setHttpSteps(httpSteps);
        exportDto.setScriptSteps(scriptSteps);
        exportDto.setInputs(inArgs);
        exportDto.setOutputs(outArgs);
        exportDto.setRelations(relations);
        exportDto.setPlanDto(PlanDto.from(plan));

        return Response.ok().build();
    }
}
