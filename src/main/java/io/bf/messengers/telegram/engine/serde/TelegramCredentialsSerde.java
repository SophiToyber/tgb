package io.bf.messengers.telegram.engine.serde;

import java.util.Map;
import java.util.Optional;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.google.protobuf.InvalidProtocolBufferException;

import io.bf.apis.protobuf.MessangerProtos;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelegramCredentialsSerde
        implements Serde<MessangerProtos.TelegramCredentials>, Serializer<MessangerProtos.TelegramCredentials>, Deserializer<MessangerProtos.TelegramCredentials> {

    @Override
    public byte[] serialize(String topic, MessangerProtos.TelegramCredentials data) {
        return Optional.ofNullable(data).map(MessangerProtos.TelegramCredentials::toByteArray).orElse(null);
    }

    @Override
    public MessangerProtos.TelegramCredentials deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        } 

        try {
            return MessangerProtos.TelegramCredentials.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            log.warn("Error while deserializing a Response, returning default instance");
            return MessangerProtos.TelegramCredentials.getDefaultInstance();
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public void close() {
    }

    @Override
    public Serializer<MessangerProtos.TelegramCredentials> serializer() {
        return this;
    }

    @Override
    public Deserializer<MessangerProtos.TelegramCredentials> deserializer() {
        return this;
    }
}
