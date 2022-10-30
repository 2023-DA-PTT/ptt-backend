package com.ptt.boundary.step;

import com.ptt.control.argument.InputArgumentRepository;
import com.ptt.control.step.NextStepRepository;
import com.ptt.control.argument.OutputArgumentRepository;
import com.ptt.control.plan.PlanRepository;
import com.ptt.control.step.StepParameterRelationRepository;
import com.ptt.control.step.StepRepository;
import com.ptt.entity.argument.InputArgument;
import com.ptt.entity.step.NextStep;
import com.ptt.entity.argument.OutputArgument;
import com.ptt.entity.step.Step;
import com.ptt.entity.step.StepParameterRelation;
import com.ptt.entity.dto.AdvancedStepParameterRelationDto;
import com.ptt.entity.dto.NextStepWithParameterRelationDto;
import com.ptt.entity.dto.StepDto;
import com.ptt.entity.dto.StepWithNextsDto;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("plan/{planId}/step")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class StepResource {

    @Inject
    StepRepository stepRepository;

    @Inject
    PlanRepository planRepository;

    @Inject
    NextStepRepository nextStepRepository;

    @Inject
    StepParameterRelationRepository relationRepository;

    @Inject
    OutputArgumentRepository outputArgumentRepository;

    @Inject
    JsonWebToken jwt;

    @Inject
    InputArgumentRepository inputArgumentRepository;

    @GET
    public List<StepWithNextsDto> getAllStepsForPlan(@PathParam("planId") long planId) {
        return stepRepository.find("plan.id=?1 and plan.ownerId=?2", planId, jwt.getSubject())
            .list()
            .stream().map(s->StepWithNextsDto.from(s))
            .toList();
    }

    @GET
    @Path("{stepId}")
    public StepDto getAllStepByIdForPlan(@PathParam("planId") long planId, @PathParam("stepId") long stepId) {
        return stepRepository.find("plan.id = ?1 and id = ?2 and plan.ownerId=?3",
                planId, stepId, jwt.getSubject()).project(StepDto.class).singleResult();
    }

    @GET
    @Path("{stepId}/nexts")
    public List<NextStepWithParameterRelationDto> getAllNextsByIdForPlan(@PathParam("planId") long planId, @PathParam("stepId") long stepId) {
        return nextStepRepository.getAdvancedNextSteps(planId, stepId, jwt.getSubject());
    }

    @POST
    @Path("{stepId}/nexts")
    @Transactional
    public Response updateAllNextsByIdForPlan(@PathParam("planId") long planId, @PathParam("stepId") long stepId, List<NextStepWithParameterRelationDto> nexts) throws IllegalStateException, SecurityException, SystemException {
        Step step = stepRepository.findById(stepId);

        if(!step.plan.ownerId.equals(jwt.getSubject())) {
            return Response.notModified().build();
        }

        relationRepository
        .getEntityManager()
        .createQuery("""
            delete from StepParameterRelation spr
            where spr.id in
            (select sprsub.id from StepParameterRelation sprsub
                where sprsub.fromArg.step.id=?1)
        """).setParameter(1, stepId).executeUpdate();

        nextStepRepository
        .getEntityManager()
        .createQuery("""
            delete from NextStep ns
            where ns.id in
            (select nssub.id from NextStep nssub where
                nssub.fromStep.id=?1)
        """).setParameter(1, stepId).executeUpdate();

        //POSTGRES doesnt support cross join delete:
        //relationRepository.delete("fromArg.step.plan.id=?1", planId);
        //nextStepRepository.delete("delete from NextStep where fromStep.plan.id = ?1", planId);
        for (NextStepWithParameterRelationDto nextDto : nexts) {
            Step toStep = stepRepository.findById(nextDto.getToStep().getId());
            if(toStep == null) {
                throw new BadRequestException();
            }
            NextStep next = new NextStep();
            next.fromStep = step;
            next.toStep = toStep;
            next.repeatAmount = nextDto.getRepeatAmount();
            nextStepRepository.persist(next);
            for(AdvancedStepParameterRelationDto relationDto : nextDto.getStepParameterRelations()) {
                OutputArgument outArg = outputArgumentRepository.findById(relationDto.getOutputArgId());
                InputArgument inArg = inputArgumentRepository.findById(relationDto.getInputArg().getId());
                if(inArg ==null || outArg == null) {
                    throw new BadRequestException();
                }
                StepParameterRelation relation = new StepParameterRelation();
                relation.fromArg = outArg;
                relation.toArg = inArg;
                relationRepository.persist(relation);
            }
        }
        return Response.ok().build();
    }

    @DELETE
    @Transactional
    @Path("{stepId}")
    public Response deleteStepById(@PathParam("planId") long planId, @PathParam("stepId") long stepId) {
        Step step = stepRepository.find("plan.id = ?1 and id = ?2 and plan.ownerId=?3", planId, stepId, jwt.getSubject()).singleResult();
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
