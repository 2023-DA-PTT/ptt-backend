package com.ptt.boundary;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ptt.control.DataPointRepository;
import com.ptt.entity.DataPoint;

@Path("datapoint")
public class DataPointResource {
    @Inject
    DataPointRepository dataPointRepository;

    @GET
    public List<DataPoint> getAllDataPoints() {
        return dataPointRepository.listAll();
    }
}
