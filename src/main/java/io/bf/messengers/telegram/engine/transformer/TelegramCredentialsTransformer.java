package io.bf.messengers.telegram.engine.transformer;

import org.springframework.stereotype.Component;

import io.bf.apis.protobuf.MessangerProtos;
import io.bf.messengers.telegram.engine.entity.TelegramCredentials;
import io.bf.messengers.telegram.engine.utils.CryptoAES;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TelegramCredentialsTransformer {
    private final CryptoAES cryptoAes;

    public TelegramCredentials transform(MessangerProtos.TelegramCredentials telegramCredentials) {
        return TelegramCredentials.builder()
            .token(cryptoAes.decrypt(telegramCredentials.getToken()))
            .username(cryptoAes.decrypt(telegramCredentials.getUsername()))
            .botId(telegramCredentials.getBotId())
            .build();
    }

}