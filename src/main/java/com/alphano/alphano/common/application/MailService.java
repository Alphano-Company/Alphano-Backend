package com.alphano.alphano.common.application;

import com.alphano.alphano.common.exception.UnableToSendEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String htmlBody) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, false, StandardCharsets.UTF_8.name());
            helper.setFrom(from, "알파노 컴퍼니");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML
            mailSender.send(mime);
        } catch (MessagingException | MailException | UnsupportedEncodingException e) {
            log.warn("Mail send failed. to={}, subject={}, cause={}", to, subject, e.toString());
            throw UnableToSendEmailException.EXCEPTION;
        }
    }
}
