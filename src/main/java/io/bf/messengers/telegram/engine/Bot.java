package io.bf.messengers.telegram.engine;

import static org.telegram.abilitybots.api.objects.Flag.CALLBACK_QUERY;
import static org.telegram.abilitybots.api.objects.Flag.MESSAGE;

import java.io.IOException;
import java.util.function.Consumer;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.toggle.DefaultToggle;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.BotSession;

import io.bf.messengers.telegram.engine.service.BotOrchestration;
import lombok.Getter;
import lombok.Setter;

import static org.telegram.abilitybots.api.db.MapDBContext.offlineInstance;

public class Bot extends AbilityBot {
    private final BotOrchestration botOrchestration;

    @Getter
    private String botId;
    private int creatorId;
    @Setter
    @Getter
    private BotSession botSession;
    private String botUsername;

    public Bot(BotOrchestration botOrchestration, String botId, String botToken, String botUsername) {
        super(botToken, botUsername, offlineInstance(botUsername), new DefaultToggle(), new DefaultBotOptions());
        this.botOrchestration = botOrchestration;
        this.botId = botId;
        this.botUsername = botUsername;

        // Fetch creatorId from first part of token
        this.creatorId = Integer.parseInt(botToken.split(":")[0]);
    }

    public Reply replyToMessage() {
        Consumer<Update> consumer = u -> botOrchestration.adaptMessage(this.botId, this.botUsername, u);
        return Reply.of(consumer, MESSAGE);
    }

    public Reply replyToCallBack() {
        Consumer<Update> consumer = u -> botOrchestration.adaptCallback(this.botId, this.botUsername, u);
        return Reply.of(consumer, CALLBACK_QUERY);
    }

    @Override
    public int creatorId() {
        return creatorId;
    }

    public void closeDB() throws IOException {
            this.db.close();
    }
}
