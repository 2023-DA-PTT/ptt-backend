package com.ptt.boundary;

import com.ptt.control.PlanRepository;
import com.ptt.control.UserRepository;
import com.ptt.entity.Plan;
import com.ptt.entity.User;
import com.ptt.entity.dto.PlanDto;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

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
    public PlanDto createPlanForUser(@PathParam("userId") long userId, PlanDto planDto) {
        User user = userRepository.findById(userId);
        Plan plan = new Plan();
        plan.name = planDto.name;
        plan.description = planDto.description;
        plan.user = user;
        planRepository.persist(plan);
        return planDto;
    }
}
