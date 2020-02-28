package com.ermakov.nikita.queue.listener;

import com.ermakov.nikita.entity.profile.VerificationToken;
import com.ermakov.nikita.entity.security.User;
import com.ermakov.nikita.event.RegisterEvent;
import com.ermakov.nikita.queue.util.EMailSender;
import com.ermakov.nikita.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * created by Nikita_Ermakov at 2/25/2020
 */
@Slf4j
@Component
public class RegisterEventListener {

    private EMailSender eMailSender;
    private VerificationTokenRepository tokenRepository;

    public RegisterEventListener(@Autowired VerificationTokenRepository tokenRepository,
                                 @Autowired EMailSender eMailSender) {
        this.tokenRepository = tokenRepository;
        this.eMailSender = eMailSender;
    }

    @Async
    @EventListener
    public void sendMailConfirmingNotification(RegisterEvent event) {
        log.info("Got register event with uuid: {}", event.getUuid());

        final User user = event.getUser();
        final VerificationToken token = createToken(user);

        tokenRepository.save(token);
        log.info("Token for user {} was saved. uuid: {}", user.getUsername(), event.getUuid());

        eMailSender.sendVerifyMessage(user, token);
        log.info("Verification email for user {} with token {} was sent to {}. uuid: {}",
                user.getUsername(), token.getToken(), user.getEmail(), event.getUuid());
    }

    private VerificationToken createToken(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Can't create verification token with null user");
        }

        final VerificationToken token = new VerificationToken();

        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpirationDate(createExpirationDate());

        return token;
    }

    private Date createExpirationDate() {
        final Instant expirationInstant = Instant.now().plus(1L, ChronoUnit.DAYS);
        return Date.from(expirationInstant);
    }
}