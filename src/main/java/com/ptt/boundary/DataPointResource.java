package com.ptt.boundary;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ptt.control.DataPointRepository;
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
}
