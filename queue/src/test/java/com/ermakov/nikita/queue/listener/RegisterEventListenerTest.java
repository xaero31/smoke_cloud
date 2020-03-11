package com.ermakov.nikita.queue.listener;

import com.ermakov.nikita.entity.profile.VerificationToken;
import com.ermakov.nikita.entity.security.User;
import com.ermakov.nikita.event.RegisterEvent;
import com.ermakov.nikita.queue.util.EMailSender;
import com.ermakov.nikita.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * created by Nikita_Ermakov at 2/25/2020
 */
@ExtendWith(MockitoExtension.class)
public class RegisterEventListenerTest {

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private EMailSender eMailSender;

    private ArgumentCaptor<VerificationToken> tokenCaptor;
    private ArgumentCaptor<String> stringCaptor;

    private RegisterEventListener eventListener;

    @BeforeEach
    void before() {
        eventListener = new RegisterEventListener(verificationTokenRepository, eMailSender);
        tokenCaptor = ArgumentCaptor.forClass(VerificationToken.class);
        stringCaptor = ArgumentCaptor.forClass(String.class);
    }

    @Test
    void registerEventListenerShouldAddExpirationDeltaToToken() {
        eventListener.sendMailConfirmingNotification(new RegisterEvent(this, new User()));

        verify(verificationTokenRepository, atLeastOnce()).save(tokenCaptor.capture());

        final Instant currentInstant = Instant.now();
        final Instant expirationInstant = tokenCaptor.getValue().getExpirationDate().toInstant();

        assertTrue(expirationInstant.isAfter(currentInstant));
    }

    @Test
    void registerEventListenerShouldSaveTokenToRegisteringUser() {
        final User user = new User();

        eventListener.sendMailConfirmingNotification(new RegisterEvent(this, user));

        verify(verificationTokenRepository, atLeastOnce()).save(tokenCaptor.capture());
        assertSame(user, tokenCaptor.getValue().getUser());
    }

    @Test
    void registerEventListenerShouldThrowAnExceptionWithNullUser() {
        assertThrows(IllegalArgumentException.class,
                () -> eventListener.sendMailConfirmingNotification(new RegisterEvent(this, null)));
    }

    @Test
    void ifTokenExistsAndNotExpiredListenerShouldSaveAnotherTokenValue() {
        final VerificationToken existingToken = new VerificationToken();
        existingToken.setExpirationDate(Date.from(Instant.now().plusSeconds(200L)));

        when(verificationTokenRepository.findByToken(anyString())).thenReturn(existingToken).thenReturn(null);

        eventListener.sendMailConfirmingNotification(new RegisterEvent(this, new User()));

        verify(verificationTokenRepository, atLeastOnce()).findByToken(stringCaptor.capture());
        verify(verificationTokenRepository, atLeastOnce()).save(tokenCaptor.capture());

        assertNotEquals(stringCaptor.getAllValues().get(0), tokenCaptor.getValue().getToken());
    }

    @Test
    void ifTokenDoesNotExistItSavesInstant() {
        eventListener.sendMailConfirmingNotification(new RegisterEvent(this, new User()));

        verify(verificationTokenRepository, times(1)).findByToken(stringCaptor.capture());
        verify(verificationTokenRepository, times(1)).save(tokenCaptor.capture());
    }

    @Test
    void ifTokenExistsButExpiredItShouldRemoveOldAndSaveNewToken() {
        final VerificationToken expiredToken = new VerificationToken();
        expiredToken.setExpirationDate(Date.from(Instant.now().minusSeconds(100L)));

        when(verificationTokenRepository.findByToken(anyString())).thenReturn(expiredToken);

        eventListener.sendMailConfirmingNotification(new RegisterEvent(this, new User()));

        verify(verificationTokenRepository, times(1)).findByToken(anyString());
        verify(verificationTokenRepository, times(1)).delete(expiredToken);
        verify(verificationTokenRepository, times(1)).save(any(VerificationToken.class));
    }
}