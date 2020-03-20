package com.ermakov.nikita.event;

import com.ermakov.nikita.entity.security.User;
import lombok.Getter;

import java.util.Locale;

/**
 * created by Nikita_Ermakov at 2/25/2020
 */
@Getter
public class RegisterEvent extends BaseSmokeCloudEvent {

    private final User user;
    private final Locale locale;

    public RegisterEvent(Object source, User user, Locale locale) {
        super(source);
        this.user = user;
        this.locale = locale;
    }
}
