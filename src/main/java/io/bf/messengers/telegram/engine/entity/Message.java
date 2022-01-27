package io.bf.messengers.telegram.engine.entity;

import java.util.List;

import io.bf.messengers.telegram.engine.entity.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String sessionId;
    private String messageId;
    private String text;
    private MessageType messageType;

    private List<List<Button>> callbacks;
    private String pictureUrl;
}
