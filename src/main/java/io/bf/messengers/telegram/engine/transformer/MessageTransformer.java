package io.bf.messengers.telegram.engine.transformer;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import io.bf.messengers.telegram.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageTransformer {
    // U+200C ZERO WIDTH NON-JOINER
    private static final String ZERO_WIDTH_SYMBOL = "‌";

    public static final Function<Message, SendMessage> TO_MESSAGE = m -> new SendMessage().setChatId(m.getSessionId())
        .setText(configureText(m))
        .setParseMode(ParseMode.MARKDOWN)
        .setReplyMarkup(transformInlineKeyboardMarkup(m));

    public static final Function<Message, EditMessageText> TO_CALLBACK = m -> new EditMessageText()
        .setChatId(m.getSessionId())
        .setMessageId(Integer.parseInt(m.getMessageId()))
        .setText(configureText(m))
        .setParseMode(configureParseMode(m))
        .setReplyMarkup(transformInlineKeyboardMarkup(m));

    private static String configureParseMode(Message message) {
        if (isMarkdownMode(message)) {
            return ParseMode.MARKDOWN;
        }
        return ParseMode.HTML;
    }

    private static String configureText(Message message) {
        if (isMarkdownMode(message)) {
            return String.format("%s [%s](%s)", message.getText(), ZERO_WIDTH_SYMBOL, message.getPictureUrl());
        }
        return message.getText();
    }

    private static boolean isMarkdownMode(Message message) {
        return !message.getPictureUrl().isEmpty();
    }

    private static InlineKeyboardMarkup transformInlineKeyboardMarkup(Message m) {
        List<List<InlineKeyboardButton>> keyboardRows = new LinkedList<>();
        try {
            m.getCallbacks().stream().forEachOrdered(row -> {
                List<InlineKeyboardButton> inlineRow = new LinkedList<>();
                row.stream().forEach(b -> {
                    if (b.getLinkUrl() != null) {
                        inlineRow.add(new InlineKeyboardButton().setUrl(b.getLinkUrl())
                            .setCallbackData(b.getCallbackQueryData())
                            .setText(b.getText()));
                    } else {
                        inlineRow.add(new InlineKeyboardButton().setText(b.getText())
                            .setCallbackData(b.getCallbackQueryData()));
                    }
                });
                keyboardRows.add(inlineRow);
            });
        } catch (NullPointerException e) {
            log.info("Keyboard is missing");
        }

        return new InlineKeyboardMarkup().setKeyboard(keyboardRows);
    }
}
