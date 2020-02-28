package com.ermakov.nikita.queue.util;

import com.ermakov.nikita.entity.profile.VerificationToken;
import com.ermakov.nikita.entity.security.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.servlet.ServletContext;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * created by Nikita_Ermakov at 2/27/2020
 */
@ExtendWith(MockitoExtension.class)
public class EMailSenderTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private ServletContext servletContext;

    private EMailSender eMailSender;

    @BeforeEach
    void before() {
        eMailSender = new EMailSender(javaMailSender, servletContext);

        eMailSender.setHost("localhost");
        eMailSender.setPort("80");
        eMailSender.setSubject("mail subject");
        eMailSender.setText("Some email text with url: {URL}");

        lenient().when(servletContext.getContextPath()).thenReturn("/smoke-cloud");
    }

    @Test
    void eMailSenderShouldInvokeJavaMailSenderSend() {
        eMailSender.sendVerifyMessage(new User(), new VerificationToken());
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void eMailSenderShouldSendMailToUsersEmail() {
        final User user = new User();
        user.setEmail("test@mail.com");

        final ArgumentCaptor<SimpleMailMessage> mailMessageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        doNothing().when(javaMailSender).send(mailMessageCaptor.capture());

        eMailSender.sendVerifyMessage(user, new VerificationToken());

        final String[] recipients = mailMessageCaptor.getValue().getTo();
        assertNotNull(recipients, "'To' in output message could be filled");

        final String messageTo = Arrays.stream(recipients)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No message provided to javaMailSender"));
        assertEquals(user.getEmail(), messageTo, "Mail recipient doesn't meet registering user's email");
    }

    @Test
    void sendingVerificationEmailShouldContainRegistrationUrl() {
        final VerificationToken token = new VerificationToken();
        token.setToken("tokenExample");

        final ArgumentCaptor<SimpleMailMessage> mailMessageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        doNothing().when(javaMailSender).send(mailMessageCaptor.capture());

        eMailSender.sendVerifyMessage(new User(), token);

        final String messageText = mailMessageCaptor.getValue().getText();
        assertNotNull(messageText, "Text of mail could be filled");
        assertTrue(messageText.contains("http://localhost:80/smoke-cloud/verifyUser?token=tokenExample"),
                "Text of mail could contain registration confirming url");
    }

    @Test
    void sendingEmailShouldHaveSubject() {
        final ArgumentCaptor<SimpleMailMessage> mailMessageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        doNothing().when(javaMailSender).send(mailMessageCaptor.capture());

        eMailSender.sendVerifyMessage(new User(), new VerificationToken());

        final String subject = mailMessageCaptor.getValue().getSubject();
        assertNotNull(subject, "Mail subject could be filled");
        assertFalse(subject.isEmpty(), "Mail subject could be filled");
    }
}
