package io.bf.messengers.telegram.engine.transformer;

import java.util.Optional;

import org.telegram.telegrambots.meta.api.objects.Update;

import io.bf.apis.protobuf.BfProtos;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TelegramUserProfileTransformer {

    public static BfProtos.Profile fetchFromMessageRequest(Update update) {
        BfProtos.Profile.Builder profileBuilder = BfProtos.Profile.newBuilder()
            .setId(update.getMessage().getFrom().getId().toString())
            .setBot(update.getMessage().getFrom().getBot())
            .setLanguageCode(update.getMessage().getFrom().getLanguageCode());

        Optional.ofNullable(update.getMessage().getFrom().getUserName()).ifPresent(profileBuilder::setUsername);
        Optional.ofNullable(update.getMessage().getFrom().getFirstName()).ifPresent(profileBuilder::setFirstName);
        Optional.ofNullable(update.getMessage().getFrom().getLastName()).ifPresent(profileBuilder::setLastName);
        return profileBuilder.build();
    }
    
    public static BfProtos.Profile fetchFromCallbackRequest(Update update){
        BfProtos.Profile.Builder profileBuilder = BfProtos.Profile.newBuilder()
                .setId(update.getCallbackQuery().getFrom().getId().toString())
                .setBot(update.getCallbackQuery().getFrom().getBot())
                .setLanguageCode(update.getCallbackQuery().getFrom().getLanguageCode());

            Optional.ofNullable(update.getCallbackQuery().getFrom().getUserName()).ifPresent(profileBuilder::setUsername);
            Optional.ofNullable(update.getCallbackQuery().getFrom().getFirstName()).ifPresent(profileBuilder::setFirstName);
            Optional.ofNullable(update.getCallbackQuery().getFrom().getLastName()).ifPresent(profileBuilder::setLastName);
            return profileBuilder.build();
    }

}
