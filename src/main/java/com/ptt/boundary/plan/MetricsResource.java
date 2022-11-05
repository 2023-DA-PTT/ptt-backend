package com.ptt.boundary.plan;


import com.ptt.control.plan.PlanRepository;
import com.ptt.control.plan.PlanRunRepository;
import com.ptt.control.step.HttpStepRepository;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("metrics")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class MetricsResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    PlanRunRepository planRunRepository;

    @Inject
    PlanRepository planRepository;

    @Inject
    EntityManager em;

    @GET
    @Path("test-runs")
    public long getTestRunsOfUser() {
        return planRunRepository.count("plan.ownerId", jwt.getSubject());
    }

    @GET
    @Path("avg-test-runs")
    public long getAverageRunsPerTestPlan() {
        long plans = planRepository.count("ownerId", jwt.getSubject());

        return plans == 0 ? 0 : planRunRepository.count("plan.ownerId", jwt.getSubject()) / plans;
    }

    @GET
    @Path("test-plans")
    public long getAmountTestPlans() {
        return planRepository.count("ownerId", jwt.getSubject());
    }

    @GET
    @Path("virtual-users-sum")
    public long getAmountVirtualUsers() {
        return planRepository.find("SELECT coalesce(SUM(p.numberOfClients), 0) FROM PlanRunInstruction p " +
                "where p.planRun.plan.ownerId=?1", jwt.getSubject()).project(Long.class).firstResult();
    }

    @GET
    @Path("/test-run/{testRunId}/virtual-users")
    public long getAmountVirtualUsersForTestRun(@PathParam("testRunId") long testRunId) {
        return planRepository.find("SELECT p.numberOfClients FROM PlanRunInstruction p " +
                        "where p.planRun.plan.ownerId=?1 and p.planRun.id=?2", jwt.getSubject(), testRunId)
                .project(Integer.class).firstResult();
    }

    @GET
    @Path("/test-run/{testRunId}/duration")
    public long getPlanRunDuration(@PathParam("testRunId") long planRunId) {
        return em.createQuery("SELECT coalesce(MAX(dp.startTime+(dp.duration/1000000))-MIN(dp.startTime+(dp.duration/1000000)),0) FROM DataPoint dp " +
                        "where dp.planRun.plan.ownerId=?1 and dp.planRun.id=?2", Long.class)
                .setParameter(1, jwt.getSubject())
                .setParameter(2, planRunId).getSingleResult();
    }

    @GET
    @Path("plan-last-test-run-id")
    public Response getLastTestRunId() {
        return planRunRepository.find("plan.ownerId=?1 order by startTime DESC", jwt.getSubject())
                .firstResultOptional()
                .map(planRun -> Response.ok(planRun.id).build())
                .orElse(Response.noContent().build());
    }

    @GET
    @Path("/test-run/{testRunId}/first-datapoint")
    public Long getFirstDataPointOfPlanRun(@PathParam("testRunId") long planRunId) {
        return planRunRepository.getFirstDatapointStartTime(jwt.getSubject(), planRunId);
    }
}
