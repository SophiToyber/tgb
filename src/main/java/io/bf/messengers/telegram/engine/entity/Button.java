package io.bf.messengers.telegram.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Button {
    /*
     * Telegram API limitation 1-64 bytes
     */
    private String callbackQueryData;
    private String text;
    private String path;
    private String linkUrl;
    private String nextId;

}
