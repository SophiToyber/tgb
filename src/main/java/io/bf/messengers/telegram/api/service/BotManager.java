package io.bf.messengers.telegram.api.service;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;

import io.bf.messengers.telegram.engine.Bot;
import io.bf.messengers.telegram.engine.entity.TelegramCredentials;
import io.bf.messengers.telegram.engine.service.BotOrchestration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The class allows us to create Telegram bots in runtime and it keeps them in a
 * map so that our microservice is free to communicate with each (bot) of them.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BotManager {

    private final GenericApplicationContext context;
    private final TelegramBotsApi botsApi;
    private final BotOrchestration botOrchestration;

    public void registerBot(TelegramCredentials telegramCredentials) {
        String botUsername = telegramCredentials.getUsername();
        try {
            log.info("adding telegram bot with username : {}", botUsername);
            
            if (!context.getBeanFactory().containsBean(botUsername)) {
                Bot bot = new Bot(botOrchestration, telegramCredentials.getBotId(),
                        telegramCredentials.getToken(), telegramCredentials.getUsername());
                context.registerBean(botUsername, Bot.class, () -> bot);
                // We register new bots in Telegram Context using this
                BotSession botSession = botsApi.registerBot(bot);
                bot.setBotSession(botSession);
                log.info("added telegram bot with username : {}", botUsername);
            } else {
                log.info("bot with username {} already exists", botUsername);
            }
        } catch (TelegramApiRequestException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void removeBot(String botUsername) {
        log.info("Going to remove telegram bot with username : {}", botUsername);
        Bot bot = context.getBeanFactory().getBean(botUsername, Bot.class);

        try {
            bot.getBotSession().stop();
            bot.clearWebhook();
            bot.closeDB();
        } catch (BeansException | TelegramApiRequestException | IOException e) {
            log.error(e.getMessage());
        }

        context.getBeanFactory().destroyBean(botUsername, Bot.class);
        context.removeBeanDefinition(botUsername);
        log.info("Removed telegram bot with username : {}", botUsername);
    }
}
