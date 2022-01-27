package io.bf.messengers.telegram.engine.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.bf.apis.protobuf.BfProtos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestProducer {

    private final KafkaTemplate<String, BfProtos.Request> messageProducer;

    @Value("${telegram.request-producer.topic}")
    private String topic;

    public void send(BfProtos.Request request) {
        messageProducer.send(topic, request);

        log.info("sent message to topic = {}", topic);
    }

}
