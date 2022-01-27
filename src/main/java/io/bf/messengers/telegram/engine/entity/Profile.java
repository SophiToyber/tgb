package io.bf.messengers.telegram.engine.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Profile {
    private String id;
    private String phone;
    private String username;
    private String firstName;
    private String lastName;
    private String languageCode;
    private boolean bot;
}
