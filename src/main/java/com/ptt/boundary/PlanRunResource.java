package com.ptt.boundary;

import java.time.Instant;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.ptt.control.PlanRepository;
import com.ptt.control.PlanRunInstructionRepository;
import com.ptt.control.PlanRunRepository;
import com.ptt.control.PttClientManager;
import com.ptt.entity.Plan;
import com.ptt.entity.PlanRun;
import com.ptt.entity.PlanRunInstruction;
import com.ptt.entity.dto.PlanRunDto;
import com.ptt.entity.dto.PlanRunInstructionDto;

@Path("planrun")
public class PlanRunResource {
    @Inject
    PlanRunRepository planRunRepository;
    
    @Inject
    PlanRepository planRepository;

    @Inject
    PlanRunInstructionRepository planRunInstructionRepository;
 
    @Inject
    PttClientManager clientManager;
 
    @GET
    public List<PlanRunDto> getAllPlanRuns() {
        return planRunRepository.findAll().project(PlanRunDto.class).list();
    }

    @GET
    @Path("{planrunid}")
    public PlanRunDto getPlanRunById(@PathParam("planrunid") long id) {
        PlanRunDto planRunDto = planRunRepository.find("id", id).project(PlanRunDto.class).singleResult();
        planRunDto.setPlanRunInstructions(planRunInstructionRepository
            .find("planRun.id", planRunDto.getId())
            .project(PlanRunInstructionDto.class).list());
        return planRunDto;
    }

    @GET
    @Path("/plan/{planId}")
    public List<PlanRunDto> getPlanRunsForPlan(@PathParam("planId") long planId) {
        return planRunRepository.find("plan.id", planId).project(PlanRunDto.class).list();
    }

    @POST
    @Transactional
    public Response createPlanRun(PlanRunDto planRunDto) {
        Plan plan = planRepository.findById(planRunDto.getPlanId());
        if(plan == null) {
            return Response.status(400).build();
        }
        long currentTime = Instant.now().getEpochSecond();
        
        PlanRun planRun = new PlanRun();
        planRun.plan = plan;
        planRun.startTime = planRunDto.getStartTime() <= currentTime ? currentTime : planRunDto.getStartTime();
        planRun.duration = planRunDto.getDuration();
        planRunRepository.persist(planRun);
        for(PlanRunInstructionDto dto : planRunDto.getPlanRunInstructions()) {
            PlanRunInstruction instruction = new PlanRunInstruction();
            instruction.setPlanRun(planRun);
            instruction.setNumberOfClients(dto.getNumberOfClients());
            instruction.setNodeName(dto.getNodeName());
            planRunInstructionRepository.persist(instruction);
            planRun.planRunInstructions.add(instruction);
        }

        if(planRun.startTime <= currentTime) {
            clientManager.startClient(planRun.id, planRunDto.getPlanRunInstructions());
        } else {
            return Response.status(501, "The scheduling of clients is not yet implemented!").build();
        }
        return Response.accepted(PlanRunDto.from(planRun)).build();
    }
}