package com.ptt.boundary;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
}
