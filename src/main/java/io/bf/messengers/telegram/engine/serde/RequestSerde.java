package io.bf.messengers.telegram.engine.serde;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import io.bf.apis.protobuf.BfProtos;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class RequestSerde implements Serde<BfProtos.Request>, Deserializer<BfProtos.Request>, Serializer<BfProtos.Request> {

    @Override
    public BfProtos.Request deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        try {
            return BfProtos.Request.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            log.warn("Error while deserializing a Request, returning default instance");
            return BfProtos.Request.getDefaultInstance();
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<BfProtos.Request> serializer() {
        return this;
    }

    @Override
    public Deserializer<BfProtos.Request> deserializer() {
        return this;
    }

    @Override
    public byte[] serialize(String topic, BfProtos.Request data) {
        return Optional.ofNullable(data)
                .map(BfProtos.Request::toByteArray)
                .orElse(null);
    }
}
