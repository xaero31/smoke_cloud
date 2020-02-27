package com.ermakov.nikita.queue.util;

import com.ermakov.nikita.ControllerPath;
import com.ermakov.nikita.entity.profile.VerificationToken;
import com.ermakov.nikita.entity.security.User;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * created by Nikita_Ermakov at 2/26/2020
 */
@Setter
@PropertySource("classpath:messages.properties")
@Scope("prototype")
@Component("eMailSender")
public class EMailSender {

    private static final String URL_MATCHER = "{URL}";
    private static final String SMOKE_CLOUD_FROM = "smoke-cloud@register.com";

    private final JavaMailSender javaMailSender;
    private final ServletContext servletContext;

    @Value("${application.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Value("${verify.message.text}")
    private String text;

    @Value("${verify.message.subject}")
    private String subject;

    public EMailSender(@Autowired JavaMailSender javaMailSender,
                       @Autowired ServletContext servletContext) {
        this.javaMailSender = javaMailSender;
        this.servletContext = servletContext;
    }

    public void sendVerifyMessage(User user, VerificationToken token) {
        final SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setFrom(SMOKE_CLOUD_FROM);
        message.setSubject(subject);
        message.setText(createVerificationText(token));

        javaMailSender.send(message);
    }

    private String createVerificationText(VerificationToken token) {
        return text.replace(URL_MATCHER, createVerificationUrl(token));
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