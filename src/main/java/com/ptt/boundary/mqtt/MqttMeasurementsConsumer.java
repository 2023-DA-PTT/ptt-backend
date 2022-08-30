package com.ptt.boundary.mqtt;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptt.control.result.DataPointRepository;
import com.ptt.control.plan.PlanRunRepository;
import com.ptt.control.step.StepRepository;
import com.ptt.entity.result.DataPoint;
import com.ptt.entity.plan.PlanRun;
import com.ptt.entity.step.Step;
import com.ptt.entity.dto.DataPointClientDto;

import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
public class MqttMeasurementsConsumer {

    private static final Logger LOG = Logger.getLogger(MqttMeasurementsConsumer.class);

    @Inject
    DataPointRepository dataPointRepository;

    @Inject
    PlanRunRepository planRunRepository;
    
    @Inject
    StepRepository stepRepository;

    @Incoming("measurements")
    @Blocking(ordered = false)
    @Transactional
    public CompletionStage<Void> consume(Message<byte[]> measurement) {
        ObjectMapper objectMapper = new ObjectMapper();
        DataPointClientDto dataPointDto = null;
        try {
            dataPointDto = objectMapper.readValue(new String(measurement.getPayload()), DataPointClientDto.class);
            persistDatapointFromDto(dataPointDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return measurement.ack();
    }

    private void persistDatapointFromDto(DataPointClientDto dataPointDto) {
        PlanRun planRun = planRunRepository.findById(dataPointDto.getPlanRunId());
        if(planRun == null) {
            LOG.error("PlanRun for datapoint wasn't found: " + dataPointDto.toString());
            return;
        }
        Step step = stepRepository.findById(dataPointDto.getStepId());
        if(step == null) {
            LOG.error("Step for datapoint wasn't found: " + dataPointDto.toString());
            return;
        }
        DataPoint dataPoint = new DataPoint();
        dataPoint.setPlanRun(planRun);
        dataPoint.setStep(step);
        dataPoint.setStartTime(dataPointDto.getStartTime());
        dataPoint.setDuration(dataPointDto.getDuration());
        dataPointRepository.persist(dataPoint);
    }
}
