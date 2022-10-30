package com.ptt.boundary.result;

import com.ptt.control.plan.PlanRunRepository;
import com.ptt.control.result.DataPointRepository;
import com.ptt.control.step.StepRepository;
import com.ptt.entity.dto.DataPointDto;
import com.ptt.entity.dto.result.AggregationType;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("datapoint")
@Authenticated
public class DataPointResource {
    @Inject
    DataPointRepository dataPointRepository;

    @Inject
    PlanRunRepository planRunRepository;

    @Inject
    StepRepository stepRepository;

    @Inject
    JsonWebToken jwt;

    @GET
    public List<DataPointDto> getAllDataPoints() {
        return dataPointRepository.find("step.plan.ownerId", jwt.getSubject()).project(DataPointDto.class).list();
    }

    @GET
    @Path("planrun/{planRunId}")
    public List<DataPointDto> getDataPointsForPlanRun(@PathParam("planRunId") long planRunId) {
        return dataPointRepository
                .find("planRun.id=?1 and step.plan.ownerId=?2", planRunId, jwt.getSubject())
                .project(DataPointDto.class)
                .list();
    }

    @GET
    @Path("planrun/{planRunId}/step/{stepId}")
    public Response getDataPointsForStep(@PathParam("planRunId") long planRunId,
                                                             @PathParam("stepId") long stepId,
                                                             @QueryParam("from") Long from,
                                                             @QueryParam("to") Long to,
                                                             @QueryParam("interval") int interval,
                                                             @QueryParam("aggr") String aggr) {
        if (this.planRunRepository.count("id=?1 and plan.ownerId=?2", planRunId, jwt.getSubject()) <= 0 ||
                this.stepRepository.count("id=?1 and plan.ownerId=?2", stepId, jwt.getSubject()) <= 0) {
            return Response.noContent().build();
        }

        AggregationType aggregationType = null;

        if(aggr != null) {
            switch (aggr.toLowerCase()) {
                case "min":
                    aggregationType = AggregationType.MIN;
                    break;
                case "max":
                    aggregationType = AggregationType.MAX;
                    break;
                case "avg":
                    aggregationType = AggregationType.AVG;
                    break;
                default:
                    return Response.status(400, "Unknown aggregation type").build();
            }
        }
        else {
            aggregationType = AggregationType.MAX;
        }

        if(interval < 100) {
            return Response.status(400, "Interval cannot be lower than 100ms").build();
        }

        return Response.ok(dataPointRepository.findWithIntervalPlPgSql(planRunId,
                stepId,
                from,
                to,
                interval,
                aggregationType)
        ).build();
    }
}
