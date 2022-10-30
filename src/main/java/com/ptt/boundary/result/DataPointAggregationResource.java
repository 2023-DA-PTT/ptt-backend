package com.ptt.boundary.result;

import com.ptt.boundary.plan.PlanRunResource;
import com.ptt.control.plan.PlanRunRepository;
import com.ptt.control.result.DataPointRepository;
import com.ptt.control.step.StepRepository;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("datapoint-aggr")
@Authenticated
public class DataPointAggregationResource {
    @Inject
    StepRepository stepRepository;

    @Inject
    PlanRunRepository planRunRepository;

    @Inject
    DataPointRepository dataPointRepository;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("planrun/{planRunId}/step/{stepId}/compare")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvgDifferenceBetweenPlanRuns(@PathParam("planRunId") long planRunId,
                                                    @PathParam("stepId") long stepId,
                                                    @QueryParam("compareTo") long compareTo) {
        if(stepRepository.find("id=?1 and plan.ownerId=?2", stepId, jwt.getSubject()) == null ||
            planRunRepository.find("id=?1 and plan.ownerId=?2", planRunId, jwt.getSubject()) == null ||
                planRunRepository.find("id=?1 and plan.ownerId=?2", compareTo, jwt.getSubject()) == null) {
            return Response.status(404).build();
        }

        return Response.ok(dataPointRepository.getAvgDifferenceBetweenPlanRuns(planRunId, stepId, compareTo)).build();
    }

    @GET
    @Path("planrun/{planRunId}/step/{stepId}/avg")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvgDurationForPlanRun(@PathParam("planRunId") long planRunId,
                                                    @PathParam("stepId") long stepId) {
        if(stepRepository.find("id=?1 and plan.ownerId=?2", stepId, jwt.getSubject()) == null ||
                planRunRepository.find("id=?1 and plan.ownerId=?2", planRunId, jwt.getSubject()) == null) {
            return Response.status(404).build();
        }

        return Response.ok(dataPointRepository.getAvgPlanRunDuration(planRunId, stepId)).build();
    }
}
