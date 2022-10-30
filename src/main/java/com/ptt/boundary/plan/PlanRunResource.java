package com.ptt.boundary.plan;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.ptt.control.plan.PlanRepository;
import com.ptt.control.plan.PlanRunInstructionRepository;
import com.ptt.control.plan.PlanRunRepository;
import com.ptt.manager.PttClientManager;
import com.ptt.entity.plan.Plan;
import com.ptt.entity.plan.PlanRun;
import com.ptt.entity.plan.PlanRunInstruction;
import com.ptt.entity.dto.PlanRunDto;
import com.ptt.entity.dto.PlanRunInstructionDto;
import io.fabric8.kubernetes.api.model.Preconditions;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

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

    @Inject
    JsonWebToken jwt;

    @ConfigProperty(name = "client.token")
    String clientToken; // Token used for communication with client

    @GET
    @Authenticated
    public List<PlanRunDto> getAllPlanRuns() {
        return planRunRepository.findAll().project(PlanRunDto.class).list();
    }

    @GET
    @Path("{planrunid}")
    @PermitAll
    public Response getPlanRunById(@PathParam("planrunid") long id, @QueryParam("token") String clientToken) {
        PlanRunDto planRunDto;
        if(this.clientToken.equals(clientToken)) {
             planRunDto = planRunRepository.find("id", id)
                    .project(PlanRunDto.class).firstResult();
        }
        else if(jwt == null) {
            return Response.status(404).build();
        }
        else {
            planRunDto = planRunRepository.find("id=?1 and plan.ownerId=?2", id, jwt.getSubject())
                    .project(PlanRunDto.class).firstResult();
        }
        if(planRunDto == null) {
          return Response.status(404).build();
        }
        planRunDto.setPlanRunInstructions(planRunInstructionRepository
            .find("planRun.id", planRunDto.getId())
            .project(PlanRunInstructionDto.class).list());
        return Response.ok(planRunDto).build();
    }

    @GET
    @Path("/plan/{planId}")
    @Authenticated
    public List<PlanRunDto> getPlanRunsForPlan(@PathParam("planId") long planId) {
        return planRunRepository.find("plan.id=?1 and plan.ownerId=?2", planId, jwt.getSubject()).project(PlanRunDto.class).list();
    }

    @POST
    @Transactional
    @Authenticated
    public Response createPlanRun(PlanRunDto planRunDto) {
        Plan plan = planRepository.findById(planRunDto.getPlanId());
        if(plan == null) {
            return Response.status(400).build();
        }
        if(!plan.ownerId.equals(jwt.getSubject())){
            return Response.status(403).build();
        }
        long currentTime = Instant.now().getEpochSecond();

        PlanRun planRun = new PlanRun();
        planRun.plan = plan;
        planRun.runOnce = planRunDto.isRunOnce();
        planRun.startTime = Math.max(planRunDto.getStartTime(), currentTime);
        planRun.duration = planRunDto.getDuration();
        planRun.name = planRunDto.getName();
        planRunRepository.persist(planRun);

        Set<String> clusterNodeList = clientManager.getNodeNames();
        for(PlanRunInstructionDto dto : planRunDto.getPlanRunInstructions()) {
            if(!dto.getNodeName().equals("any") && !clusterNodeList.contains(dto.getNodeName())) {
                return Response.status(400).build();
            }
            PlanRunInstruction instruction = new PlanRunInstruction();
            instruction.setPlanRun(planRun);
            instruction.setNumberOfClients(dto.getNumberOfClients());
            instruction.setNodeName(dto.getNodeName());
            planRunInstructionRepository.persist(instruction);
            planRun.planRunInstructions.add(instruction);
        }

        if(planRun.startTime <= currentTime) {
            clientManager.startClient(planRun.id, planRunDto.getPlanRunInstructions(), clientToken);
        } else {
            return Response.status(501, "The scheduling of clients is not yet implemented!").build();
        }
        return Response.accepted(PlanRunDto.from(planRun)).build();
    }
}
