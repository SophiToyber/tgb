package io.bf.messengers.telegram.engine.serde;

import com.google.protobuf.InvalidProtocolBufferException;
import io.bf.apis.protobuf.BfProtos;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class ResponseSerde implements Serde<BfProtos.Response>, Serializer<BfProtos.Response>,
        Deserializer<BfProtos.Response> {

    @Override
    public BfProtos.Response deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        try {
            return BfProtos.Response.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            log.warn("Error while deserializing a Response, returning default instance");
            return BfProtos.Response.getDefaultInstance();
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<BfProtos.Response> serializer() {
        return this;
    }

    @Override
    public Deserializer<BfProtos.Response> deserializer() {
        return this;
    }

    @Override
    public byte[] serialize(String topic, BfProtos.Response data) {
        return Optional.ofNullable(data)
                .map(BfProtos.Response::toByteArray)
                .orElse(null);
    }
}
