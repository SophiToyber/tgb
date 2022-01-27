package io.bf.messengers.telegram.engine.service;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import io.bf.apis.protobuf.BfProtos;
import io.bf.messengers.telegram.engine.Bot;
import io.bf.messengers.telegram.engine.entity.Message;
import io.bf.messengers.telegram.engine.transformer.MessageTransformer;
import io.bf.messengers.telegram.engine.transformer.ResponseTransformer;
import io.bf.messengers.telegram.logger.LoggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableKafka
public class ResponseConsumer {
    private final DefaultListableBeanFactory beanFactory;

    @KafkaListener(topics = "#{'${telegram.response-consumer.topic}'}", containerFactory = "bfKafkaListenerContainerFactory")
    public void consume(BfProtos.Response response) {
        LoggerService.populateTrace(response.getTrace());

        log.info("consuming a response = {}", response);

        Message message = ResponseTransformer.transform(response);
        Bot bot = (Bot) beanFactory.getBean(response.getBotUsername());

        try {
            switch (response.getMessageType()) {
                case MESSAGE:
                    log.info("send MESSAGE");
                    bot.execute(MessageTransformer.TO_MESSAGE.apply(message));
                    break;
                default:
                case CALLBACK:
                    log.info("send CALLBACK");
                    bot.execute(MessageTransformer.TO_CALLBACK.apply(message));
                    break;
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
        LoggerService.clear();
    }

}
