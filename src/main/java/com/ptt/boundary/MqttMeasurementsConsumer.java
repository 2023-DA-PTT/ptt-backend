package com.ptt.boundary;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptt.control.DataPointRepository;
import com.ptt.control.PlanRunRepository;
import com.ptt.control.StepRepository;
import com.ptt.entity.DataPoint;
import com.ptt.entity.dto.DataPointClientDto;

import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
public class MqttMeasurementsConsumer {

    @Inject
    DataPointRepository dataPointRepository;

    @Inject
    PlanRunRepository planRunRepository;
    
    @Inject
    StepRepository stepRepository;

    @Incoming("measurements")
    @Blocking
    @Transactional
    public CompletionStage<Void> consume(Message<byte[]> measurement) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DataPointClientDto dataPointDto = objectMapper.readValue(new String(measurement.getPayload()) , DataPointClientDto.class);

            DataPoint dataPoint = new DataPoint();
            dataPoint.setPlanRun(planRunRepository.findById(dataPointDto.getPlanRunId()));
            dataPoint.setStep(stepRepository.findById(dataPointDto.getStepId()));
            dataPoint.setStartTime(dataPointDto.getStartTime());
            dataPoint.setDuration(dataPointDto.getDuration());

            dataPointRepository.persist(dataPoint);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return measurement.ack();
    }
}
