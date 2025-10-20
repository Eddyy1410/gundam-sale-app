package com.huyntd.superapp.gundam_shop.service.email.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.huyntd.superapp.gundam_shop.service.email.EmailService;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    @Value("${gundamshop.contact.email}")
    private String fromEmail;

    @Value("${gundamshop.contact.name:Gundam Shop}")
    private String fromName;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false);
            // set From with personal name
            try {
                helper.setFrom(new InternetAddress(fromEmail, fromName));
            } catch (Exception e) {
                // fallback to simple from
                helper.setFrom(fromEmail);
            }
            mailSender.send(mimeMessage);
        } catch (Exception ex) {
            log.error("Failed to send email", ex);
            // swallow or rethrow depending on your policy; choose to log and continue
        }
    }
}
