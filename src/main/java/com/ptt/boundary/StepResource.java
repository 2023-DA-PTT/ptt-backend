package com.ptt.boundary;

import com.ptt.control.InputArgumentRepository;
import com.ptt.control.NextStepRepository;
import com.ptt.control.OutputArgumentRepository;
import com.ptt.control.PlanRepository;
import com.ptt.control.StepParameterRelationRepository;
import com.ptt.control.StepRepository;
import com.ptt.entity.InputArgument;
import com.ptt.entity.NextStep;
import com.ptt.entity.OutputArgument;
import com.ptt.entity.Step;
import com.ptt.entity.StepParameterRelation;
import com.ptt.entity.dto.AdvancedStepParameterRelationDto;
import com.ptt.entity.dto.NextStepWithParameterRelationDto;
import com.ptt.entity.dto.StepDto;

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
    InputArgumentRepository inputArgumentRepository;

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
    public List<NextStepWithParameterRelationDto> getAllNextsByIdForPlan(@PathParam("planId") long planId, @PathParam("stepId") long stepId) {
        return nextStepRepository.getAdvancedNextSteps(planId, stepId);
    }

    @POST
    @Path("{stepId}/nexts")
    @Transactional
    public Response updateAllNextsByIdForPlan(@PathParam("planId") long planId, @PathParam("stepId") long stepId, List<NextStepWithParameterRelationDto> nexts) throws IllegalStateException, SecurityException, SystemException {
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
        Step step = stepRepository.findById(stepId);
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
