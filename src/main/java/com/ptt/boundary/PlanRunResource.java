package com.ptt.boundary;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.ptt.control.PlanRepository;
import com.ptt.control.PlanRunRepository;
import com.ptt.control.PttClientManager;
import com.ptt.entity.Plan;
import com.ptt.entity.PlanRun;
import com.ptt.entity.dto.PlanRunDto;
import com.ptt.entity.dto.PlanRunInstructionDto;

@Path("planrun")
public class PlanRunResource {
    @Inject
    PlanRunRepository planRunRepository;
    
    @Inject
    PlanRepository planRepository;
 
    @Inject
    PttClientManager clientManager;
 
    @POST
    @Path("{planrunid}/run")
    public Response runTestPlan(@PathParam("planrunid") long planRunId, List<PlanRunInstructionDto> planRunInstructionDtos) {
        PlanRun planRun = planRunRepository.findById(planRunId);
        if(planRun == null) {
            return Response.status(404, "Plan run doesn't exist!").build();
        }
        clientManager.startClient(planRunId, planRunInstructionDtos);
        return Response.status(202, "Clients have been started!").build();
    }

    @GET
    public List<PlanRunDto> getAllDataPoints() {
        return planRunRepository.findAll().project(PlanRunDto.class).list();
    }

    @GET
    @Path("{planrunid}")
    public PlanRunDto getAllDataPointById(@PathParam("planrunid") long id) {
        return planRunRepository.find("id", id).project(PlanRunDto.class).singleResult();
    }

    @POST
    @Transactional
    public Response createPlanRun(PlanRunDto planRunDto) {
        Plan plan = planRepository.findById(planRunDto.getPlanId());
        if(plan == null) {
            return Response.status(400).build();
        }
        PlanRun planRun = new PlanRun();
        planRun.plan = plan;
        planRun.startTime = planRunDto.getStartTime();
        planRun.duration = planRunDto.getDuration();
        planRunRepository.persist(planRun);
        return Response.ok(PlanRunDto.from(planRun)).build();
    }
}