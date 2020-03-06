package com.ermakov.nikita.event;

import com.ermakov.nikita.entity.security.User;
import lombok.Getter;

/**
 * created by Nikita_Ermakov at 2/25/2020
 */
@Getter
public class RegisterEvent extends BaseSmokeCloudEvent {

    private final User user;

    public RegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
