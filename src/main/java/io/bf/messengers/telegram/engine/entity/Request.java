package io.bf.messengers.telegram.engine.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request {
    private String botId;
    private String sessionId;
    private String messageId;
    private String inputMessageText;
    @Builder.Default
    private String messenger = "TELEGRAM";
    private String callbackQueryData;
    private Profile profile;
}
