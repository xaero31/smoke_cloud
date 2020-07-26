package com.ermakov.nikita.queue.util;

import com.ermakov.nikita.ControllerPath;
import com.ermakov.nikita.entity.profile.VerificationToken;
import com.ermakov.nikita.entity.security.User;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.Locale;

/**
 * created by Nikita_Ermakov at 2/26/2020
 */
@Setter
@Scope("prototype")
@Component("eMailSender")
public class EMailSender {

    private static final String URL_MATCHER = "{URL}";

    private final JavaMailSender javaMailSender;
    private final ServletContext servletContext;
    private final MessageSource messageSource;

    @Value("${application.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Value("${mail.from}")
    private String from;

    public EMailSender(@Autowired JavaMailSender javaMailSender,
                       @Autowired ServletContext servletContext,
                       @Autowired MessageSource messageSource) {
        this.javaMailSender = javaMailSender;
        this.servletContext = servletContext;
        this.messageSource = messageSource;
    }

    public void sendVerifyMessage(User user, VerificationToken token, Locale locale) {
        final SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setFrom(from);
        message.setSubject(messageSource.getMessage("verify.message.subject", null, locale));
        message.setText(createVerificationText(token, locale));

        javaMailSender.send(message);
    }

    private String createVerificationText(VerificationToken token, Locale locale) {
        return messageSource.getMessage("verify.message.text", new Object[]{createVerificationUrl(token)}, locale);
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private String createVerificationUrl(VerificationToken token) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("http://");
        stringBuilder.append(host);
        stringBuilder.append(":");
        stringBuilder.append(port);
        stringBuilder.append(servletContext.getContextPath());
        stringBuilder.append(ControllerPath.VERIFY_USER);
        stringBuilder.append("?token=");
        stringBuilder.append(token.getToken());

        return stringBuilder.toString();
    }
}