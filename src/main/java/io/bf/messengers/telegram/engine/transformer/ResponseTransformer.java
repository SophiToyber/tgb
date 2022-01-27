package io.bf.messengers.telegram.engine.transformer;

import java.util.stream.Collectors;

import io.bf.apis.protobuf.BfProtos;
import io.bf.messengers.telegram.engine.entity.Button;
import io.bf.messengers.telegram.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseTransformer {

    public static Message transform(BfProtos.Response response) {
        return Message.builder()
            .sessionId(response.getSessionId())
            .messageId(response.getMessageId())
            .text(response.getText())
            .pictureUrl(response.getPictureUrl())
            .callbacks(response.getCallbacks()
                .getButtonsRowList()
                .stream()
                .map(row -> row.getButtonsList()
                    .stream()
                    .map(b -> Button.builder()
                        .callbackQueryData(b.getCallbackQueryData())
                        .text(b.getText())
                        .linkUrl(b.getLinkUrl())
                        .nextId(b.getNextId())
                        .build())
                    .collect(Collectors.toList()))
                .collect(Collectors.toList()))
            .build();
    }
}
