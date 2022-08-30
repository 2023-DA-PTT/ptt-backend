package com.ptt.boundary.result;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ptt.control.result.DataPointRepository;
import com.ptt.entity.dto.DataPointDto;

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
    public List<DataPointDto> getDataPointsForStep(@PathParam("planRunId") long planRunId,@PathParam("stepId") long stepId) {
        return dataPointRepository.find("planRun.id=?1 and step.id=?2 order by startTime", planRunId, stepId).project(DataPointDto.class).list();
    }
}
