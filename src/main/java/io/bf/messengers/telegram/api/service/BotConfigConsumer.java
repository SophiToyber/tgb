package io.bf.messengers.telegram.api.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import io.bf.apis.protobuf.MessangerProtos;
import io.bf.messengers.telegram.engine.transformer.TelegramCredentialsTransformer;
import io.bf.messengers.telegram.engine.utils.CryptoAES;
import io.bf.messengers.telegram.logger.LoggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotConfigConsumer {
    
    private final TelegramCredentialsTransformer credentialsTransformer;
    private final BotManager botManager;
    private final CryptoAES cryptoAes;

    @KafkaListener(topics = "#{'${telegram.bot-config.topic}'}", containerFactory = "botConfigKafkaListenerContainerFactory")
    public void manageBot(@Payload(required = false) MessangerProtos.TelegramCredentials telegramCredentials,
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key) {
        log.info("Received message with key: {}", key);
        if (telegramCredentials != null) {
            LoggerService.populateTrace();
            botManager.registerBot(credentialsTransformer.transform(telegramCredentials));
            LoggerService.clear();
        } else {
            botManager.removeBot(cryptoAes.decrypt(key));
        }
    }
}
