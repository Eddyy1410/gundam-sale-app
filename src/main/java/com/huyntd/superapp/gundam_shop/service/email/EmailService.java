package com.huyntd.superapp.gundam_shop.service.email;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
