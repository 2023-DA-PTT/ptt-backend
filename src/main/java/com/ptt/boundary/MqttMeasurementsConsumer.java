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
import com.ptt.entity.DataPoint;

import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
public class MqttMeasurementsConsumer {

    @Inject
    DataPointRepository dataPointRepository;

    @Incoming("measurements")
    @Blocking
    @Transactional
    public CompletionStage<Void> consume(Message<byte[]> measurement) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DataPoint dp = objectMapper.readValue(new String(measurement.getPayload()) , DataPoint.class);
            dataPointRepository.persist(dp);
            System.out.println(dp);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return measurement.ack();
    }
}
