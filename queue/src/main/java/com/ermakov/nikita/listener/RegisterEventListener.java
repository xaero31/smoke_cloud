package com.ermakov.nikita.listener;

import com.ermakov.nikita.event.RegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * created by Nikita_Ermakov at 2/25/2020
 */
@Slf4j
@Component
public class RegisterEventListener {

    @Async
    @EventListener
    public void sendMail(RegisterEvent event) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
