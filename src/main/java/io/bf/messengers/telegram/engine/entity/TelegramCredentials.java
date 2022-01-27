package io.bf.messengers.telegram.engine.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelegramCredentials {
    private String botId;
    private String token;
    private String username;
}
