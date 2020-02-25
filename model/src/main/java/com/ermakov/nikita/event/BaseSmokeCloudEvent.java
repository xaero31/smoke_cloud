package com.ermakov.nikita.event;

import com.ermakov.nikita.entity.security.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

/**
 * created by Nikita_Ermakov at 2/25/2020
 */
@Getter
public class BaseSmokeCloudEvent extends ApplicationEvent {

    private final String uuid;
    private final User user;

    public BaseSmokeCloudEvent(Object source, User user) {
        super(source);
        this.uuid = UUID.randomUUID().toString();
        this.user = user;
    }
}
