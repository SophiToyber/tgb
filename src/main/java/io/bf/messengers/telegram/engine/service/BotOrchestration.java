package io.bf.messengers.telegram.engine.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import io.bf.apis.protobuf.BfProtos;
import io.bf.messengers.telegram.engine.transformer.TelegramUserProfileTransformer;
import io.bf.messengers.telegram.logger.LoggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotOrchestration {
    private final RequestProducer requestProducer;

    public void adaptCallback(String botId, String botUsername, Update update) {
        String trace = LoggerService.populateTrace();

        BfProtos.Request request = BfProtos.Request.newBuilder()
                .setBotId(botId)
                .setBotUsername(botUsername)
                .setMessageId(Integer.toString(update.getCallbackQuery().getMessage().getMessageId()))
                .setSessionId(update.getCallbackQuery().getMessage().getChatId().toString())
                .setTrace(trace)
                .setMessageType(BfProtos.MessageType.CALLBACK)
                .setMessenger(BfProtos.Messenger.TELEGRAM)
                .setCallbackQueryData(update.getCallbackQuery().getData())
                .setProfile(TelegramUserProfileTransformer.fetchFromCallbackRequest(update))
                .build();

        log.info("sending request {} to bot-father", request);

        requestProducer.send(request);

        LoggerService.clear();
    }

    public void adaptMessage(String botId, String botUsername, Update update) {
        String trace = LoggerService.populateTrace();

        BfProtos.Request request = BfProtos.Request.newBuilder()
                .setBotId(botId)
                .setBotUsername(botUsername)
                .setSessionId(update.getMessage().getChatId().toString())
                .setMessageId(Integer.toString(update.getMessage().getMessageId()))
                .setTrace(trace)
                .setMessageType(BfProtos.MessageType.MESSAGE)
                .setMessenger(BfProtos.Messenger.TELEGRAM)
                .setInputMessageText(update.getMessage().getText())
                .setProfile(TelegramUserProfileTransformer.fetchFromMessageRequest(update))
                .build();

        log.info("sending request {} to bot-father.", request);

        requestProducer.send(request);

        LoggerService.clear();
    }
}
