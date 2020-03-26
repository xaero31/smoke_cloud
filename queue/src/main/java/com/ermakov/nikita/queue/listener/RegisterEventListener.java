package com.ermakov.nikita.queue.listener;

import com.ermakov.nikita.entity.profile.VerificationToken;
import com.ermakov.nikita.entity.security.User;
import com.ermakov.nikita.event.RegisterEvent;
import com.ermakov.nikita.queue.util.EMailSender;
import com.ermakov.nikita.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Component("registerEventListener")
public class RegisterEventListener {

    private EMailSender eMailSender;
    private VerificationTokenRepository tokenRepository;

    public RegisterEventListener(@Autowired @Qualifier("verificationTokenRepository")
                                         VerificationTokenRepository tokenRepository,
                                 @Autowired @Qualifier("eMailSender") EMailSender eMailSender) {
        this.tokenRepository = tokenRepository;
        this.eMailSender = eMailSender;
    }

    @Async
    @EventListener
    public void sendMailConfirmingNotification(RegisterEvent event) {
        log.info("Got register event with uuid: {}", event.getUuid());

        final User user = event.getUser();
        final VerificationToken token = createToken(user);

        saveToken(token);
        log.info("Token for user {} was saved. uuid: {}", user.getUsername(), event.getUuid());

        eMailSender.sendVerifyMessage(user, token, event.getLocale());
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

    private void saveToken(VerificationToken token) {
        while (tokenExistsAndNotExpired(token)) {
            token.setToken(UUID.randomUUID().toString());
        }

        tokenRepository.save(token);
    }

    private boolean tokenExistsAndNotExpired(VerificationToken tokenObject) {
        final VerificationToken foundToken = tokenRepository.findByToken(tokenObject.getToken());

        if (foundToken == null) {
            return false;
        }

        if (tokenNotExpired(foundToken)) {
            return true;
        }

        // Not update by saving with existing id for more code readability
        tokenRepository.delete(foundToken); // Remove expired token from db
        return false;
    }

    private boolean tokenNotExpired(VerificationToken token) {
        final Instant expireInstant = token.getExpirationDate().toInstant();
        final Instant now = Instant.now();

        return now.isBefore(expireInstant);
    }

    private Date createExpirationDate() {
        final Instant expirationInstant = Instant.now().plus(1L, ChronoUnit.DAYS);
        return Date.from(expirationInstant);
    }
}