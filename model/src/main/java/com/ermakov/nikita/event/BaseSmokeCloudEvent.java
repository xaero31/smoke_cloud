package com.ermakov.nikita.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

/**
 * created by Nikita_Ermakov at 2/25/2020
 */
@Getter
public abstract class BaseSmokeCloudEvent extends ApplicationEvent {

    private final String uuid;

    public BaseSmokeCloudEvent(Object source) {
        super(source);
        this.uuid = UUID.randomUUID().toString();
    }
}
