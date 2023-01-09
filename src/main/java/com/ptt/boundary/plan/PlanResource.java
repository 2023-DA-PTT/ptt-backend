package com.ptt.boundary.plan;

import com.ptt.control.argument.InputArgumentRepository;
import com.ptt.control.argument.OutputArgumentRepository;
import com.ptt.control.plan.PlanRepository;
import com.ptt.control.step.*;
import com.ptt.entity.argument.InputArgument;
import com.ptt.entity.argument.OutputArgument;
import com.ptt.entity.dto.*;
import com.ptt.entity.plan.Plan;
import com.ptt.entity.step.*;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("plan")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlanResource {
    @ApplicationPath("/api")
    public static class ApplicationConfiguration extends Application {

    }

    @Inject
    JsonWebToken jwt;

    @Inject
    PlanRepository planRepository;
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
    @Inject
    NextStepRepository nextStepRepository;
    @Inject
    HttpStepHeaderRepository httpStepHeaderRepository;

    @ConfigProperty(name = "client.token")
    String clientToken; // Token used for communication with client

    @GET
    @Authenticated
    public List<PlanDto> getAllPlansForUser() {
        return planRepository.find("ownerId", jwt.getSubject()).project(PlanDto.class).list();
    }

    @GET
    @Path("{id}")
    @Authenticated
    public PlanDto getPlanById(@PathParam("id") long planId) {
        return planRepository.find("id=?1 and ownerId=?2", planId, jwt.getSubject()).project(PlanDto.class).singleResult();
    }

    @POST
    @Transactional
    @Authenticated
    public Response createPlanForUser(PlanDto planDto) {
        Plan plan = new Plan();
        plan.name = planDto.getName();
        plan.description = planDto.getDescription();
        plan.ownerId = jwt.getSubject();
        planRepository.persist(plan);
        return Response.ok(PlanDto.from(plan)).status(201).build();
    }

    @POST
    @Transactional
    @Authenticated
    @Path("start/{id}")
    public Response setPlanStartId(@PathParam("id") long planId, long startId) {
        Plan plan = planRepository.find("id", planId).firstResult();
        Step startStep = httpStepRepository.find("id", startId).firstResult();
        if(plan == null) {
            return Response.status(404).build();
        }
        if(!plan.ownerId.equals(jwt.getSubject())){
            if (jwt.getSubject() == null) {
                return Response.status(401).build();
            }
            return Response.status(403).build();
        }

        plan.start = startStep;
        planRepository.persist(plan);
        return Response.ok(PlanDto.from(plan)).status(201).build();
    }

    @GET
    @Path("export/{id}")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response export(@PathParam("id") long planId, @QueryParam("token") String token) {
        PlanExportDto exportDto = new PlanExportDto();
        Plan plan = planRepository.find("id", planId).firstResult();
        if(plan == null) {
            return Response.status(404).build();
        }
        if(!clientToken.equals(token) && !plan.ownerId.equals(jwt.getSubject())){
            if (jwt.getSubject() == null) {
                return Response.status(401).build();
            }
            return Response.status(403).build();
        }
        List<HttpStepDto> httpSteps = httpStepRepository.find("plan.id", planId).list().stream().map(HttpStepDto::from).collect(Collectors.toList());
        List<ScriptStepDto> scriptSteps = scriptStepRepository.find("plan.id", planId).project(ScriptStepDto.class).list();
        List<InputArgumentDto> inArgs = inputArgumentRepository.find("step.plan.id", planId).project(InputArgumentDto.class).list();
        List<OutputArgumentDto> outArgs = outputArgumentRepository.find("step.plan.id", planId).project(OutputArgumentDto.class).list();
        List<StepParameterRelationDto> relations = relationRepository.find("toArg.step.plan.id = ?1 and fromArg.step.plan.id=?1", planId).project(StepParameterRelationDto.class).list();
        List<SimpleNextStepDto> nexts = nextStepRepository.find("fromStep.plan.id = ?1 and toStep.plan.id = ?1", planId).project(SimpleNextStepDto.class).list();

        exportDto.setHttpSteps(httpSteps);
        exportDto.setScriptSteps(scriptSteps);
        exportDto.setInputs(inArgs);
        exportDto.setOutputs(outArgs);
        exportDto.setRelations(relations);
        exportDto.setNextSteps(nexts);
        exportDto.setPlan(PlanDto.from(plan));

        return Response.ok(exportDto).build();
    }

    @POST
    @Path("import")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Authenticated
    public Response importPlan(PlanExportDto importDto) {
        Plan plan = new Plan();
        plan.name = importDto.getPlan().getName();
        plan.description = importDto.getPlan().getDescription();
        plan.ownerId = jwt.getSubject();
        planRepository.persist(plan);

        Map<Long, Step> stepLookUp = new HashMap<>();
        Map<Long, OutputArgument> outArgLookUp = new HashMap<>();
        Map<Long, InputArgument> inArgLookUp = new HashMap<>();

        for(HttpStepDto httpStepDto : importDto.getHttpSteps()) {
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
            for(HttpStepHeaderDto headerDto : httpStepDto.getHeaders()) {
                HttpStepHeader httpStepHeader = new HttpStepHeader();
                httpStepHeader.name = headerDto.getName();
                httpStepHeader.value = headerDto.getValue();
                httpStepHeader.step = httpStep;
                httpStepHeaderRepository.persist(httpStepHeader);
            }
            if(httpStepDto.getId() == importDto.getPlan().getStartId()) {
                plan.start = httpStep;
                planRepository.persist(plan);
            }
            stepLookUp.put(httpStepDto.getId(), httpStep);
        }

        for(ScriptStepDto scriptStepDto : importDto.getScriptSteps()) {
            ScriptStep scriptStep = new ScriptStep();
            scriptStep.name = scriptStepDto.getName();
            scriptStep.description = scriptStepDto.getDescription();
            scriptStep.plan = plan;
            scriptStep.script = scriptStepDto.getScript();
            scriptStepRepository.persist(scriptStep);
            if(scriptStepDto.getId() == importDto.getPlan().getStartId()) {
                plan.start = scriptStep;
                planRepository.persist(plan);
            }
            stepLookUp.put(scriptStepDto.getId(), scriptStep);
        }

        for(InputArgumentDto inArgDto: importDto.getInputs()) {
            InputArgument inputArgument = new InputArgument();
            inputArgument.name = inArgDto.getName();
            inputArgument.step = stepLookUp.get(inArgDto.getStepId());
            inputArgumentRepository.persist(inputArgument);
            inArgLookUp.put(inArgDto.getId(), inputArgument);
        }

        for(OutputArgumentDto outArgDto: importDto.getOutputs()) {
            OutputArgument outputArgument = new OutputArgument();
            outputArgument.name = outArgDto.getName();
            outputArgument.parameterLocation = outArgDto.getParameterLocation();
            outputArgument.outputType = outArgDto.getOutputType();
            outputArgument.step = stepLookUp.get(outArgDto.getStepId());
            outputArgumentRepository.persist(outputArgument);
            outArgLookUp.put(outArgDto.getId(), outputArgument);
        }

        for(StepParameterRelationDto relationDto : importDto.getRelations()) {
            StepParameterRelation relation = new StepParameterRelation();
            relation.fromArg = outArgLookUp.get(relationDto.getFromId());
            relation.toArg = inArgLookUp.get(relationDto.getToId());
            relationRepository.persist(relation);
        }

        for(SimpleNextStepDto nextDto : importDto.getNextSteps()) {
            NextStep nextStep = new NextStep();
            nextStep.fromStep = stepLookUp.get(nextDto.getFromStepId());
            nextStep.toStep = stepLookUp.get(nextDto.getToStepId());
            nextStep.repeatAmount = nextDto.getRepeatAmount();
            nextStepRepository.persist(nextStep);
        }

        return Response.ok().build();
    }
}
