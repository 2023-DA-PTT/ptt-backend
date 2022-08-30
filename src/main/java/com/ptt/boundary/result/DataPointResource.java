package com.ptt.boundary.result;

import java.awt.print.Book;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.ptt.control.result.DataPointRepository;
import com.ptt.entity.dto.DataPointDto;
import com.ptt.entity.dto.result.AggregationType;
import com.ptt.entity.result.DataPoint;
import io.fabric8.kubernetes.api.model.Preconditions;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;

@Path("datapoint")
public class DataPointResource {
    @Inject
    DataPointRepository dataPointRepository;

    @GET
    public List<DataPointDto> getAllDataPoints() {
        return dataPointRepository.findAll().project(DataPointDto.class).list();
    }

    @GET
    @Path("planrun/{planRunId}")
    public List<DataPointDto> getDataPointsForPlanRun(@PathParam("planRunId") long planRunId) {
        return dataPointRepository.find("planRun.id", planRunId).project(DataPointDto.class).list();
    }

    @GET
    @Path("planrun/{planRunId}/step/{stepId}")
    public Response getDataPointsForStep(@PathParam("planRunId") long planRunId,
                                         @PathParam("stepId") long stepId,
                                         @QueryParam("from") Long from,
                                         @QueryParam("to") Long to,
                                         @QueryParam("interval") Long interval,
                                         @QueryParam("aggr") String aggr) {
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

        return Response.ok(dataPointRepository.findForStep(
                        planRunId,
                        stepId,
                        from,
                        to,
                        interval,
                        aggregationType
                )
        ).build();
    }
}
