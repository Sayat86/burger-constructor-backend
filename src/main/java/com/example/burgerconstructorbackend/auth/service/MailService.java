package com.example.burgerconstructorbackend.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    public void sendPasswordResetCode(String toEmail, String token) {
        if (from == null || from.isBlank()) {
            throw new IllegalStateException("app.mail.from is empty. Set MAIL_FROM (or MAIL_USERNAME).");
        }
        log.info("Mail config: from='{}'", from);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setSubject("Восстановление пароля");
        message.setText("Ваш код для восстановления пароля: " + token);

        mailSender.send(message);
    }
}
